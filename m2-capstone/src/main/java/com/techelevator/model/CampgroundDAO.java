package com.techelevator.model;

import java.math.BigDecimal;
import java.util.List;


public interface CampgroundDAO {
	// Create
	public 	Campground createCampground(int parkId, String name, String openFromMm, String openToMm, BigDecimal dailyFee);
	
	// Read
	public List<Campground> getAllCampgrounds();
	public List<Campground> searchCampgroundByName(String nameSearch);
	public Campground getCampgroundById(int campgroundId);
	public List<Campground> getCampgroundsByParkId(int parkId);

	
	// Update
	public void updateCampgroundName(int campgroundId, String campgroundName);
	
	// Delete
	public void deleteCampgroundById(int campgroundId);


}

