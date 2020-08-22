package com.techelevator;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;


import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JdcbSiteDAO implements SiteDAO{
	private JdbcTemplate jdbcTemplate;
	
	public JdcbSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Site> getAvailableSites(long campId, LocalDate from, LocalDate to) {
		ArrayList<Site> sites = new ArrayList<>();
		String sqlSiteAvail = "SELECT s.site_id, s.campground_id, s.site_number, s.max_occupancy, s.accessible, s.max_rv_length, s.utilities, c.daily_fee " + 
				"FROM site s LEFT OUTER JOIN reservation r ON r.site_id = s.site_id JOIN campground c ON c.campground_id = s.campground_id " + 
				"WHERE  s.campground_id = ? AND s.site_id NOT IN (SELECT s.site_id FROM reservation r JOIN site s ON s.site_id = r.site_id " + 
				"WHERE s.campground_id = ? AND ((? <= r.to_date AND ? >= r.from_date) OR (? <= r.to_date AND ? >= r.from_date) OR (? <= r.from_date AND ? >= r.to_date))) " + 
				"GROUP BY s.site_id, c.daily_fee ORDER BY COUNT(r.reservation_id) DESC LIMIT 5";
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlSiteAvail, campId, campId, to, to, from, from, from, to);
		while (sqlRowSet.next()) {
			Site site = addRowToSite(sqlRowSet, to, from);
			sites.add(site);
		}	
		return sites;
	}

	private Site addRowToSite(SqlRowSet sqlRowSet, LocalDate to, LocalDate from) {
		Site newSite = new Site();
		newSite.setAvailable(sqlRowSet.getBoolean("accessible"));
		newSite.setCampgroundId(sqlRowSet.getLong("campground_id"));
		newSite.setDailyFee(sqlRowSet.getBigDecimal("daily_fee"));
		newSite.setMaxOccupancy(sqlRowSet.getLong("max_occupancy"));
		newSite.setMaxRVLength(sqlRowSet.getLong("max_rv_length"));
		newSite.setSiteNum(sqlRowSet.getLong("site_number"));
		newSite.setSiteId(sqlRowSet.getLong("site_id"));
		newSite.setUtilities(sqlRowSet.getBoolean("utilities"));
		int totalDays = to.compareTo(from);
		newSite.setTotalDays(totalDays);
		return newSite;
	}
	
}
