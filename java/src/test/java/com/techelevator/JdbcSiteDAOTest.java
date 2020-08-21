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

import com.techelevator.JdcbSiteDAO;

public class JdbcSiteDAOTest {
	private static SingleConnectionDataSource dataSource;
	private JdcbSiteDAO siteDao;
	private long campIdTest;
	
	
	/*
	 * 1. Get list of all available sites
	 * 2. Add row to site
	 */
	
	
	@BeforeClass
	public static void setUpBeforeClass() {
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
	public static void tearDownAfterClass() {
		dataSource.destroy();
		System.out.println("All tests are done");
	}

	@Before
	public void setUp() {
		System.out.println("Starting test");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		//Lines needed to make a unique campId variable
		campIdTest = jdbcTemplate.queryForObject("INSERT INTO campground (park_id, name, open_from_mm, open_to_mm, daily_fee) VALUES (1, 'Camp Camp', '01', '12', '$22.00') RETURNING campground_id", Long.class);
		siteDao = new JdcbSiteDAO(dataSource);
	
	}

	@After
	public void tearDown() {
		System.out.println("Ending test");
		try {
			dataSource.getConnection().rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void List_all_available_sites() {
		LocalDate to = LocalDate.of(2020, 12, 31);
		LocalDate from = LocalDate.of(2020, 12, 25);
		List<Site> availableSites = siteDao.getAvailableSites(campIdTest, to, from);
		System.out.println(availableSites.size());
	}

}
