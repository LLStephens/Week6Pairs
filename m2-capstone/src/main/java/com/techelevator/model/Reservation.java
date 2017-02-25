package com.techelevator.model;


import java.util.Date;


public class Reservation {
	private int reservationId;
	private int siteId;
	private String name;
	private Date fromDate;
	private Date toDate;
	private Date createDate;

	
	public int getReservationId() {
		return reservationId;
	}
	
	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}
	
	public int getSiteId() {
		return siteId;
	}
	
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Date getFromDate() {
		return fromDate;
	}
	
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	
	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Override
	public String toString() {
		return name;
//		return (name + reservationId + siteId + fromDate +  toDate + createDate);
	}
	
}
