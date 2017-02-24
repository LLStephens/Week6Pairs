package com.techelevator.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

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


}