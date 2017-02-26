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

import com.techelevator.model.jdbc.JDBCParkDAO;
import com.techelevator.model.jdbc.JDBCReservationDAO;
import com.techelevator.model.jdbc.JDBCSiteDAO;

public class SiteDAOIntegrationTest {
	private static SingleConnectionDataSource dataSource;
	private SiteDAO dao;
	private ParkDAO parkDAO;
	
	
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
		dao = new JDBCSiteDAO(dataSource);
		parkDAO = new JDBCParkDAO(dataSource);
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void finds_new_site_by_siteId_after_being_created() {
		Site newSite = getSite(7,77, 99, true, 99, false);
		dao.createSite(newSite);
		Site savedSite = dao.getSiteById(newSite.getSiteId());
		
		assertNotEquals(null, newSite.getSiteId());
		assertSitesAreEqual(newSite, savedSite);
	}
	
	@Test
	public void returns_multiple_sites () {
		Site newSite = getSite(7,77, 99, true, 99, false);
		Site newSite2 = getSite(7,78, 99, true, 99, false);
		dao.createSite(newSite);
		dao.createSite(newSite2);
		
		Site savedSite = dao.getSiteById(newSite.getSiteId());
		
		List<Site> results = dao.getAllSites();
		
		assertNotNull(results);
		assertTrue(results.contains(newSite));
		assertTrue(results.contains(newSite2));		
	}
	
	@Test 
	public void returns_site_by_campground_id () {
		Site newSite = getSite(7,77, 99, true, 99, false);
		dao.createSite(newSite);
		List<Site> results = dao.getSitesByCampgroundId(newSite.getCampgroundId());
		
		assertNotNull(results);
		assertEquals(newSite.getCampgroundId(), results.get(0).getCampgroundId());	
		assertTrue(results.contains(newSite));
	}
	
	@Test
	public void gets_sites_given_park_id () {
		//campground 7, park 3
		Site newSite = getSite(7,77, 99, true, 94, false);
		Site newSite2 = getSite(7,78, 99, true, 94, false);
		dao.createSite(newSite);
		dao.createSite(newSite2);
		
		Park park = parkDAO.getParkBySiteId(newSite.getSiteId());
		Park park2 = parkDAO.getParkBySiteId(newSite2.getSiteId());
		
		List <Site> results = dao.getSitesByParkId(park.getParkId());
		assertTrue(results.contains(newSite));
		assertTrue(results.contains(newSite2));
	}
	
	@Test  //test works if JDBCSiteDAO limit of 5 removed
	public void returns_all_site_with_availability_for_given_campground() throws ParseException{
		Site newSite = getSite(7,77, 99, true, 94, false);
		Site newSite2 = getSite(7,78, 99, true, 94, false);
		dao.createSite(newSite);
		dao.createSite(newSite2);
		
		String dateStr = "06/27/2007"; 
		String dateStr2 = "07/27/2007"; 
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); 
		Date fromDate = (Date)formatter.parse(dateStr);
		Date toDate = (Date)formatter.parse(dateStr2);
		
		ReservationDAO reservationDao = new JDBCReservationDAO(dataSource);
		Reservation reservation = new Reservation();
		reservationDao.createReservation(newSite2.getSiteId(), "David Bowie", fromDate, toDate);

		List<Site> results = dao.getSiteByAvailabilityPerCampground(newSite.getCampgroundId(), fromDate, toDate);
		
		assertNotNull(results);
		assertTrue(results.contains(newSite));
		assertFalse(results.contains(newSite2));
	}
	
	
	@Test
	public void changes_max_occupancy () {
		Site newSite = getSite(7,77, 99, true, 94, false);
		dao.createSite(newSite);
		dao.updateSiteMaxOccupancy(newSite.getSiteId(), 7);
		Site updatedSite = dao.getSiteById(newSite.getSiteId());
		
		assertEquals(7, updatedSite.getMaxOccupancy());	
	}
	
	@Test
	public void deletesSite() {
		Site newSite = getSite(7,77, 99, true, 94, false);
		dao.createSite(newSite);
		dao.deleteSiteById(newSite.getSiteId());
		Site result = dao.getSiteById(newSite.getSiteId());
		
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
	
	private void assertSitesAreEqual(Site expected, Site actual){
		assertEquals(expected.getSiteId(), actual.getSiteId());
		assertEquals(expected.getCampgroundId(), actual.getCampgroundId());
		assertEquals(expected.getSiteNumber(), actual.getSiteNumber());
	}
	
}





//protected DataSource getDataSource() {
//return dataSource;
//}
