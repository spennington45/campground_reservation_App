package com.techelevator;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.view.Menu;

public class CampgroundCLI {

	private static Object[] PARK_OPTIONS;
	private static Object[] AVAILABLE_CAMPGROUNDS;
	private static Object[] AVALIABLE_SITES;
	private static Object[] CAMPGROUND_MENU = {"See all campgrounds.", "See available campgrounds.", "Back"};
	private static String [] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	private Menu menu = new Menu(System.in, System.out);
	private static JDBCParkDAO park;
	private static JDBCCampgroundDAO campground;
	private static JdcbSiteDAO site;
	private static JdbcReservationsDAO reservation;
	private JdbcTemplate jdbcTemplate;
	
	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		park = new JDBCParkDAO(dataSource);
		campground = new JDBCCampgroundDAO(dataSource);
		site = new JdcbSiteDAO(dataSource);
		reservation = new JdbcReservationsDAO(dataSource);
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
				park.desplayParkInfo(1);
				campgroundMenu(1);
			} else if (choice.equals("Arches")) {
				park.desplayParkInfo(2);
				campgroundMenu(2);
			} else if (choice.equals("Cuyahoga Valley")) {
				park.desplayParkInfo(3);
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
			campgroundMenu(parkId);
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
		if (AVAILABLE_CAMPGROUNDS.length == 1) {
			System.out.println("No available campgrounds for the indicated time. Please pick other months.");
			campgroundMenu(parkId);
		} else {
			String campgroundChoice = (String) menu.getChoiceFromOptions(AVAILABLE_CAMPGROUNDS);
			String [] temp = campgroundChoice.split(" ID: ");
			System.out.println(temp[0]);
			String campgroundId = "SELECT campground_id FROM campground "
					+ "WHERE campground.name = ?";
			SqlRowSet results = jdbcTemplate.queryForRowSet(campgroundId, temp[0]);
			long id = 0;
			while (results.next()) {
				id = results.getLong("campground_id");
			}
			campsiteMenu(id);
		}
	}
	
	public void campsiteMenu(long campId) {
		LocalDate from = null;
		LocalDate to = null;
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter dates in the following format YYYY-MM-DD seperated by a '-' such as 2020-08-18");
		System.out.print("Enter the date you wish to start camping >>> ");
		String startDate = scanner.nextLine();
		System.out.println("Please enter dates in the following format YYYY-MM-DD seperated by a '-' such as 2020-08-18");
		System.out.print("Enter the date you wish to end camping >>> ");
		String endDate = scanner.nextLine();
		System.out.println();
		try {
			from = LocalDate.parse(startDate);
			to = LocalDate.parse(endDate);
			List<Site> sites = site.getAvailableSites(campId, from, to);
			if (sites.size() > 0) {
				AVALIABLE_SITES = new String [6]; 
				for (int i = 0; i < sites.size(); i++) {
					if (!sites.get(i).toString().equals(null)) {
						AVALIABLE_SITES[i] = sites.get(i).toString();					
					}
				}
				AVALIABLE_SITES[5] = "Back";
				System.out.println("Please select an avaliable site to make a reservation.");
				String choice = (String) menu.getChoiceFromOptions(AVALIABLE_SITES);
				int index = 0;
				if (!choice.equals("Back")) {
					for (int i = 0; i < AVALIABLE_SITES.length; i++) {
						if (choice.equals(AVALIABLE_SITES[i])) {
							index = i;
						}
					}
					long siteId = sites.get(index).getSiteId();
					System.out.print("Please enter a name for the reservation >>> ");
					String name = scanner.nextLine();
					reservation.bookReservation(siteId, to, from, name);
					String res = "SELECT reservation_id FROM reservation ORDER BY reservation_id DESC LIMIT 1";
					SqlRowSet sql = jdbcTemplate.queryForRowSet(res);
					long reservationId = 0;
					while (sql.next()) {
						reservationId = sql.getLong("reservation_id");
					}
					reservation.confirmationId(reservationId);
				}			
			} else {
				System.out.println("No avalable campsites for the dates " + from + " to " + to);
			}
		} catch (Exception e) {
			System.out.println("Invaled date please enter a different date");
			System.out.println();
			campsiteMenu(campId);
		}
	}
	
	public LocalDate getDate() {
		LocalDate date = null;
		try (Scanner scanner = new Scanner(System.in)) {
			String test = scanner.nextLine();
			if (!test.equals("q") || !test.equals("Q")) {
				date = LocalDate.parse(test);
			} else {
				run();
			}
		} catch (Exception e) {
			System.out.println("Invaled date please enter a different date or press q to quit");
			getDate();
		}
		return date;
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
