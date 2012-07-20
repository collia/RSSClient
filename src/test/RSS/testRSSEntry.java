package test.RSS;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.sun.syndication.feed.rss.Description;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;

import RSS.data.RSSEntry;



public class testRSSEntry {

	RSSEntry entry;
	SyndEntry seTrue;
	SyndEntry seFalse;
	@Before
	public void setUp() throws Exception {
		
		
		
		
		//entry = 
	}
	

	@Test
	public void testIs18plusTrue() {
		
		seTrue = new SyndEntryImpl();
		seTrue.setTitle("One word1 about cool place");
		seTrue.setLink("Link2");
		SyndContent d = new SyndContentImpl();
		d.setValue("word2");
		seTrue.setDescription(d);
		
		entry = new RSSEntry(seTrue);
		assertTrue(entry.is18plus());
		
	
	}
	
	@Test
	public void testIs18plusFalse() {
		
		seFalse = new SyndEntryImpl();
		seFalse.setTitle("One word about cool place");
		seFalse.setLink("Link");
		SyndContent d = new SyndContentImpl();
		d.setValue("wordwordword");
		seFalse.setDescription(d);
		
		entry = new RSSEntry(seFalse);
		assertTrue(!entry.is18plus());
	}

}
