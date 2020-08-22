package com.techelevator;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.JdcbSiteDAO;

public class JdbcSiteDAOTest {
	private static SingleConnectionDataSource dataSource;
	private JdcbSiteDAO siteDao;
	private String PName = "'Steve and Tim Park'";
	private String PLocation = "'State'";
	private long PArea = 999999;
	private long PVisitors = 9999999;
	private String PDescription = "'This description is only a test'";
	private long parkIdTest;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
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
	public static void tearDownAfterClass() throws Exception {
		dataSource.destroy();
		System.out.println("All tests are done");
	}

	@Before
	public void setUp() throws Exception {
		System.out.println("Starting test");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		parkIdTest = jdbcTemplate.queryForObject(" INSERT INTO park (name, location, establish_date, area, visitors, description) "
					+ "VALUES ("+ PName +", "+PLocation+", '1987-01-07', "+PArea+", "+PVisitors+", "+PDescription+") RETURNING park_id", Long.class);			
		siteDao = new JdcbSiteDAO(dataSource);
	
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("Ending test");
		dataSource.getConnection().rollback();
	}

	@Test
	public void List_all_available_sites() {
		LocalDate to = LocalDate.of(2020, 10, 12);
		LocalDate from = LocalDate.of(2020, 10, 10);
		List<Site> availableSites = siteDao.getAvailableSites(1, to, from);
		assertEquals(5, availableSites.size());
	}

}
