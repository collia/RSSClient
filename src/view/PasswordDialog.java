package view;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.NoSuchPaddingException;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import controller.Controller;

import RSS.data.Category;
import RSS.data.RSSServer;


public class PasswordDialog extends JDialog
		implements ActionListener, PropertyChangeListener {
	private static final long serialVersionUID = -2958560984116850066L;
	private JPasswordField pass;

	private JOptionPane optionPane;
	
	private String btnString1 = "OK";
	private String btnString2 = "Cancel";
	
//	private RSSServer server;
	private Controller controller;

/** Creates the reusable dialog. */
	public PasswordDialog(Controller c)
	{
		super((Frame)null, true);
		controller = c;
		//Create an array of the text and components to be displayed.
		String msgString1 = "Input password: ";
		pass = new JPasswordField(50);
	
		Object[] array = {msgString1, pass};

		//	Create an array specifying the number of dialog buttons
		//and their text.
		Object[] options = {btnString1, btnString2};

		//Create the JOptionPane.
		optionPane = new JOptionPane(array,
			JOptionPane.QUESTION_MESSAGE,
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
				pass.requestFocusInWindow();
			}
		});

		//Register an event handler that puts the text into the option pane.
		pass.addActionListener(this);

		//Register an event handler that reacts to option pane state changes.
		optionPane.addPropertyChangeListener(this);
		this.pack();
		this.setResizable(false);
}

	/** This method handles events for the text field. */
	public void actionPerformed(ActionEvent e) {
		optionPane.setValue(btnString1);
	}

	/** This method reacts to state changes in the option pane. */
	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();
		
		String typedPass = null;
		
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
				typedPass = new String(pass.getPassword());
				//typedPath = serverPath.getText();
				if(!typedPass.equals(""))
				{
					try {
						controller.logged(typedPass);
					} catch (InvalidKeyException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, 
		 			   			 "Invalid keyfile","alert", 
		 			   			JOptionPane.ERROR_MESSAGE);
					} catch (NoSuchAlgorithmException e1) {
						e1.printStackTrace();
					} catch (InvalidKeySpecException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, 
		 			   			 "Invalid keyfile","alert", 
		 			   			JOptionPane.ERROR_MESSAGE);
					} catch (NoSuchPaddingException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, 
		 			   			 "Can't read keyfile","alert", 
		 			   			JOptionPane.ERROR_MESSAGE);
					}
					clearAndHide();
				}
			} else { 
				clearAndHide();
			}
		}
	}

	/** This method clears the dialog and hides it. */
	public void clearAndHide() {
		pass.setText(null);
		setVisible(false);
	}
}
