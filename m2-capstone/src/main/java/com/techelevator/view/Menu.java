package com.techelevator.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import com.techelevator.model.Site;

public class Menu {

	private PrintWriter out;
	private Scanner in;

	public Menu(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) { //takes array of anything, present list to user, ask user for selection, 
		Object choice = null;
		while(choice == null) {
			displayMenuOptions(options); //print menu options
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}
	
	public Object getCampgroundChoiceFromOptions(Object[] options) { 
		Object camgroundChoice = null;
		while(camgroundChoice == null) {
			displayCampgroundMenuOptions(options); 
			camgroundChoice = getChoiceFromUserInput(options);
		}
		return camgroundChoice;
	}
	
	public Object getSiteChoiceFromOptions(List<Site> siteList) { 
		Object siteChoice = null;
		while(siteChoice == null) {
			displaySiteMenuOptions(siteList); 
			siteChoice = getSiteChoiceFromUserInput(siteList);
		}
		return siteChoice;
	}
	
	public Object getSiteChoiceFromUserInput(List<Site> siteList) { 
		Object siteChoice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput); 
			if(selectedOption > 0 && selectedOption <= siteList.size()) {
				siteChoice = siteList.get(selectedOption - 1);
			}
		} catch(NumberFormatException e) {
			e.getMessage();
		}
		if(siteChoice == null) {
			out.println("\n*** "+userInput+" is not a valid option ***\n");
		}
		return siteChoice;
	}

	public Object getChoiceFromUserInput(Object[] options) { 
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput); 
			if(selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch(NumberFormatException e) {
			e.getMessage();
		}
		if(choice == null) {
			out.println("\n*** "+userInput+" is not a valid option ***\n");
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for(int i = 0; i < options.length; i++) {
			int optionNum = i+1;
			out.println(optionNum+") "+options[i]);
		}
		out.print("\nPlease choose from the above options >>> ");
		out.flush();
	}
	
	private void displayCampgroundMenuOptions(Object[] options) {
		out.println();
		for(int i = 0; i < options.length; i++) {
			int optionNum = i+1;
			out.println(optionNum+") "+options[i].toString());
		}
		out.print("\nPlease choose from the above campground options >>> ");
		out.flush();
	}
	
	private void displaySiteMenuOptions(List<Site> siteList) {
		out.println();
		for(int i = 0; i < siteList.size(); i++) {
			int optionNum = i+1;
			out.println(optionNum+") "+(String.format("%-25s %-15s %-15s %-10s",siteList.get(i).getSiteNumber(), siteList.get(i).getMaxOccupancy(), siteList.get(i).getIsAccessible(), siteList.get(i).getMaxRvLength(),  siteList.get(i).getIsUtilities())));
			out.println(optionNum+") "+ siteList.get(i).getSiteNumber() +" "+ siteList.get(i).getMaxOccupancy()+" "+ siteList.get(i).getIsAccessible()+" "+ siteList.get(i).getMaxRvLength()+" "+  siteList.get(i).getIsUtilities());
		}
		
		out.print("\nPlease choose from the above site options >>> ");
		out.flush();
	}


}
