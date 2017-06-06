package it.polito.tdp.babs;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.babs.model.Model;
import it.polito.tdp.babs.model.SimulationResult;
import it.polito.tdp.babs.model.Simulazione;
import it.polito.tdp.babs.model.Statistics;
import it.polito.tdp.babs.model.Trip;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;

public class BabsController {

	private Model model;

	public void setModel(Model model) {
		this.model = model;
	}

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private DatePicker pickData;    //DatePicker restituisce un LocalDate

	@FXML
	private Slider sliderK;    //barra di scorrimento

	@FXML
	private TextArea txtResult;

	@FXML
	void doContaTrip(ActionEvent event) {

		txtResult.clear();

		LocalDate ld = pickData.getValue();    //non sara` mai null perche in inizialize() inizializzo sempre una data per l'interfaccia grafica
		if (ld == null) {						// ma se non fosse presente quella istruzione dovrei fare il controllo.
			txtResult.setText("Selezionare una data");		//Lo faccio anche qui per completezza
			return;
		}

		List<Statistics> stats = model.getStats(ld);   //chiedo al model le statistiche per quel giorno
		Collections.sort(stats);     // ho implementato Comparable nella classe Statistics che ordina secondo la latitudine

		for (Statistics stat : stats) {
			
			if(stat.getPick() <= 0 && stat.getDrop() <= 0) {
				txtResult.appendText(String.format("WARNING!! Stazione %s con 0 pick e 0 drop\n", stat.getStazione().getName()));
			}
			else if(stat.getDrop() <= 0) {
				txtResult.appendText(String.format("WARNING!! Stazione %s con 0 drop\n", stat.getStazione().getName()));
			} 
			else if (stat.getPick() <= 0) {
				txtResult.appendText(String.format("WARNING!! Stazione %s con 0 pick\n", stat.getStazione().getName()));
			} 
			else {
				txtResult.appendText(String.format("%s %d %d\n", stat.getStazione().getName(), stat.getPick(), stat.getDrop()));
			}
		}

	}

	@FXML
	void doSimula(ActionEvent event) {
		
		txtResult.clear();

		LocalDate ld = pickData.getValue();
		//Vincolo: accettabili solo giorni feriali
		if (ld == null || ld.getDayOfWeek() == DayOfWeek.SATURDAY || ld.getDayOfWeek() == DayOfWeek.SUNDAY) {					
			txtResult.setText("Selezionare un giorno feriale");		
			return;
		}
		
		Double k =  (Double) sliderK.getValue() /100.0;     //getValue() restituisce un INTERO tra 0 e 99
															// divido per 100.0 perche voglio un numero reale tra 0 e 0.99
		
		List <Trip> tripsPick = model.getTripsWithPickForDay(ld);
		List <Trip> tripsDrop = model.getTripsWithDropForDay(ld);
		
		Simulazione simulazione = new Simulazione(model);
		simulazione.loadPick(tripsPick);
	//	simulazione.loadDrop(tripsDrop);   //potrebbe falsare il risultato della simulazione
		simulazione.loadStations(k, model.getStazioni());
		simulazione.run();
		SimulationResult simulationResult = simulazione.collectResults();
		
		txtResult.appendText( "PICK MISS: " + simulationResult.getNumberOfPickMissed() + "\n");
		txtResult.appendText( "DROP MISS: " + simulationResult.getNumberOfDropMissed() + "\n");
	}

	@FXML
	void initialize() {
		assert pickData != null : "fx:id=\"pickData\" was not injected: check your FXML file 'Babs.fxml'.";
		assert sliderK != null : "fx:id=\"sliderK\" was not injected: check your FXML file 'Babs.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Babs.fxml'.";

		pickData.setValue(LocalDate.of(2013, 9, 1));
	}
}
