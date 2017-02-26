package com.techelevator.model;

public class Site {
	private int siteId;
	private int campgroundId;
	private int siteNumber;
	private int maxOccupancy;
	private boolean accessible;
	private int maxRvLength;
	private boolean utilities;
		
	public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	public int getCampgroundId() {
		return campgroundId;
	}
	public void setCampgroundId(int campgroundId) {
		this.campgroundId = campgroundId;
	}
	public int getSiteNumber() {
		return siteNumber;
	}
	public void setSiteNumber(int siteNumber) {
		this.siteNumber = siteNumber;
	}
	public int getMaxOccupancy() {
		return maxOccupancy;
	}
	public void setMaxOccupancy(int maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}
	public boolean getIsAccessible() {
		return accessible;
	}
	public void setIsAccessible(boolean accessible) {
		this.accessible = accessible;
	}
	public int getMaxRvLength() {
		return maxRvLength;
	}
	public void setMaxRvLength(int max_rv_length) {
		this.maxRvLength = max_rv_length;
	}
	public boolean getIsUtilities() {
		return utilities;
	}
	public void setIsUtilities(boolean utilities) {
		this.utilities = utilities;
	}
	
	@Override
	public String toString() {
		return (String.format("%-15s %-10s %5s %5s %15s ", siteNumber , maxOccupancy, accessible, maxRvLength, utilities));
	}
	@Override
	public boolean equals(Object other) {
		if(other == null) {
			return false;
		} else if(!(other instanceof Site)) {
			return false;
		} else {
			Site otherSite = (Site)other;
			return this.siteId==(otherSite.siteId) && this.campgroundId==(otherSite.campgroundId);
		}
	}
	

}
