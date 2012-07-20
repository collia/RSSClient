package test.Contorller;

import static org.junit.Assert.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Set;

import javax.crypto.NoSuchPaddingException;

import org.junit.Before;
import org.junit.Test;

import controller.Ban;


public class BadWordsTest {

	Ban words;
	
	@Before
	public void setUp(){
		words = Ban.getInstanceBan();
	}
	@Test
	public void testGetInstanceBadWords() {
		//fail("Not yet implemented");
		Ban bw = Ban.getInstanceBan();
		Ban bw2 = Ban.getInstanceBan();
		assertNotNull(bw);
		assertSame(bw, bw2);
	}


	@Test
	public void testGetBadWords() {
		//fail("Not yet implemented");
		Set<String> s = words.getBadWords();
		
		assertTrue(s.contains("word1"));
		assertTrue(s.contains("word2"));
		assertTrue(s.contains("word3"));
	}

	
	@Test
	public void testAddWord() {
		//fail("Not yet implemented");
		try {
			words.addWord("word4");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception");
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			fail("Exception");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			fail("Exception");
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			fail("Exception");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			fail("Exception");
		}
		assertTrue(words.getBadWords().contains("word4"));
		assertTrue(words.getBadWords().contains("word1"));
		assertTrue(words.getBadWords().contains("word2"));
		assertTrue(words.getBadWords().contains("word3"));
	}
	
	@Test
	public void testGetBanLinks() {
		//fail("Not yet implemented");
		Set<String> s = words.getBanLinks();
		
		assertTrue(s.contains("link1"));
		assertTrue(s.contains("link2"));
		assertTrue(s.contains("link3"));
	}

	
	@Test
	public void testAddLink() {
		//fail("Not yet implemented");
		try {
			words.addLink("word4");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception");
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			fail("Exception");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			fail("Exception");
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			fail("Exception");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			fail("Exception");
		}
		assertTrue(words.getBanLinks().contains("word4"));
		assertTrue(words.getBanLinks().contains("link1"));
		assertTrue(words.getBanLinks().contains("link2"));
		assertTrue(words.getBanLinks().contains("link3"));
	}

}
