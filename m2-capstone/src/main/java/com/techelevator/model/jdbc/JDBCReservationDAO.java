package com.techelevator.model.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDAO;


public class JDBCReservationDAO implements ReservationDAO{
	private JdbcTemplate jdbcTemplate;

	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Override
	public Reservation createReservation(int siteId, String reservationName, Date fromDate, Date toDate) {
		Reservation reservation = new Reservation();
		String sqlCreateReservation = "Insert into reservation (site_id, name, from_date, to_date) " 
									+ "values (?, ?, ?, ?)";
		jdbcTemplate.update(sqlCreateReservation, siteId, reservationName, fromDate, toDate);					
				
		return getReservationById((getNextReservationId())-1);
	}

	@Override
	public List<Reservation> getAllReservationsForPark(int parkId) {
		ArrayList<Reservation> reservation = new ArrayList<>();
		String sqlGetAllReservations = "SELECT * " +
										"FROM reservation WHERE park_id = ? Limit 30"; // BONUS need to join to work
																						//And supposed to limit by 30 days, not 30
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllReservations, parkId);
		while(results.next()) {
			Reservation theReservation = mapRowToReservation(results);
			reservation.add(theReservation);

		}
		return reservation;
	}

	@Override
	public List<Reservation> getReservationByName(String nameSearch) {
		ArrayList<Reservation> reservation = new ArrayList<>();
		String sqlGetReservationByName = "Select * from reservation where name = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetReservationByName, nameSearch);
		while(results.next()){
			Reservation theReservation = mapRowToReservation(results);
			reservation.add(theReservation);
		}
		return reservation;
	}

	@Override
	public Reservation getReservationById(int reservationId) {
		String sqlGetReservationById = "select * from reservation where reservation_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetReservationById, reservationId);
		if(results.next()){
			Reservation reservation = mapRowToReservation(results);
			return reservation;
		} else {
			return null;
		}
	}

	@Override
	public void updateReservationName(int reservationId, String reservationName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteReservationById(int reservationId) {
		String sqlRemoveReservation = "delete from reservation where reservation_id = ?";
		jdbcTemplate.update(sqlRemoveReservation, reservationId);
		
	}


	@Override
	public List<Reservation> getAllReservations() {
		List<Reservation> res = new ArrayList<>();
		String sqlGetAllReservations = "select * from reservation";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllReservations);
		while(results.next()){
			Reservation theRes = mapRowToReservation(results);
			res.add(theRes);
		}
		return res;
	}
	
	private int getNextReservationId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('reservation_reservation_id_seq')");
		if(nextIdResult.next()) {
			return nextIdResult.getInt(1);
		} else {
			throw new RuntimeException("Something went wrong while getting an id for the new reservation");
		}
	}
	
	
	private Reservation mapRowToReservation(SqlRowSet results) {
		Reservation theReservation;
		theReservation = new Reservation();
		theReservation.setReservationId(results.getInt("reservation_id"));
		theReservation.setSiteId(results.getInt("site_id"));
		theReservation.setName(results.getString("name"));
		theReservation.setFromDate(results.getDate("from_date"));
		theReservation.setToDate(results.getDate("to_date"));

		return theReservation;
	}
}
