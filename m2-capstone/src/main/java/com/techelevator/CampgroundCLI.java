package com.techelevator;


import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
	private Period period;
	private Date fromDate;
	private Date toDate;
	private NumberFormat currency = NumberFormat.getCurrencyInstance();
	private BigDecimal dailyFee;
	private List<Site> siteList;

	
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
		printHeading("Welcome to the National Parks Reservation System.\nPlease select a park to start your journey.");
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
			siteList = siteDAO.getSiteByAvailabilityPerPark(parkChoice.getParkId(), fromDate, toDate);
			siteMenu();
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
		System.out.println(String.format("%-25s %-15s %-15s %-15s", "Name" , "Opening Month", "Closing Month", "Daily Fee"));
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
			siteList = siteDAO.getSiteByAvailabilityPerCampground(campgroundChoice.getCampgroundId(), fromDate, toDate);
			siteMenu();
		} else {
			System.out.println("\n*** No results ***");
		}
	}
	private void handleUnavailableDates(){
		System.out.println("\n*** No results ***");
		System.out.println("Would you like to enter new dates?");
		String nextStep = menu.getUserInput("Hit 'y' to enter new dates or any key to exit back to main menu.");
		if(nextStep.equals("y")){
			siteMenu();
		} else {
			run();
		}
	}
	
	private void siteMenu() {
		getUserDates();
		printHeading("Available Sites");
		System.out.printf("%-30s %-10s %-10s %-10s %-10s %-10s %-5s", "Campground", "Site", "Max Occ", "Accessible?", "Max RV", "Utility", "Daily Fee\n");
		
		if(siteList.size() > 0){
			for(int i = 0; i<siteList.size(); i++){
				int optionNum = i+1;
				int campgroundIdForSite = siteList.get(i).getCampgroundId();
				Campground siteCampground = campgroundDAO.getCampgroundById(campgroundIdForSite);
				System.out.print(optionNum+") ");
				System.out.printf("%-25s", campgroundDAO.getCampgroundById(campgroundIdForSite).getName()+ "\t");
				System.out.print(siteList.get(i));
				System.out.printf("%10s", currency.format(siteCampground.getDailyFee()) + "\n");
			}
		} else {
			handleUnavailableDates();
		}
		siteChoice = (Site)menu.getSiteChoiceFromOptions(siteList);
		System.out.println();
		calculateTotalCost();
		confirmation();
	}
	
	
	private void calculateTotalCost(){
		Campground campgroundForSiteChoice= campgroundDAO.getCampgroundById(siteChoice.getCampgroundId());
		dailyFee = campgroundForSiteChoice.getDailyFee();
		BigDecimal totalCost = dailyFee.multiply(new BigDecimal(period.getDays()));
		System.out.println("The total cost of your stay will be " + currency.format(totalCost) + " .");
	}
	
	private void parksDisplay(){
		NumberFormat nf = NumberFormat.getInstance(Locale.US);
		printHeading("Park Information Screen");
		System.out.println(parkChoice.getName() + "\n");
		System.out.println("Location: " + parkChoice.getLocation());
		System.out.println("Established: " + parkChoice.getEstablishDate());
		System.out.println("Area: " + nf.format(parkChoice.getArea()) + " sq km");
		System.out.println("Annual Visitors: " + nf.format(parkChoice.getVisitors()));
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
	
	private void getUserDates(){
		printHeading("Travel dates");
		DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
		String arrivalDateString = menu.getUserInput("What is your arrival date? (yyyy-mm-dd)"); //arrival date in String
		String departureDateString = menu.getUserInput("What is your departure date? (yyyy-mm-dd)");//departure date in String
	    try{
		arrivalDate = LocalDate.parse(arrivalDateString);//arrival date in LocalDate
	    departureDate = LocalDate.parse(departureDateString);//departure date in LocalDate
	    } catch (DateTimeParseException e) {
	    	System.out.println("Invalid date format. Please re-enter.");
	    	getUserDates();
	    }
	    if(departureDate.isBefore(arrivalDate)){
	    	System.out.println("Invalid date range. Please re-enter.");
	    	getUserDates();
	    }
	    toDate = java.sql.Date.valueOf(departureDate);//arrival date in Date
		fromDate = java.sql.Date.valueOf(arrivalDate);//departure date in Date
		period = Period.between(arrivalDate, departureDate );
		System.out.println();
	}
		
	private void confirmation() {
		String confirmation = menu.getUserInput("Would you like to confirm this registration? ('y' to confirm or any key to return to the main menu)");
		if (confirmation.equals("y")){
			
			String reservationName = menu.getUserInput("Please enter a name for the reservation: ");
			Reservation reservation = reservationDAO.createReservation(siteChoice.getSiteId(), reservationName, fromDate, toDate);
			System.out.println("A reservation for " + period.getDays() + " day(s) has been made.  The confirmation number is " +  reservation.getReservationId() + ".");

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
