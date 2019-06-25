package it.polito.tdp.newufosightings.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.newufosightings.model.Sighting;
import it.polito.tdp.newufosightings.model.State;
import it.polito.tdp.newufosightings.model.Vicini;

public class NewUfoSightingsDAO {

	public List<Sighting> loadAllSightings() {
		String sql = "SELECT * FROM sighting";
		List<Sighting> list = new ArrayList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);	
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state"), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}

	public List<State> loadAllStates() {
		String sql = "SELECT * FROM state";
		List<State> result = new ArrayList<State>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				State state = new State(rs.getString("id"), rs.getString("Name"), rs.getString("Capital"),
						rs.getDouble("Lat"), rs.getDouble("Lng"), rs.getInt("Area"), rs.getInt("Population"),
						rs.getString("Neighbors"));
				result.add(state);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<String> getForme(int anno) {
		List<String> result = new ArrayList<>();
		String sql ="SELECT DISTINCT shape " + 
				"FROM sighting " + 
				"WHERE YEAR(datetime) = ? ";
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				result.add(rs.getString("shape"));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<State> getStatiAnno(int anno) {
		String sql = "SELECT st.id,st.name,st.capital,st.lat,st.lng,st.area,st.population,st.neighbors " + 
				"FROM sighting AS s, state AS st " + 
				"WHERE s.state = st.id AND YEAR(s.datetime) = ? " + 
				"GROUP BY st.id,st.name,st.capital,st.lat,st.lng,st.area,st.population,st.neighbors ";
		List<State> result = new ArrayList<State>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				State state = new State(rs.getString("id"), rs.getString("Name"), rs.getString("Capital"),
						rs.getDouble("Lat"), rs.getDouble("Lng"), rs.getInt("Area"), rs.getInt("Population"),
						rs.getString("Neighbors"));
				result.add(state);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Vicini> getVicini(Map<String, State> idMap, String forma) {
		List<Vicini> result = new ArrayList<>();
		String sql = "SELECT n.state1,n.state2, COUNT(*) " + 
				"FROM sighting AS s1, sighting AS s2, neighbor AS n " + 
				"WHERE s1.shape = ? AND s2.shape = ? AND ((n.state1=s1.state AND n.state2=s2.state) OR (n.state1=s2.state AND n.state2=s1.state)) " + 
				"GROUP BY n.state1,n.state2 ";
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, forma);
			st.setString(2, forma);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if (idMap.get(rs.getString("state1"))!= null && idMap.get(rs.getString("state2"))!=null) {
					result.add(new Vicini (idMap.get(rs.getString("state1")),idMap.get(rs.getString("state2")),rs.getInt("COUNT(*)")));
				}
			
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

}
