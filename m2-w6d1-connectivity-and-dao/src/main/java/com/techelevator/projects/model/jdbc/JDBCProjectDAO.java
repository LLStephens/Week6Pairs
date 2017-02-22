package com.techelevator.projects.model.jdbc;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.ProjectDAO;

public class JDBCProjectDAO implements ProjectDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCProjectDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	@Override 
	public Project createProject(String projectName) {
		Long id = getNextProjectId();
		String sqlCreateProject = "INSERT into project " +
				" (project_id, name) VALUES (?, ?)";	
		jdbcTemplate.update(sqlCreateProject, id, projectName);
		
		Project theProject = new Project();
		theProject.setId(id);
		theProject.setName(projectName);
		
		return theProject;
	}
	
	@Override
	public List<Project> getAllActiveProjects() {
		ArrayList<Project> project = new ArrayList<>();
		String sqlGetAllActiveProjects = "SELECT project_id, name, from_date, to_date " +
										"FROM project";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllActiveProjects);
		while(results.next()) {
			Project theProject = mapRowToProject(results);
			project.add(theProject);

		}
		return project;
	}
	
	@Override
	public Project getProjectById(Long projectId) {
	
		String sqlGetProjectById = "SELECT project_id, name, from_date, to_date FROM project WHERE project_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetProjectById, projectId);
		if(results.next()){
			Project theProject = mapRowToProject(results);
			return theProject;
		} else {
		return null;
		}
	}


	@Override
	public void removeEmployeeFromProject(Long projectId, Long employeeId) {
		String sqlRemoveEmployeeFromProject = "DELETE FROM project_employee WHERE employee_id = ? AND project_id = ?";
		jdbcTemplate.update(sqlRemoveEmployeeFromProject, employeeId, projectId);
	}

	@Override
	public void addEmployeeToProject(Long projectId, Long employeeId) {
		String sqlAddEmployeeToProject = "INSERT INTO project_employee " +
										"(employee_id, project_id) VALUES (?, ?)";
		jdbcTemplate.update(sqlAddEmployeeToProject, employeeId, projectId);
	}

	private Project mapRowToProject(SqlRowSet results) {
		Project theProject;
		theProject = new Project();
		theProject.setId(results.getLong("project_id"));
		theProject.setName(results.getString("name"));
		Date fromDate = results.getDate ("from_date");
		if(fromDate != null) {
			theProject.setStartDate(fromDate.toLocalDate());
		}
		Date toDate = results.getDate("to_date");
		if(toDate != null) {
			theProject.setEndDate(toDate.toLocalDate());
		}

		return theProject;
	}

	@Override
	public void removeProject(Long projectId) {
		String sqlRemoveProject = "DELETE FROM project WHERE project_id = ?";
		jdbcTemplate.update(sqlRemoveProject, projectId);		
	}
	
	
	
	private long getNextProjectId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_project_id')");
		if(nextIdResult.next()) {
			return nextIdResult.getLong(1);
		} else {
			throw new RuntimeException("Something went wrong while getting an id for the new project");
		}
	}
	
	
	
}
