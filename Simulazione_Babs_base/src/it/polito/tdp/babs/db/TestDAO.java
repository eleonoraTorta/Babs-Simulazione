package it.polito.tdp.babs.db;

import java.time.LocalDate;
import java.util.List;

import it.polito.tdp.babs.model.Station;
import it.polito.tdp.babs.model.Trip;

public class TestDAO {

	public static void main(String args[]) {
		
		BabsDAO dao = new BabsDAO();

		List<Station> stations = dao.getAllStations();

		for (Station s : stations) {
			System.out.format("%2d %-20s\n", s.getStationID(), s.getName());
		}

//		List<Trip> trips = dao.getAllTrips();
//		System.out.println("We have " + trips.size() + " trips");

		LocalDate ld = LocalDate.of(2013, 8, 29);
		System.out.println(dao.getPickNumber(stations.get(1), ld));
		System.out.println(dao.getDropNumber(stations.get(1), ld));
	}
}
