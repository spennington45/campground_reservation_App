package com.techelevator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCCampgroundDAO implements CampgroundDAO {

	private JdbcTemplate jdbcTemplate;
	
	public JDBCCampgroundDAO(DataSource ds) {
		this.jdbcTemplate = new JdbcTemplate(ds);
	}
	
	@Override
	public List<Campground> getCampgroundByParkId(long parkId) {
		List<Campground> campgrounds = new ArrayList <Campground>();
		String allCampground = "SELECT * FROM campground WHERE park_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(allCampground, parkId);
		while (results.next()) {
			Campground thisCampground = mapRowToCampground(results);
			campgrounds.add(thisCampground);
		}
		return campgrounds;
	}

	@Override
	public List<Campground> searchAvailableCampgroundByPark(long parkId, int from, int to) {
		List<Campground> campgrounds = getCampgroundByParkId(parkId);
		List<Campground> availableCampgrounds = new ArrayList <Campground>();
		for (Campground i : campgrounds) {
			if (i.getOpenFrom() <= from && i.getOpenTo() >= to) {
				availableCampgrounds.add(i);
			}
		}
		return availableCampgrounds;
	}

	public List <String> getAllCampgroundNames(long id) {
		List<Campground> campgrounds = getCampgroundByParkId(id);
		List<String> names = new ArrayList <String>();
		for (Campground i : campgrounds) {
			names.add(i.toString());
		} 
		return names;
	}
	
	public List <String> getAllAvailableCampgroundNames(long id, int from, int to) {
		List<Campground> campgrounds = searchAvailableCampgroundByPark(id, from, to);
		List<String> names = new ArrayList <String>();
		for (Campground i : campgrounds) {
			names.add(i.toString());
		} 		
		names.add("Back");
		return names;
	}
	
	public Campground mapRowToCampground(SqlRowSet results) {
		Campground thisCampground = new Campground();
		thisCampground.setCampgroundId(results.getLong("campground_id"));
		thisCampground.setParkId(results.getLong("park_id"));
		thisCampground.setName(results.getString("name"));
		thisCampground.setOpenFrom(results.getInt("open_from_mm"));
		thisCampground.setOpenTo(results.getInt("open_to_mm"));
		thisCampground.setFee(new BigDecimal(results.getDouble("daily_fee")));
		return thisCampground;
	}
	
}
