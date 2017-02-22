package com.techelevator.projects.model.jdbc;


import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.DepartmentDAO;
import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.EmployeeDAO;
import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.ProjectDAO;
import com.techelevator.projects.model.jdbc.JDBCDepartmentDAO;
import com.techelevator.projects.model.jdbc.JDBCEmployeeDAO;

public class JDBCEmployeeDAOIntegrationTest {

	private static SingleConnectionDataSource dataSource;
	private EmployeeDAO dao;
	private ProjectDAO projectDao;
	private DepartmentDAO deptDao;
	
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
		dao = new JDBCEmployeeDAO(dataSource);
		projectDao = new JDBCProjectDAO(dataSource);
		deptDao = new JDBCDepartmentDAO(dataSource);
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void save_new_employee_and_read_it_back() throws SQLException {
		Employee theEmployee = dao.createEmployee("Charlie", "Bucket", LocalDate.now(), 'M', LocalDate.now());
		
		List<Employee> results = dao.searchEmployeesByName(theEmployee.getFirstName(), theEmployee.getLastName());
		
		assertNotNull(results);
		assertNotEquals(null, theEmployee.getId());
		assertEquals(results.get(0).getFirstName(),theEmployee.getFirstName());
		
	}
	
	@Test
	public void deletes_employee(){
		Employee theEmployee = dao.createEmployee("Charlie", "Bucket", LocalDate.now(), 'M', LocalDate.now());
		
		List<Employee> results = dao.searchEmployeesByName(theEmployee.getFirstName(), theEmployee.getLastName());
		
		assertNotNull(results);
		assertNotEquals(null, theEmployee.getId()); 
		assertEquals(results.get(0).getFirstName(),theEmployee.getFirstName());

		Long employeeId = theEmployee.getId();
		dao.removeEmployee(employeeId);
		Employee deletedEmployee = dao.getEmployeeById(employeeId);
		assertNull(deletedEmployee);

	}
	
	@Test
	public void retrieves_employee_by_id(){
		Employee theEmployee = dao.createEmployee("Charlie", "Bucket", LocalDate.now(), 'M', LocalDate.now());
		Long employeeId = theEmployee.getId();
		Employee savedEmployee = dao.getEmployeeById(employeeId);
		

		assertNotNull(savedEmployee.getId());
		assertEquals(theEmployee.getFirstName(), savedEmployee.getFirstName());
		assertEquals(employeeId, savedEmployee.getId());
	
	}
	
	@Test
	public void retrieves_employee_by_name(){
		Employee theEmployee = dao.createEmployee("Charlie", "Bucket", LocalDate.now(), 'M', LocalDate.now());
		List<Employee> results = dao.searchEmployeesByName(theEmployee.getFirstName(), theEmployee.getLastName());
		
		Long employeeId = theEmployee.getId();

		assertNotNull(results);
		assertEquals(theEmployee.getFirstName(),results.get(0).getFirstName());
		assertEquals(theEmployee.getLastName(), results.get(0).getLastName());
		assertEquals(employeeId, results.get(0).getId());
	
	}
	@Test 
	public void retrieves_employee_by_deptId () {
		Employee theEmployee = dao.createEmployee("Flo", "Henderson", LocalDate.now(), 'F', LocalDate.now());
		Department department= deptDao.createDepartment("Creative");
		dao.changeEmployeeDepartment(theEmployee.getId(), department.getId());
		List <Employee> results = dao.getEmployeesByDepartmentId(department.getId());

		assertNotNull(results);
		assertEquals(1, results.size());
		assertEquals(theEmployee.getFirstName(),results.get(0).getFirstName());
		assertEquals(theEmployee.getLastName(), results.get(0).getLastName());
		
	}
	
	@Test
	public void returns_all_employees() {
		Employee theEmployee = dao.createEmployee("Charlie", "Bucket", LocalDate.now(), 'M', LocalDate.now());
		Employee theEmployee2 = dao.createEmployee("Marlie", "Rucket", LocalDate.now(), 'F', LocalDate.now());
		
		List<Employee> results = dao.searchEmployeesByName(theEmployee.getFirstName(), theEmployee.getLastName());
		List<Employee> results2 = dao.searchEmployeesByName(theEmployee2.getFirstName(), theEmployee2.getLastName());

		List<Employee> allResults = dao.getAllEmployees();
		
		assertNotNull(allResults);
		assertTrue("There should be at least 2 employees", allResults.size() >= 2);
	}
	


	@Test
	public void return_employees_given_projectId(){
		Employee theEmployee = dao.createEmployee("Charlie", "Bucket", LocalDate.now(), 'M', LocalDate.now());
		Project project = projectDao.createProject("Howl");
		projectDao.addEmployeeToProject(project.getId(), theEmployee.getId());
	
		List<Employee> results = dao.getEmployeesByProjectId(project.getId());
		
		assertNotNull(results);
		assertEquals(theEmployee.getFirstName(), results.get(0).getFirstName());
	}
	
	@Test 
	public void return_employees_who_have_no_project(){
		Employee theEmployee = dao.createEmployee("Charlie", "Bucket", LocalDate.now(), 'M', LocalDate.now());
		Project project = projectDao.createProject("Howl");
		projectDao.addEmployeeToProject(project.getId(), theEmployee.getId());

		List<Employee> allResults = dao.getEmployeesWithoutProjects();
		
		assertNotNull(allResults);
		assertFalse(allResults.contains(theEmployee.getFirstName()));
	}
	
	@Test
	public void employee_department_changes(){ 
		Employee theEmployee = dao.createEmployee("Flo", "Henderson", LocalDate.now(), 'F', LocalDate.now());
		Department department= deptDao.createDepartment("Creative");
		dao.changeEmployeeDepartment(theEmployee.getId(), department.getId());
		Department department2 = deptDao.createDepartment("Uncreative");
		dao.changeEmployeeDepartment(theEmployee.getId(), department2.getId());
		List <Employee> results = dao.getEmployeesByDepartmentId(department2.getId());
		
	
		assertEquals(theEmployee.getFirstName(),results.get(0).getFirstName());
		assertEquals(theEmployee.getLastName(), results.get(0).getLastName());
			

	}
}
