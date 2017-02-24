package com.techelevator.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Campground;
import com.techelevator.model.CampgroundDAO;


public class JDBCCampgroundDAO implements CampgroundDAO {
	private JdbcTemplate jdbcTemplate;

	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Campground createCampground(String campgroundName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Campground> getAllCampgrounds() {
		ArrayList<Campground> campground = new ArrayList();
		String sqlGetAllCampgrounds = "Select * from campground";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllCampgrounds);
		while(results.next()){
			Campground theCampground = mapRowToCampground(results);
			campground.add(theCampground);
		}
		return campground;
	}
	
	@Override
	public List<Campground> getCampgroundsByParkId(int parkId) {
		ArrayList<Campground> campground = new ArrayList();
		String sqlGetCampgroundsByParkId = "Select * from campground where park_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetCampgroundsByParkId, parkId);
		while(results.next()){
			Campground theCampground = mapRowToCampground(results);
			campground.add(theCampground);
		}
		return campground;
	}

	@Override
	public List<Campground> searchCampgroundByName(String nameSearch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Campground getCampgroundById(int campgroundId) {
		String sqlGetCampgroundById = "Select * from campground where campground_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetCampgroundById, campgroundId);
		if(results.next()) {
			Campground campground = mapRowToCampground(results);
			return campground;
		} else {
		return null;
		}
	}

	@Override
	public void updateCampgroundName(int campgroundId, String campgroundName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteCampgroundById(int campgroundId) {
		// TODO Auto-generated method stub
		
	}
	
	private Campground mapRowToCampground(SqlRowSet results) {
		Campground theCampground;
		theCampground = new Campground();
		theCampground.setCampgroundId(results.getInt("campground_id"));
		theCampground.setParkId(results.getInt("park_id"));
		theCampground.setName(results.getString("name"));
		theCampground.setOpeningMonth(results.getString("open_from_mm"));
		theCampground.setClosingMonth(results.getString("open_to_mm"));
		theCampground.setDailyFee(results.getBigDecimal("daily_fee"));
		return theCampground;
	}

}
