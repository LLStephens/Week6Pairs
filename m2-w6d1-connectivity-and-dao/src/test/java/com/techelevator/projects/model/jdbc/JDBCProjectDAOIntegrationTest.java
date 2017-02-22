package com.techelevator.projects.model.jdbc;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;
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
import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.EmployeeDAO;
import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.ProjectDAO;

public class JDBCProjectDAOIntegrationTest {

	private static SingleConnectionDataSource dataSource;
	private ProjectDAO dao;
	private EmployeeDAO empDao;
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
		dao = new JDBCProjectDAO(dataSource);
		empDao = new JDBCEmployeeDAO(dataSource);
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test  
	public void delete_a_project(){
		String projectName = "Howl";
		Project savedProject = dao.createProject(projectName);
		
		assertNotNull(savedProject);
		Long projectId = savedProject.getId();
		dao.removeProject(projectId);
		
		Project deletedProject = dao.getProjectById(projectId);
		assertNull(deletedProject);
	}
	
	
	@Test
	public void created_project_has_correct_name() {
		String projectName = "Howl";
		Project savedProject = dao.createProject(projectName);
		assertNotNull(savedProject);
		assertNotNull(savedProject.getId());
		assertEquals(projectName, savedProject.getName());
	}
	
	
	@Test
	public void returns_all_projects() {
		String projectName = "Howl";
		Project savedProject = dao.createProject(projectName);
		String projectName2 = "Leave";
		Project savedProject2 = dao.createProject(projectName2);

		List<Project> results = dao.getAllActiveProjects();
		
		assertNotNull(results);
		assertTrue("There should be at least 2 projects", results.size() >= 2);
		assertTrue(results.contains(savedProject));
		assertTrue(results.contains(savedProject2));
	}
	
	@Test
	public void project_can_be_found_by_id_after_being_created() {
		String projectName = "Howl";
		
		Project savedProject = dao.createProject(projectName);
		Project foundProject = dao.getProjectById(savedProject.getId());
		
		assertNotNull(foundProject);
		assertEquals(savedProject.getId(), foundProject.getId());
		assertEquals(savedProject.getName(), foundProject.getName());
	}
	

	@Test
	public void employee_gets_added_to_project() {
		String projectName = "Howl";
		Project savedProject = dao.createProject(projectName);
		Project foundProject = dao.getProjectById(savedProject.getId());
		Long proNum = foundProject.getId();
		
		Employee employee = new Employee();
		employee = empDao.createEmployee("Charlie", "Bucket", LocalDate.now(), 'M', LocalDate.now());

		Long empNum = employee.getId();
		dao.addEmployeeToProject(proNum, empNum);
		

		empDao.getEmployeesByProjectId(foundProject.getId());
		List <Employee> results = empDao.getEmployeesByProjectId(foundProject.getId());
		
		assertNotNull(foundProject);
		assertEquals(projectName, savedProject.getName());
		assertEquals(results.get(0).getFirstName(), employee.getFirstName());
		assertEquals(results.get(0).getId(), employee.getId());
		
	}
	
	@Test 
	public void employee_gets_removed_from_project(){
		String projectName = "Howl";
		
		Project savedProject = dao.createProject(projectName);
		Project foundProject = dao.getProjectById(savedProject.getId());
		Long proNum = foundProject.getId();
		
		Employee employee = new Employee();
		employee = empDao.createEmployee("Charlie", "Bucket", LocalDate.now(), 'M', LocalDate.now());
		Employee employee2 = empDao.createEmployee("Marlie", "Rucket", LocalDate.now(), 'F', LocalDate.now());

		Long empNum = employee.getId();
		dao.addEmployeeToProject(proNum, empNum);
		Long emp2Num = employee2.getId();
		dao.addEmployeeToProject(proNum, emp2Num);
		
		List <Employee> results = empDao.getEmployeesByProjectId(foundProject.getId());
		
		assertNotNull(foundProject);
		assertEquals(projectName, savedProject.getName());
		assertEquals(results.get(0).getFirstName(), employee.getFirstName());
		assertEquals(results.get(0).getId(), employee.getId());
		assertEquals(results.get(1).getFirstName(), employee2.getFirstName());
		assertEquals(results.get(1).getId(), employee2.getId());
		
		dao.removeEmployeeFromProject(proNum, empNum);
		Project deletedEmployeeFromProject = dao.getProjectById(proNum);
		List <Employee> results2 = empDao.getEmployeesByProjectId(deletedEmployeeFromProject.getId());

		assertNotNull(deletedEmployeeFromProject);
		assertNotNull(results2);
		assertEquals(results2.get(0).getFirstName(), employee2.getFirstName());

		

		
	}

}


