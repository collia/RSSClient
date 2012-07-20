package test.view.html;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import view.html.SmallStack;

public class SmallStackTest {

	SmallStack<Integer> intCollect;
//	SmallCollection<Double> doubleCollect;
	@Before
	public void setUp() throws Exception {
		intCollect = new SmallStack<Integer>(10);
//		doubleCollect = new SmallCollection<Double>(10);
	}

	@Test
	public void testAdd() {
	//	Random r = new Random();
		for(int i=0; i < 10; i++)
		{
			intCollect.push(i);
			assertEquals(i+1, intCollect.size());
		}
		assertEquals(10, intCollect.size());
		
		intCollect.push(10);
		
		assertEquals(10, intCollect.size());
		
	/*	Iterator<Integer> it = intCollect.iterator();
		
		assertTrue(it.hasNext());
		assertEquals(10, it.next().intValue());
		assertTrue(it.hasNext());
		assertEquals(9, it.next().intValue());
		assertTrue(it.hasNext());
		assertEquals(8, it.next().intValue());
		assertTrue(it.hasNext());
		assertEquals(7, it.next().intValue());
		assertTrue(it.hasNext());
		assertEquals(6, it.next().intValue());
		assertTrue(it.hasNext());
		assertEquals(5, it.next().intValue());
		assertTrue(it.hasNext());
		assertEquals(4, it.next().intValue());
		assertTrue(it.hasNext());
		assertEquals(3, it.next().intValue());
		assertTrue(it.hasNext());
		assertEquals(2, it.next().intValue());
		assertTrue(it.hasNext());
		assertEquals(1, it.next().intValue());
		assertFalse(it.hasNext());
		
			*/
		assertEquals(10, intCollect.pop().intValue());
		assertEquals(9, intCollect.pop().intValue());
		assertEquals(8, intCollect.pop().intValue());
		assertEquals(7, intCollect.pop().intValue());
		assertEquals(6, intCollect.pop().intValue());
		assertEquals(5, intCollect.pop().intValue());
		assertEquals(4, intCollect.pop().intValue());
		assertEquals(3, intCollect.pop().intValue());
		assertEquals(2, intCollect.pop().intValue());
		assertEquals(1, intCollect.pop().intValue());
		
		assertEquals(0, intCollect.size());
		
	}


}
