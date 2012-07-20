package test.RSS.OPML;


import java.io.File;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;

import RSS.OPML.*;

public class testOpml2 extends TestCase{

	Opml opml;
	@Before
	public void setUp() throws Exception {
		opml = new Opml(new File("doc/news.xml")); 
		//subscriptionList.opml
		//opml = new Opml(new File("doc/subscriptionList.opml"));
	}
	public void testHead() throws Exception{
		assertEquals("playlist.xml", opml.getTitle());
		assertEquals("Thu, 27 Jul 2000 03:24:18 GMT", opml.getCreated());
		assertEquals("Fri, 15 Sep 2000 09:01:23 GMT", opml.getModified());
		//System.out.println(new Date("Fri, 15 Sep 2000 09:01:23 GMT"));
		assertEquals("Dave Winer",opml.getOwnerName());
		assertEquals("dave@userland.com",opml.getOwnerEmail());
		assertEquals("1,3,17",opml.getExpansionState());
	}
	public void testBody() {
		try {
			List l = opml.getOutlines();
			
			assertEquals("Background",((Outline)l.get(0)).getText());
			assertEquals("The Last Napster Sunday?",((Outline)l.get(1)).getText());
			assertEquals("The Thrill is Gone?",((Outline)l.get(2)).getText());
			
			List l2 = ((Outline)l.get(0)).getOutlines();
			
			assertEquals("I've started to note the songs I was listening to as I was writing DaveNet pieces. ", ((Outline)l2.get(0)).getText());
			
			l2 = ((Outline)l.get(1)).getOutlines();
		
			assertEquals("Heart of Glass.mp3", ((Outline)l2.get(0)).getText());
			assertEquals("song", ((Outline)l2.get(0)).getType());
			assertEquals("Blondie - Heart of Glass.mp3", ((Outline)l2.get(0)).getAttributeValue("f"));
			
			
			assertEquals("Manic Monday.mp3", ((Outline)l2.get(1)).getText());
			assertEquals("song", ((Outline)l2.get(1)).getType());
			assertEquals("Bangles - Manic Monday.mp3", ((Outline)l2.get(1)).getAttributeValue("f"));
			
			assertEquals("Everybody Have Fun Tonight.mp3", ((Outline)l2.get(2)).getText());
			assertEquals("song", ((Outline)l2.get(2)).getType());
			assertEquals("Wang Chung - Everybody Have Fun Tonight.mp3", ((Outline)l2.get(2)).getAttributeValue("f"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void testNull(){
		Opml op = opml;
		try {
			op = new Opml(new File("dummy"));
			fail("All is bad!");
		} catch (Exception e) {
			// All OK
			//e.printStackTrace();
		} 
	
	}
}
