package RSS.filer;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.crypto.NoSuchPaddingException;

import main.RSSLogger;

import org.jdom.JDOMException;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;

import controller.Controller;
import controller.Parameters;

import RSS.data.RSSEntry;

/**
 * 
 * Class read entries from file 
 * @author Nicolay Klimchuk
 *
 */
public class RSSFilesReader {
	
	private String path = "";
	private static Logger logger = RSSLogger.loggerRSSFilerReader; 
	
	/**
	 * Constructor. 
	 * @param feed  - Path to files with entries
	 */
	public RSSFilesReader(String feed){
		path = feed;
		logger.fine("Constructor");
	}

	/**
	 * Method read from file last number entries and return. If isLogged
	 * read 18+ entries too.   
	 * @param number - number of entries need read
	 * @param wantToRead - mark entries as read
	 * @param isLogged - if true - loading 18+ entries
	 * @return list with loaded entries
	 * @throws JDOMException
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws FeedException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws ClassNotFoundException
	 */
	public List<RSSEntry> getLastEntries(int number, boolean wantToRead, boolean isLogged) 
		throws JDOMException, IOException, IllegalArgumentException, 
		FeedException, InvalidKeyException, NoSuchAlgorithmException, 
		InvalidKeySpecException, NoSuchPaddingException, ClassNotFoundException
	{
		logger.entering(this.getClass().getName(), 
				"List<RSSEntry> getLastEntries(int number, boolean wantToRead, boolean isLogged)");
		List<RSSEntry> result = new ArrayList<RSSEntry>();
		RSSEntry entry;
		int nuberOfEntryes = 0;
		TreeFile tf = new TreeFile(path);
		List<String> fileNames = tf.getFilenames(number, isLogged);
		
		Map<String,Set<Integer>> readed = tf.getNumberNotReaded(fileNames);
		Map<String,Set<Integer>> realyReaded = new HashMap<String, Set<Integer>>();
		
		Iterator<String> it = fileNames.iterator();
		
		SyndFeedInput sfi = new SyndFeedInput();
		
		iter:	while(it.hasNext()){
			String filename = it.next();
			realyReaded.put(filename, new TreeSet<Integer>());
			logger.fine("Read: " + filename);
			if(filename.matches("[0-9_]*.xml")){
			
				SyndFeed sf = sfi.build(new File("doc/"+path+"/"+ filename));
				List<SyndEntry> entries = sf.getEntries();
				logger.fine("Load " + entries.size() + " entries");
				int nuberOfEntryesInFile = 0;
				for(SyndEntry se : entries)
				{	
					entry = new RSSEntry(se); 
					if(readed.get(filename).contains(nuberOfEntryesInFile))
					{
						entry.setReaded(false);
						realyReaded.get(filename).add(nuberOfEntryesInFile);
					}
					else
					{
						entry.setReaded(true);
					}
					result.add(entry );
					if((number != -1) && (++nuberOfEntryes >= number)) break iter;
					nuberOfEntryesInFile++;
				}
			}
			 else if(filename.matches("[0-9_]*.crt"))
				{
					Crypt crypt = new Crypt(Parameters.getParameters().getKeyFile());
					List<RSSEntry> entries =  (List<RSSEntry>) crypt.decryptFile("doc/"+path+"/"+ filename);
					
					logger.fine("Load " + entries.size() + " entries");
					
					int nuberOfEntryesInFile = 0;
					for(RSSEntry se : entries)
					{	
						if(readed.get(filename).contains(nuberOfEntryesInFile))	{
							se.setReaded(false);
							realyReaded.get(filename).add(nuberOfEntryesInFile);
						}
						else {
							se.setReaded(true);
						}
						result.add(se);
						if((number != -1) && (++nuberOfEntryes >= number)) break iter;
						nuberOfEntryesInFile++;
					}
					
				}
		}
		if(wantToRead)
			tf.markUsReaded(realyReaded);
		logger.entering(this.getClass().getName(), 
			"List<RSSEntry> getLastEntries(int number, boolean wantToRead, boolean isLogged)");
		return result;	
	}
	
}
