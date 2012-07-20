package view.html;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.Printable;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import RSS.data.RSSServer;
import controller.Controller;
import controller.Parameters;

/**
 * Class implements panel with html viewer 
 * 
 * @author Nicolay Klimchuk
 *
 */
public class TextPanel extends JPanel {
	private static final long serialVersionUID = 6132339745677348121L;
	private JEditorPane html;
	private Controller controller;
	
	private String textBuffer;
	
	/**
	 * Constructor
	 * @param c - instance of Controller 
	 */
	public TextPanel(Controller c)
	{
		controller = c;
		html = new JEditorPane();
		
		html.setContentType("text/html");
		html.addHyperlinkListener(createHyperLinkListener());

		html.setEditable(false);
		 JScrollPane bottomScrollPane = new JScrollPane(html);
		 
		 setLayout(new BorderLayout());
		 add(bottomScrollPane, BorderLayout.CENTER);
		 add(createSearchPanel(),BorderLayout.SOUTH);
	}

	/**
	 * 
	 * Load RSS feeds from file and open it.
	 * @param s - server
	 */
	public void openRSS(final RSSServer s)
	{
		final DataFormater df = new DataFormater(controller);
		final int num = Parameters.getParameters().getShowNumber();
		EventQueue.invokeLater(new Runnable(){

			@Override
			public void run() {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			       
				html.setText(df.openRSSPage(s, num/*, true*/));
				html.setCaretPosition(0);
				textBuffer = html.getText();
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});

	}
	/**
	 * Load RSS feeds from file and open it.
	 *  @param s - list of servers
	 */
	public void openRSS(final List<RSSServer> s)
	{
		final DataFormater df = new DataFormater(controller);
		final int num = Parameters.getParameters().getShowNumber();
		EventQueue.invokeLater(new Runnable(){

			@Override
			public void run() {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			       
				html.setText(df.openRSSPage(s, num/*, true*/));
				html.setCaretPosition(0);
	//			html.moveCaretPosition(600);
				textBuffer = html.getText();
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		        
			}
			
		});

	}
	/**
	 * Open num entries from file and show it on text area 
	 * @param s - server for entries
	 * @param num - number to show
	 */
	public void openRSS(final RSSServer s, final int num)
	{
		final DataFormater df = new DataFormater(controller);
		EventQueue.invokeLater(new Runnable(){

			@Override
			public void run() {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			       
				html.setText(df.openRSSPage(s, num/*, true*/));
				html.setCaretPosition(0);
				textBuffer = html.getText();
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		        
			}
			
		});
	
	}
	
	/**
	 * Open num entries from file and show it on text area 
	 * @param s - List of servers for entries
	 * @param num - number to show
	 */
	public void openRSS(final List<RSSServer> s, final int num)
	{
		final DataFormater df = new DataFormater(controller);
		EventQueue.invokeLater(new Runnable(){

			@Override
			public void run() {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			       
				html.setText(df.openRSSPage(s, num/*, true*/));
				html.setCaretPosition(0);
				textBuffer = html.getText();
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		        
			}
			
		});
	}
	
	/**
	 * Download and show all new entries to server <i>s</i> 
	 * @param s - server
	 */
	public void updateRSS(final RSSServer s)
	{
		EventQueue.invokeLater(new Runnable(){

			@Override
			public void run() {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	      
				DataFormater df = new DataFormater(controller);
				int num = Parameters.getParameters().getShowNumber();
				html.setText(df.updateRSSPage(s, num/*, false*/));
				html.setCaretPosition(0);
				textBuffer = html.getText();
//				html.moveCaretPosition(600);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}});
        
	}
	/**
	 * Download and show all new entries to list of servers <i>s</i>
	 * @param s - list of servers
	 */
	public void updateRSS(final List<RSSServer> s)
	{
		EventQueue.invokeLater(new Runnable(){

			@Override
			public void run() {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
				DataFormater df = new DataFormater(controller);
				int num = Parameters.getParameters().getShowNumber();
				html.setText(df.updateRSSPage(s, num/*, false*/));
				html.setCaretPosition(0);
				textBuffer = html.getText();
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}});
        
	}
	/**
	 * Load from file and show entries marked us not read 
	 * @param s - server to show
	 */
	public void showNotReadRSS(final RSSServer s)
	{
		EventQueue.invokeLater(new Runnable(){

			@Override
			public void run() {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	       
				DataFormater df = new DataFormater(controller);
				int num = Parameters.getParameters().getShowNumber();
					html.setText(df.notReadedRSSPage(s));
				html.setCaretPosition(0);
				textBuffer = html.getText();
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}});
        
	}
	/**
	 * Load from file and show entries marked us not read 
	 * @param s list of servers
	 */
	public void showNotReadRSS(final List<RSSServer> s)
	{
		EventQueue.invokeLater(new Runnable(){

			@Override
			public void run() {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	       
				DataFormater df = new DataFormater(controller);
				int num = Parameters.getParameters().getShowNumber();
				html.setText(df.updateRSSPage(s, num/*, false*/));
				html.setCaretPosition(0);
				textBuffer = html.getText();
				//html.moveCaretPosition(600);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}});
        
	}
	/**
	 * Load from file and show all entries by server <i>s</i>
	 * @param s - server
	 */
	public void showAllRSS(final RSSServer s)
	{
		EventQueue.invokeLater(new Runnable(){

			@Override
			public void run() {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	       
				DataFormater df = new DataFormater(controller);
				html.setText(df.openRSSPage(s, -1/*, false*/));
				html.setCaretPosition(0);
				textBuffer = html.getText();
				//		html.moveCaretPosition(600);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}});
        
	}
	/**
	 * Load from file and show all entries by list of servers <i>s</i>
	 * @param s - list of servers
	 */
	public void showAllRSS(final List<RSSServer> s)
	{
		EventQueue.invokeLater(new Runnable(){

			@Override
			public void run() {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	       
				DataFormater df = new DataFormater(controller);
				html.setText(df.openRSSPage(s, -1/*, false*/));
				html.setCaretPosition(0);
				textBuffer = html.getText();
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}});
        		
	}
	
	private JTextField input;
	private JButton down;
	private JButton up;
	/**
	 * Method create search panel
	 * @return - search panel
	 */
	private JPanel createSearchPanel(){
		LayoutManager l = new FlowLayout();
		
		searchList = new ArrayList<Integer>();
		
		JPanel search = new JPanel(l);
		search.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		JLabel s = new JLabel("Search: ");
		input = new JTextField(25);
	
		down = new JButton();
		down.setIcon(createImageIcon("FlecheBas2_3.png","RSS"));
		//save.setPreferredSize(new Dimension(52,52));open.setBorderPainted(false);
		down.setPreferredSize(new Dimension(26,26));
		down.setFocusPainted(false);
		down.setBorderPainted(false);
		down.setEnabled(false);
		down.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectNext();
			}
		});
		
		up = new JButton();
		
		up.setIcon(createImageIcon("FlecheHaut2_3.png","RSS"));
		up.setPreferredSize(new Dimension(26,26));
		up.setFocusPainted(false);
		up.setBorderPainted(false);
		up.setEnabled(false);
		up.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectPrev();
			}
		});
		
		search.add(s);
		search.add(input);
		search.add(down);
		search.add(up);
		
		input.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			//	lastFounded = 0;
				search();
					
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
			}
			
		});
		return search;
	}
	
	private List<Integer> searchList;
	private ListIterator<Integer> searchIterator;
	
	/**
	 * Prepare for search substring and invoke search(0)
	 */
	private void search()
	{
				
		searchString = input.getText();;
		if(textBuffer == null)
			textBuffer = html.getText();
		search(0);
		searchIterator = searchList.listIterator();
		up.setEnabled(searchIterator.hasPrevious());
		down.setEnabled(searchIterator.hasNext());
		if(!searchList.isEmpty()) input.setBackground(Color.WHITE);
		if(searchList.size() == 1) //input.setBackground(Color.WHITE);
		{
			up.setEnabled(false);
			down.setEnabled(false);
		}
		else input.setBackground(Color.ORANGE);
		
		selectNext();
	}
	
	private String searchString = "";
	/**
	 * Found all substrings and put it numbers to <i>searchList</i>
	 * @param last - index of begin search
	 */
	private void search(int last)
	{
		if(last == 0) searchList.clear();
		final String result = HTMLUnicode.convertToUnicode(searchString);
		final int a = textBuffer.indexOf(result, last);
		if((a != -1) && (!searchString.equals(""))) 
		{
			searchList.add(a);
			search(a+1);
		}
	}
	/**
	 * Select with color <i>text</i> begin from <i>a</i>
	 * @param a - begin index
	 * @param text - text to search
	 */
	private void select(final int a, final String text){
		EventQueue.invokeLater(new Runnable(){

			@Override
			public void run() {
				//html.setCaretPosition(0);
				//html.moveCaretPosition(500);
				String result = HTMLUnicode.convertToUnicode(text);
				int newPosition = HTMLUnicode.calculateShift(textBuffer, a);// + "<SPAN STYLE=\"BACKGROUND-COLOR: #ff9933\">".length();
				if(newPosition > 0)
				{
					String textString = textBuffer;//html.getText();
					textString = textString.substring(0, a).concat("<A name=\"select\"><SPAN STYLE=\"BACKGROUND-COLOR: #ff9933\">").
							concat(textString.substring(a, a+result.length())).concat("</SPAN></A>").
							concat(textString.substring(a+result.length(), textString.length()));
					html.setText(textString);
				
					
					scrollToReference("select");
					
					
				}
				if(newPosition == 0)
				{
					input.setBackground(Color.ORANGE);
				}
			}
			
		});
	}
	
	/**
	 * Scrolls the view to the given reference location 
	 * (that is, the value returned by the UL.getRef  
	 * method for the URL being displayed). By default, 
	 * this method only knows how to locate a reference in an 
	 * HTMLDocument. The implementation calls the scrollRectToVisible 
	 * method to accomplish the actual scrolling. If scrolling to a reference 
	 * location is needed for document types other than HTML, this method should be 
	 * reimplemented. This method will have no effect if the component is not visible. 
	 * 
	 * @param ref - the named location to scroll to
	 */
	private  void scrollToReference(String ref) 

	{
	         Document doc = html.getDocument();
	        if (ref == null || !(doc instanceof HTMLDocument)) {
	            return;
	        }
	        HTMLDocument.Iterator it = ((HTMLDocument)doc).getIterator(HTML.Tag.A);
	        int offset = 0;
	        while (it.isValid()) {
	            AttributeSet set = it.getAttributes();
	            Object name = set.getAttribute(HTML.Attribute.NAME);
	            if (ref.equals(name)) {
	                offset = it.getStartOffset();
	                break;
	            }
	            it.next();
	        }
	        html.setCaretPosition(offset);
	}
	private boolean isNext = true;
	/**
	 * select next founded substring
	 */
	private void selectNext()
	{
		
		if(searchIterator.hasNext()){
			if(!isNext) searchIterator.next();
			if(searchIterator.hasNext()){
				int a = searchIterator.next();
				System.out.println(a);
				select(a, searchString);
			
				isNext = true;
				up.setEnabled(searchList.indexOf(a) != 0);
				down.setEnabled(searchList.indexOf(a) != searchList.size()-1);
			}
		}
		
	if(searchList.size() == 1) 
		{
			up.setEnabled(false);
			down.setEnabled(false);
		}
		if(!searchList.isEmpty()) input.setBackground(Color.WHITE);
		else input.setBackground(Color.ORANGE);
	}
	/**
	 * select previously founded substring
	 */
	private void selectPrev()
	{
		if(searchIterator.hasPrevious()){
			if(isNext) searchIterator.previous();
			if(searchIterator.hasPrevious()){
				int a = searchIterator.previous();
				select(a, searchString);
				//System.out.println(a);
				isNext = false;
				up.setEnabled(searchList.indexOf(a) != 0);
				down.setEnabled(searchList.indexOf(a) != searchList.size()-1);
	//			if(searchList.indexOf(a) == 0) searchIterator.next();
			}
		}
		if(searchList.size() == 1) //input.setBackground(Color.WHITE);
		{
			up.setEnabled(false);
			down.setEnabled(false);
		}
		if(!searchList.isEmpty()) input.setBackground(Color.WHITE);
		else input.setBackground(Color.ORANGE);
	}

	/**
	 * Return page in text area  
	 * @return
	 */
	public String getPage()
	{
		return html.getText();
	}
	/**
	 * Open and load page by URL
	 * @param url - url of page
	 * @throws IOException
	 */
	public void openPage(URL url) throws IOException
	{
		html.setPage(url);
	}
	/**
	 * Open image and create ImageIcom 
	 * @param filename - String of filename
	 * @param description - description of image
	 * @return
	 */
	 private ImageIcon createImageIcon(String filename, String description) {
		    String path = "/resources/images/" + filename;
		    ImageIcon ii = new ImageIcon(getClass().getResource(path), description);
		    
		    return ii;
	 }
	 /**
	  * Method create HyperlinkListener. All links send to system
	  * webclient
	  * @return new HyperlinkListener
	  */
	 private HyperlinkListener createHyperLinkListener() { 
			return new HyperlinkListener() { 
			    public void hyperlinkUpdate(HyperlinkEvent e) { 
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) { 
				/*	String ref = e.getURL().getRef();
                    if (ref != null && ref.length() > 0) {
                       // log.debug("reference to scroll to = " + ref);
                        html.scrollToReference(ref);
                    }else {*/
				    	try {
			    			java.awt.Desktop.getDesktop().browse(e.getURL().toURI());
			    		} catch (IOException e1) {
							e1.printStackTrace();
						} catch (URISyntaxException e2) {
							e2.printStackTrace();
						}
                    }
			//	} 
			    } 
			}; 
		}

	 /**
	  * Format page and prepare it to print
	  * @return - printed format of html page
	  */
	 public Printable print(){
		 MessageFormat header = new MessageFormat("");
		 MessageFormat footer = new MessageFormat("page {0,number,integer}");
		 return html.getPrintable(header, footer);
	 }
}
