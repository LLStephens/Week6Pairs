package com.techelevator.projects.model;

import java.util.List;

public interface ProjectDAO {
	//Create
	public Project createProject(String projectName);
	//Read
	public List<Project> getAllActiveProjects();
	public Project getProjectById(Long projectId);
	//Update
	public void removeEmployeeFromProject(Long projectId, Long employeeId); //incomplete
	public void addEmployeeToProject(Long projectId, Long employeeId); //incomplete
	//Delete
	public void removeProject(Long projectId);
}
