package com.techelevator.projects.model.jdbc;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.ProjectDAO;

public class JDBCProjectDAO implements ProjectDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCProjectDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
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
	public void removeEmployeeFromProject(Long projectId, Long employeeId) {
		String sqlRemoveEmployeeFromProject = "DELETE FROM project_employee WHERE project_id = ? AND employee_id = ?";
		jdbcTemplate.update(sqlRemoveEmployeeFromProject, projectId, employeeId);
	}

	@Override
	public void addEmployeeToProject(Long projectId, Long employeeId) {
		String sqlAddEmployeeToProject = "INSERT INTO project_employee " +
										"(employee_id, project_id) VALUES (?,?)";
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
		
//	public void showProjectEmployee (Long projectId, Long employeeId) {
//		
//	}
	}
	
	
	
	
	
}
