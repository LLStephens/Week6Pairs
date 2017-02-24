package com.techelevator.model;

import java.util.List;


public interface SiteDAO {
	// Create
	public Site createSite(String siteName);
	
	// Read
	public List<Site> getAllSites();
	public List<Site> searchSiteByName(String nameSearch);
	public Site getSiteById(int siteId);
	public List<Site> getSitesByCampgroundId(int campgroundId);
	
	// Update
	public void updateSiteName(int siteId, String siteName);
	
	// Delete
	public void deleteSiteById(int siteId);

}
