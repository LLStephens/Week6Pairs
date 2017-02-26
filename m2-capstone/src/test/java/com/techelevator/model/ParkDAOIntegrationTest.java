package com.techelevator.model;

import static org.junit.Assert.*;

import java.math.BigDecimal;
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

import com.techelevator.model.jdbc.JDBCSiteDAO;
import com.techelevator.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.model.jdbc.JDBCParkDAO;

public class ParkDAOIntegrationTest {
	private static SingleConnectionDataSource dataSource;
	private ParkDAO dao;
	private SiteDAO siteDAO;
	private CampgroundDAO campDAO;
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
		dao = new JDBCParkDAO(dataSource);
		siteDAO = new JDBCSiteDAO(dataSource);
		campDAO = new JDBCCampgroundDAO(dataSource);
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void returns_park_by_id_after_being_created() throws ParseException{
		String dateStr = "06/27/2007"; 
		Date estDate = (Date)formatter.parse(dateStr);
		Park park = dao.createPark("Reynolds", "Ohio", estDate, 50, 10, "Former elementary school playground");

		Park newPark = dao.getParkById(park.getParkId());
		
		assertNotNull(newPark);
		assertEquals(park.getName(), newPark.getName());
		assertEquals(park.getParkId(), newPark.getParkId());
	}
	
	@Test
	public void returns_park_by_name_after_being_created() throws ParseException{
		String dateStr = "06/27/2007"; 
		Date estDate = (Date)formatter.parse(dateStr);
		Park park = dao.createPark("Reynolds", "Ohio", estDate, 50, 10, "Former elementary school playground");

		List<Park> results = dao.searchParkByName(park.getName());
		
		assertNotNull(results);
		assertTrue(results.contains(park));
	}
	
	@Test
	public void returns_multiple_parks() throws ParseException{
		String dateStr = "06/27/2007"; 
		Date estDate = (Date)formatter.parse(dateStr);
		Park park = dao.createPark("Reynolds", "Ohio", estDate, 50, 10, "Former elementary school playground");
		Park park2 = dao.createPark("Bellflower", "Ohio", estDate, 50, 10, "Current elementary school playground");
		List <Park> results = dao.getAllParks();
		
		assertNotNull(results);
		assertTrue(results.size() >=2);
	}
	
	@Test
	public void updates_park_name () throws ParseException{
		String dateStr = "06/27/2007"; 
		Date estDate = (Date)formatter.parse(dateStr);
		Park park = dao.createPark("Reynolds", "Ohio", estDate, 50, 10, "Former elementary school playground");
		List<Park> results = dao.searchParkByName(park.getName());
		
		assertTrue(results.contains(park));
		assertEquals("Reynolds", results.get(0).getName());
		
		dao.updateParkName(park.getParkId(), "Formerly Reynolds");
		Park result = dao.getParkById(park.getParkId());
		List<Park> results2 = dao.searchParkByName(result.getName());
		
		assertNotNull(results2);
		assertEquals("Formerly Reynolds" ,results2.get(0).getName());
		assertEquals(result.getName(), results2.get(0).getName());
		
	}
	
	@Test
	public void returns_park_given_siteId() throws ParseException{
		String dateStr = "06/27/2007"; 
		Date estDate = (Date)formatter.parse(dateStr);
		Park park = dao.createPark("Reynolds", "Ohio", estDate, 50, 10, "Former elementary school playground");
		Park savedPark = dao.getParkById(park.getParkId());
		
		BigDecimal bD = new BigDecimal(12);
		Campground camp = campDAO.createCampground(savedPark.getParkId(), "Gymnasium", "01", "12", bD);
		
		Site newSite = getSite(camp.getCampgroundId(), 1000, 99, true, 99, false);
		siteDAO.createSite(newSite);
		Site savedSite = siteDAO.getSiteById(newSite.getSiteId());
		
		Park newPark = dao.getParkBySiteId(savedSite.getSiteId());
		
		assertEquals(camp.getParkId(), newPark.getParkId());
	}
	
	@Test
	public void remove_Park_by_id () throws ParseException{
		String dateStr = "06/27/2007"; 
		Date estDate = (Date)formatter.parse(dateStr);
		Park park = dao.createPark("Reynolds", "Ohio", estDate, 50, 10, "Former elementary school playground");

		Park newPark = dao.getParkById(park.getParkId());
		
		assertNotNull(newPark);
		assertEquals(park.getName(), newPark.getName());
		assertEquals(park.getParkId(), newPark.getParkId());
		
		dao.deleteParkById(newPark.getParkId());
		Park result = dao.getParkById(newPark.getParkId());
		assertNull(result);
	}
	
	
	private Site getSite(int campgroundId, int siteNumber, int maxOccupancy, boolean accessible, int maxRvLength, boolean utilities){
		Site theSite = new Site();
		theSite.setCampgroundId(campgroundId);
		theSite.setSiteNumber(siteNumber);
		theSite.setMaxOccupancy(maxOccupancy);
		theSite.setIsAccessible(accessible);
		theSite.setMaxRvLength(maxRvLength);
		theSite.setIsUtilities(utilities);
		return theSite;
	}
	
}
