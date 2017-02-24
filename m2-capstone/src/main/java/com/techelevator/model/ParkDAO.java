package com.techelevator.model;

import java.util.List;

public interface ParkDAO {
	// Create
	public Park createPark(String reservationName);
	
	// Read
	public List<Park> getAllParks();
	public List<Park> searchParkByName(String nameSearch);
	public Park getParkById(int reservationId);
	
	// Update
	public void updateParkName(int reservationId, String reservationName);
	
	// Delete
	public void deleteParkById(int reservationId);
}
	

