package com.techelevator.projects.model;

import java.util.List;

public interface DepartmentDAO {
	// Create
	public Department createDepartment(String departmentName);
	
	// Read
	public List<Department> getAllDepartments();
	public List<Department> searchDepartmentsByName(String nameSearch);
	public Department getDepartmentById(Long id);
	
	// Update
	public void updateDepartmentName(Long departmentId, String departmentName);
	
	// Delete
	public void deleteDepartmentById(Long departmentId);
}
