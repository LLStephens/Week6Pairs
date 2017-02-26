package com.techelevator.model;

import java.util.Date;
import java.util.List;


public interface SiteDAO {
	// Create
//	public Site createSite(int campgroundId, int siteNumber);
	public void createSite(Site newSite);
	// Read
	public Site getSiteById(int siteId);
	public List<Site> getAllSites();
	public List<Site> getSitesByCampgroundId(int campgroundId);
	public List<Site> getSiteByAvailabilityPerCampground(int campgroundId, Date fromDate, Date toDate);
	public List<Site> getSiteByAvailabilityPerPark(int parkId, Date fromDate, Date toDate);
	public List<Site> getSitesByParkId(int parkId);
	
	// Update
	public void updateSiteMaxOccupancy(int siteId, int maxOccupancy);
	
	// Delete
	public void deleteSiteById(int siteId);

	

	

	

}
