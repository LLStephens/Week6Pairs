package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.DepartmentDAO;
import com.techelevator.projects.model.Project;

public class JDBCDepartmentDAO implements DepartmentDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCDepartmentDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Department> getAllDepartments() {
		ArrayList<Department> department = new ArrayList<>();
		String sqlGetAllDepartments = "SELECT department_id, name " +
										"FROM department";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllDepartments);
		while(results.next()) {
			Department theDepartment = mapRowToDepartment(results);
			department.add(theDepartment);

		}
		return department;
	}

	@Override
	public List<Department> searchDepartmentsByName(String nameSearch) {
		ArrayList<Department> department = new ArrayList<>();
		String sqlSearchDepartmentsByName = "SELECT department_id, name " +
											"FROM department " +
											"WHERE name = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchDepartmentsByName, nameSearch);
		while(results.next()) {
			Department theDepartment = mapRowToDepartment(results);
			department.add(theDepartment);

		}
		return department;
	}

	@Override
	public void updateDepartmentName(Long departmentId, String departmentName) {
		String sqlUpdateDepartmentName = "UPDATE department " +
				"SET name = ? " +
				"WHERE department_id = ?";
		jdbcTemplate.update(sqlUpdateDepartmentName, departmentName, departmentId);
	}

	@Override
	public Department createDepartment(String departmentName) {
		String sqlCreateDepartment = "INSERT into department " +
				" (name) VALUES (?)";	
		jdbcTemplate.update(sqlCreateDepartment, departmentName);
	
		return searchDepartmentsByName(departmentName).get(0);
		
	}

	@Override
	public Department getDepartmentById(Long id) {
		String sqlGetDepartmentById = "SELECT department_id, name " +
										"FROM department " +
										"WHERE department_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetDepartmentById, id);
		if(results.next()) {
			Department department = mapRowToDepartment(results);
			return department;
		} else {
			return null;
		}
	}
	
	
	private Department mapRowToDepartment(SqlRowSet results) {
		Department theDepartment;
		theDepartment = new Department();
		theDepartment.setId(results.getLong("department_id"));
		theDepartment.setName(results.getString("name"));
		return theDepartment;
	}

	@Override
	public void deleteDepartmentById(Long departmentId) {
		String sqlRemoveDepartment = "DELETE FROM department WHERE department_id = ?";
		jdbcTemplate.update(sqlRemoveDepartment, departmentId);
		
	}

	

}
