package com.techelevator.projects.model;

public class Department {

	private Long id;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String toString() {
		return this.name;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) {
			return false;
		} else if(!(other instanceof Department)) {
			return false;
		} else {
			Department otherDept = (Department)other;
			return this.id.equals(otherDept.id) && this.name.equals(otherDept.name);
		}
	}
}
