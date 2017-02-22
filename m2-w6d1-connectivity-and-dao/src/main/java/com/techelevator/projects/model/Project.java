package com.techelevator.projects.model;

import java.time.LocalDate;

public class Project {
	private Long id;
	private String name;
	private LocalDate startDate;
	private LocalDate endDate;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public String toString() {
		return name;
	}
	@Override
	public boolean equals(Object other) {
		if(other == null) {
			return false;
		} else if(!(other instanceof Project)) {
			return false;
		} else {
			Project otherProject = (Project)other;
			return this.id.equals(otherProject.id) && this.name.equals(otherProject.name);
		}
	}
}
