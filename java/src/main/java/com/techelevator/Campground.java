package com.techelevator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Campground {

	private long campgroundIdd;
	private long parkId;
	private String name;
	private String openFrom;
	private String openTo;
	private BigDecimal fee;
	
	public long getCampgroundIdd() {
		return campgroundIdd;
	}
	public void setCampgroundIdd(long campgroundIdd) {
		this.campgroundIdd = campgroundIdd;
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
	public String getOpenFrom() {
		return openFrom;
	}
	public void setOpenFrom(String openFrom) {
		this.openFrom = openFrom;
	}
	public String getOpenTo() {
		return openTo;
	}
	public void setOpenTo(String openTo) {
		this.openTo = openTo;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee.setScale(2, RoundingMode.HALF_UP);
	}

}
