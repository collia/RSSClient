package test.RSS;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
//import java.util.List;

import org.jdom.*;
import org.jdom.input.SAXBuilder;


import org.junit.Before;
import org.junit.Test;

import RSS.data.RSSServer;

import com.sun.syndication.feed.synd.*;
import com.sun.syndication.io.FeedException;

public class RSSServerTest {

	SyndFeed feed;
	
	@Before
	public void setUp() throws Exception {
		feed = new SyndFeedImpl();
		feed.setAuthor("Tester");
		feed.setLanguage("ru");
		feed.setTitle("Hello world");
		
	}
	
	@Test
	public void testHashCode() {
		SyndFeed feed2 = new SyndFeedImpl();
		feed2.setAuthor("Tester");
		feed2.setLanguage("ru");
		feed2.setTitle("Hello world");
		
		RSSServer server1 = new RSSServer(feed, "feed");
		RSSServer server2 = new RSSServer(feed2, "feed");
		
		assertTrue(server1.hashCode() == server2.hashCode());
		
		SyndFeed feed3 = new SyndFeedImpl();
		feed3.setAuthor("Tester2");
		feed3.setLanguage("ru");
		feed3.setTitle("Hello world");
		RSSServer server3 = new RSSServer(feed3, "feed3");
		
		assertTrue(server1.hashCode() != server3.hashCode());
	}

	@Test
	public void testRSSServerSyndFeedString() {
		RSSServer server = new RSSServer(feed, "first");
		
		assertEquals("first", server.getName());
		assertEquals("Tester", server.getAuthor());
		assertEquals("ru", server.getLanguage());
		assertEquals("Hello world", server.getTitle());
	}

	@Test
	public void testRSSServerElementString() {
		//fail("Not yet implemented");
		File f = new File("doc/testRSS.xml");
		
		SAXBuilder builder = new SAXBuilder();
		try {
			Document doc = builder.build(f);
		/*	Element e = doc.getRootElement();
			List channels = e.getChildren("channel");
			Element channel = (Element) channels.get(0);
			
			RSSServer server = new RSSServer(channel, 
											channel.getAttributeValue("name"));
		*/	
			RSSServer server = new RSSServer(doc,	"name");
			assertEquals("name", server.getName());
			assertEquals("IT happens", server.getTitle());
			assertEquals("IT-истории из жизни", server.getDescription());
			
			
		} catch (JDOMException e) {
			e.printStackTrace();
			fail("Exception" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception" + e.getMessage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail("Exception" + e.getMessage());
		} catch (FeedException e) {
			e.printStackTrace();
			fail("Exception" + e.getMessage());
		}
		
	}

	@Test
	public void testEqualsObject() {
		//fail("Not yet implemented");
		SyndFeed feed2 = new SyndFeedImpl();
		feed2.setAuthor("Tester");
		feed2.setLanguage("ru");
		feed2.setTitle("Hello world");
		
		RSSServer server1 = new RSSServer(feed, "feed");
		RSSServer server2 = new RSSServer(feed2, "feed");
		
		assertTrue(server1.equals(server1));
		assertTrue(server1.equals(server2));
		assertTrue(server2.equals(server2));
		assertTrue(server2.equals(server1));
		
		SyndFeed feed3 = new SyndFeedImpl();
		feed3.setAuthor("Tester2");
		feed3.setLanguage("ru");
		feed3.setTitle("Hello world");
		RSSServer server3 = new RSSServer(feed3, "feed3");
		
		assertFalse(server1.equals(server3));
		assertFalse(server3.equals(server1));
	}

}
