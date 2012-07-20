package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import main.RSSLogger;

import org.jdom.JDOMException;

import RSS.data.Answer;
import RSS.data.Category;
import RSS.data.RSSServer;

/**
 * Class create thread. Thread compare current time and
 * tile of last update - if time greater then update period - update server  
 * 
 * @author Nicolay Klimchuk
 */

public class Schedule {
	
	private List<RSSServer> servers;
	
	private Updater updater;
	private Controller controller;
	
	private static Logger logger = RSSLogger.loggerControllerSchedule; 
	
	/**
	 * constructor
	 * @param c - instance of controller
	 * @throws JDOMException
	 * @throws IOException
	 */
	public Schedule(Controller c) throws JDOMException, IOException
	{
		logger.entering(this.getClass().getName(), "Schedule(Controller c)");
	
		controller = c;
		servers = new ArrayList<RSSServer>();
		load(controller.getServers());
		updater = new Updater();
		updater.setName("Updater");
		updater.start();
		
		logger.exiting(this.getClass().getName(), "Schedule(Controller c)");
	}
	/**
	 * class - thread which compare current time and
	 * 	tile of last update - if time greater then update period - update server  
	 * @author Nicolay Klimchuk
	 *
	 */
	private class Updater extends Thread
	{
		/**
		 * Constructor is empty
		 */
		public Updater(){
			
		}
		/**
		 * main method of thread
		 */
		public void run(){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Thread t = Thread.currentThread();
			while(t == updater)
			{
				Iterator<RSSServer> it = servers.iterator();
				List<RSSServer> up = new ArrayList<RSSServer>();
				while(it.hasNext())
				{
					RSSServer s = it.next();
					if(s.getLastModified() != null)
						if(s.getLastModified().getTime() + 
							s.getModifyInterval() < (new Date()).getTime())
						{
							up.add(s);
							/*System.out.println("Add to update: " + s.getName() + 
									" " + (s.getLastModified().getTime() + s.getModifyInterval()) +
									" " + (new Date()).getTime());*/
							logger.finest("Add to update: " + s.getName() + 
									" " + (s.getLastModified().getTime() + s.getModifyInterval()) +
									" " + (new Date()).getTime());
						}
						
				}
				if(!up.isEmpty())
				{
					try {
						Answer ans = controller.update(up, Parameters.getParameters().getUpdateNumber());
						//System.out.println("Update! " + up + " "+ new Date());
						logger.finest("Update: " + up + " "+ new Date());
						controller.writeRSS(ans);
					} catch (Exception e) {
						logger.throwing(this.getClass().getName(),"run()" , e);
						JOptionPane error = new JOptionPane("Error, can;t update: " + e.getMessage(),
								JOptionPane.ERROR_MESSAGE);
						error.setVisible(true);
						//e.printStackTrace();
					}
					up.clear();
				}
				try {
					Thread.sleep(Parameters.getParameters().getUpdatePeriod());
				} catch (InterruptedException e) {
					//e.printStackTrace();
					logger.throwing(this.getClass().getName(),"run()" , e);
				}
			}
		}
	}
	
	
	/**
	 * Method load all servers from category root and all subcategories.
	 * Method recursively.
	 * @param root
	 */
	private void load(Category root)
	{
		Iterator<RSSServer> itServ = root.getServers().iterator();
		while(itServ.hasNext())
		{
			servers.add(itServ.next());
		}
		Iterator<Category> itCat = root.getChildren().iterator();
		while(itCat.hasNext())
		{
			load(itCat.next());
		}
	}
}
