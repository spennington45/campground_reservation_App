package com.techelevator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JdcbSiteDAO implements SiteDAO{
	private JdbcTemplate jdbcTemplate;
//	private NamedParameterJdbcTemplate jdbcSpecial;
	
	public JdcbSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
//		jdbcSpecial = new NamedParameterJdbcTemplate(dataSource);
	}
	
	
	@Override
	public List<Site> getAvailableSites(long campId, LocalDate to, LocalDate from) {

		ArrayList<Site> sites = new ArrayList<>();
//		Set <LocalDate> dates = new HashSet<LocalDate>();
//		dates.add(from);
//		dates.add(to);
//		Set <Long> campIds = new HashSet<Long>();
//		campIds.add(campId);
//		MapSqlParameterSource parameters = new MapSqlParameterSource();
//		parameters.addValue("dates", dates);
//		parameters.addValue("campIds", campIds);
//		String sqlSelect = "SELECT site.*, campground.daily_fee "
//				+ "FROM site JOIN campground ON campground.campground_id = site.campground_id "
//				+ "WHERE campground_id = :campIds AND site.site_id NOT IN (SELECT site_id FROM reservation WHERE (from_date, to_date) OVERLAPS ( :dates ) ) "
//				+ "GROUP BY reservation.reservation_id, site.site_id, campground.daily_fee ORDER BY COUNT(reservation.site_id) DESC";
//		SqlRowSet rowset = jdbcSpecial.queryForRowSet(sqlSelect, parameters);
			
		String sqlSiteAvail = "SELECT s.site_id, s.campground_id, s.site_number, s.max_occupancy, s.accessible, s.max_rv_length, s.utilities, c.daily_fee " + 
				"FROM site s LEFT OUTER JOIN reservation r ON r.site_id = s.site_id JOIN campground c ON c.campground_id = s.campground_id " + 
				"WHERE NOT ((r.to_date BETWEEN ? AND ?) OR (r.from_date BETWEEN ? AND ?) " + 
				"OR (r.to_date < ? AND r.from_date > ?)) AND s.campground_id = ? OR r.reservation_id IS NULL GROUP BY s.site_id, r.site_id, c.daily_fee ORDER BY COUNT(r.reservation_id) DESC LIMIT 5";
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlSiteAvail, from, to, from, to, from, to, campId);


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
