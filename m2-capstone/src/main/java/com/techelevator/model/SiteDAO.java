package com.techelevator.model;

import java.util.Date;
import java.util.List;


public interface SiteDAO {
	// Create
	public Site createSite(String siteName);
	
	// Read
	public List<Site> getAllSites();
	public List<Site> searchSiteByName(String nameSearch);
	public Site getSiteById(int siteId);
	public List<Site> getSitesByCampgroundId(int campgroundId);
	public List<Site> getSiteByAvailability(int campgroundId, Date fromDate, Date toDate);
	public List<Site> getSitesByParkId(int parkId);
	
	// Update
	public void updateSiteName(int siteId, String siteName);
	
	// Delete
	public void deleteSiteById(int siteId);

	

	

}
