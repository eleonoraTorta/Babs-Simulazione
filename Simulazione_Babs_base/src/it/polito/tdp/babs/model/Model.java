package it.polito.tdp.babs.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.babs.db.BabsDAO;

public class Model {

	BabsDAO babsDAO;
	List<Station> stazioni;
	Map <Integer, Station> mappaStazioni;
	
	public Model(){
		babsDAO = new BabsDAO();
		mappaStazioni = new HashMap <Integer, Station>();
	}
	
	public List<Station> getStazioni() {
		if (stazioni == null)
			stazioni = babsDAO.getAllStations();
		    for( Station s : stazioni){
		    	mappaStazioni.put(s.getStationID(), s);
		    }
		return stazioni;
	}
	
	public List<Statistics> getStats(LocalDate ld) {
		List<Statistics> stats = new ArrayList<Statistics>();
		
		for (Station stazione : getStazioni()){
			int picks = babsDAO.getPickNumber(stazione, ld);
			int drops = babsDAO.getDropNumber(stazione, ld);
			Statistics stat = new Statistics(stazione, picks, drops);
			stats.add(stat);
		}
		
		return stats;
	}

	public List<Trip> getTripsWithPickForDay(LocalDate ld) {
		return babsDAO.getTripsForDayPick(ld);
	}
	
	public List<Trip> getTripsWithDropForDay(LocalDate ld) {
		return babsDAO.getTripsForDayDrop(ld);
	}
	
	public Station getStationById (int idStazione){
		return mappaStazioni.get(idStazione);
		
	}

}
