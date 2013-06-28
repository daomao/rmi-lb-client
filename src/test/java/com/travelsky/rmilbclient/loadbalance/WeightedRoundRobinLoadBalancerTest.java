package com.travelsky.rmilbclient.loadbalance;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.googlecode.rmilbclient.loadbalance.WeightedRoundRobinLoadBalancer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WeightedRoundRobinLoadBalancerTest {

	private WeightedRoundRobinLoadBalancer<User> lb  ;
	@Before
	public void setUp() throws Exception {
		User u = new User();
		u.username = "1";
		u.age = 1;
		User u2 = new User();
		u2.username = "2";
		u2.age = 2;
		List<User> elements = new ArrayList<User>();
		elements.add(u);
		elements.add(u2);
		List<Integer> ratio = new ArrayList<Integer>();
		ratio.add(100);
		ratio.add(50);
		lb = new WeightedRoundRobinLoadBalancer<User>(elements,ratio);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSelect() {
		for(int i = 0;i<5;i++)
		System.out.println(lb.select());
	}
	
	public static class User
	{
		public String username;
		
		public int age;
		
		@Override
		public String toString() {
			return "User [age=" + age + ", username=" + username + "]";
		}
		
	};

}
