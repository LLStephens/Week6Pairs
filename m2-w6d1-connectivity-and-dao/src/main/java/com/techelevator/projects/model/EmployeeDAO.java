package com.techelevator.projects.model;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeDAO {
	//Create
	public Employee createEmployee(String firstName, String lastName, LocalDate birthDay, char gender, LocalDate hireDate);
	//Read
	public Employee getEmployeeById(Long employeeId);
	public List<Employee> getAllEmployees();
	public List<Employee> searchEmployeesByName(String firstNameSearch, String lastNameSearch);
	public List<Employee> getEmployeesByDepartmentId(long id);
	public List<Employee> getEmployeesWithoutProjects(); //incomplete
	public List<Employee> getEmployeesByProjectId(Long projectId); //incomplete
	//Update
	public void changeEmployeeDepartment(Long employeeId, Long departmentId); //incomplete
	//Delete
	public void removeEmployee(Long employeeId);
	
			
}
