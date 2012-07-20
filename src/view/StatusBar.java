package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.SoftBevelBorder;

/**
 * Class implements status bar in the bottom of the frame.
 * Implements pattern singleton 
 * @author Nicolay Klimchuk
 *
 */

public class StatusBar extends JPanel {
	
	private static final long serialVersionUID = -1627404107931468163L;
	private JLabel message;
	private JProgressBar progress;
	private static StatusBar statusBar = new StatusBar();

	/**
	 * Maximum size of progress bar
	 */
	public static final int MAX_SIZE = 100;
	/**
	 * Minimum size of progress bar
	 */
	public static final int MIN_SIZE = 0;
	
	/**
	 * Constructor. Create status bar
	 */
	private StatusBar()
	{
		message = new JLabel("    ");
		progress = new JProgressBar();
		progress.setMaximum(MAX_SIZE);
		progress.setMinimum(MIN_SIZE);

		BorderLayout manager = new BorderLayout();
		manager.setHgap(10);
		manager.setVgap(10);
		
		this.setLayout(manager);
		
		//Border border = new LineBorder(Color.BLACK);
		Border border = new  SoftBevelBorder(SoftBevelBorder.LOWERED);
		this.setBorder(border);
		
		add(message, BorderLayout.WEST);
		add(progress, BorderLayout.EAST);
		this.setPreferredSize(new Dimension(4,20));
	}
	/**
	 * Return this
	 * @return Status bar
	 */
	public static StatusBar getStatusBar(){
		return statusBar;
	}
	/**
	 * Set message text on status bar
	 * @param text - new message of sttus bar
	 */
	public void setMessage(String text)
	{
		message.setText("    " + text);
	}
	/**
	 * set status of progress bar
	 * @param pr - new position of status bar
	 */
	public void setProgress(int pr)
	{
		progress.setValue(pr);
	}
	/**
	 * Return current position of status bar 
	 * @return  current position of status bar
	 */
	public int getProgress(){
		return progress.getValue();
	}
}
