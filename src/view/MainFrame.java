package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Hashtable;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.crypto.NoSuchPaddingException;
import javax.swing.*;

import org.jdom.JDOMException;

import RSS.data.Category;

import controller.Ban;
import controller.Controller;

import view.html.TextPanel;
import view.tree.TreePanel;
/**
 * Class implements main frame of application 
 * @author Nicolay Klimchuk
 *
 */
public class MainFrame extends JFrame {

	private Controller controller;
	private static final long serialVersionUID = 8456560429229699542L;
	private JSplitPane splitPane;
	private TextPanel textPanel;
	private TreePanel treePanel;
	
	/**
	 * Constructor.
	 * @param controller - instance of Controller
	 */
	public MainFrame(Controller controller){
		this.controller = controller;
		try {
		    //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		    // If you want the System L&F instead, comment out the above line and
		    // uncomment the following:
		     UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception exc) {
		    System.err.println("Error loading L&F: " + exc);
		}
		
		
		textPanel = new TextPanel(controller);
		treePanel = new TreePanel(controller, textPanel);
		
		JPanel statusBar = StatusBar.getStatusBar();
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePanel, textPanel);
		splitPane.setContinuousLayout(true);
		splitPane.setResizeWeight(0.20);
		
		openWelcome();
		
		JPanel all = new JPanel(new BorderLayout());
		all.add(splitPane, BorderLayout.CENTER);
		all.add(statusBar, BorderLayout.SOUTH);
		all.add(getParamPane(), BorderLayout.NORTH);
		 
		this.setJMenuBar(getMenu());
		this.getContentPane().add(all);
		this.setTitle("RSS agregator");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		 
		this.setSize(700, 550);
		
		this.setLocation((d.width - getWidth())/2, 
						(d.height-getHeight())/2);
		
