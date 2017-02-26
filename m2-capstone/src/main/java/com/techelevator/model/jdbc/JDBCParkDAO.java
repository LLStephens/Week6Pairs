package com.techelevator.model.jdbc;


import java.util.ArrayList;
import java.util.Date;
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
	public Park createPark(String parkName, String location, Date establishDate, int area, int visitors, String description) {
		String sqlCreatPark = "insert into park (name, location, establish_date, area, visitors, description) " +
								"values(?,?,?,?,?,?)";
		jdbcTemplate.update(sqlCreatPark, parkName, location, establishDate, area, visitors, description);
		return getParkById(getNextParkId()-1);
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
	public List<Park> searchParkByName(String name) {
		ArrayList <Park> park = new ArrayList<>();
		String sqlGetParkByName = "select * from park where name = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetParkByName, name);
		while(results.next()){
			Park thePark = mapRowToPark(results);
			park.add(thePark);
		} return park;
	}
	

	@Override
	public Park getParkById(int parkId) {
		String sqlGetParkById = "select * from park where park_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetParkById, parkId);
		if(results.next()){
			Park park = mapRowToPark(results);
			return park;
		} else {
			return null;
		}
	}
	
	@Override
	public Park getParkBySiteId(int siteId){
		String sqlGetParkBySiteId = "select * from park p join campground c on p.park_id = c.park_id where c.campground_id in " +
										"(select c.campground_id from campground c join site s on s.campground_id = c.campground_id where site_id = ?)";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetParkBySiteId, siteId);
		if(results.next()) {
			Park park = mapRowToPark(results);
			return park;
		} else {
			return null;
		}
	}

	@Override
	public void updateParkName(int parkId, String parkName) {
		String sqlUpdateParkName = "UPDATE park " +
				"SET name = ? " +
				"WHERE park_id = ?";
		jdbcTemplate.update(sqlUpdateParkName, parkName, parkId);
	}

	@Override
	public void deleteParkById(int parkId) {
		String sqlRemovePark = "delete from park where park_id = ?";
		jdbcTemplate.update(sqlRemovePark, parkId);
				
	}

	private int getNextParkId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('park_park_id_seq')");
		if(nextIdResult.next()) {
			return nextIdResult.getInt(1);
		} else {
			throw new RuntimeException("Something went wrong while getting an id for the new park");
		}
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