package com.techelevator.projects.model.jdbc;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.DepartmentDAO;

public class JDBCDepartmentDAOIntegrationTest {

	private static SingleConnectionDataSource dataSource;
	private DepartmentDAO dao;
	
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/projects");
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
		dao = new JDBCDepartmentDAO(dataSource);
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void created_department_has_correct_name() {

		String departmentName = "Marketing";

		Department savedDepartment = dao.createDepartment(departmentName);
		
		assertNotNull(savedDepartment);
		assertNotNull(savedDepartment.getId());
		assertEquals(departmentName, savedDepartment.getName());
	}

	@Test
	public void department_can_be_found_by_id_after_being_created() {
		String departmentName = "Marketing";
		
		Department savedDepartment = dao.createDepartment(departmentName);
		Department foundDepartment = dao.getDepartmentById(savedDepartment.getId());
		
		assertNotNull(foundDepartment);
		assertEquals(savedDepartment.getId(), foundDepartment.getId());
		assertEquals(savedDepartment.getName(), foundDepartment.getName());
	}
	
	@Test
	public void returns_dept_by_name() {
		String departmentName = "Marketing";
		
		Department savedDepartment = dao.createDepartment(departmentName);
		List<Department> foundDepartments = dao.searchDepartmentsByName(departmentName);
		
		assertNotNull(foundDepartments);
		assertEquals(1, foundDepartments.size());
		
		Department foundDepartment = foundDepartments.get(0);
		assertEquals(departmentName, foundDepartment.getName());
	}
	
	@Test
	public void changes_department_name_(){
		String departmentName = "Marketing";
		String newName = "Money";

		Department savedDepartment = dao.createDepartment(departmentName);
		
		Long departmentId = savedDepartment.getId();
		
		dao.updateDepartmentName(departmentId, newName);
		
		Department updatedDepartment = dao.getDepartmentById(departmentId);
		
		assertNotNull(updatedDepartment);
		assertEquals(newName, updatedDepartment.getName());
	}
	
	@Test   
	public void create_several_departments_and_get_verify_multiple_returned() {
		String departmentName = "Marketing";
		Department savedDepartment = dao.createDepartment(departmentName);
		String departmentName2 = "Bowling";
		Department savedDepartment2 = dao.createDepartment(departmentName2);

		List<Department> results = dao.getAllDepartments();
		
		assertNotNull(results);
		assertTrue("There should be at least 2 departments", results.size() >= 2);
		assertTrue(results.contains(savedDepartment));
		assertTrue(results.contains(savedDepartment2));
	}
	
	
	@Test  
	public void delete_a_department(){
		String departmentName = "Marketing";
		Department savedDepartment = dao.createDepartment(departmentName);
		
		assertNotNull(savedDepartment);
		
		Long departmentId = savedDepartment.getId();
		dao.deleteDepartmentById(departmentId);
		
		Department deletedDepartment = dao.getDepartmentById(departmentId);
		assertNull(deletedDepartment);
	}
	
	
	
}
