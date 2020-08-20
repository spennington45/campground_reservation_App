package com.techelevator;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.view.Menu;

public class CampgroundCLI {

	private static Object[] PARK_OPTIONS;
	private Menu menu = new Menu(System.in, System.out);
	private static JDBCParkDAO park;
	private static JDBCCampgroundDAO campground;
	
	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		park = new JDBCParkDAO(dataSource);
		campground = new JDBCCampgroundDAO(dataSource);
		PARK_OPTIONS = park.getParkNames().toArray();
		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		// create your DAOs here
	}

	public void run() {
		while (true) {
			System.out.println("Please select a park you would like to visit.");
			String choice = (String) menu.getChoiceFromOptions(PARK_OPTIONS);

			if (choice.equals("Acadia")) {
				campground.getCampgroundByParkId(1);
			} else if (choice.equals("Arches")) {
				campground.getCampgroundByParkId(2);
			} else if (choice.equals("Cuyahoga Valley")) {
				campground.getCampgroundByParkId(3);
			} else {
				System.exit(0);
			}
		}
	}
}
