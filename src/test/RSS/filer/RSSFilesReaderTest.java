package test.RSS.filer;

import static org.junit.Assert.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

import org.jdom.JDOMException;
import org.junit.Before;
import org.junit.Test;

import com.sun.syndication.io.FeedException;

import RSS.data.RSSEntry;
import RSS.filer.RSSFilesReader;

public class RSSFilesReaderTest {
	RSSFilesReader reader; 
	@Before
	public void setUp() throws Exception {
		reader = new RSSFilesReader("news/bash_org");
	}

	@Test
	public void testGetLastEntries() {
		//fail("Not yet implemented");
		try {
			List<RSSEntry> list = reader.getLastEntries(10, false, false);
			assertEquals(10, list.size());
			//System.out.println(list);
			
			RSSEntry entry = list.get(0);
			assertEquals("#3068: Испорченные торрентами",entry.getEntry().getTitle());
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testGetLastEntriesIsReaded() {
		//fail("Not yet implemented");
		try {
			List<RSSEntry> list = reader.getLastEntries(60, false, false);
			assertEquals(60, list.size());
			//System.out.println(list);
			
			RSSEntry entry = list.get(0);
			assertEquals("#3068: Испорченные торрентами",entry.getEntry().getTitle());
			assertTrue(!entry.isReaded());
			
			entry = list.get(51);
			assertEquals("#3006: Короче, патч-корд",entry.getEntry().getTitle());
			assertTrue(entry.isReaded());
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Test
	public void testGetLastEntriesIsReadedOne() {
		//fail("Not yet implemented");
		try {
			List<RSSEntry> list = reader.getLastEntries(-1, false, false);
			assertEquals(100, list.size());
			//System.out.println(list);
			
			RSSEntry entry = list.get(0);
			assertEquals("#3068: Испорченные торрентами",entry.getEntry().getTitle());
			assertTrue(!entry.isReaded());
			
			entry = list.get(51);
			assertEquals("#3006: Короче, патч-корд",entry.getEntry().getTitle());
			assertTrue(entry.isReaded());
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
