package test.RSS.filer;


import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.JDOMException;
import org.junit.Before;
import org.junit.Test;

import com.sun.syndication.feed.synd.SyndEntryImpl;

import RSS.data.RSSEntry;
import RSS.filer.TreeFile;

public class TestTreeFile {

	TreeFile treeFile;
	@Before
	public void setUp() throws Exception {
		treeFile = new TreeFile("news");
	}

	@Test
	public void testGetAllFileNames(){
//		try {
			List<String> result = treeFile.getAllFilenames();
			assertEquals("file2.xml", result.get(0));
			assertEquals("file1.xml", result.get(1));
	//		assertEquals("file2.crp", result.get(2));
			
/*		} catch (JDOMException e) {
			e.printStackTrace();
			fail("Exception "+e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception: "+e.getMessage());
		}
*/		
	}
	@Test
	public void testGetAllFileNamesCRP(){
//		try {
			List<String> result = treeFile.getAllFilenames(true);
			assertEquals("file3.crp", result.get(0));
			assertEquals("file2.xml", result.get(1));
			assertEquals("file1.xml", result.get(2));
			
/*		} catch (JDOMException e) {
			e.printStackTrace();
			fail("Exception "+e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception: "+e.getMessage());
		}
*/		
	}
	@Test
	public void testGetFileNames(){
//		try {
			List<String> result = treeFile.getFilenames(20,true);
			assertEquals("file3.crp", result.get(0));
			assertEquals("file2.xml", result.get(1));
			assertEquals("file1.xml", result.get(2));
		
			assertEquals(3, result.size());
			
			result = treeFile.getFilenames(1,true);
			
			assertEquals("file3.crp", result.get(0));
			//assertEquals("file2.xml", result.get(1));
		
			assertEquals(1, result.size());
			
			result = treeFile.getFilenames(11,true);
			assertEquals("file3.crp", result.get(0));
			assertEquals("file2.xml", result.get(1));
		
			assertEquals(2, result.size());
			
			result = treeFile.getFilenames(25,false);
			assertEquals("file2.xml", result.get(0));
			assertEquals("file1.xml", result.get(1));
		
			assertEquals(2, result.size());
			
			result = treeFile.getFilenames(11,true);
			assertEquals("file3.crp", result.get(0));
			assertEquals("file2.xml", result.get(1));
			//assertEquals("file2.crp", result.get(2));
		
			assertEquals(2, result.size());
			
			result = treeFile.getFilenames(26,true);
			assertEquals("file3.crp", result.get(0));
			assertEquals("file2.xml", result.get(1));
			assertEquals("file1.xml", result.get(2));
		
			assertEquals(3, result.size());
			
			result = treeFile.getFilenames(62,true);
			assertEquals("file3.crp", result.get(0));
			assertEquals("file2.xml", result.get(1));
			assertEquals("file1.xml", result.get(2));
		
			assertEquals(3, result.size());
		
			
/*		} catch (JDOMException e) {
			e.printStackTrace();
			fail("Exception "+e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception: "+e.getMessage());
		}
		*/
	}
	
	@Test
	public void testGetNumberNotReaded(){
		List<String> result = treeFile.getAllFilenames(true);
		assertEquals("file3.crp", result.get(0));
		assertEquals("file2.xml", result.get(1));
		assertEquals("file1.xml", result.get(2));

		Map<String,Set<Integer>> data = treeFile.getNumberNotReaded(result);
		
		assertTrue(!data.get(result.get(0)).isEmpty());
		//1,2,3,8,32
		assertTrue(data.get(result.get(0)).contains(1));
		assertTrue(data.get(result.get(0)).contains(2));
		assertTrue(data.get(result.get(0)).contains(32));
		
		assertTrue(!data.get(result.get(1)).isEmpty());
		//1,3,2,34
		assertTrue(data.get(result.get(1)).contains(1));
		assertTrue(data.get(result.get(1)).contains(3));
		assertTrue(data.get(result.get(1)).contains(34));
		
		assertTrue(data.get(result.get(2)).isEmpty());
	}
	
	@Test
	public void testWriteFeeds(){
		List<String> result = treeFile.getAllFilenames(true);
		assertEquals("file3.crp", result.get(0));
		assertEquals("file2.xml", result.get(1));
		assertEquals("file1.xml", result.get(2));

/*		Map<String,Set<Integer>> data = treeFile.getNumberNotReaded(result);
		
		assertTrue(!data.get(result.get(0)).isEmpty());
		//1,2,3,8,32
		assertTrue(data.get(result.get(0)).contains(1));
		assertTrue(data.get(result.get(0)).contains(2));
		assertTrue(data.get(result.get(0)).contains(32));
		
		assertTrue(!data.get(result.get(1)).isEmpty());
		//1,3,2,34
		assertTrue(data.get(result.get(1)).contains(1));
		assertTrue(data.get(result.get(1)).contains(3));
		assertTrue(data.get(result.get(1)).contains(34));
		
		assertTrue(data.get(result.get(2)).isEmpty());
*/
		
		RSSEntry one = new RSSEntry(new SyndEntryImpl());
		one.getEntry().setTitle("Hello World");
		
		List<RSSEntry> l = new ArrayList<RSSEntry>();
		
				
		try {
			treeFile.writeFeeds(l,0, false);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception");
		}
		
		result = treeFile.getAllFilenames(true);
		assertEquals("file3.crp", result.get(1));
		assertEquals("file2.xml", result.get(2));
		assertEquals("file1.xml", result.get(3));

		assertEquals(4,result.size());
	}
	
	@Test
	public void testRemoveFeeds(){
		List<String> result = treeFile.getAllFilenames(true);
		assertEquals("file3.crp", result.get(1));
		assertEquals("file2.xml", result.get(2));
		assertEquals("file1.xml", result.get(3));

		assertEquals(4,result.size());
/*		Map<String,Set<Integer>> data = treeFile.getNumberNotReaded(result);
		
		assertTrue(!data.get(result.get(0)).isEmpty());
		//1,2,3,8,32
		assertTrue(data.get(result.get(0)).contains(1));
		assertTrue(data.get(result.get(0)).contains(2));
		assertTrue(data.get(result.get(0)).contains(32));
		
		assertTrue(!data.get(result.get(1)).isEmpty());
		//1,3,2,34
		assertTrue(data.get(result.get(1)).contains(1));
		assertTrue(data.get(result.get(1)).contains(3));
		assertTrue(data.get(result.get(1)).contains(34));
		
		assertTrue(data.get(result.get(2)).isEmpty());
*/
		
/*		RSSEntry one = new RSSEntry(new SyndEntryImpl());
		one.getEntry().setTitle("Hello World");
		
		List<RSSEntry> l = new ArrayList<RSSEntry>();
		
	*/	
/*		try {
			treeFile.writeFeeds(l, false);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception");
		}*/
		try {
			treeFile.removeFeeds(result.get(0), false);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception");
		}
		result = treeFile.getAllFilenames(true);
		assertEquals("file3.crp", result.get(0));
		assertEquals("file2.xml", result.get(1));
		assertEquals("file1.xml", result.get(2));

		assertEquals(3,result.size());
		
	}
}
