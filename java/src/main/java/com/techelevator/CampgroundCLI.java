package com.techelevator;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.view.Menu;

public class CampgroundCLI {

	private static Object[] PARK_OPTIONS;
	private static Object[] AVAILABLE_CAMPGROUNDS;
	private static Object[] AVALIABLE_SITES;
	private static Object[] CAMPGROUND_MENU = {"See all campgrounds.", "See available campgrounds."};
	private static String [] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	private Menu menu = new Menu(System.in, System.out);
	private static JDBCParkDAO park;
	private static JDBCCampgroundDAO campground;
	private static JdcbSiteDAO site;
	private JdbcTemplate jdbcTemplate;
	
	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		park = new JDBCParkDAO(dataSource);
		campground = new JDBCCampgroundDAO(dataSource);
		site = new JdcbSiteDAO(dataSource);
		PARK_OPTIONS = park.getParkNames().toArray();
		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		jdbcTemplate = new JdbcTemplate(datasource);
	}

	public void run() {
		while (true) {
			System.out.println("Please select a park you would like to visit.");
			String choice = (String) menu.getChoiceFromOptions(PARK_OPTIONS);
			if (choice.equals("Acadia")) {
				System.out.println();
				campgroundMenu(1);
			} else if (choice.equals("Arches")) {
				campgroundMenu(2);
			} else if (choice.equals("Cuyahoga Valley")) {
				campgroundMenu(3);
			} else {
				System.exit(0);
			}
		}
	}
	
	public void campgroundMenu(long parkId) {
		String choice = (String) menu.getChoiceFromOptions(CAMPGROUND_MENU);
		if (choice.equals("See all campgrounds.")) {
			seeAllCampgrounds(parkId);
		} else if (choice.equals("See available campgrounds.")) {
			seeAvailableCampgrounds(parkId);
		}
	}
	
	public void seeAvailableCampgrounds(long parkId) {
		System.out.println("Which month would you like to start your camping trip?");
		String from = (String) menu.getChoiceFromOptions(MONTHS);
		System.out.println("What month would you like to end your camping trip");
		String to = (String) menu.getChoiceFromOptions(MONTHS);
		int fromInt = monthToInt(from);
		int toInt = monthToInt(to);
		List <String> allAvailableCampgrounds = campground.getAllAvailableCampgroundNames(parkId, fromInt, toInt);
		AVAILABLE_CAMPGROUNDS = allAvailableCampgrounds.toArray();
		if (AVAILABLE_CAMPGROUNDS.length == 0) {
			System.out.println("No available campgrounds for the indicated time. Please pick other months.");
			campgroundMenu(parkId);
		} else {
			String campgroundChoice = (String) menu.getChoiceFromOptions(AVAILABLE_CAMPGROUNDS);
			String campgroundId = "SELECT campground_id FROM campground "
					+ "WHERE campground.name = ?";
			SqlRowSet results = jdbcTemplate.queryForRowSet(campgroundId, campgroundChoice);
			long id = 0;
			while (results.next()) {
				id = results.getLong("campground_id");
			}
			campsiteMenu(id);
		}
	}
	
	public void campsiteMenu(long campId) {
		LocalDate from = getStartDate();
		LocalDate to = getEndDate();
		List<Site> sites = site.getAvailableSites(campId, to, from);
		AVALIABLE_SITES = new String [sites.size()]; 
		for (int i = 0; i < sites.size(); i++) {
			AVALIABLE_SITES[i] = sites.get(i).toString();
		}
		String choice = (String) menu.getChoiceFromOptions(AVALIABLE_SITES);
	}
	
	public LocalDate getStartDate() {
		LocalDate from = null;
		try (Scanner scanner = new Scanner(System.in)) {
			//scanner.useDelimiter(System.lineSeparator());
			System.out.println("Please enter dates in the following format YYYY-MM-DD seperated by a '-' such as 2020-08-18 ");
			System.out.print("Enter the date you wish to start camping >>> ");
			String test = scanner.nextLine();
			if (!test.equals("q") || !test.equals("Q")) {
				from = LocalDate.parse(test);
			} else {
				run();
			}
		} catch (Exception e) {
			System.out.println("Invaled date please enter a different date or press q to quit");
			//getStartDate();
		}
		return from;
	}
	
	public LocalDate getEndDate() {
		LocalDate to = null;
		try (Scanner scanner = new Scanner(System.in)) {
			//scanner.useDelimiter("\n");
			System.out.println("Please enter dates in the following format YYYY-MM-DD seperated by a '-' such as 2020-08-18 ");
			System.out.print("Enter the date you wish to end camping >>> ");
			String test = scanner.nextLine();
			if (!test.equals("q") || !test.equals("Q")) {
				to = LocalDate.parse(test);
			} else {
				run();
			}
		} catch (Exception e) {
			System.out.println("Invaled date please enter a different date or press q to quit");
			//getEndDate();
		}
		return to;
	}
	
	public void seeAllCampgrounds(long parkId) {
		List <String> allCampgrounds = campground.getAllCampgroundNames(parkId);
		for (String i : allCampgrounds) {
			System.out.println(i);
		}
	}
	
	public int monthToInt(String month) {
		int output = 0;
		for (int i = 0; i < MONTHS.length; i++) {
			if (MONTHS[i].equals(month)) {
				output = i + 1;
			}
		}
	return output;
	}
}
