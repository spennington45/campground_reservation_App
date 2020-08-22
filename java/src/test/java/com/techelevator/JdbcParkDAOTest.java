package com.techelevator;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class JdbcParkDAOTest {

	private static SingleConnectionDataSource dataSource;
	private static JdbcTemplate jdbcTemplate;
	private JDBCParkDAO park;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		System.out.println("Starting test suite");
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
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
		jdbcTemplate = new JdbcTemplate(dataSource);
		park = new JDBCParkDAO(dataSource);
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
	public void getAllParksTest() {
		List <Park> testPark = new ArrayList <>();
		testPark = park.getAllParks();
		assertEquals(3, testPark.size());
	}
	
	@Test
	public void getParkNamesTest() {
		List <String> names = new ArrayList <>();
		names = park.getParkNames();
		assertEquals(4, names.size());
		assertEquals(true, names.contains("Acadia"));
	}

}
