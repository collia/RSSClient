package view.tree;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import controller.Controller;

import RSS.data.Category;
import RSS.data.RSSServer;

/**
 * Class implements dialog with server properties
 * @author Nicolay Klimchuk
 *
 */
public class DialogServerProperties extends JDialog 
			implements ActionListener, PropertyChangeListener {
	
	
	private static final long serialVersionUID = 233828703090141L;
	private RSSServer server;
	private JTextField serverName;
	private JTextField serverPath;
	private PanelPeriod interval;
	
	private JOptionPane optionPane;
	
	private String btnString1 = "OK";
	private String btnString2 = "Cancel";
			
	private String oldName = "";
	private Controller controller;
	
	/**
	 * Constructor
	 * @param server - server, which properties is showing
	 * @param controller - instance of controller
	 */
	public DialogServerProperties(RSSServer server, Controller controller){
		this.server = server;
		this.controller = controller;
		oldName = server.getName();
		
		String msgString1 = "Server name: ";
		serverName = new JTextField(50);
		serverName.setText(server.getName());
		String msgString2 = "Server path";
		serverPath = new JTextField(50);
		serverPath.setText(server.getLink());
		String msgString3 = "Feed type: " + server.getFeedType();
		String msgString4 = "Author: "+server.getAuthor();
		String msgString5 = "Copyright: " + server.getCopyright();
		String msgString6 = "Last update: " + server.getLastModified();
		String msgString7 = "Update interval: ";
			
		long lmIntervalMin = server.getModifyInterval()/(1000*60);
		
		interval = new PanelPeriod(lmIntervalMin);
		
		Object[] array = {msgString1, serverName, 
						msgString2, serverPath,
						msgString3, //feedType,
						msgString4, //author,
						msgString5, //copyright
						msgString6,
						msgString7, interval
						};

		//	Create an array specifying the number of dialog buttons
		//and their text.
		Object[] options = {btnString1, btnString2};

		//Create the JOptionPane.
		optionPane = new JOptionPane(array,
				JOptionPane.INFORMATION_MESSAGE,
				JOptionPane.YES_NO_OPTION,
				null,
				options,
				options[0]);

		//Make this dialog display it.
		setContentPane(optionPane);

		//Handle window closing correctly.
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent we) {
					/*
					 * Instead of directly closing the window,
					 * we're going to change the JOptionPane's
					 * value property.
					 */
					optionPane.setValue(new Integer(
							JOptionPane.CLOSED_OPTION));
				}
		});

		//Ensure the text field always gets the first focus.
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent ce) {
				serverName.requestFocusInWindow();
			}
		});

		//Register an event handler that puts the text into the option pane.
		serverName.addActionListener(this);

		//Register an event handler that reacts to option pane state changes.
		optionPane.addPropertyChangeListener(this);
		this.pack();
		this.setResizable(false);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		optionPane.setValue(btnString1);
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();
		
		String typedName = null;
		String typedPath = null;

		if (isVisible()
				&& (e.getSource() == optionPane)
				&& (JOptionPane.VALUE_PROPERTY.equals(prop) ||
						JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
			Object value = optionPane.getValue();

			if (value == JOptionPane.UNINITIALIZED_VALUE) {
				//ignore reset
				return;
			}

			//Reset the JOptionPane's value.
			//If you don't do this, then if the user
			//presses the same button next time, no
			//property change event will be fired.
			optionPane.setValue(
					JOptionPane.UNINITIALIZED_VALUE);
			
			if (btnString1.equals(value)) {
				typedName = serverName.getText();
				typedPath = serverPath.getText();
				if(!typedName.equals(""))
						clearAndHide();
				if(!oldName.equals(typedName))
					controller.renameServer(server, typedName);
			
				server.setName(typedName);
				server.setLink(typedPath);
			//	server.setFeedType((String)feedType.getSelectedItem());
			//	System.out.println(interval.getSelectInterval());
				server.setModifyInterval(interval.getSelectInterval());
				
				try {
					controller.writeServers();
				} catch (IOException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(this, 
	 			   			"alert", "Could not write file", 
	 			   			JOptionPane.ERROR_MESSAGE);
				}
			} else { //user closed dialog or clicked cancel
				server = null;
				clearAndHide();
			}
		}
		
	}

	/**
	 * Class - panel, implements sets update period
	 * @author Nicolay Klimchuk
	 *
	 */
	private class PanelPeriod extends JPanel{  
	
		JSpinner spWeeks;
		JSpinner spDays;
		JSpinner spHours;
		JSpinner spMinutes;
		
		PanelPeriod(long lmIntervalMin){
		//long lmIntervalMin = server.getModifyInterval()/(1000*60);
		
		int weeks   = (int) ( lmIntervalMin / (60*24*7));
		int days    = (int) ((lmIntervalMin - (60*24*7)*weeks) /(60*24));
		int hours   = (int) ((lmIntervalMin - (60*24*7)*weeks  - 60*24*days) / 60);
		int minutes = (int) ((lmIntervalMin - (60*24*7)*weeks  - 60*24*days  - 60*hours));
		
		SpinnerNumberModel spModelWeek = 
			new SpinnerNumberModel(weeks, //initial value
					   0, 				  //min
					   99,  			  //max
					   1);                //step
		SpinnerNumberModel spModelDays = 
			new SpinnerNumberModel(days, //initial value
					   0, 				  //min
					   6, 				  //max
					   1);                //step
		SpinnerNumberModel spModelHours = 
			new SpinnerNumberModel(hours, //initial value
					   0, 				  //min
					   23, 				  //max
					   1);                //step
		SpinnerNumberModel spModelMinutes = 
			new SpinnerNumberModel(minutes, //initial value
					   0,  				  //min
					   59,  			  //max
					   1);                //step

		
		 spWeeks = new JSpinner(spModelWeek);
		 spDays = new JSpinner(spModelDays);
		 spHours = new JSpinner(spModelHours);
		 spMinutes = new JSpinner(spModelMinutes);
		
		this.setLayout(new FlowLayout());
		add(new JLabel("Weeks: "));
		add(spWeeks);
		add(new JLabel("Days: "));
		add(spDays);
		add(new JLabel("Hours: "));
		add(spHours);
		add(new JLabel("Minutes: "));
		add(spMinutes);
	}
		public long getSelectInterval(){
			long result =  
				(Integer)spWeeks.getValue()*60*24*7+
				(Integer)spDays.getValue()*60*24+
				(Integer)spHours.getValue()*60+ 
				(Integer)spMinutes.getValue();
			return result*60*1000;
		}
}
	/** This method clears the dialog and hides it. */
	public void clearAndHide() {
		serverName.setText(null);
		serverPath.setText(null);
		setVisible(false);
	}
}
