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
	public List<Reservation> getReservationByAvailability(int campgroundId, Date fromDate, Date toDate){
		ArrayList<Reservation> reservation = new ArrayList();
		String sqlGetReservationByAvailability = "Select * from site where campground_id = ? and site_id not in(select site_id from reservation as r where r.from_date between ? and ? or r.to_date between ? and ? or (r.from_date <= ? and r.to_date >= ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetReservationByAvailability, campgroundId);
		while(results.next()){
			Reservation theReservation = mapRowToReservation(results);
			reservation.add(theReservation);
		}
		return reservation;
	}
	
	@Override
	public Reservation createReservation(String reservationName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reservation> getAllReservations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reservation> searchReservationByName(String nameSearch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reservation getReservationById(int reservationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateReservationName(int reservationId, String reservationName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteReservationById(int reservationId) {
		// TODO Auto-generated method stub
		
	}
	
	private Reservation mapRowToReservation(SqlRowSet results) {
		Reservation theReservation = new Reservation();;
		theReservation.setReservationId(results.getInt("reservation_id"));
		theReservation.setSiteId(results.getInt("site_id"));
		theReservation.setCreateDate(results.getDate("create_date"));
		theReservation.setName(results.getString("name"));
		theReservation.setFromDate(results.getDate("from_date"));
		theReservation.setToDate(results.getDate("to_date"));
		return theReservation;
	}
}
