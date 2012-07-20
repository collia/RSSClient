package RSS.filer;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.crypto.NoSuchPaddingException;

import main.RSSLogger;

import org.jdom.JDOMException;

import RSS.data.Category;
import RSS.data.RSSEntry;
import RSS.data.RSSServer;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.SyndFeedOutput;

import controller.Parameters;
/**
 * Class write RSS feeds to file. 
 * 
 * @author Nicolay Klimchuk
 *
 */
public class RSSFileWriter {
	private String path = "";
	private static Logger logger = RSSLogger.loggerRSSFilerWriter;
	
	/**
	 * Constructor.
	 * @param feed - server path
	 */
	public RSSFileWriter(String feed){
		path = feed;
		logger.fine("Constructor");
	}
	/**
	 * Method write data to file. Filename generating by TreeFile
	 * @see TreeFile
	 * @param data - list with entries
	 * @param serv - server, where write entries
	 * @throws IllegalArgumentException
	 * @throws JDOMException
	 * @throws IOException
	 * @throws FeedException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 */
	public void writeRSS(List<RSSEntry> data, RSSServer serv) 
		throws IllegalArgumentException, JDOMException, 
		IOException, FeedException, InvalidKeyException,
		NoSuchAlgorithmException, InvalidKeySpecException, 
		NoSuchPaddingException
	{
		logger.entering(this.getClass().getName(), 
				"writeRSS(List<RSSEntry> data, RSSServer serv) ");
		if(!data.isEmpty()){
			TreeFile tf = new TreeFile(path + "/" + serv.toString() + "/");
			
			List<RSSEntry> d = new ArrayList<RSSEntry>();
			RSSEntry old = null;
			int counter = 0;
			for (RSSEntry i : data)
			{
				if( old != null && (old.is18plus() == i.is18plus()) )		
					d.add(i);
				else if(old == null) 
					d.add(i);
				else {
					if(!old.is18plus())
					{
						writeLastEntries(d, serv, tf.writeFeeds(data, counter++, false));
						logger.fine("Write " + d.size() + " not 18+ entryes");
						d.clear();
						d.add(i);
					}else
					{
						writeLastEntries18plus(d, serv, tf.writeFeeds(data, counter++, true));
						logger.fine("Write " + d.size() + " 18+ entryes");
						d.clear();
						d.add(i);
					}
				}
				old = i;	
			}
			if(!old.is18plus())
			{
				writeLastEntries(d, serv, tf.writeFeeds(data, counter++, false));
				logger.fine("Write " + d.size() + " not 18+ entryes");
			}else
			{
				writeLastEntries18plus(d, serv, tf.writeFeeds(data, counter++, true));
				logger.fine("Write " + d.size() + " 18+ entryes");
			}
			
		}
	}
	/**
	 * Write list of entries to file. 
	 * @param data - writing data
	 * @param serv - server of data
	 * @param filename - name of file
	 * @throws JDOMException
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws FeedException
	 */
	// Public only for JUnit tests.
	/*private*/public void writeLastEntries(List<RSSEntry> data, RSSServer serv, String filename) 
				throws JDOMException, IOException, IllegalArgumentException, FeedException{

		logger.entering(this.getClass().getName(), 
				"writeLastEntries(List<RSSEntry> data, RSSServer serv, String filename)");

		SyndFeedImpl sfi = new SyndFeedImpl();
		sfi.setAuthor(serv.getAuthor());
		sfi.setDescription(serv.getDescription());
		sfi.setFeedType(serv.getFeedType());
		sfi.setTitle(serv.getTitle());
		sfi.setLink(serv.getLink());
		
		ArrayList<SyndEntry> entries = new ArrayList<SyndEntry>();
		
		Iterator<RSSEntry> it = data.iterator();
		SyndEntry entry;
		while(it.hasNext())
		{
			entry = it.next().getEntry();
			entries.add(entry);
		}
		SyndFeedOutput sfo = new SyndFeedOutput();
	
		sfi.setEntries(entries);
		
		File dir = new File("doc/"+path+"/" + serv.toString() + "/");
		logger.fine("Directory: " + dir.getAbsoluteFile());
		if(!dir.exists())
			dir.mkdir();
		else if(dir.isDirectory())
		{
			sfo.output(sfi, new File("doc/"+path+"/" +  serv.toString() + "/" + filename));
			logger.fine("File: " + "doc/"+path+"/" +  serv.toString() + "/" + filename);
		}
		logger.exiting(this.getClass().getName(), 
			"writeLastEntries(List<RSSEntry> data, RSSServer serv, String filename)");
	}
	/**
	 * Encode and write entries, that 18+ 
	 * @param data - Data to write
	 * @param serv - server of entries
	 * @param filename - file name, where write
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws IOException
	 */
	private void writeLastEntries18plus(List<RSSEntry> data, RSSServer serv, String filename) 
		throws InvalidKeyException, NoSuchAlgorithmException, 
			InvalidKeySpecException, NoSuchPaddingException, IOException{
		Crypt crypt = new Crypt(Parameters.getParameters().getKeyFile());
		crypt.encryptFile("doc/"+path+"/" +  serv.toString() + "/" +filename, data);
		logger.fine("Write 18+ to file: "+ "doc/"+path+"/" +  serv.toString() + "/" +filename);
	}
	/**
	 * Method rename directory, where saved entries 
	 * @param server - renamed server 
	 * @param newName - new name of server
	 */
	public void renameServer(RSSServer server, String newName)
	{
		File dir = new File("doc/"+path+"/" + server.toString() + "/");
		if(!dir.exists())
			dir.mkdir();
		else if(dir.isDirectory())
		{
			File newDir = new File("doc/"+path+"/" + newName);
			//System.out.println("rename server: " + dir.renameTo(newDir));
			logger.info("Rename server: " + dir.renameTo(newDir));
			logger.fine("New name: " + newDir.getName());
		}
	}
	/**
	 * Rename directory - category
	 * @param oldName - name of renamed directory
	 * @param newName - new name of directory - 
	 */
	public void renameCategory(Category oldName, String newName)
	{
		File dir = new File("doc/"+oldName.toString()+"/");
		if(!dir.exists())
			dir.mkdir();
		else if(dir.isDirectory())
		{
			File newDir = new File("doc/"+oldName.getParent().toString()+"/"+newName+"/");
			//System.out.println("rename category: " + dir.renameTo(newDir));
			logger.info("Rename category: " + dir.renameTo(newDir));
			logger.fine("New name: " + newDir.getName());
		}
	}
	
}
