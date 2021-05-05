package it.polito.tdp.extflightdelays.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.extflightdelays.model.Airline;
import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Flight;
import it.polito.tdp.extflightdelays.model.Rotta;

public class ExtFlightDelaysDAO {

	public List<Airline> loadAllAirlines() {
		String sql = "SELECT * from airlines";
		List<Airline> result = new ArrayList<Airline>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Airline(rs.getInt("ID"), rs.getString("IATA_CODE"), rs.getString("AIRLINE")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public void loadAllAirports(Map<Integer,Airport> mappa) {
		String sql = "SELECT * FROM airports";

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Airport airport = new Airport(rs.getInt("ID"), rs.getString("IATA_CODE"), rs.getString("AIRPORT"),
						rs.getString("CITY"), rs.getString("STATE"), rs.getString("COUNTRY"), rs.getDouble("LATITUDE"),
						rs.getDouble("LONGITUDE"), rs.getDouble("TIMEZONE_OFFSET"));
				mappa.put(airport.getId(),airport);
				
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public void loadAllRotte(Map<String,Rotta> rotte) {
		String sql = "SELECT f1.ORIGIN_AIRPORT_ID AS id1, f1.DESTINATION_AIRPORT_ID AS id2, SUM(f1.DISTANCE) AS distanza, COUNT(*) AS numVoli "
				+ "FROM flights f1, flights f2 "
				+ "WHERE f1.ID=f2.ID "
				+ "GROUP BY f1.ORIGIN_AIRPORT_ID, f2.DESTINATION_AIRPORT_ID";

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Rotta rotta = new Rotta(rs.getInt("id1"),rs.getInt("id2"),rs.getDouble("distanza"), rs.getInt("numVoli"));
				
				if(rotte.containsKey(rs.getInt("id2")+"-"+rs.getInt("id1"))) {
					Rotta precedente = rotte.get(rs.getInt("id2")+"-"+rs.getInt("id1"));
					double nuovaDistanza = precedente.getDistanza() + rs.getDouble("distanza");
					int nuovoNumVoli = precedente.getNumVoli() + rs.getInt("numVoli");
					precedente.setDistanza(nuovaDistanza);
					precedente.setNumVoli(nuovoNumVoli);
				}
				else {
					rotte.put(rs.getInt("id1")+"-"+rs.getInt("id2"),rotta);
				}
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
}
