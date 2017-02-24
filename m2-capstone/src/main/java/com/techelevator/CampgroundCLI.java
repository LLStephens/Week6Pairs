package com.techelevator;


import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;


import java.util.List;

import java.util.Scanner;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.model.Campground;
import com.techelevator.model.CampgroundDAO;
import com.techelevator.model.Park;
import com.techelevator.model.ParkDAO;
import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDAO;
import com.techelevator.model.Site;
import com.techelevator.model.SiteDAO;
import com.techelevator.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.model.jdbc.JDBCParkDAO;
import com.techelevator.model.jdbc.JDBCReservationDAO;
import com.techelevator.model.jdbc.JDBCSiteDAO;
import com.techelevator.view.Menu;


public class CampgroundCLI {
	private static final String MAIN_MENU_OPTION_SEARCH_WHOLE_PARK = "Search this park for any available reservation";
	private static final String MAIN_MENU_OPTION_VIEW_CAMPGROUNDS_AT_THIS_PARK = "Select a campground";
	private static final String MAIN_MENU_OPTION_VIEW_ALL_RESERVATIONS = "View Reservations for next 30 days";
	private static final String MAIN_MENU_OPTION_RETURN = "Return to previous menu";
	private static final String[] MAIN_MENU_OPTIONS = new String[] { MAIN_MENU_OPTION_SEARCH_WHOLE_PARK, 
																	MAIN_MENU_OPTION_VIEW_CAMPGROUNDS_AT_THIS_PARK,
																	MAIN_MENU_OPTION_RETURN};
	private Menu menu;
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private SiteDAO siteDAO;
	private ReservationDAO reservationDAO;
	private static BasicDataSource dataSource;
	private Park parkChoice;
	private Campground campgroundChoice;

	
	public static void main(String[] args) {
		dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		
		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		this.menu = new Menu(System.in, System.out);

		parkDAO = new JDBCParkDAO(dataSource);
		campgroundDAO = new JDBCCampgroundDAO(dataSource);
		siteDAO = new JDBCSiteDAO(dataSource);
		reservationDAO = new JDBCReservationDAO(dataSource);
	}
	
	public void run() {
		printHeading("Welcome to the National Parks Reservation System. Please select a park to start your journey.");
		parksMenu();
	}
	
	private void parksMenu() {
		List<Park> parkList = parkDAO.getAllParks();
		parkChoice = (Park)menu.getChoiceFromOptions(parkList.toArray());
		System.out.println();
		parksDisplay();
		displayMenu();
	}
	
	private void siteMenu() {
		List<Site> siteList = siteDAO.getSitesByCampgroundId(campgroundChoice.getCamgroundId());
		Site siteChoice = (Site)menu.getChoiceFromOptions(siteList.toArray());
		System.out.println();
		displayMenu();
	}
	
	private void campgroundMenu() {
		List<Campground> parkCampgrounds = campgroundDAO.getCampgroundsByParkId(parkChoice.getParkId());
		campgroundChoice = (Campground)menu.getChoiceFromOptions(parkCampgrounds.toArray());
		System.out.println();
		handleCampgroundSiteList();
	}
	
	private void handleParkCampgroundList() {
		printHeading("Campground List");
		System.out.println("\t Name \t\t Open \t Close \t Daily Fee");
		List<Park> allParks = parkDAO.getAllParks();
		if(allParks.size() > 0) {
			campgroundMenu();
		} else {
			System.out.println("\n*** No results ***");
		}
	}
	
	private void handleCampgroundSiteList() {
		printHeading("Sites List");
		List<Campground> allCampgrounds = campgroundDAO.getAllCampgrounds();
		if(allCampgrounds.size() > 0) {
			siteMenu();
		} else {
			System.out.println("\n*** No results ***");
		}
	}
	
	private void displayMenu()  {	
		printHeading("Menu");
		String choice = (String)menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
		if(choice.equals(MAIN_MENU_OPTION_SEARCH_WHOLE_PARK)) {
			getUserDates(); 
		} else if(choice.equals(MAIN_MENU_OPTION_VIEW_CAMPGROUNDS_AT_THIS_PARK)) {
			handleParkCampgroundList();
		} else if(choice.equals(MAIN_MENU_OPTION_VIEW_ALL_RESERVATIONS)) {
			System.out.println("you chose display upcoming reservations");
			//getAllReservations(choice); 
		} else if(choice.equals(MAIN_MENU_OPTION_RETURN)) {
			run();
		}
	}
	
	private void parksDisplay(){
		printHeading("Park Information Screen");
		System.out.println(parkChoice.getName() + "\n");
		System.out.println("Location: " + parkChoice.getLocation());
		System.out.println("Established: " + parkChoice.getEstablishDate());
		System.out.println("Area: " + parkChoice.getArea() + " sq km");
		System.out.println("Annual Visitors: " + parkChoice.getVisitors());
		System.out.println();
		System.out.println(parkChoice.getDescription());
	}
	

	private void listCampgrounds(List<Campground> campground) {
		System.out.println();
		if(campground.size() > 0) {
			for(Campground camp : campground) {
				System.out.println(camp.getName());
			}
		} else {
			System.out.println("\n*** No results ***");
		}
	}

	private void printHeading(String headingText) {
		System.out.println("\n"+headingText);
		for(int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}
		System.out.println();
	}
	
	private String getUserInput(String prompt) {
		System.out.print(prompt + " >>> ");
		return new Scanner(System.in).nextLine();
	}
	
	private void getUserDates(){
		printHeading("Travel dates");
		String arrivalDateString = getUserInput("What is your arrival date?");
		String departureDateString = getUserInput("What is your departure date?");
		DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);

	    LocalDate arrivalDate = LocalDate.parse(arrivalDateString);
	    LocalDate departureDate = LocalDate.parse(departureDateString);
		Period period = Period.between( arrivalDate, departureDate );
		System.out.println();
	    System.out.println("You're planning on staying " + period.getDays() + " days");
	}
		
	private void confirmation() {
		Reservation reservation = new Reservation();
		int reservationId = reservation.getReservationId();
		System.out.println("The reservation has been made and the confirmation id is " +  reservationId);
	}
	
}
