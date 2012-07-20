package test.RSS.filer;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.jdom.JDOMException;
import org.junit.Before;
import org.junit.Test;

import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;

import RSS.data.Category;
import RSS.data.RSSEntry;
import RSS.data.RSSServer;
import RSS.filer.RSSFileWriter;
import RSS.filer.TreeFile;

public class RSSFileWriterTest {
	
	TreeFile tree;
	RSSFileWriter writer;
	RSSServer server;
	RSSEntry entry;
	
	@Before
	public void setUp() throws Exception {
		tree = new TreeFile("test");
		writer = new RSSFileWriter("test");
		server = new RSSServer("test");
		
		
		server.setAuthor("collia");
		server.setLink("http://localhost:8080/rss/testRSS2.xml");
		server.setLocalCategory(new Category(null).setName("test"));
		server.setFeedType("rss_2.0");
		server.setTitle("TESTING");
		server.setDescription("test description");
		
		
		
	//	e.setContents(contents);
		
/*		entry = new RSSEntry(e);
		entry.setLastViewed(new Date());
	*/		
		
	}

	@Test
	public void testWriteLastEntries() {
		//fail("Not yet implemented");
	//	List l = new ArrayList();
		
/*		SyndFeedImpl sfi = new SyndFeedImpl();
		sfi.setAuthor("collia");
		sfi.setDescription("Hello world");
		sfi.setFeedType("rss_2.0");
		sfi.setTitle("Test RSS");
		sfi.setLink("http://localhost:8080");
		
		SyndEntryImpl e = new SyndEntryImpl();
		e.setAuthor("TESTING");
	*/	
		
		ArrayList<RSSEntry> entries = new ArrayList<RSSEntry>();
		SyndEntryImpl sei = new SyndEntryImpl();
		sei.setAuthor("collia");
		SyndContentImpl sci =new SyndContentImpl();
		sci.setValue("HELLO WORLD");
		sei.setDescription(sci);
		
		
		entries.add(new RSSEntry(sei));
		
		sei = new SyndEntryImpl();
		sei.setAuthor("collia!!!");
		sci =new SyndContentImpl();
		sci.setValue("HELLO WORLD number two");
		sei.setDescription(sci);
		
		entries.add(new RSSEntry(sei));
		
//		entry = new RSSEntry(e);
//		entry.setLastViewed(new Date());
	//	sfi.setEntries(entries);
		
		
		
		//l.add(entry);
		
		try {
			writer.writeLastEntries(entries, server, tree.writeFeeds(entries,0, false));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail("Exception");
		} catch (JDOMException e) {
			e.printStackTrace();
			fail("Exception");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception");
		} catch (FeedException e) {
			e.printStackTrace();
			fail("Exception");
		}
	}
	
	@Test
	public void testWriteLastEntriesSeveralDirectory() 
	{
	
		try {
			tree = new TreeFile("test/test");
		
			writer = new RSSFileWriter("test/test");
			//server = new RSSServer("test");
	//		server.setLocalCategory(new Category(new Category(null).setName("test")).setName("test"));
			
			
			ArrayList<RSSEntry> entries = new ArrayList<RSSEntry>();
			SyndEntryImpl sei = new SyndEntryImpl();
			sei.setAuthor("collia");
			SyndContentImpl sci =new SyndContentImpl();
			sci.setValue("HELLO WORLD");
			sei.setDescription(sci);
			
			
			entries.add(new RSSEntry(sei));
			
			sei = new SyndEntryImpl();
			sei.setAuthor("collia!!!");
			sci =new SyndContentImpl();
			sci.setValue("HELLO WORLD number two");
			sei.setDescription(sci);
			
			entries.add(new RSSEntry(sei));
			
			writer.writeLastEntries(entries, server, tree.writeFeeds(entries,0, false));
			
		} catch (JDOMException e) {
			e.printStackTrace();
			fail("Exception");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail("Exception");
		} catch (FeedException e) {
			e.printStackTrace();
			fail("Exception");
		}
		
		
	}
	

}
