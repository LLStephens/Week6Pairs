package com.techelevator.model;

import java.util.Date;
import java.util.List;


public interface ReservationDAO {
	// Create
	public Reservation createReservation(int siteId, String reservationName, Date fromDate, Date toDate);
	
	// Read
	public List<Reservation> getAllReservations();
	public List<Reservation> getReservationByName(String nameSearch);
	public Reservation getReservationById(int reservationId);
	public List<Reservation> getAllReservationsForPark(int parkId);
	
	// Update
	public void updateReservationName(int reservationId, String reservationName);
	
	// Delete
	public void deleteReservationById(int reservationId);

	

}
	

