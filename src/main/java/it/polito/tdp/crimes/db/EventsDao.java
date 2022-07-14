package it.polito.tdp.crimes.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.crimes.model.Distretto;
import it.polito.tdp.crimes.model.Event;



public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Year> getAnni ()
	{
		String sql = "SELECT DISTINCT  YEAR(e.reported_date) as anno "
				+ "FROM `events` e " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Year> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Year anno= Year.of(res.getInt("anno"));
				list.add(anno);
			}
				
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Distretto> getArchi (Year anno)
	{
		String sql = "SELECT e.district_id as d, AVG(e.geo_lon) AS lon , AVG(e.geo_lat) AS lat  "
				+ "FROM `events` e "
				+ "WHERE YEAR(e.reported_date)=? "
				+ "GROUP BY e.district_id " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1,anno.getValue());
			
			ResultSet res = st.executeQuery() ;
			List<Distretto> distretti = new ArrayList<>() ;
			while(res.next()) {
				LatLng posizione= new LatLng(res.getDouble("lat"), res.getDouble("lon"));
				distretti.add(new Distretto (res.getInt("d"), posizione));
			}
				
			conn.close();
			return distretti ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> getVertici(){
		String sql = "SELECT DISTINCT district_id as vertice FROM events";
		List<Integer> result = new ArrayList<Integer>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				result.add(res.getInt("vertice"));
			}
			conn.close();
			return result;
		} catch(Throwable t) {
			t.printStackTrace();
			return null;
		}
	}
	
	public List<Month> getMesi(Year anno)
	{
		String sql = "SELECT DISTINCT MONTH(e.reported_date) as mese "
				+ "FROM `events` e "
				+ "WHERE YEAR(e.reported_date)=? "
				+ "ORDER BY MONTH(e.reported_date) ";
		List<Month> result = new ArrayList<Month>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno.getValue());
			
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				Month mese= Month.of(res.getInt("mese"));
				result.add(mese);
			}
				
			
			conn.close();
			return result;
		} catch(Throwable t) {
			t.printStackTrace();
			return null;
	}
	}
		
		public List<Integer> getGiorno()
		{
			String sql = "SELECT DISTINCT DAY(e.reported_date) as giorno "
					+ "FROM `events` e "
					+ "ORDER BY DAY (e.reported_date) ";
			List<Integer> result = new ArrayList<Integer>();
			try {
				Connection conn = DBConnect.getConnection() ;
				PreparedStatement st = conn.prepareStatement(sql) ;
				
				
				ResultSet res = st.executeQuery() ;
				while(res.next()) {
					
					result.add(res.getInt("giorno"));
				}
					
				
				conn.close();
				return result;
			} catch(Throwable t) {
				t.printStackTrace();
				return null;
			}
	}

		public Integer getDistrettoMin() {
			String sql = "SELECT e.district_id AS d "
					+ "FROM `events` e "
					+ "GROUP BY e.district_id "
					+ "ORDER BY COUNT(*) ASC "
					+ "LIMIT 1";
			
			try {
				Connection conn = DBConnect.getConnection() ;
				PreparedStatement st = conn.prepareStatement(sql) ;
				
				
				ResultSet res = st.executeQuery() ;
				res.first();
				Integer distretto= res.getInt("d");
					
				
				conn.close();
				return distretto;
			} catch(Throwable t) {
				t.printStackTrace();
				return null;
			}
			
			
		}
		
		public List<Event> CriminiDelGiorno(Integer anno, Integer mese, Integer giorno){
			String sql = "SELECT * "
					+ "FROM `events` e "
					+ "WHERE YEAR(e.reported_date)=? AND MONTH(e.reported_date)=? AND DAY(e.reported_date)=? ";
			try {
				Connection conn = DBConnect.getConnection() ;

				PreparedStatement st = conn.prepareStatement(sql) ;
				
				List<Event> list = new ArrayList<>() ;
				st.setInt(1, anno);
				st.setInt(2, mese);
				st.setInt(3, giorno);
				
				ResultSet res = st.executeQuery() ;
				
				while(res.next()) {
					try {
						list.add(new Event(res.getLong("incident_id"),
								res.getInt("offense_code"),
								res.getInt("offense_code_extension"), 
								res.getString("offense_type_id"), 
								res.getString("offense_category_id"),
								res.getTimestamp("reported_date").toLocalDateTime(),
								res.getString("incident_address"),
								res.getDouble("geo_lon"),
								res.getDouble("geo_lat"),
								res.getInt("district_id"),
								res.getInt("precinct_id"), 
								res.getString("neighborhood_id"),
								res.getInt("is_crime"),
								res.getInt("is_traffic")));
					} catch (Throwable t) {
						t.printStackTrace();
						System.out.println(res.getInt("id"));
					}
				}
				
				conn.close();
				return list ;

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null ;
			}
		}
}