		this.setVisible(true);
	}

	/**
	 * Creating menu panel 
	 * @return panel with buttons
	 */
	private JPanel getParamPane(){
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		JPanel parameters = new JPanel(fl);
		
		final JPanel buttons = new JPanel(new GridLayout(1, 6));
		
		JButton save = new JButton("");
		save.setIcon(createImageIcon("Floppy2.png","Save"));
		save.setPreferredSize(new Dimension(52,52));
		save.setBorderPainted(false);
		save.setFocusPainted(false);
		save.setBorderPainted(false);
		
		save.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveFile();
			}
		});

		buttons.add(save);

		JButton print = new JButton("");
		print.setIcon(createImageIcon("print.png","Print"));
		print.setPreferredSize(new Dimension(52,52));
		print.setBorderPainted(false);
		print.setFocusPainted(false);
		print.setBorderPainted(false);
		
		print.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
					printFile();
				
			}
		});

		buttons.add(print);

		
		JButton addServer = new JButton("");
		addServer.setIcon(createImageIcon("rssPlus2.png","Add new"));
		addServer.setPreferredSize(new Dimension(52,52));addServer.setBorderPainted(false);
		
		addServer.setBorderPainted(false);
		addServer.setFocusPainted(false);
	
		addServer.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				treePanel.addNewServer();
			}
		});
		
		buttons.add(addServer);
		
		JButton delServer = new JButton("");
		delServer.setIcon(createImageIcon("rssMinus2.png","Remove"));
		delServer.setPreferredSize(new Dimension(52,52));
		delServer.setBorderPainted(false);
		delServer.setFocusPainted(false);
		delServer.setBorderPainted(false);
		delServer.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				treePanel.removeServer();
			}
		});
		
		buttons.add(delServer);

		JButton update = new JButton("");
		update.setIcon(createImageIcon("rssUpdate2.png","RSS"));
		update.setPreferredSize(new Dimension(52,52));
		update.setBorderPainted(false);
		update.setFocusPainted(false);
		update.setBorderPainted(false);

		update.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				treePanel.updateAllServers();
			}
		});
		
		buttons.add(update);
		
		JButton addCategory = new JButton("");
		addCategory.setIcon(createImageIcon("DossierJaunePlus2.png","RSS"));
		addCategory.setPreferredSize(new Dimension(52,52));
		addCategory.setBorderPainted(false);
		addCategory.setFocusPainted(false);
		addCategory.setBorderPainted(false);
		addCategory.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				treePanel.addNewCategory();
			}
		});
		
		buttons.add(addCategory);

		JButton remCategory = new JButton("");
		remCategory.setIcon(createImageIcon("DossierJauneMinus2.png","RSS"));
		remCategory.setPreferredSize(new Dimension(52,52));
		remCategory.setBorderPainted(false);
		remCategory.setFocusPainted(false);
		remCategory.setBorderPainted(false);

		remCategory.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				treePanel.removeCategory();
			}
		});
		
		buttons.add(remCategory);
		
		parameters.add(buttons);
		parameters.setAlignmentX(JPanel.RIGHT_ALIGNMENT);
		return parameters;
	}
	
	/**
	 * Method ask filename, get page from  textPanel and save it to file
	 */
	private void saveFile() {
		JFileChooser fc = new JFileChooser(); 
		// show the filechooser
		int result = fc.showSaveDialog(this);
		if(result == JFileChooser.APPROVE_OPTION) {
			try {
				
				FileWriter fw = new FileWriter(fc.getSelectedFile().getPath());
				fw.write(textPanel.getPage());
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, 
			   			 "Error write file", "alert",
			   			JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Method get page from textPanel and print it
	 */
	private void printFile()  {
		
		PrinterJob job = PrinterJob.getPrinterJob();
		if(job != null)
		{
			Printable prn = textPanel.print();
//			PageFormat pf = job.pageDialog(job.defaultPage());
			
			job.setPrintable(prn);
			if(job.printDialog())
			{
				try {
					job.print();
				} catch (PrinterException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, 
				   			 "Error on print", "alert",
				   			JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	/**
	 * Method create main menu of application 
	 * @return menu of application
	 */
	private JMenuBar getMenu(){
		JMenuBar menu = new JMenuBar();
		
		JMenu file = new JMenu("File");
		JMenuItem fileOpen,fileSaveServers, fileSave,/* fileAddNew,*/ fileClose;
		file.add( fileOpen = new JMenuItem("Open OPML list"));
		file.add( fileSaveServers = new JMenuItem("Import to OPML list"));
		file.add( fileSave = new JMenuItem("Save current page as..."));
		file.addSeparator();
		file.add( fileClose = new JMenuItem("Exit"));
		
		
		menu.add(file);
		
		JMenu server = new JMenu("Servers");
		final JMenuItem  
					itemAddServers,itemAddCategory,itemAddRootCategory,itemRemServers,itemRemCategory, itemParameters, itemAddNewBadWord, itemAddNewBadLink;
		final JMenuItem itemLogged;
		JMenuItem itemChangePass;  
		server.add( itemAddServers = new JMenuItem("Add new server"));
		server.add( itemAddCategory = new JMenuItem("Add new category"));
		server.add( itemAddRootCategory = new JMenuItem("Add new root category"));
		server.addSeparator();
		server.add( itemRemServers = new JMenuItem("Delete server"));
		server.add( itemRemCategory = new JMenuItem("Delete category"));
		server.addSeparator();
				server.add( itemLogged = new JMenuItem("Log on"));
		server.add( itemChangePass = new JMenuItem("Change password..."));
		server.add( itemAddNewBadWord = new JMenuItem("Add new bad word..."));
		server.add( itemAddNewBadLink = new JMenuItem("Add new bad link..."));
		itemAddNewBadWord.setEnabled(false);
		itemAddNewBadLink.setEnabled(false);
		
		server.addSeparator();
		server.add( itemParameters = new JMenuItem("Parameters"));
		
		menu.add(server);
		
		JMenu news = new JMenu("News");
		JMenuItem newsShowAll, newsShowNew,newsShowLast, newsReloadAll,newsClearWorkSet;  
		news.add( newsShowAll = new JMenuItem("Show All"));
		news.add( newsShowNew = new JMenuItem("Show new"));
		news.add( newsShowLast = new JMenuItem("Show last"));
		news.add( newsReloadAll = new JMenuItem("Reload All"));
		news.addSeparator();
		news.add( newsClearWorkSet = new JMenuItem("Clear data"));
		newsClearWorkSet.setEnabled(false);
		
		menu.add(news);
		
		JMenu help = new JMenu("Help");
		JMenuItem helpWelcome, helpHelp, helpCheck, helpAbout;
		help.add( helpWelcome = new JMenuItem("Welcome"));
		help.add( helpHelp = new JMenuItem("Help..."));
		help.add( helpCheck = new JMenuItem("Check for updates"));
		help.add( helpAbout = new JMenuItem("About"));
		helpCheck.setEnabled(false);
		menu.add(help);
		
		fileOpen.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					treePanel.addOPML();
				} catch (JDOMException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, 
	 			   			 "Error read file", "alert",
	 			   			JOptionPane.ERROR_MESSAGE);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, 
	 			   			 "Error read file", "alert",
	 			   			JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, 
	 			   			 "Error read file", "alert",
	 			   			JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		});
		
		fileSaveServers.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					treePanel.saveOPML();
				} catch (JDOMException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, 
	 			   			 "Error read file", "alert",
	 			   			JOptionPane.ERROR_MESSAGE);
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, 
	 			   			 "Error read file", "alert",
	 			   			JOptionPane.ERROR_MESSAGE);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, 
	 			   			 "Error read file", "alert",
	 			   			JOptionPane.ERROR_MESSAGE);
				}
			}
			
		});
		
		fileSave.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveFile();
			}
			
		});
		
		fileClose.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
			
		});
		
		itemAddServers.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				treePanel.addNewServer();
			}
		});
		
		itemAddCategory.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				treePanel.addNewCategory();
			}
		});
		itemAddRootCategory.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//controller.getServers().addChild(cat);
				treePanel.addRootcategory();
			}
		});
		itemRemServers.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				treePanel.removeServer();
			}
		});
		
		itemRemCategory.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				treePanel.removeCategory();
			}
		});

		itemLogged.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!controller.isLogged())
				{
					PasswordDialog pd = new PasswordDialog(controller);
					pd.setVisible(true);
					System.out.println(controller.isLogged());
					if(!controller.isLogged())
					{
						JOptionPane.showMessageDialog(null, 
	 			   			 "Wrong password", "alert",
	 			   			JOptionPane.ERROR_MESSAGE);
					} else {
						itemLogged.setText("Log off");
						itemAddNewBadWord.setEnabled(true);
						itemAddNewBadLink.setEnabled(true);
					}
				}else {
					itemLogged.setText("Log on");
					controller.logOff();
					itemAddNewBadWord.setEnabled(false);
					itemAddNewBadLink.setEnabled(false);
					
				}
			}
		});
		itemChangePass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				PasswordChangeDialog pd = new PasswordChangeDialog(controller);
				pd.setVisible(true);
				System.out.println(controller.isLogged());
			}
		});
		itemAddNewBadWord.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String newWord = JOptionPane.showInputDialog(
						   null, 
						   "Add new word: ", 
						   "Input", 
						   JOptionPane.OK_CANCEL_OPTION);
				if(newWord != null)
				{
					Ban ban = Ban.getInstanceBan();
					try {
						ban.addWord(newWord);
					} catch (IOException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, 
		 			   			 "Error write file", "alert",
		 			   			JOptionPane.ERROR_MESSAGE);
					} catch (InvalidKeyException e) {
						e.printStackTrace();
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					} catch (InvalidKeySpecException e) {
						e.printStackTrace();
					} catch (NoSuchPaddingException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		itemAddNewBadLink.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String newLink = JOptionPane.showInputDialog(
						   null, 
						   "Add new link: ", 
						   "Input", 
						   JOptionPane.OK_CANCEL_OPTION);
				if(newLink != null)
				{
					Ban ban = Ban.getInstanceBan();
					try {
						ban.addLink(newLink);
					} catch (IOException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, 
		 			   			 "Error write file", "alert",
		 			   			JOptionPane.ERROR_MESSAGE);
					} catch (InvalidKeyException e) {
						e.printStackTrace();
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					} catch (InvalidKeySpecException e) {
						e.printStackTrace();
					} catch (NoSuchPaddingException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		itemParameters.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DialogParameters dp = new DialogParameters();
				dp.setVisible(true);
			}
		});
		
		newsShowAll.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				treePanel.showAllRSS();
			}
		});
		newsShowNew.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				treePanel.showAllRSSNotRead();
			}
		});
		newsShowLast.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				treePanel.showAllLastRSSCategory();
			}
		});
		newsReloadAll.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				treePanel.updateAllServers();
			}
		});
		helpHelp.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
	    			//java.awt.Desktop.getDesktop().browse(new URI("/resources/pages/help.html"));
					String path = "";
					URL url;
					 try {
							path = "/resources/pages/help.html";
							url = getClass().getResource(path);
					  } catch (Exception e) {
							System.err.println("Failed to open " + path);
							url = null;
					  }
					  if(url != null)
					  {
						  java.awt.Desktop.getDesktop().browse(url.toURI());
					  } else {
						  System.err.println("Failed to open " + path);
					  }
						     
	    		} catch (IOException e1) {
	    			JOptionPane.showMessageDialog(null, 
	 			   			 "Error read help file", "alert",
	 			   			JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				} catch (URISyntaxException e2) {
					JOptionPane.showMessageDialog(null, 
	 			   			 "Error read help file", "alert",
	 			   			JOptionPane.ERROR_MESSAGE);
					e2.printStackTrace();
				}
			/*	try {
					FileReader fw = new FileReader("resources/pages/help.html");
					char[] c = new char[1];
					fw.read(c);
					System.out.println(Arrays.toString(c));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
		});
		helpWelcome.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				openWelcome();
			
			}
		});
		
		helpAbout.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new About();
			}
		});
		
		return menu;
		
	}
	/**
	 * Open welcome page on textPage
	 */
	private void openWelcome()
	{
		try {
			String path = "";
			URL url;
			 try {
					path = "/resources/pages/welcome.html";
					url = getClass().getResource(path);
			  } catch (Exception e) {
					System.err.println("Failed to open " + path);
					url = null;
			  }
			  if(url != null)
			  {
				 // java.awt.Desktop.getDesktop().browse(url.toURI());
				  textPanel.openPage(url);
			  } else {
				  System.err.println("Failed to open " + path);
			  }
				     
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, 
			   			 "Error read help file", "alert",
			   			JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} 
	}
	/**
	 * Create and open About window 
	 * @author Nikolay Klimchuk
	 */
	private class About extends JFrame
	{
		JFrame th = this;
		public About()
		{
			ImageIcon icon = createImageIcon("31.png","");
			
			JLabel ic = new JLabel(icon);
			
			JLabel text = new JLabel("Program for colecting " +
					"RSS feeds!");
			JLabel autor = new JLabel("Written by Nicolay Klimchuk");
			
			
			
			JButton Ok = new JButton("  OK  ");
			Ok.addActionListener(new ActionListener(){
			@Override
				public void actionPerformed(ActionEvent arg0) {
					th.setVisible(false);
				}
				
			});
			JPanel panOk = new JPanel(new FlowLayout());
			panOk.add(Ok);
			
			JPanel textPan = new JPanel(new GridLayout(2,1));
			textPan.add(text);
			textPan.add(autor);
			
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setLayout(new BorderLayout());
			
			add(textPan, BorderLayout.CENTER);
			add(panOk, BorderLayout.SOUTH);
			add(ic, BorderLayout.WEST);
			
			this.setTitle("About");
			//this.pack();
			this.setSize(400, 200);
			this.setVisible(true);
		}
	}
	/**
	 * Open image and create ImageIcon 
	 * @param filename - image filename
	 * @param description - description of image
	 * @return
	 */
	 private ImageIcon createImageIcon(String filename, String description) {
			    String path = "/resources/images/" + filename;
			    ImageIcon ii = new ImageIcon(getClass().getResource(path), description);
			    return ii;
    } 
}
