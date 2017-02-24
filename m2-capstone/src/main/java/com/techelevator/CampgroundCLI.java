package com.techelevator;


import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
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
	private static final String MAIN_MENU_OPTION_SEARCH_WHOLE_PARK = "Search this park for any available reservations.";
	private static final String MAIN_MENU_OPTION_VIEW_CAMPGROUNDS_AT_THIS_PARK = "Select a campground.";
	private static final String MAIN_MENU_OPTION_VIEW_ALL_RESERVATIONS = "View reservations for next 30 days.";
	private static final String MAIN_MENU_OPTION_RETURN = "Return to previous menu.";
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
	private Site siteChoice;
	private LocalDate arrivalDate;
	private LocalDate departureDate;
	private Site reservationChoice;
	private Period period;
	private Date fromDate;
	private Date toDate;


	
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
	
	private void displayMenu()  {	
		printHeading("Menu");
		String choice = (String)menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
		if(choice.equals(MAIN_MENU_OPTION_SEARCH_WHOLE_PARK)) {
			parkSiteMenu();
		} else if(choice.equals(MAIN_MENU_OPTION_VIEW_CAMPGROUNDS_AT_THIS_PARK)) {
			handleParkCampgroundList();
		} else if(choice.equals(MAIN_MENU_OPTION_VIEW_ALL_RESERVATIONS)) {
			System.out.println("you chose display upcoming reservations");
			List<Reservation> reservations = reservationDAO.getAllReservationsForPark(parkChoice.getParkId());
			for(Reservation reservation : reservations) {
				System.out.println(reservation);
			}
		} else if(choice.equals(MAIN_MENU_OPTION_RETURN)) {
			run();
		}
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
	
	private void campgroundMenu() {
		List<Campground> parkCampgrounds = campgroundDAO.getCampgroundsByParkId(parkChoice.getParkId());
		campgroundChoice = (Campground)menu.getChoiceFromOptions(parkCampgrounds.toArray());
		System.out.println();
		handleCampgroundSiteList();
	}
	
	private void handleCampgroundSiteList() {
		List<Campground> allCampgrounds = campgroundDAO.getAllCampgrounds();
		if(allCampgrounds.size() > 0) {
			siteMenu();
		} else {
			System.out.println("\n*** No results ***");
		}
	}
	
	private void siteMenu() {
		getUserDates();
		toDate = java.sql.Date.valueOf(departureDate);
		fromDate = java.sql.Date.valueOf(arrivalDate);
		printHeading("Available Sites");
		List<Site> siteList = siteDAO.getSiteByAvailability(campgroundChoice.getCamgroundId(), fromDate, toDate);
		if(siteList.size() > 0){
			siteChoice = (Site)menu.getChoiceFromOptions(siteList.toArray());
		} else {
			System.out.println("\n*** No results ***");
			//run();
		}
		System.out.println();
		System.out.println("You have selected option # " + siteChoice.getSiteNumber());
		calculateTotalCost();
		confirmation();
	}
	
	private void parkSiteMenu() {
		getUserDates();
		toDate = java.sql.Date.valueOf(departureDate);
		fromDate = java.sql.Date.valueOf(arrivalDate);
		printHeading("Available Sites");
		List<Site> siteList = siteDAO.getSiteByAvailability(parkChoice.getParkId(), fromDate, toDate);
		if(siteList.size() > 0){
			siteChoice = (Site)menu.getChoiceFromOptions(siteList.toArray());
		} else {
			System.out.println("\n*** No results ***");
			//run();
		}
		System.out.println();
		System.out.println("You have selected option # " + siteChoice.getSiteNumber());
		//NEED TO CHANGE CALCULATECOST() TO USE PARKID INSTEAD OF CAMPGROUND ID
		//calculateTotalCost();
		confirmation();
	}
	
	private void calculateTotalCost(){
		BigDecimal totalCost = campgroundChoice.getDailyFee().multiply(new BigDecimal(period.getDays()));
		System.out.println("The total cost of your stay will be $" + totalCost + " .");
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
		String arrivalDateString = getUserInput("What is your arrival date? (yyyy-mm-dd)");
		String departureDateString = getUserInput("What is your departure date? (yyyy-mm-dd)");
		DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);

	    arrivalDate = LocalDate.parse(arrivalDateString);
	    departureDate = LocalDate.parse(departureDateString);
		period = Period.between( arrivalDate, departureDate );
		System.out.println();
	    
	}
		
	private void confirmation() {
		String confirmation = getUserInput("Would you like to confirm this registration? ('y' to confirm or any key to return to the main menu)");
		if (confirmation.equals("y")){
			
			String reservationName = getUserInput("Please enter a name for the reservation: ");
			//THIS IS HOW IT NEEDS TO BE  getSiteId()
			//reservation = reservationDAO.createReservation(siteChoice.getSiteId(), reservationName, fromDate, toDate);
			//THIS IS HOW IT IS
			Reservation reservation = reservationDAO.createReservation(siteChoice.getSiteNumber(), reservationName, fromDate, toDate);
			System.out.println("A reservation for " + period.getDays() + " day(s) has been made.  The confirmation number is " +  reservation.getReservationId() + " .");;
		} else {
			run();
		}
	}
	
	
//	private void isCampgroundOpen(){
//		Campground campground = campgroundDAO.getCampgroundById(siteChoice.getCampgroundId());
//		String openingMonthString = campground.getOpeningMonth();
//		String closingMonthString = campground.getClosingMonth();
//		int openingMonth = Integer.parseInt(openingMonthString);
//		int closingMonth = Integer.parseInt(closingMonthString);
//		int arrivalMonth = fromDate.getMonth();
//		int departureMonth = toDate.getMonth();
//		if arrivalMonth is between openingMonth and closingMonth, will have arrived too early
//		if departureMonth is between openingMonth and closingMonth, will have departed too late
//		if (arrivalMonth <= openingMonth and departureMonth >= closingMonth))
//	}
	
}
