package com.techelevator;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class JDBCCampgroundDAOTest {
	private static SingleConnectionDataSource dataSource;
	private JDBCCampgroundDAO campDao;
	private String PName = "'Steve and Tim Park'";
	private String PLocation = "'State'";
	private long PArea = 999999;
	private long PVisitors = 9999999;
	private String PDescription = "'This description is only a test'";
	private long campIdTest;
	private long parkIdTest;	
	
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
		campDao = new JDBCCampgroundDAO(dataSource);
	}

	@After
	public void tearDown() {
		System.out.println("Ending test");
		try {
			this.dataSource.getConnection().rollback();
		} catch (SQLException e) {
			System.out.println("Database connection problems");
		}
	}

	@Test
	public void get_camp_by_parkId() {
		List<Campground> test = campDao.getCampgroundByParkId(parkIdTest);
		String testInfo = test.get(0).toString();
		assertEquals("Camp Camp ID: "+campIdTest+", open from month 1 to month 12, daily fee 22.00", testInfo);
	}

	@Test
	public void search_available_camp_by_park() {
		List<Campground> test = campDao.searchAvailableCampgroundByPark(parkIdTest, 1, 2);
		String testInfo = test.get(0).toString();
		assertEquals("Camp Camp ID: "+campIdTest+", open from month 1 to month 12, daily fee 22.00", testInfo);
	}
	@Test
	public void get_all_camp_names() {
		List<String> test = campDao.getAllCampgroundNames(Long.parseLong("1"));
		int size = test.size();
		assertEquals(3, size);
	}
	
	@Test
	public void get_all_available_camp_names() {
		List<String> test = campDao.getAllAvailableCampgroundNames(Long.parseLong("2"), 2, 3);
		int size = test.size();
		assertEquals(4, size);
		assertEquals("Devil's Garden ID: 4, open from month 1 to month 12, daily fee 25.00",test.get(0));
	}
}
