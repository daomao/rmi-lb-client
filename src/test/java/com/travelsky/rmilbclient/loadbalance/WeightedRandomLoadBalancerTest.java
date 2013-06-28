package com.travelsky.rmilbclient.loadbalance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.googlecode.rmilbclient.loadbalance.WeightedRandomLoadBalancer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WeightedRandomLoadBalancerTest {

	private WeightedRandomLoadBalancer<User> lb;

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
		lb = new WeightedRandomLoadBalancer<User>(elements, ratio);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSelect() {
		Map<User, Integer> statistic = new HashMap<User, Integer>();

		for (int i = 0; i < 10000; i++) {
			User u = lb.select();
			System.out.println(u);
			if (statistic.get(u) == null)
				statistic.put(u, 1);
			else
				statistic.put(u, statistic.get(u) + 1);
		}
		System.out.println(statistic);
	}

	public static class User {
		public String username;

		public int age;

		@Override
		public String toString() {
			return "User [age=" + age + ", username=" + username + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + age;
			result = prime * result
					+ ((username == null) ? 0 : username.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			User other = (User) obj;
			if (age != other.age)
				return false;
			if (username == null) {
				if (other.username != null)
					return false;
			} else if (!username.equals(other.username))
				return false;
			return true;
		}

	};

}
