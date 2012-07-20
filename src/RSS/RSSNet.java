package RSS;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import main.RSSLogger;

import view.StatusBar;

import RSS.data.Answer;
import RSS.data.RSSEntry;
import RSS.data.RSSServer;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * Class can download RSS entries and return for user  
 * @author Nikolay Klimchuk
 *
 */
public class RSSNet implements RSSReader {

	private List<SyndEntry> feed;
	private StatusBar status;
	
	private static Logger logger = RSSLogger.loggerRSSNet;
	/**
	 * Constructor
	 */
	public RSSNet(){
		logger.fine("Constructor");
 		status = StatusBar.getStatusBar();
	}
	
	/**
	 * Method send request to link <i>server.getUrl()</i>
	 * load num entries. num = -1 - load all 
	 */
	@Override
	public Answer getLastRSS(RSSServer server, int num) {
		  logger.entering(this.getClass().getName(), 
				"getLastRSS(RSSServer server, int num)");
		  URL feedUrl = server.getUrl();//new URL(link);
		  SyndFeedInput input = new SyndFeedInput();
	      SyndFeed f;
	      Answer ans = new Answer(new HashMap<RSSServer, List<RSSEntry>>(), 
					new HashMap<RSSServer, Exception>());
		  
	      status.setMessage("Downloading: " + server.getName());
	      logger.fine("Downloading: " + server.getName());
	      try {
	    		  f = input.build(new XmlReader(feedUrl));
	    		  logger.fine("Parsing");
	    		  ArrayList<RSSEntry> al = new ArrayList<RSSEntry>(); 
	    		  feed = f.getEntries();
	    		  int j = 0;
	    		  for(SyndEntry i : feed){
	    			  if((++j > num) && (num != -1)) break;
	    			  al.add(new RSSEntry(i));
	    		  }
	    		
	    		  copyParameters(server,f);
	    		  server.setLastModified(new Date());
	    		  ans.addEntryes(server, al);
	    		  status.setMessage("OK download: " + server.getName());
	    		  logger.fine("OK download: " + server.getName());

	      } catch (IllegalArgumentException e) {
	    	  logger.throwing(this.getClass().getName(), 
	    			  "getLastRSS(RSSServer server, int num)", e);
	    	  //e.printStackTrace();
	    	  ans.addException(server, e);
	      } catch (FeedException e) {
	    	  //e.printStackTrace();
	    	  logger.throwing(this.getClass().getName(), 
	    			  "getLastRSS(RSSServer server, int num)", e);
	    	  ans.addException(server, e);
	    	  status.setMessage("Bad data: " + server.getName());
	      } catch (IOException e) {
	    	  //e.printStackTrace();
	    	  logger.throwing(this.getClass().getName(), 
	    			  "getLastRSS(RSSServer server, int num)", e);
	    	  ans.addException(server, e);
	    	  status.setMessage("Timeout: " + server.getName());
	    	  logger.info("Timeout: " + server.getName());
	      } catch (NullPointerException e){
	    	  logger.throwing(this.getClass().getName(), 
	    			  "getLastRSS(RSSServer server, int num)", e);
	    	  ans.addException(server, e);
	      }
	      logger.exiting(this.getClass().getName(), 
				"getLastRSS(RSSServer server, int num)");
	      return ans;
	}
	/**
	 * Copy parameters from feed to server
	 * @param server - destination of parameters
	 * @param feed - source of server parameters
	 */
	private void copyParameters(RSSServer server, SyndFeed feed){
		logger.fine("Begin copy parameters");
		 if(server.getFeedType() == null)
			  server.setFeedType(feed.getFeedType());
		 if(server.getAuthor() == null)
			  server.setAuthor(feed.getAuthor());
		 if(server.getCategories() == null)
			  server.setCategories(feed.getCategories());
		 if(server.getEncoding() == null)
			  server.setEncoding(feed.getEncoding());
		 if(server.getCopyright() == null)
			  server.setCopyright(feed.getCopyright());
		 if(server.getImage() == null)
			  server.setImage(feed.getImage());
		 if(server.getLanguage() == null)
			  server.setLanguage(feed.getLanguage());
		 if(server.getTitle() == null)
			  server.setTitle(feed.getTitle());
		 if(server.getDescription() == null)
			  server.setDescription(feed.getDescription());
		  
	}
	/**
	 * Method download num entries for each server in servers.
	 *  
	 */
	@Override
	public Answer getLastRSS(List<RSSServer> servers, List<Integer> num) {
		logger.entering(this.getClass().getName(), 
				"getLastRSS(List<RSSServer> servers, List<Integer> num)");
		
		Iterator<RSSServer> itServer = servers.iterator();
		Iterator<Integer> itNum = num.iterator();
		
		Answer ans = new Answer(new HashMap<RSSServer, List<RSSEntry>>(), 
				new HashMap<RSSServer, Exception>());
		int step = (int)((status.MAX_SIZE-status.MIN_SIZE)/(servers.size()+0.01));
		status.setProgress(0);
		Map<RSSServer, List<RSSEntry>> result = new HashMap<RSSServer, List<RSSEntry>>();
		while(itServer.hasNext()){
			RSSServer serv = itServer.next();
				status.setProgress(status.getProgress() + step +1);
				
				if(itNum.hasNext()){
					ans.add(getLastRSS(serv, itNum.next().intValue()));
				} else {
					ans.add(getLastRSS(serv, -1));
				}
		}
		logger.exiting(this.getClass().getName(), 
			"getLastRSS(List<RSSServer> servers, List<Integer> num)");
		return ans;
	}
}
