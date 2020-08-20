package com.techelevator;

import java.time.LocalDate;
import java.util.List;

public interface CampgroundDAO {

	public List<Campground> getCampgroundByParkId(long parkId);
	public List<Campground> searchAvaliableCampgroundByPark(long parkId, LocalDate from, LocalDate to);
	
}
