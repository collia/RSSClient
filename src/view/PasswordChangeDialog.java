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
import java.util.Arrays;

import javax.crypto.NoSuchPaddingException;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import controller.Controller;

import RSS.data.Category;
import RSS.data.RSSServer;

/**
 * Class implements dialog of change password
 * @author Nikolay Klimchuk
 *
 */
public class PasswordChangeDialog extends JDialog
		implements ActionListener, PropertyChangeListener {

	private static final long serialVersionUID = -2958560984116850066L;
	private JPasswordField pass1;
	private JPasswordField pass2;
	private JPasswordField oldPass;
	private JOptionPane optionPane;
	private String btnString1 = "OK";
	private String btnString2 = "Cancel";
	private Controller controller;

	/**
	 * Constructor.
	 * @param c - instance of Controller
	 */
	public PasswordChangeDialog(Controller c)
	{
		super((Frame)null, true);
		controller = c;
		//Create an array of the text and components to be displayed.
		String msgString1 = "Input password: ";
		pass1 = new JPasswordField(15);
		pass2 = new JPasswordField(15);
		String msgString2 = "Input new password: ";
		oldPass = new JPasswordField(15);
	
		Object[] array = {	msgString1,
						oldPass,
						msgString2, 
						pass1,
						pass2,
						};

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
				pass1.requestFocusInWindow();
			}
		});

		//Register an event handler that puts the text into the option pane.
		pass1.addActionListener(this);

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
		
		String typedNewPass = null;
		String typedOldPass = null;
		
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
				typedOldPass = new String(oldPass.getPassword());
				typedNewPass = new String(pass1.getPassword());
				if(!typedNewPass.equals("") && !typedOldPass.equals("") && Arrays.equals(pass1.getPassword(),pass2.getPassword()))
				{
							
						
					try {
						controller.logged(typedOldPass);
						if(controller.isLogged())
							controller.changePassword(typedOldPass, typedNewPass);
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
			} else { //user closed dialog or clicked cancel
				clearAndHide();
			}
		}
	}
	/** This method clears the dialog and hides it. */
	public void clearAndHide() {
		pass1.setText(null);
		pass2.setText(null);
		oldPass.setText(null);
		setVisible(false);
	}
}
