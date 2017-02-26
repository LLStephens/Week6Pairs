package com.techelevator.model.jdbc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Campground;
import com.techelevator.model.CampgroundDAO;
import com.techelevator.model.Reservation;


public class JDBCCampgroundDAO implements CampgroundDAO {
	private JdbcTemplate jdbcTemplate;

	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Campground createCampground(int parkId, String name, String openFromMm, String openToMm, BigDecimal dailyFee) {
		String sqlCreateCampground = "insert into campground (park_id, name, open_from_mm, open_to_mm, daily_fee) " +
									"values (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sqlCreateCampground, parkId, name, openFromMm, openToMm, dailyFee);
		return getCampgroundById(getNextCampgroundId()-1);
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
		ArrayList<Campground> camp = new ArrayList<>();
		String sqlGetCampgroundByName = "Select * from campground where name = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetCampgroundByName, nameSearch);
		while(results.next()){
			Campground theCamp = mapRowToCampground(results);
			camp.add(theCamp);
		}
		return camp;
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
		String sqlUpdateCampName = "UPDATE campground " +
				"SET name = ? " +
				"WHERE campground_id = ?";
		jdbcTemplate.update(sqlUpdateCampName, campgroundName, campgroundId);
	}

	@Override
	public void deleteCampgroundById(int campgroundId) {
		String sqlRemoveCampground = "delete from campground where campground_id = ?";
		jdbcTemplate.update(sqlRemoveCampground, campgroundId);
		
	}
	
	private int getNextCampgroundId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("Select nextval('campground_campground_id_seq')");
		if(nextIdResult.next()) {
			return nextIdResult.getInt(1);
		} else {
			throw new RuntimeException("Something went wrong while getting an id for the new camp");
		}
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
