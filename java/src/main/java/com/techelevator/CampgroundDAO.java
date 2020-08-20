package com.techelevator;

import java.util.List;

public interface CampgroundDAO {

	public List<Campground> getCampgroundByParkId(long parkId);
	public List<Campground> searchAvailableCampgroundByPark(long parkId, int from, int to);
	
}
