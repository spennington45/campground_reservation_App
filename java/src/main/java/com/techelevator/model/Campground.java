package com.techelevator.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Campground {

	private long campgroundId;
	private long parkId;
	private String name;
	private int openFrom;
	private int openTo;
	private BigDecimal fee;
	
	public long getCampgroundId() {
		return campgroundId;
	}
	public void setCampgroundId(long campgroundIdd) {
		this.campgroundId = campgroundIdd;
	}
	public long getParkId() {
		return parkId;
	}
	public void setParkId(long parkId) {
		this.parkId = parkId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOpenFrom() {
		return openFrom;
	}
	public void setOpenFrom(int openFrom) {
		this.openFrom = openFrom;
	}
	public int getOpenTo() {
		return openTo;
	}
	public void setOpenTo(int openTo) {
		this.openTo = openTo;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee.setScale(2, RoundingMode.HALF_UP);
	}
	@Override
	public String toString() {
		return name + " ID: "+ campgroundId + ", open from month " + openFrom + " to month " + openTo + ", daily fee " + fee;
	}
	
}
