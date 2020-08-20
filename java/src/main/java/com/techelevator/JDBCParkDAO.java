package com.techelevator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCParkDAO implements ParkDAO {

	private JdbcTemplate jdbcTemplate;
	
	public JDBCParkDAO(DataSource ds) {
		this.jdbcTemplate = new JdbcTemplate(ds);
	}
	
	@Override
	public List<Park> getAllParks() {
		List<Park> parks = new ArrayList <Park>();
		String allParks = "SELECT * FROM park";
		SqlRowSet results = jdbcTemplate.queryForRowSet(allParks);
		while (results.next()) {
			Park thisPark = mapRowToPark(results);
			parks.add(thisPark);
		}
		return parks;
	}

	public List<String> getParkNames() {
		List<Park> parks = getAllParks();
		List<String> names = new ArrayList <String>();
		for (Park i : parks) {
			names.add(i.getName());
		}
		names.add("Exit");
		return names;
	}
	
	private Park mapRowToPark(SqlRowSet results) {
		Park thisPark = new Park();
		thisPark.setParkId(results.getLong("park_id"));
		thisPark.setName(results.getString("name"));
		thisPark.setLocation(results.getString("location"));
		thisPark.setEstablishDate(LocalDate.parse(results.getString("establish_date")));
		thisPark.setArea(results.getInt("area"));
		thisPark.setVisitors(results.getInt("visitors"));
		thisPark.setDescription(results.getString("Description"));
		return thisPark;
	}
	
}
