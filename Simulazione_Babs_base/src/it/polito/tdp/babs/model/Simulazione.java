package it.polito.tdp.babs.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Simulazione {
	
	private enum EventType{
		PICK, DROP;
	}
	
	private PriorityQueue <Event> pt;
	private SimulationResult simulationResult;
	private Map<Integer, Integer> mapStationAvailability;
	private Model model;
	
	public Simulazione(Model model){
		pt = new PriorityQueue<Event>();
		simulationResult = new SimulationResult();
		mapStationAvailability = new HashMap <Integer, Integer>();
		this.model = model;
	}
	
	public void loadPick(List<Trip> trips){
		for( Trip trip : trips){
			pt.add(new Event (EventType.PICK, trip.getStartDate(), trip));
		}	
	}
	
//	public void loadDrop(List<Trip> trips){
//		for( Trip trip : trips){
//			pt.add(new Event (EventType.DROP, trip.getEndDate(), trip));
//		}	
//	}

	public void loadStations(Double k, List <Station> stazioni) {
		for(Station station : stazioni){
			int occupacy = (int) (station.getDockCount() * k);
			mapStationAvailability.put(station.getStationID(), occupacy);
		}	
	}
	
	public void run(){
		
		while(!pt.isEmpty()){
			Event event = (Event) pt.poll();
			
			switch (event.type){
			
			case PICK:
				int availability = mapStationAvailability.get(event.trip.getStartStationID()) ;
					
				if( availability >0){
						// Diminuisco la disponibilita` della stazione
						availability--;
						mapStationAvailability.put(event.trip.getStartStationID(), availability);
						
						//Aggiungo l'evento di DROP
						pt.add(new Event (EventType.DROP, event.trip.getEndDate(), event.trip));
						
				} 
				else{
						// Aumenta il numero di Pick Missed
						simulationResult.increaseNumberOfPickMissed();
				}
				break;
				
			case DROP:
				
				availability = mapStationAvailability.get(event.trip.getEndStationID()) ;
				int capacitaTotale = model.getStationById(event.trip.getEndStationID()).getDockCount();
				
				if(availability >=capacitaTotale ){
					// stazione piena : non si riesce a restituire la bici
					simulationResult.increaseNumberOfDropMissed();
				}
				else{
					// viene restutuita la bici
					 availability ++;
					 mapStationAvailability.put(event.trip.getEndStationID(), availability);
				}
				
				break;
			}
		}
		
	}
	
	public SimulationResult collectResults(){
		return simulationResult;
	}
	

	public class Event implements Comparable <Event>{
		
		EventType type;
		LocalDateTime ldt;
		Trip trip;
		
		public Event( EventType type, LocalDateTime dateTime, Trip trip){
			this.type = type;
			this.ldt = dateTime;
			this.trip = trip;
		}

		@Override
		public int compareTo(Event o) {
			return this.ldt.compareTo(o.ldt);  // metodo compareTo di LocalDateTime
		}
		
	}

}
