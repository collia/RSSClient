package RSS.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class contains answer from  RSSReader
 * @author Nicolay Klimchuk
 *
 */

public class Answer {
	private Map<RSSServer, List<RSSEntry>> entryes;
	private Map<RSSServer, Exception> exceptions;
	
	/**
	 * Constructor. Gets Map(server, List<RSSEntry>) and Map(server, Extension>)
	 * If server in first Map, it can't be in second. 
	 * @param entryes - Map with entries 
	 * @param exceptions - Map with Extensions
	 */
	public Answer(Map<RSSServer, List<RSSEntry>> entryes, 
					Map<RSSServer, Exception> exceptions)
	{
		this.entryes = entryes;
		this.exceptions = exceptions;
	}

	/**
	 * Method returns map with entries.  
	 * @return Map(server, list(entries))
	 */
	public Map<RSSServer, List<RSSEntry>> getEntryes() {
		return entryes;
	}
	/**
	 * Method returns map with exceptions.  
	 * @return Map(server, list(entries))
	 */
	public Map<RSSServer, Exception> getExceptions() {
		return exceptions;
	}
	/**
	 * Add all data from another answer
	 * @param a - another answer
	 */
	public void add(Answer a){
		entryes.putAll(a.getEntryes());
		exceptions.putAll(a.getExceptions());
	}
	/**
	 * Add data.
	 * @param server - server name
	 * @param ans - new data
	 */
	public void addEntryes(RSSServer server, List<RSSEntry> ans){
		entryes.put(server, ans);
	}
	/**
	 * add one entry to the list in map
	 * @param server - server name
	 * @param ans - entry
	 */
	public void addEntry(RSSServer server, RSSEntry ans){
		if(!entryes.containsKey(server))
		{
			ArrayList<RSSEntry> list = new ArrayList<RSSEntry>();
			list.add(ans);
			entryes.put(server, list);
		} else 
		{
			entryes.get(server).add(ans);
		}
	}
	/**
	 * Add exception to map
	 * @param server 
	 * @param ans
	 */
	public void addException(RSSServer server, Exception ans)
	{
		exceptions.put(server, ans);
	}
	/**
	 * Add all exceptions from another answer
	 * @param ans - answer with exceptions
	 */
	public void addExceptions(Answer ans)
	{
		exceptions.putAll(ans.getExceptions());
	}
	/**
	 * Method remove all entries from local Map, which contains in anotherAnswer
	 * if( local.contains(A) && anotherAnswer.contains(A)) then remove A 
	 * @param anotherAnswer - entries needed to remove 
	 */
	public void minus(Answer anotherAnswer)
	{
		Iterator<RSSServer> itServer = anotherAnswer.getEntryes().keySet().iterator();
		while(itServer.hasNext())
		{
			RSSServer s = itServer.next();
			if(entryes.containsKey(s))
			{
			//	entryes.remove(s);
				Iterator<RSSEntry> itEntry = anotherAnswer.getEntryes().get(s).iterator();
				while(itEntry.hasNext())
					entryes.get(s).remove(itEntry.next());
			}
		}
		Iterator<RSSServer> itExceptions = anotherAnswer.getEntryes().keySet().iterator();
		while(itExceptions.hasNext())
		{
			RSSServer s = itExceptions.next();
			if(exceptions.containsKey(s))
				exceptions.remove(s);
		
		}
			
	}
	
	
}
