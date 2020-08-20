package com.techelevator;

import java.time.LocalDate;
import java.util.List;

public interface SiteDAO {
	public List<Site> getAvailableSites(long campId, LocalDate to, LocalDate from);
}
