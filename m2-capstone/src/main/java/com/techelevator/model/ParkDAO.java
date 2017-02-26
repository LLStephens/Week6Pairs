package com.techelevator.model;

import java.util.Date;
import java.util.List;

public interface ParkDAO {
	// Create
	public Park createPark(String parkName, String location, Date establishDate, int area, int visitors, String description);
	
	// Read
	public List<Park> getAllParks();
	public List<Park> searchParkByName(String name);
	public Park getParkById(int parkId);
	public Park getParkBySiteId(int siteId);
	
	// Update
	public void updateParkName(int parkId, String parkName);
	
	// Delete
	public void deleteParkById(int parkId);

}
	

