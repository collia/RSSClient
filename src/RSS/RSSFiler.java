package RSS;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.crypto.NoSuchPaddingException;

import main.RSSLogger;

import org.jdom.JDOMException;
import view.StatusBar;

import com.sun.syndication.io.FeedException;

import controller.Controller;

import RSS.data.Answer;
import RSS.data.RSSEntry;
import RSS.data.RSSServer;
import RSS.filer.RSSFilesReader;

/**
 * Class implements RSSReader. Loading old RSS feeds from file.
 * 
 * @author Nikolay Klimchuk
 *
 */

public class RSSFiler implements RSSReader {
	private static Logger logger = RSSLogger.loggerRSSFile;
	
	private RSSFilesReader reader;
	private Controller controller;
	private StatusBar status;
	/**
	 * Constructor
	 * @param c - instance of Controller
	 */
	
	public RSSFiler(Controller c){
		logger.fine("Construstor");
		status = StatusBar.getStatusBar();
		controller = c;
	}
	/**
	 * Return last num entries for server and mark them us read. 
	 */
	@Override
	public Answer getLastRSS(RSSServer server, int num) {
		logger.fine("getLastRSS(RSSServer server, int num)");
		return getLastRSS(server, num, true, controller.isLogged());
	}
	/**
	 * Return <i>num</i> last entries for <i>server</i>
	 * @param server - name of server
	 * @param num - number of last entries
	 * @param isWantToRead - if true - mark entries as read
	 * @param islogged - if true returns 18+ entries too. 
	 * @return - num last entries for server
	 */
	public Answer getLastRSS(RSSServer server, int num, boolean isWantToRead, boolean islogged) {
		logger.entering(this.getClass().getName(), "getLastRSS(RSSServer server, int num, boolean isWantRead, boolean islogged)");
		reader = new RSSFilesReader("/"+server.getLocalCategory().toString()+"/"+server.toString());
		Answer result = new Answer(new HashMap<RSSServer, List<RSSEntry>>(), 
				new HashMap<RSSServer, Exception>());
		
		status.setMessage("Loading: "+server.getName());
		logger.fine("Loading: " + server.getName());		
		try {
			List<RSSEntry> list = reader.getLastEntries(num, isWantToRead, islogged);
			result.addEntryes(server, list);
			 status.setMessage("OK load: " + server.getName());
		} catch (IllegalArgumentException e) {
			//e.printStackTrace();
			logger.throwing(this.getClass().getName(),
					"getLastRSS(RSSServer server, int num, boolean isWantRead, boolean islogged)", 
					e);
			result.addException(server, e);
		} catch (JDOMException e) {
			//e.printStackTrace();
			logger.throwing(this.getClass().getName(),
					"getLastRSS(RSSServer server, int num, boolean isWantRead, boolean islogged)", 
					e);
			result.addException(server, e);
		} catch (IOException e) {
			//e.printStackTrace();
			logger.throwing(this.getClass().getName(),
					"getLastRSS(RSSServer server, int num, boolean isWantRead, boolean islogged)", 
					e);
			result.addException(server, e);
			 status.setMessage("Error file read: " + server.getName());
		} catch (FeedException e) {
			//e.printStackTrace();
			logger.throwing(this.getClass().getName(),
					"getLastRSS(RSSServer server, int num, boolean isWantRead, boolean islogged)", 
					e);
		} catch (InvalidKeyException e) {
			//e.printStackTrace();
			logger.throwing(this.getClass().getName(),
					"getLastRSS(RSSServer server, int num, boolean isWantRead, boolean islogged)", 
					e);
			result.addException(server, e);
		} catch (NoSuchAlgorithmException e) {
			//e.printStackTrace();
			logger.throwing(this.getClass().getName(),
					"getLastRSS(RSSServer server, int num, boolean isWantRead, boolean islogged)", 
					e);
			result.addException(server, e);
		} catch (InvalidKeySpecException e) {
			//e.printStackTrace();
			logger.throwing(this.getClass().getName(),
					"getLastRSS(RSSServer server, int num, boolean isWantRead, boolean islogged)", 
					e);
			result.addException(server, e);
		} catch (NoSuchPaddingException e) {
			//e.printStackTrace();
			logger.throwing(this.getClass().getName(),
					"getLastRSS(RSSServer server, int num, boolean isWantRead, boolean islogged)", 
					e);
			result.addException(server, e);
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			logger.throwing(this.getClass().getName(),
					"getLastRSS(RSSServer server, int num, boolean isWantRead, boolean islogged)", 
					e);
			result.addException(server, e);
		}
		logger.exiting(this.getClass().getName(), "getLastRSS(RSSServer server, int num, boolean isWantRead, boolean islogged)");
		return result;
	}

	/**
	 * Method return last <i>num</i> entries for the <i>server</i>
	 */
	@Override
	public Answer getLastRSS(List<RSSServer> server, List<Integer> num) {
		logger.fine("getLastRSS(List<RSSServer> server, List<Integer> num)");
		return getLastRSS(server, num, true, controller.isLogged());
	}
	/**
	 * Method return last <i>num</i> entries for the <i>server</i>
	 * @param server
	 * @param num
	 * @param isWantRead
	 * @param isLogged
	 * @return last <i>num</i> entries for the <i>server</i>
	 */
	public Answer getLastRSS(List<RSSServer> server, List<Integer> num, boolean isWantRead, boolean isLogged) {
		logger.entering(this.getClass().getName(), 
				"getLastRSS(List<RSSServer> server, List<Integer> num, boolean isWantRead, boolean isLogged)");
		Iterator<RSSServer> itServer = server.iterator();
		Iterator<Integer> itNum = num.iterator();
		
		Answer ans = new Answer(new HashMap<RSSServer, List<RSSEntry>>(), 
				new HashMap<RSSServer, Exception>());
		status.setProgress(0);
		int step = (int)((status.MAX_SIZE-status.MIN_SIZE)/(server.size()+0.01));
		
		while(itServer.hasNext()){
			RSSServer serv = itServer.next();
			logger.fine("Load from file: " + serv);
			status.setProgress(status.getProgress() + step +1);
			if(itNum.hasNext()){
				ans.add(getLastRSS(serv, itNum.next().intValue(), isWantRead, isLogged));
			} else {
				ans.add(getLastRSS(serv, -1, isWantRead, isLogged));
			}
		}
		logger.exiting(this.getClass().getName(), 
				"getLastRSS(List<RSSServer> server, List<Integer> num, boolean isWantRead, boolean isLogged)");
		return ans;
	}

}
