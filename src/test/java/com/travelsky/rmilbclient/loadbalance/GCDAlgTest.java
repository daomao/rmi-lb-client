package com.travelsky.rmilbclient.loadbalance;

import static org.junit.Assert.*;

import org.googlecode.rmilbclient.loadbalance.GCDAlg;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GCDAlgTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGcd() {
		fail("Not yet implemented");
	}

	@Test
	public void testGcdN() {
		Integer[] as = new Integer[]{200,100,50,25};
		System.out.println(GCDAlg.gcdN(as, as.length));
	}

}
