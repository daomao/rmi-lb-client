package org.googlecode.rmilbclient.loadbalance;

import java.util.Collection;

/**
 * @author zhongfeng
 *
 */
public class GCDAlg {
	public static int gcd(int n, int m) {
		int x;
		while (m % n != 0) {
			x = m % n;
			m = n;
			n = x;
		}
		return n;
	}

	public static int gcdN(Integer[] digits, int length) {
		if (1 == length)
			return digits[0];
		else
			return gcd(digits[length - 1], gcdN(digits, length - 1));
	}

	public static int gcdN(Collection<Integer> digits, int length) {
		Integer[] di = new Integer[digits.size()];
		digits.toArray(di);
		return gcdN(di, length);
	}
}
