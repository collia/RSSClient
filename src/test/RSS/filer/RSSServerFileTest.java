package test.RSS.filer;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import RSS.data.Category;
import RSS.data.RSSServer;
import RSS.filer.RSSServerFile;

public class RSSServerFileTest {

	RSSServerFile file;
	
	@Before
	public void setUp() throws Exception {
		file = new RSSServerFile();	
	}

	@Test
	public void testLoad() {
		//fail("Not yet implemented");
		try {
			Category cat = file.load();
			
			Set<Category> children = cat.getChildren();
			Iterator<Category> cit = children.iterator();
		/*	while(cit.hasNext())
			{
				//System.out.println(c.next());
				Category c = cit.next(); 
				if(c.hasChild())
				{
					
				}
			}*/
			assertTrue(cit.hasNext());
			Category c = cit.next();
			assertEquals("/news", c.toString());
			
			Iterator<Category> cit2 = c.getChildren().iterator();
			
			assertTrue(cit2.hasNext());
			Category c2 = cit2.next();
			assertEquals("/news/ukrainian", c2.toString());
			
			Iterator<RSSServer> sit = c2.getServers().iterator();
			
			assertTrue(sit.hasNext());
			RSSServer s = sit.next();
			assertEquals("IT", s.getName());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception");
		}
		
	}

	@Test
	public void testWrite() {
		//fail("Not yet implemented");
		try {
			Category cat = file.load();
			Iterator<Category> itc =  cat.getChildren().iterator();
			
			assertTrue(itc.hasNext());
			Category c = itc.next();
			
			Category nw = new Category(c).setName("English");
			c.addChild(nw);
			
			RSSServer s = new RSSServer("BBC");
			s.setLink("AAAABBB");
			nw.addServer(s);
			
			file.write(cat);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception");
		}
		
		
	}
	
	@Test
	public void testWriteRemove() {
		//fail("Not yet implemented");
		try {
			Category cat = new Category(null);//file.load();
			cat.setName("root");
		//	Iterator<Category> itc =  cat.getChildren().iterator();
			Category ch1 = new Category(cat);
			ch1.setName("Hello");
			cat.addChild(ch1);
			Category ch2 = new Category(cat);
			ch2.setName("World");
			cat.addChild(ch2);
		//	assertTrue(itc.hasNext());
		//	Category c = itc.next();
			
		//	Category nw = new Category(c).setName("English");
		//	c.addChild(nw);
			
			RSSServer s = new RSSServer("BBC");
			s.setLink("AAAABBB");
			s.setLocalCategory(ch1);
			ch1.addServer(s);
			
			s = new RSSServer("IT");
			s.setLink("dddddffff");
			s.setLocalCategory(ch2);
			ch2.addServer(s);
			
			s = new RSSServer("IT2");
			s.setLink("cvcvc");
			s.setLocalCategory(ch2);
			ch2.addServer(s);
			
			file.write(cat);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception");
		}
	}
}
