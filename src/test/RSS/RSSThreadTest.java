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
import RSS.RSSThread;
import RSS.data.Answer;
import RSS.data.RSSEntry;
import RSS.data.RSSServer;

public class RSSThreadTest {
	RSSServer rssUrl1;
	RSSServer rssUrl2;
	RSSServer rssUrl3;
	RSSServer rssUrl4;
	
	RSSServer rssUrlBad;
	
	RSSThread rss;
	
	@Before
	public void setUp() throws Exception {
		rssUrl1 = new RSSServer("test1");
		rssUrl1.setUrl(new URL("http://localhost:8080/rss/testRSS.xml"));
		
		rssUrl2 = new RSSServer("test2");
		rssUrl2.setUrl(new URL("http://localhost:8080/rss/testRSS2.xml"));
		
		rssUrl3 = new RSSServer("test3");
		rssUrl3.setUrl(new URL("http://localhost:8080/rss/testRSS.xml"));
		
		rssUrl4 = new RSSServer("test4");
		rssUrl4.setUrl(new URL("http://localhost:8080/rss/testRSS2.xml"));
	
		rssUrlBad = new RSSServer("test4");
		rssUrlBad.setUrl(new URL("http://localhost:8090/rss/testRSS2.xml"));
	
		
		rss = new RSSThread();
	}

	
	@Test
	public void testGetLastRSSListOfRSSServerListOfInteger2() {
		//fail("Not yet implemented");
//		try {
			List<RSSServer>  serv = new ArrayList<RSSServer>();
			serv.add(rssUrl1);
			serv.add(rssUrl2);
			
			List<Integer> num = new ArrayList<Integer>();
			num.add(-1);
			num.add(15);
			
		//	Map<RSSServer, List<RSSEntry>> data = 
			Answer data = rss.getLastRSS(serv, num);
			 			
			assertEquals(2, data.getEntryes().size());
			
			assertEquals(50, data.getEntryes().get(rssUrl1).size());
			assertEquals(15, data.getEntryes().get(rssUrl2).size());
			
			assertEquals("#3007: Миллиард на одно лицо", 
					data.getEntryes().get(rssUrl1).get(0).getEntry().getTitle());
			assertEquals("#3003: Дератизация", 
				data.getEntryes().get(rssUrl1).get(4).getEntry().getTitle());
			assertEquals("#2958: А Ethernet — на сладкое", 
					data.getEntryes().get(rssUrl1).get(49).getEntry().getTitle());
			
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
	public void testGetLastRSSListOfRSSServerListOfInteger4() {
		//fail("Not yet implemented");
//		try {
			List<RSSServer>  serv = new ArrayList<RSSServer>();
			serv.add(rssUrl1);
			serv.add(rssUrl2);
			serv.add(rssUrl3);
			serv.add(rssUrl4);
			
			List<Integer> num = new ArrayList<Integer>();
			num.add(-1);
			num.add(15);
			num.add(25);
			num.add(45);
			
			//Map<RSSServer, List<RSSEntry>> 
			Answer data = rss.getLastRSS(serv, num);
			
			assertEquals(4, data.getEntryes().size());
			
			assertEquals(50, data.getEntryes().get(rssUrl1).size());
			assertEquals(15, data.getEntryes().get(rssUrl2).size());
			
			assertEquals("#3007: Миллиард на одно лицо", 
					data.getEntryes().get(rssUrl1).get(0).getEntry().getTitle());
			assertEquals("#3003: Дератизация", 
					data.getEntryes().get(rssUrl1).get(4).getEntry().getTitle());
			assertEquals("#2958: А Ethernet — на сладкое", 
					data.getEntryes().get(rssUrl1).get(49).getEntry().getTitle());
			
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
			//List<RSSEntry> data = rss.getLastRSS(rssUrl3, -1);
			List<RSSServer>  serv = new ArrayList<RSSServer>();
			serv.add(rssUrl1);
			serv.add(rssUrl2);
			serv.add(rssUrl3);
			serv.add(rssUrl4);
			serv.add(rssUrlBad);
			
			List<Integer> num = new ArrayList<Integer>();
			num.add(-1);
			num.add(15);
			num.add(25);
			num.add(45);
			
			//Map<RSSServer, List<RSSEntry>> data 
			Answer data = rss.getLastRSS(serv, num);
			 
			
			assertEquals(50, data.getEntryes().get(rssUrl1).size());
			assertEquals(15, data.getEntryes().get(rssUrl2).size());
			
			assertEquals("#3007: Миллиард на одно лицо", 
					data.getEntryes().get(rssUrl1).get(0).getEntry().getTitle());
			assertEquals("#3003: Дератизация", 
					data.getEntryes().get(rssUrl1).get(4).getEntry().getTitle());
			assertEquals("#2958: А Ethernet — на сладкое", 
					data.getEntryes().get(rssUrl1).get(49).getEntry().getTitle());
			
			assertEquals("#3068: Испорченные торрентами", 
					data.getEntryes().get(rssUrl2).get(0).getEntry().getTitle());
			assertEquals("#3064: Естественный отбор железным сбором", 
					data.getEntryes().get(rssUrl2).get(4).getEntry().getTitle());
			assertEquals("#3054: Помощь на опережение", 
					data.getEntryes().get(rssUrl2).get(14).getEntry().getTitle());
			
			assertEquals(1, data.getExceptions().size());
			
			
			if(data.getExceptions().isEmpty())
				fail("No Exception!!!");
			else {
				assertTrue(data.getExceptions().get(rssUrlBad) instanceof IOException);
			}
			
	/*	} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail("Exception" + e.getMessage());
		} catch (FeedException e) {
			e.printStackTrace();
			fail("Exception" + e.getMessage());
		}	catch(java.net.ConnectException e){
			//All Ok	
			//	e.printStackTrace();	
		} catch (IOException e) {
			e.printStackTrace();
			//fail("Exception" + e.getMessage());
			//All Ok
			//System.out.println(e.getClass());
		} */
	}


}
