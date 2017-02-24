package com.techelevator.model.jdbc;


import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Campground;
import com.techelevator.model.Site;
import com.techelevator.model.SiteDAO;

public class JDBCSiteDAO implements SiteDAO {
	private JdbcTemplate jdbcTemplate;

	public JDBCSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Site createSite(String siteName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Site> getAllSites() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Site> getSitesByCampgroundId(int campgroundId){
		ArrayList<Site> site = new ArrayList();
		String sqlGetSitesByCampgroundId = "Select * from site where campground_id = ? Limit 5";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetSitesByCampgroundId, campgroundId);
		while(results.next()){
			Site theSite = mapRowToSite(results);
			site.add(theSite);
		}
		return site;
	}

	@Override
	public List<Site> searchSiteByName(String nameSearch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Site getSiteById(int siteId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateSiteName(int siteId, String siteName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteSiteById(int siteId) {
		// TODO Auto-generated method stub
		
	}
	private Site mapRowToSite(SqlRowSet results) {
		Site theSite;
		theSite = new Site();
		theSite.setSiteId(results.getString("site_id").toString());
		theSite.setCampgroundId(results.getInt("campground_id"));
		theSite.setSiteNumber(results.getInt("site_number"));
		theSite.setMaxOccupancy(results.getInt("max_occupancy"));
		theSite.setAccessible(results.getBoolean("accessible"));
		theSite.setMaxRvLength(results.getInt("max_rv_length"));
		theSite.setUtilities(results.getBoolean("utilities"));
		return theSite;
	}

}