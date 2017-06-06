package it.polito.tdp.babs.db;

import it.polito.tdp.babs.model.Station;
import it.polito.tdp.babs.model.Trip;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BabsDAO {

	public List<Station> getAllStations() {
		List<Station> result = new ArrayList<Station>();
		Connection conn = DBConnect.getInstance().getConnection();
		String sql = "SELECT * FROM station";

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Station station = new Station(rs.getInt("station_id"), rs.getString("name"), rs.getDouble("lat"), rs.getDouble("long"), rs.getInt("dockcount"));
				result.add(station);
			}
			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error in database query", e);
		}

		return result;
	}

	public List<Trip> getAllTrips() {
		List<Trip> result = new LinkedList<Trip>();
		Connection conn = DBConnect.getInstance().getConnection();
		String sql = "SELECT * FROM trip";

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Trip trip = new Trip(rs.getInt("tripid"), rs.getInt("duration"), rs.getTimestamp("startdate").toLocalDateTime(), rs.getInt("startterminal"),
						rs.getTimestamp("enddate").toLocalDateTime(), rs.getInt("endterminal"));
				result.add(trip);
			}
			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error in database query", e);
		}

		return result;
	}

	public int getPickNumber(Station stazione, LocalDate ld) {
		int result;
		Connection conn = DBConnect.getInstance().getConnection();
		String sql = "Select count(*) as counter from trip where DATE(StartDate) = ? and StartTerminal = ?";  
		// count(*) restituisce quante volte ce l'occorrenza  (e` un contatore)
		// per la data ho usato DATE(StartDate)
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDate(1, Date.valueOf(ld));      // Date.valueOf(ld) = questo metodo converte una LocalDate in un oggetto Date
			st.setInt(2,  stazione.getStationID());
			ResultSet rs = st.executeQuery();
			
			// prima di usare rs.getInt(..) devo dirgli di prendere il risultato restituito dalla query
			// e poiche e` una sola riga posso usare rs.first() anziche rs.next()
//			rs.next();
   		    rs.first();  					// restituisce la prima riga del risultato
			result = rs.getInt("counter");  // restituisce il campo counter che era l'unico parametro che ho richiesto
											// se in sql non ci sono risultati restituisce 0 --> il testo mi chiede di stampare errore
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error in database query", e);
		}

		return result;
	}
	
	public int getDropNumber(Station stazione, LocalDate ld) {
		int result;
		Connection conn = DBConnect.getInstance().getConnection();
		String sql = "Select count(*) as counter from trip where DATE(EndDate) = ? and EndTerminal = ?";

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDate(1, Date.valueOf(ld));
			st.setInt(2,  stazione.getStationID());
			ResultSet rs = st.executeQuery();
			
			rs.first();
			result = rs.getInt("counter");
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error in database query", e);
		}

		return result;
	}

	public List<Trip> getTripsForDayPick(LocalDate ld) {
		List<Trip> result = new LinkedList<Trip>();
		Connection conn = DBConnect.getInstance().getConnection();
		String sql = "SELECT * FROM trip WHERE DATE(StartDate) = ?";

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDate(1, Date.valueOf(ld));
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Trip trip = new Trip(rs.getInt("tripid"), rs.getInt("duration"), rs.getTimestamp("startdate").toLocalDateTime(), rs.getInt("startterminal"),
						rs.getTimestamp("enddate").toLocalDateTime(), rs.getInt("endterminal"));
				result.add(trip);
			}
			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error in database query", e);
		}

		return result;
	}
	
	public List<Trip> getTripsForDayDrop(LocalDate ld) {
		List<Trip> result = new LinkedList<Trip>();
		Connection conn = DBConnect.getInstance().getConnection();
		String sql = "SELECT * FROM trip WHERE DATE(EndDate) = ?";

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDate(1, Date.valueOf(ld));
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Trip trip = new Trip(rs.getInt("tripid"), rs.getInt("duration"), rs.getTimestamp("startdate").toLocalDateTime(), rs.getInt("startterminal"),
						rs.getTimestamp("enddate").toLocalDateTime(), rs.getInt("endterminal"));
				result.add(trip);
			}
			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error in database query", e);
		}

		return result;
	}
}