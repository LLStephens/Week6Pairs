package com.techelevator.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Campground {
	private int campgroundId;
	private int parkId;
	private String name;
	private String openingMonth;
	private String closingMonth;
	private BigDecimal dailyFee;

	
	public int getCamgroundId() {
		return campgroundId;
	}
	
	public void setCampgroundId(int campgroundId) {
		this.campgroundId = campgroundId;
	}
	
	public int getParkId() {
		return parkId;
	}
	
	public void setParkId(int parkId) {
		this.parkId = parkId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getOpeningMonth() {
		return openingMonth;
	}
	
	public void setOpeningMonth(String openingMonth) {
		this.openingMonth = openingMonth;
	}
	
	public String getClosingMonth() {
		return closingMonth;
	}

	public void setClosingMonth(String closingMonth) {
		this.closingMonth = closingMonth;
	}
	
	public BigDecimal getDailyFee() {
		return dailyFee;
	}
	
	public void setDailyFee(BigDecimal dailyFee) {
		this.dailyFee = dailyFee;
	}
	@Override
	public String toString() {
		return name;
		//return name + "\t\t" + openingMonth + "\t" + closingMonth + "\t" + dailyFee;
	}
}
