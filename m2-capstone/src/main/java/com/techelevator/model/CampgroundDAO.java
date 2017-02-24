package com.techelevator.model;

import java.util.List;


public interface CampgroundDAO {
	// Create
	public Campground createCampground(String campgroundName);
	
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

