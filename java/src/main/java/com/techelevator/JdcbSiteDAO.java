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
	public List<Site> getAvailableSites(long campId, LocalDate to, LocalDate from) {
		ArrayList<Site> sites = new ArrayList<>();
		String sqlSiteAvail = "SELECT s.site_id, s.campground_id, s.site_number, s.max_occupancy, s.accessible, s.max_rv_length, s.utilities, c.daily_fee "
				+ "FROM site s JOIN reservation r ON r.site_id = s.site_id JOIN campground c ON c.campground_id = s.campground_id "
				+ "WHERE s.site_id NOT IN (SELECT r.site_id FROM reservation "
				+ "WHERE (r.to_date BETWEEN ? AND ?) OR (r.from_date BETWEEN ? AND ?) "
				+ "OR (r.to_date < ? AND r.from_date > ?)) AND s.campground_id = ? GROUP BY s.site_id,s.campground_id, c.daily_fee LIMIT 5";
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlSiteAvail, from, to, from, to, from, to, campId);
		while (sqlRowSet.next()) {
			Site site = addRowToSite(sqlRowSet);
			sites.add(site);
		}
		
		
		return sites;
	}

	private Site addRowToSite(SqlRowSet sqlRowSet) {
		Site newSite = new Site();
		newSite.setAvailable(sqlRowSet.getBoolean("accessible"));
		newSite.setCampgroundId(sqlRowSet.getLong("campground_id"));
		newSite.setDailyFee(sqlRowSet.getBigDecimal("daily_fee"));
		newSite.setMaxOccupancy(sqlRowSet.getLong("max_occupancy"));
		newSite.setMaxRVLength(sqlRowSet.getLong("max_rv_length"));
		newSite.setSiteNum(sqlRowSet.getLong("site_number"));
		newSite.setSiteId(sqlRowSet.getLong("site_id"));
		newSite.setUtilities(sqlRowSet.getBoolean("utilities"));
		return newSite;
	}
	
	
}
