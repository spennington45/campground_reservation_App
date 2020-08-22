package com.techelevator;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class JdbcReservationDAOtest {

	private static SingleConnectionDataSource dataSource;
	private JdbcReservationsDAO resDao;
	private String PName = "'Steve and Tim Park'";
	private String PLocation = "'State'";
	private long PArea = 999999;
	private long PVisitors = 9999999;
	private String PDescription = "'This description is only a test'";
	private long campIdTest;
	private long siteIdTest;
	private long parkIdTest;
	private LocalDate from = LocalDate.of(2020, 10, 10);
	private LocalDate to = LocalDate.of(2020, 10, 12);
	
	
	@BeforeClass
	public static void setUpBeforeClass(){
		System.out.println("Starting test suite");
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		/* The following line disables autocommit for connections
		 * returned by this DataSource. This allows us to rollback
		 * any changes after each test */
		dataSource.setAutoCommit(false);
	}

	@AfterClass
	public static void tearDownAfterClass(){
		dataSource.destroy();
		System.out.println("All tests are done");
	}

	@Before
	public void setUp() throws Exception {
		System.out.println("Starting test");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		parkIdTest = jdbcTemplate.queryForObject(" INSERT INTO park (name, location, establish_date, area, visitors, description) "
					+ "VALUES ("+ PName +", "+PLocation+", '1987-01-07', "+PArea+", "+PVisitors+", "+PDescription+") RETURNING park_id", Long.class);			
		campIdTest = jdbcTemplate.queryForObject("INSERT INTO campground (park_id, name, open_from_mm, open_to_mm, daily_fee) VALUES ("+parkIdTest+", 'Camp Camp', '01', '12', '$22.00') RETURNING campground_id", Long.class);
		siteIdTest = jdbcTemplate.queryForObject("INSERT INTO site (campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities) VALUES ("+campIdTest+", 999, 3, true, 0, true) RETURNING site_id",  Long.class);
		resDao = new JdbcReservationsDAO(dataSource);
	}

	@After
	public void tearDown() {
		System.out.println("Ending test");
		try {
			dataSource.getConnection().rollback();
		} catch (SQLException e) {
			System.out.println("Database connection problems");
		}
	}
	
	@Test
	public void book_reservation() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String beforeRes = "SELECT COUNT (reservation_id) FROM reservation";
		long resNumB = jdbcTemplate.queryForObject(beforeRes, Long.class);
		resDao.bookReservation(siteIdTest, from, to, "Beyonce Knowles");
		String test = "SELECT name FROM reservation WHERE name = 'Beyonce Knowles'";
		String afterRes = "SELECT COUNT (reservation_id) FROM reservation";
		long resNumA = jdbcTemplate.queryForObject(afterRes, Long.class);
		String beyonce = jdbcTemplate.queryForObject(test, String.class);
		assertEquals("Beyonce Knowles", beyonce);
		assertEquals(resNumB+1L, resNumA);
	}

}
