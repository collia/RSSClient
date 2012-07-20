package view.html;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

import org.jdom.JDOMException;

import com.sun.syndication.io.FeedException;

import RSS.data.Answer;
import RSS.data.RSSEntry;
import RSS.data.RSSServer;
import controller.Controller;
import controller.Parameters;;

/**
 * Class implements formatter html page for TextPanel
 * @author Nicolay Klimchuk
 *
 */
public class DataFormater {
	SmallStack<String> backHtml;
	SmallStack<String> forvHtml;// = new SmallStack<String>(Parameters.BUFFER_LENGHT);
	Controller controller;
	StringBuilder page;
	
	/**
	 * Constructor. get instance of controller
	 * @param contr - instance of controller
	 */
	public DataFormater(Controller contr)
	{
		controller = contr;
		int bufLen = Parameters.getParameters().getBufferLenght();
		backHtml = new SmallStack<String>(bufLen);
		forvHtml = new SmallStack<String>(bufLen);
		
	}
	/**
	 * Open last <i>numLast</i> entries of List<RSSServer> <i>s</i>  
	 * @param s - List of servers 
	 * @param numLast - number of entries to load
	 * @return  html page with entries
	 */
	public String openRSSPage(List<RSSServer> s, int numLast)
	{
		page = new StringBuilder();
		page.append("<html><body>");
		Answer ans = controller.getLast(s, numLast);
		
		formatAnswer(s, ans/*, isAll*/);

		page.append("</body></html>");
		backHtml.add(page.toString());
		return page.toString();
	}
	
	/**
	 * Open last <i>numLast</i> entries of RSSServer <i>s</i>  
	 * @param s - List of servers 
	 * @param numLast - number of entries to load
	 * @return  html page with entries
	 */
	public String openRSSPage(RSSServer s, int numLast)
	{
		ArrayList<RSSServer> list = new ArrayList<RSSServer>();
		list.add(s);
		return openRSSPage(list, numLast);
	}
	/**
	 * Load last RSS and create html page  
	 * @param s - list of servers 
	 * @param numLast - number of entries to download. -1 - load all
	 * @return  html page with <i>numList</i> entries
	 */
	public String updateRSSPage(List<RSSServer> s, int numLast)
	{
		page = new StringBuilder();
		page.append("<html><body><TABLE>");
		Answer ans = controller.update(s, numLast);
		
		formatAnswer(s, ans);
	
		try {
			controller.writeRSS(ans);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			page.append(e.getLocalizedMessage());
		} catch (JDOMException e) {
			e.printStackTrace();
			page.append(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			page.append(e.getLocalizedMessage());
		} catch (FeedException e) {
			e.printStackTrace();
			page.append(e.getLocalizedMessage());
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			page.append(e.getLocalizedMessage());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			page.append(e.getLocalizedMessage());
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			page.append(e.getLocalizedMessage());
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			page.append(e.getLocalizedMessage());
		}
		page.append("</TABLE></body></html>");
		backHtml.add(page.toString());
		return page.toString();
	}
	
	/**
	 * Method load new RSS from server and format 
	 * @param s - server  
	 * @param numLast - number of entries to download. -1 - load all
	 * @return formatted html page with loaded entries
	 */
	public String updateRSSPage(RSSServer s, int numLast)
	{
		ArrayList<RSSServer> list = new ArrayList<RSSServer>();
		list.add(s);
		return updateRSSPage(list, numLast/*, isAll*/);
	}
	
	/**
	 * Load from disk all not read entries
	 * @param s - List of servers 
	 * @return formatted html page with loaded entries
	 */
	public String notReadedRSSPage(List<RSSServer> s)
	{
		page = new StringBuilder();
		page.append("<html><body>");
		Answer ans = controller.getNotReadEntryes(s);
		
		formatAnswer(s, ans);
	
		page.append("</body></html>");
		backHtml.add(page.toString());
		return page.toString();
	}
	/**
	 * Load from disk all not read entries
	 * @param s - server
	 * @return formatted html page with loaded entries
	 */
	public String notReadedRSSPage(RSSServer s)
	{
		ArrayList<RSSServer> list = new ArrayList<RSSServer>();
		list.add(s);
		return notReadedRSSPage(list);
	}
	/**
	 * Method create html page with entries from <i>ans</i> with servers in <i>s</i>
	 * @param s - list of servers 
	 * @param ans - Answer with entries 
	 */
	private void formatAnswer(List<RSSServer> s, Answer ans)
	{
		Iterator<RSSServer> it = s.iterator();
		
		while(it.hasNext())
		{
			RSSServer server = it.next();
			addServerToPage(page,server);
			if(ans.getExceptions().containsKey(server))
			{
				addException(page,ans.getExceptions().get(server));
			}
			else
			{
				if(ans.getEntryes().containsKey(server))
				{
					Iterator<RSSEntry> itEntr = ans.getEntryes().get(server).iterator();
					while(itEntr.hasNext())
					{
						RSSEntry entry = itEntr.next(); 
						addEntry(page, entry);
						entry.setReaded(true);
					}
				}
			}
			
		}
	}
	/**
	 * Add to page name of server
	 * @param page - result StringBuffer
	 * @param s - server
	 */
	private void addServerToPage(StringBuilder page, RSSServer s){
		page.append("<tr  bgcolor=#D0D0E5> <th height=\"60\"> <h2>"+s.getName() + "</h2></th></tr>\n");
	}
	/**
	 * Add message of exception to page 
	 * @param page -  result StringBuffer
	 * @param e - exception
	 */
	private void addException(StringBuilder page, Exception e)
	{
		page.append( "<tr> <th align=left height=\"25\"> <font color=red>"+"Exception: "+
						e.getLocalizedMessage()+
						"</font> </th> </tr>");
	}
	/**
	 * Add text of entry to page 
	 * @param page - result StringBuffer
	 * @param ent - RSS entry
	 */
	private void addEntry(StringBuilder page, RSSEntry ent)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(" dd MMMM yyyy, HH:mm" );
		
		page.append("<tr bgcolor=#CDff65> <th height=\"25\"  align=left>"+ent.getEntry().getTitle()+"</th></tr>");
		page.append("<tr> <th align=left>"+ent.getEntry().getDescription().getValue()+"</th> </tr>");
		page.append("<tr> <th align=left  height=15 >"+ 
				formatter.format(ent.getEntry().getPublishedDate()) + 
				"</th> </tr>"
				);
		page.append("<tr> <th align=right  height=25 valign=bottom>"+ "Read more: " +
				"<a href=\""+ent.getEntry().getUri()+"\">"+
				ent.getEntry().getUri()+"</a>" + 
				"</th> </tr>"
				);
		
	}
	
}
