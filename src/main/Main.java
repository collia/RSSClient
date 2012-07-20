package main;

import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import controller.Controller;
import controller.Schedule;

import view.MainFrame;

/**
 * Main class of program
 * @author Nikolay Klimchuk
 */

public class Main {

	/**
	 * Main method. Create instance Controller, MainFrame and Schedule
	 * @param args - User parameters
	 * @see Controller, MainFrame and Schedule
	 */
	public static void main(String[] args) {
		Logger log = RSSLogger.loggerMain;
		
		log.entering("main.Main", "static main(...)");
		log.info("Start RSS reader");
		
		Controller controller = new Controller();
		try {
			log.fine("Start schedule");
			Schedule schedule = new Schedule(controller);
			log.fine("Start frame");
			JFrame frame = new MainFrame(controller);
		} catch (Exception e) {
			//e.printStackTrace();
			log.throwing("main.Main","public static void main(...)", e);	
			JOptionPane error = new JOptionPane("Can't start program: " + e.getMessage(),
					JOptionPane.ERROR_MESSAGE);
			error.setVisible(true);

		}
	}

}
