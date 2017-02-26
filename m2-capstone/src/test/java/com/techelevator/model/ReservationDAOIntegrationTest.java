package com.techelevator.model;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.model.jdbc.JDBCReservationDAO;
import com.techelevator.model.jdbc.JDBCSiteDAO;


public class ReservationDAOIntegrationTest {
	private static SingleConnectionDataSource dataSource;
	private ReservationDAO dao;
	private DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); 

	
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}
	
	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}
	
	@Before
	public void setup() {
		dao = new JDBCReservationDAO(dataSource);
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void finds_new_reservation_after_being_created() throws ParseException{
		String dateStr = "06/27/2007"; 
		String dateStr2 = "07/27/2007"; 
		Date fromDate = (Date)formatter.parse(dateStr);
		Date toDate = (Date)formatter.parse(dateStr2);
		Reservation res = new Reservation();
		res = dao.createReservation(400, "Ruby", fromDate, toDate);
		
		assertNotNull(res);
		assertEquals("Ruby", res.getName());
	}
	
	@Test
	public void deletes_reservation() throws ParseException {
		String dateStr = "06/27/2007"; 
		String dateStr2 = "07/27/2007"; 
		Date fromDate = (Date)formatter.parse(dateStr);
		Date toDate = (Date)formatter.parse(dateStr2);
		Reservation res = new Reservation();
		res = dao.createReservation(400, "Ruby", fromDate, toDate);
		dao.deleteReservationById(res.getReservationId());
		List <Reservation> results = dao.getAllReservations();
		
		assertFalse(results.contains(res));
		
	}
	
	private Reservation getReservation(int reservationId, int siteId, String name, Date fromDate, Date toDate, Date createDate){
		Reservation theReservation = new Reservation();
		theReservation.setSiteId(siteId);
		theReservation.setName(name);
		theReservation.setFromDate(fromDate);
		theReservation.setToDate(toDate);
		theReservation.setCreateDate(createDate);
		return theReservation;
	}
	
	private void assertReservationsAreEqual(Reservation expected, Reservation actual){
		assertEquals(expected.getReservationId(), actual.getReservationId());
		assertEquals(expected.getSiteId(), actual.getSiteId());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getFromDate(), actual.getFromDate());
		assertEquals(expected.getToDate(), actual.getToDate());
		assertEquals(expected.getCreateDate(), actual.getCreateDate());
		
	}
	
	
	
	
}
