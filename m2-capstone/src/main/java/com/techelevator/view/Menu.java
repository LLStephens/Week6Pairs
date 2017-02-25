package com.techelevator.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
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
	//general menus
	public Object getChoiceFromOptions(Object[] options) { //takes array of anything, present list to user, ask user for selection, 
		Object choice = null;
		while(choice == null) {
			displayMenuOptions(options); //print menu options
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}
	
	public Object getChoiceFromUserInput(Object[] options) { 
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput); 
			if(selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			} else if (selectedOption == 0){
				System.exit(0);
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
		out.print("\nPlease choose from the above options (or enter 0 to cancel) >>> ");
		out.flush();
	}
	
	public String getUserInput(String prompt) {
		System.out.print(prompt + " >>> ");
		return new Scanner(System.in).nextLine();
	}
	
	//site specific menus
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
			} else if (selectedOption == 0){
				System.exit(0);
			}
		} catch(NumberFormatException e) {
			e.getMessage();
		}
		if(siteChoice == null) {
			out.println("\n*** "+userInput+" is not a valid option ***\n");
		}
		return siteChoice;
	}

	private void displaySiteMenuOptions(List<Site> siteList) {
		out.println();
		out.print("\nPlease choose from the above site options (or enter 0 to cancel) >>> ");
		out.flush();
	}
}
