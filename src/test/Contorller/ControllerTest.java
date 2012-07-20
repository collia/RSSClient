package test.Contorller;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import RSS.RSSThread;
import RSS.data.Answer;
import RSS.data.Category;
import RSS.data.RSSServer;

import controller.Controller;

public class ControllerTest {

	Controller contr;
	
	RSSServer rssUrl1;
	RSSServer rssUrl2;
	RSSServer rssUrl3;
	RSSServer rssUrl4;
	
	RSSServer rssUrlBad;

	RSSServer rssFile;
	
	
	@Before
	public void setUp() throws Exception {
		contr = new Controller(); 	
		
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
	
		rssFile = new RSSServer("bash_org");
		rssFile.setLocalCategory(new Category(null).setName("news"));
		
	}

	@Test
	public void testUpdate() {
		//fail("Not yet implemented");
		
		//contr.update(servers, num)
		List<RSSServer> serv = new ArrayList<RSSServer>();
		serv.add(rssUrl1);
		serv.add(rssUrl2);
		//serv.add(rssUrl3);
		//serv.add(rssUrl4);
		
		Answer data = contr.update(serv, 10); 
			
		assertEquals(2, data.getEntryes().size());
		
		assertEquals(10, data.getEntryes().get(rssUrl1).size());
		assertEquals(10, data.getEntryes().get(rssUrl2).size());
		
		assertEquals("#3007: Миллиард на одно лицо", 
				data.getEntryes().get(rssUrl1).get(0).getEntry().getTitle());
		assertEquals("#3003: Дератизация", 
			data.getEntryes().get(rssUrl1).get(4).getEntry().getTitle());
	
		assertEquals("#3068: Испорченные торрентами", 
				data.getEntryes().get(rssUrl2).get(0).getEntry().getTitle());
		assertEquals("#3064: Естественный отбор железным сбором", 
			data.getEntryes().get(rssUrl2).get(4).getEntry().getTitle());
	
	}

	@Test
	public void testGetLast() {
		//fail("Not yet implemented");
		
		//contr.update(servers, num)
		List<RSSServer> serv = new ArrayList<RSSServer>();
		serv.add(rssFile);
	//	serv.add(rssUrl2);
		//serv.add(rssUrl3);
		//serv.add(rssUrl4);
		
		Answer data = contr.getLast(serv, 10); 
			
		assertEquals(1, data.getEntryes().size());
		
		assertEquals(10, data.getEntryes().get(rssFile).size());
		
		assertEquals("#3068: Испорченные торрентами", 
				data.getEntryes().get(rssFile).get(0).getEntry().getTitle());
		assertEquals("#3064: Естественный отбор железным сбором", 
			data.getEntryes().get(rssFile).get(4).getEntry().getTitle());
	
		
	}

	@Test
	public void testGetLast1() {
		//fail("Not yet implemented");
		
		//contr.update(servers, num)
		List<RSSServer> serv = new ArrayList<RSSServer>();
		serv.add(rssFile);
	//	serv.add(rssUrl2);
		//serv.add(rssUrl3);
		//serv.add(rssUrl4);
		
		Answer data = contr.getLast(serv, -1); 
			
		assertEquals(1, data.getEntryes().size());
		
		assertEquals(100, data.getEntryes().get(rssFile).size());
		
		assertEquals("#3068: Испорченные торрентами", 
				data.getEntryes().get(rssFile).get(0).getEntry().getTitle());
		assertEquals("#3064: Естественный отбор железным сбором", 
			data.getEntryes().get(rssFile).get(4).getEntry().getTitle());
	
		
	}
	
	@Test
	public void testGetNotReadEntryes() {
		//fail("Not yet implemented");
		
		List<RSSServer> serv = new ArrayList<RSSServer>();
		serv.add(rssFile);
		//serv.add(rssUrl3);
		//serv.add(rssUrl4);
		
		Answer data = contr.getNotReadEntryes(serv);
		
		assertTrue(data.getExceptions().isEmpty());
		assertEquals(5, data.getEntryes().get(rssFile).size());
		
		
		assertEquals("#3006: Короче, патч-корд", 
				data.getEntryes().get(rssFile).get(0).getEntry().getTitle());
	
		assertEquals("#3005: Истина где-то здесь", 
				data.getEntryes().get(rssFile).get(1).getEntry().getTitle());
	
		assertEquals("#3004: VintageDB", 
				data.getEntryes().get(rssFile).get(2).getEntry().getTitle());
				
		
		assertEquals("#2999: Альтернатива", 
				data.getEntryes().get(rssFile).get(3).getEntry().getTitle());
	
		
		
		assertEquals("#2975: Режим авиасимулятора", 
				data.getEntryes().get(rssFile).get(4).getEntry().getTitle());
		
		
		
		assertEquals(5, data.getEntryes().get(rssFile).size());
		
	}

}
