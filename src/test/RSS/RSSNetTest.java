package test.RSS;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.sun.syndication.io.FeedException;

import RSS.RSSNet;
import RSS.data.Answer;
import RSS.data.RSSEntry;
import RSS.data.RSSServer;

public class RSSNetTest {

	RSSServer rssUrl;
	RSSServer rssUrl2;
	RSSServer rssUrl3;
	
	RSSNet net;
	
	@Before
	public void setUp() throws Exception {
		rssUrl = new RSSServer("test1");
		rssUrl.setUrl(new URL("http://localhost:8080/rss/testRSS.xml"));
		
		rssUrl2 = new RSSServer("test2");
		rssUrl2.setUrl(new URL("http://localhost:8080/rss/testRSS2.xml"));
		
		rssUrl3 = new RSSServer("test3");
		rssUrl3.setUrl(new URL("http://localhost:8080/rss/testRSS5.xml"));
		
		
		net = new RSSNet();
	}

	@Test
	public void testGetLastRSSRSSServerIntNum() {
		//fail("Not yet implemented");
	//	try {
			//List<RSSEntry> data = net.getLastRSS(rssUrl, 5);
			Answer data = net.getLastRSS(rssUrl, 5);
			
			assertEquals(5, data.getEntryes().get(rssUrl).size());
			assertEquals("#3007: Миллиард на одно лицо", 
						data.getEntryes().get(rssUrl).get(0).getEntry().getTitle());
			assertEquals("#3003: Дератизация", 
					//data.get(4).getEntry().getTitle());
					data.getEntryes().get(rssUrl).get(4).getEntry().getTitle());
			assertEquals(0, data.getExceptions().size());
			
/*		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail("Exception" + e.getMessage());
		} catch (FeedException e) {
			e.printStackTrace();
			fail("Exception" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception" + e.getMessage());
		}*/
	}
	@Test
	public void testGetLastRSSRSSServerIntAll() {
		//fail("Not yet implemented");
	//	try {
			//List<RSSEntry> data 
			Answer data = net.getLastRSS(rssUrl, -1);
			
			assertEquals(50, data.getEntryes().get(rssUrl).size());
			
			assertEquals("#3007: Миллиард на одно лицо", 
					data.getEntryes().get(rssUrl).get(0).getEntry().getTitle());
			assertEquals("#3003: Дератизация", 
				data.getEntryes().get(rssUrl).get(4).getEntry().getTitle());
			assertEquals("#2958: А Ethernet — на сладкое", 
					data.getEntryes().get(rssUrl).get(49).getEntry().getTitle());
			
			assertEquals(0, data.getExceptions().size());
/*		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail("Exception" + e.getMessage());
		} catch (FeedException e) {
			e.printStackTrace();
			fail("Exception" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception" + e.getMessage());
		}*/
	}

	@Test
	public void testGetLastRSSListOfRSSServerListOfInteger() {
		//fail("Not yet implemented");
//		try {
			List<RSSServer>  serv = new ArrayList<RSSServer>();
			serv.add(rssUrl);
			serv.add(rssUrl2);
			
			List<Integer> num = new ArrayList<Integer>();
			num.add(-1);
			num.add(15);
			
		//	Map<RSSServer, List<RSSEntry>> data = net.getLastRSS(serv, num);
			Answer data = net.getLastRSS(serv, num);
			
			assertEquals(2, data.getEntryes().size());
			
			assertEquals(50, data.getEntryes().get(rssUrl).size());
			assertEquals(15, data.getEntryes().get(rssUrl2).size());
			
			assertEquals("#3007: Миллиард на одно лицо", 
					data.getEntryes().get(rssUrl).get(0).getEntry().getTitle());
			assertEquals("#3003: Дератизация", 
					data.getEntryes().get(rssUrl).get(4).getEntry().getTitle());
			assertEquals("#2958: А Ethernet — на сладкое", 
					data.getEntryes().get(rssUrl).get(49).getEntry().getTitle());
			
			assertEquals("#3068: Испорченные торрентами", 
					data.getEntryes().get(rssUrl2).get(0).getEntry().getTitle());
			assertEquals("#3064: Естественный отбор железным сбором", 
					data.getEntryes().get(rssUrl2).get(4).getEntry().getTitle());
			assertEquals("#3054: Помощь на опережение", 
					data.getEntryes().get(rssUrl2).get(14).getEntry().getTitle());
			
			assertEquals(0, data.getExceptions().size());
			
/*		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail("Exception" + e.getMessage());
		} catch (FeedException e) {
			e.printStackTrace();
			fail("Exception" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception" + e.getMessage());
		}*/
	}
	
	@Test
	public void testGetLastRSSRSSServerIntError() {
		//fail("Not yet implemented");
//		try {
			//List<RSSEntry> data = net.getLastRSS(rssUrl3, -1);
			Answer data = net.getLastRSS(rssUrl3, -1);
			
			if(data.getExceptions().isEmpty())
				fail("No Exception!!!");
			else {
				assertTrue(data.getExceptions().get(rssUrl3) instanceof IOException);
			}
			
/*		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail("Exception" + e.getMessage());
		} catch (FeedException e) {
			e.printStackTrace();
			fail("Exception" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			//fail("Exception" + e.getMessage());
			//All Ok
			//System.out.println(e.getClass());
		}*/
	}


}
