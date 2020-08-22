package com.techelevator.model;

import java.time.LocalDate;
import java.util.List;

public interface SiteDAO {
	public List<Site> getAvailableSites(long campId, LocalDate to, LocalDate from);
}
