package com.techelevator.model;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.model.jdbc.JDBCCampgroundDAO;


public class CampgroundDAOIntegrationTest {
	private static SingleConnectionDataSource dataSource;
	private CampgroundDAO dao;
	
	
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
		dao = new JDBCCampgroundDAO(dataSource);
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void returns_new_campground_by_id_after_being_created (){
		BigDecimal bD = new BigDecimal(12);
		Campground camp = dao.createCampground(3, "Ulysses", "01", "12", bD);
		Campground newCamp = dao.getCampgroundById(camp.getCampgroundId());
		
		assertNotNull(newCamp);
		assertEquals(camp.getName(), newCamp.getName());
		assertEquals(camp.getCampgroundId(), newCamp.getCampgroundId());
	}
	
	@Test
	public void returns_new_campground_by_name_after_being_updated (){
		BigDecimal bD = new BigDecimal(12);
		Campground camp = dao.createCampground(3, "Ulysses", "01", "12", bD);
		Campground newCamp = dao.getCampgroundById(camp.getCampgroundId());
		List <Campground> results = dao.searchCampgroundByName(newCamp.getName());
		assertEquals("Ulysses", results.get(0).getName());
		
		dao.updateCampgroundName(newCamp.getCampgroundId(), "Gymnasium");
		Campground result = dao.getCampgroundById(newCamp.getCampgroundId());
		List <Campground> results2 = dao.searchCampgroundByName(result.getName());
		assertEquals("Gymnasium", results2.get(0).getName());
	}
	
	@Test
	public void gets_multiple_campgrounds(){
		BigDecimal bD = new BigDecimal(12);
		Campground camp = dao.createCampground(3, "Ulysses", "01", "12", bD);
		Campground newCamp = dao.getCampgroundById(camp.getCampgroundId());
		List <Campground> results = dao.getAllCampgrounds();
		
		assertNotNull(results);
		assertTrue(results.size() >=2);;
	}
	
	@Test
	public void removes_campground(){
		BigDecimal bD = new BigDecimal(12);
		Campground camp = dao.createCampground(3, "Ulysses", "01", "12", bD);
		Campground newCamp = dao.getCampgroundById(camp.getCampgroundId());
		
		assertNotNull(newCamp);
		
		dao.deleteCampgroundById(newCamp.getCampgroundId());
		Campground result = dao.getCampgroundById(newCamp.getCampgroundId());
		
		assertNull(result);
		
	}
	
}
