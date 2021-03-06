package com.techelevator.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Site {
	//Data Members
	private long siteId;
	private long campgroundId;
	private long siteNum;
	private long maxOccupancy;
	private boolean available;
	private long maxRVLength;
	private boolean utilities;
	private BigDecimal dailyFee;
	private BigDecimal total;
	private int totalDays;
	
	
	//Getters and Setters
	public long getSiteId() {
		return siteId;
	}
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
	public long getCampgroundId() {
		return campgroundId;
	}
	public void setCampgroundId(long campgroundId) {
		this.campgroundId = campgroundId;
	}
	public long getSiteNum() {
		return siteNum;
	}
	public void setSiteNum(long siteNum) {
		this.siteNum = siteNum;
	}
	public long getMaxOccupancy() {
		return maxOccupancy;
	}
	public void setMaxOccupancy(long maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public long getMaxRVLength() {
		return maxRVLength;
	}
	public void setMaxRVLength(long maxRVLength) {
		this.maxRVLength = maxRVLength;
	}
	public boolean isUtilities() {
		return utilities;
	}
	public void setUtilities(boolean utilities) {
		this.utilities = utilities;
	}
	public BigDecimal getDailyFee() {
		return dailyFee;
	}
	public void setDailyFee(BigDecimal dailyFee) {
		this.dailyFee = dailyFee.setScale(2, RoundingMode.HALF_UP);
	}
	public BigDecimal getTotal() {
		return this.total;
	}
	public int getTotalDays() {
		return totalDays;
	}
	public void setTotalDays(int totalDays) {
		this.totalDays = totalDays;
		this.total = this.dailyFee.multiply(new BigDecimal(totalDays));
	}

	
	@Override
	public String toString() {
		String yesNo = "no";
		if (utilities) {
			yesNo = "yes";
		} 
		return "Site number " + siteNum + ", max occupancy " + maxOccupancy 
				+ ", max RV Length " + maxRVLength + ", utilities " + yesNo + ", total fee $" + total;
	}
	
	
	
}
