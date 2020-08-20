package com.techelevator;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.view.Menu;

public class CampgroundCLI {

	private static String [] PARK_OPTIONS;
	private Menu menu;
	
	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		Menu menu = new Menu(System.in, System.out);
		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		// create your DAOs here
	}

	public void run() {
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(PARK_OPTIONS);

			if (choice.equals("")) {
				
			} else if (choice.equals("")) {
				
			} else if (choice.equals("")) {
				
			} else {
				System.exit(0);
			}
		}
	}
}
