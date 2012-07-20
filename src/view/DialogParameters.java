package view;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import RSS.data.RSSServer;
import controller.Controller;
import controller.Parameters;

/**
 * Class implements dialog with parameters of RSSServer
 * @author Nikolay Klimchuk
 *
 */
class DialogParameters extends JDialog 
			implements ActionListener, PropertyChangeListener {
	
	
	private JTextField keyfile;
	private JTextField passfile;
	private JTextField showint;
	private JTextField updateint;
		
	private PanelPeriod interval;
	private JOptionPane optionPane;
	private String btnString1 = "OK";
	private String btnString2 = "Cancel";
	private String oldName = "";
	private Parameters param;
	
	/**
	 * Constructor
	 */
	public DialogParameters(){
		
		param = Parameters.getParameters();
		
		String msgString1 = "Keyfile: ";
		keyfile = new JTextField(50);
		keyfile.setText(param.getKeyFile());
		String msgString2 = "Path to password file";
		passfile = new JTextField(50);
		passfile.setText(param.getPassFile());
		
		String msgString4 = "Number of showing:";
		showint = new JTextField(50);
		showint.setText(""+param.getShowNumber());
		
		String msgString5 = "Number of updating:";
		updateint = new JTextField(50);
		updateint.setText(""+param.getUpdateNumber());
		
			String msgString7 = "Update interval: ";
			
		long lmIntervalMin = param.getUpdatePeriod()/(1000*60);
		
		interval = new PanelPeriod(lmIntervalMin);
		
		Object[] array = {msgString1, keyfile, 
						msgString2, passfile,
						msgString4, showint,
						msgString5, updateint,
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
				keyfile.requestFocusInWindow();
			}
		});

		//Register an event handler that puts the text into the option pane.
		keyfile.addActionListener(this);

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
		
		String keyString = null;
		String passString = null;
		String showString = null;
		String updateString = null;
		

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
				
				keyString=keyfile.getText();
				passString = passfile.getText();
				showString = showint.getText();
				updateString = updateint.getText();
				
				if((!keyString.equals(""))&&(!passString.equals(""))
						&&(!showString.equals(""))&&(!updateString.equals("")))
				{
					param.setKeyFile(keyString);
					param.setPassFile(passString);
					param.setShowNumber(Integer.parseInt(showString));
					param.setUpdateNumber(Integer.parseInt(updateString));
					param.setUpdatePeriod(interval.getSelectInterval());
				}
				clearAndHide();
			} else { //user closed dialog or clicked cancel
				clearAndHide();
			}
		}
		
	}

	/**
	 * Class implements panel for input number update interval
	 * @author Nikolay Klimchuk
	 */
	private class PanelPeriod extends JPanel{  
	
		JSpinner spWeeks;
		JSpinner spDays;
		JSpinner spHours;
		JSpinner spMinutes;
		
		/**
		 * Constructor
		 * @param lmIntervalMin - begin interval
		 */
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
		//return setDate;
	}
		/**
		 * Return interval inpeted by user
		 * @return
		 */
		public long getSelectInterval(){
			//System.out.println(spWeeks.getValue());
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
		keyfile.setText(null);
		passfile.setText(null);
		setVisible(false);
	}
}
