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

import com.techelevator.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.model.jdbc.JDBCParkDAO;
import com.techelevator.model.jdbc.JDBCReservationDAO;
import com.techelevator.model.jdbc.JDBCSiteDAO;


public class ReservationDAOIntegrationTest {
	private static SingleConnectionDataSource dataSource;
	private ReservationDAO dao;
	private CampgroundDAO campDAO;
	private ParkDAO parkDAO;
	private SiteDAO siteDAO;
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
		campDAO = new JDBCCampgroundDAO(dataSource);
		parkDAO = new JDBCParkDAO(dataSource);
		siteDAO = new JDBCSiteDAO(dataSource);
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void finds_new_reservation_by_id_after_being_created() throws ParseException{
		String dateStr = "06/27/2007"; 
		String dateStr2 = "07/27/2007"; 
		Date fromDate = (Date)formatter.parse(dateStr);
		Date toDate = (Date)formatter.parse(dateStr2);
		Reservation res = new Reservation();
		res = dao.createReservation(400, "Ruby", fromDate, toDate);
		Reservation newRes = dao.getReservationById(res.getReservationId());
		
		assertNotNull(res);
		assertEquals("Ruby", res.getName());
		assertEquals(res.getReservationId(), newRes.getReservationId());
	}
	
	@Test
	public void deletes_reservation() throws ParseException {
		String dateStr = "06/27/2007"; 
		String dateStr2 = "07/27/2007"; 
		Date fromDate = (Date)formatter.parse(dateStr);
		Date toDate = (Date)formatter.parse(dateStr2);
		Reservation res = new Reservation();
		res = dao.createReservation(400, "Ruby", fromDate, toDate);
		
		assertNotNull(res.getReservationId());
				
		dao.deleteReservationById(res.getReservationId());
		List <Reservation> results = dao.getAllReservations();
		
		assertFalse(results.contains(res));
	}
	
	@Test
	public void gets_reservation_by_name_after_updating_name () throws ParseException {
		String dateStr = "06/27/2007"; 
		String dateStr2 = "07/27/2007"; 
		Date fromDate = (Date)formatter.parse(dateStr);
		Date toDate = (Date)formatter.parse(dateStr2);
		Reservation res = new Reservation();
		res = dao.createReservation(400, "Ruby", fromDate, toDate);
		dao.updateReservationName(res.getReservationId(), "Kiki");
		List <Reservation> results = dao.getReservationByName("Kiki");
		List <Reservation> fullResults = dao.getAllReservations();
		
		assertNotNull(res);
		assertEquals("Kiki", results.get(0).getName());
		assertFalse(fullResults.contains("Ruby"));
		
	}

	@Test 
	public void gets_multiple_reservations() throws ParseException{
		String dateStr = "06/27/2007"; 
		String dateStr2 = "07/27/2007"; 
		Date fromDate = (Date)formatter.parse(dateStr);
		Date toDate = (Date)formatter.parse(dateStr2);
		Reservation res = new Reservation();
		Reservation res2 = new Reservation();
		res = dao.createReservation(400, "Ruby", fromDate, toDate);
		res2 = dao.createReservation(499, "Kiki", fromDate, toDate);
		List <Reservation> results = dao.getAllReservations();
		
		assertNotNull(results);
		assertTrue(results.size() >=2);
	}
	
	public void get_all_reservations_given_the_parkId() throws ParseException{
		String dateStr = "06/27/2007"; 
		String dateStr2 = "07/27/2007"; 
		Date fromDate = (Date)formatter.parse(dateStr);
		Date toDate = (Date)formatter.parse(dateStr2);
		Reservation res = new Reservation();
		Reservation res2 = new Reservation();
		//site = 621 camp = 7 park = 3
		res = dao.createReservation(621, "Ruby", fromDate, toDate);
		//site = 1 camp = 1 park = 1
		res2 = dao.createReservation(1, "Kiki", fromDate, toDate);
		Park park = parkDAO.getParkBySiteId(res.getSiteId());
		List <Reservation> results = dao.getAllReservationsForPark(park.getParkId());
		
		assertTrue(results.contains(res));
		assertFalse(results.contains(res2));
	}

	
	
}
