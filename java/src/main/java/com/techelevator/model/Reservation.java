package com.techelevator.model;

import java.time.LocalDate;

public class Reservation {

	//Data Members
	private long reservationId;
	 private long siteId;
	 private String name;
	 private LocalDate fromDate;
	 private LocalDate toDate;
	 private LocalDate createDate;

	 
	 
	 
	 //Getters and Setters
	public long getReservationId() {
		return reservationId;
	}
	public void setReservationId(long reservationId) {
		this.reservationId = reservationId;
	}
	public long getSiteId() {
		return siteId;
	}
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getFromDate() {
		return fromDate;
	}
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}
	public LocalDate getToDate() {
		return toDate;
	}
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}
	public LocalDate getCreateDate() {
		return createDate;
	}
	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}
	public int totalDays() {
		int totalDays = toDate.compareTo(fromDate);
		return totalDays;
	}
	@Override
	public String toString() {
		return "Reservation Id = " + reservationId + ", site = " + siteId + ", name = " + name + ", from = "
				+ fromDate + ", to = " + toDate + ", reserved on = " + createDate;
	}
	 

	 
	 
}
