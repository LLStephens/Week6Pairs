package com.techelevator.model.jdbc;


import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Park;
import com.techelevator.model.ParkDAO;


public class JDBCParkDAO implements ParkDAO {
	private JdbcTemplate jdbcTemplate;

	public JDBCParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	@Override
	public Park createPark(String reservationName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Park> getAllParks() {
		ArrayList<Park> park = new ArrayList<>();
		String sqlGetAllParks = "Select * from park";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllParks);
		while(results.next()){
			Park thePark = mapRowToPark(results);
			park.add(thePark);
		}
		return park;
	}

	@Override
	public List<Park> searchParkByName(String nameSearch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Park getParkById(int reservationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateParkName(int reservationId, String reservationName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteParkById(int reservationId) {
		// TODO Auto-generated method stub
		
	}
	private Park mapRowToPark(SqlRowSet results) {
		Park thePark;
		thePark = new Park();
		thePark.setParkId(results.getInt("park_id"));
		thePark.setName(results.getString("name"));
		thePark.setLocation(results.getString("location"));
		thePark.setEstablishDate(results.getDate("establish_date"));
		thePark.setArea(results.getInt("area"));
		thePark.setVisitors(results.getInt("visitors"));
		thePark.setDescription(results.getString("description"));
		return thePark;
	}
	
}