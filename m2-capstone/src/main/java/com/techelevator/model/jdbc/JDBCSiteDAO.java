package com.techelevator.model.jdbc;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Campground;
import com.techelevator.model.Reservation;
import com.techelevator.model.Site;
import com.techelevator.model.SiteDAO;

public class JDBCSiteDAO implements SiteDAO {
	private JdbcTemplate jdbcTemplate;

	public JDBCSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void createSite(Site newSite) {
		String sqlCreateSite = "insert into site "+
								"(site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities) "+
								"values (?,?,?, ?, ?, ?, ?)";
		newSite.setSiteId(getNextSiteId());
		jdbcTemplate.update(sqlCreateSite, newSite.getSiteId(), newSite.getCampgroundId(), newSite.getSiteNumber(), newSite.getMaxOccupancy(), newSite.getIsAccessible(), newSite.getMaxRvLength(), newSite.getIsUtilities());

	}
	
	@Override
	public Site getSiteById(int siteId) {
		String sqlGetSiteById = "select * from site where site_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetSiteById, siteId);
		if(results.next()){
			Site site = mapRowToSite(results);
			return site;
		} else {
			return null;
		}
	}

	@Override
	public List<Site> getAllSites() {
		List<Site> site = new ArrayList<>();
		String sqlGetAllSites = "select * from site";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllSites);
		while(results.next()) {
			Site theSite = mapRowToSite(results);
			site.add(theSite);
		}
		return site;
	}
	
	@Override
	public List<Site> getSitesByParkId(int parkId){
		ArrayList<Site> site = new ArrayList();
		String sqlGetSitesByParkId = "Select * from site where park_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetSitesByParkId, parkId);
		while(results.next()){
			Site theSite = mapRowToSite(results);
			site.add(theSite);
		}
		return site;
	}
	
	@Override
	public List<Site> getSitesByCampgroundId(int campgroundId){
		ArrayList<Site> site = new ArrayList();
		String sqlGetSitesByCampgroundId = "Select * from site where campground_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetSitesByCampgroundId, campgroundId);
		while(results.next()){
			Site theSite = mapRowToSite(results);
			site.add(theSite);
		}
		return site;
	}
	
	@Override 
	public List<Site> getSiteByAvailabilityPerPark(int parkId, Date fromDate, Date toDate){
		ArrayList<Site> site = new ArrayList();
		String sqlGetSiteByAvailability = "Select * from site where campground_id in "
										+ "(select campground_id from campground where park_id = ?) "
										+ "and site_id not in (select site_id "
										+ "from reservation as r where r.from_date "
										+ "between ? and ? or r.to_date between ? and ? "
										+ "or (r.from_date <= ? and r.to_date >= ?)) Limit 5";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetSiteByAvailability, parkId, fromDate, toDate, fromDate, toDate, fromDate, toDate);
		while(results.next()){
			Site theSite = mapRowToSite(results);
			site.add(theSite);
		}
		return site;
	}
	
	@Override 
	public List<Site> getSiteByAvailabilityPerCampground(int campgroundId, Date fromDate, Date toDate){
		ArrayList<Site> site = new ArrayList();
		String sqlGetSiteByAvailability = "Select * from site where campground_id = ? and site_id not in " +
												"(select site_id from reservation as r where r.from_date between ? and ? " +
												"or r.to_date between ? and ? or (r.from_date <= ? and r.to_date >= ?)) Limit 5";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetSiteByAvailability, campgroundId, fromDate, toDate, fromDate, toDate, fromDate, toDate);
		while(results.next()){
			Site theSite = mapRowToSite(results);
			site.add(theSite);
		}
		return site;
	}

	@Override
	public void updateSiteMaxOccupancy(int siteId, int maxOccupancy) {
		String sqlUpdateSiteMaxOccupancy = "Update site set max_occupancy = ? where site_id = ?";
		jdbcTemplate.update(sqlUpdateSiteMaxOccupancy, maxOccupancy, siteId);
	}

	@Override
	public void deleteSiteById(int siteId) {
		String sqlRemoveSite = "DELETE FROM site WHERE site_id = ?";
		jdbcTemplate.update(sqlRemoveSite, siteId);		
	}
	
	private int getNextSiteId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('site_site_id_seq')");
		if(nextIdResult.next()) {
			return nextIdResult.getInt(1);
		} else {
			throw new RuntimeException("Something went wrong while getting an id for the new site");
		}
	}
	
	private Site mapRowToSite(SqlRowSet results) {
		Site theSite;
		theSite = new Site();
		theSite.setSiteId(results.getInt("site_id"));
		theSite.setCampgroundId(results.getInt("campground_id"));
		theSite.setSiteNumber(results.getInt("site_number"));
		theSite.setMaxOccupancy(results.getInt("max_occupancy"));
		theSite.setIsAccessible(results.getBoolean("accessible"));
		theSite.setMaxRvLength(results.getInt("max_rv_length"));
		theSite.setIsUtilities(results.getBoolean("utilities"));
		return theSite;
	}

}