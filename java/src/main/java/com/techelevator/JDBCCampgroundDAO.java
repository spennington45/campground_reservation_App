package com.techelevator;

import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class JDBCCampgroundDAO implements CampgroundDAO {

	private JdbcTemplate jdbcTemplate;
	
	public JDBCCampgroundDAO(DataSource ds) {
		this.jdbcTemplate = new JdbcTemplate(ds);
	}
	
	@Override
	public List<Campground> getCampgroundByParkId(long parkId) {
		
		return null;
	}

	@Override
	public List<Campground> searchAvaliableCampgroundByPark(long parkId, LocalDate from, LocalDate to) {
		// TODO Auto-generated method stub
		return null;
	}

}
