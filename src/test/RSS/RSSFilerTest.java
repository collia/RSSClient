package test.RSS;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import controller.Controller;

import RSS.RSSFiler;
import RSS.RSSReader;
import RSS.data.Answer;
import RSS.data.Category;
import RSS.data.RSSServer;

public class RSSFilerTest {

	RSSReader reader;
	

	RSSServer rssUrl;
/*	RSSServer rssUrl2;
	RSSServer rssUrl3;
*/	
	
	@Before
	public void setUp() throws Exception {
		reader = new RSSFiler(new Controller());
		
		rssUrl = new RSSServer("bash_org");
		rssUrl.setLocalCategory(new Category(null).setName("news"));
		
		
		
	}

	@Test
	public void testGetLastRSSRSSServerInt() {
		//fail("Not yet implemented");
		Answer data = reader.getLastRSS(rssUrl, 5);

		assertEquals(5, data.getEntryes().get(rssUrl).size());
		
		assertEquals("#3068: Испорченные торрентами", 
				data.getEntryes().get(rssUrl).get(0).getEntry().getTitle());
		assertEquals("#3064: Естественный отбор железным сбором", 
				data.getEntryes().get(rssUrl).get(4).getEntry().getTitle());
		
		
		assertEquals(0, data.getExceptions().size());
	
	}

	@Test
	public void testGetLastRSSException() {
		//fail("Not yet implemented");
		RSSServer badUrl = new RSSServer("hello", new Category(null).setName("world"));
		Answer data = reader.getLastRSS(badUrl, 5);

		assertFalse(data.getExceptions().isEmpty());
		assertTrue(data.getExceptions().get(badUrl) instanceof IOException);
		
	}

}
