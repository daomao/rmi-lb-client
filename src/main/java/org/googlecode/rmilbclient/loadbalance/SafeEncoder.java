package org.googlecode.rmilbclient.loadbalance;

import java.io.UnsupportedEncodingException;

/**
 * @author zhongfeng
 *
 */
public class SafeEncoder {
	public final static String DEFAULT_CHARSET = "UTF-8";

	public static byte[] encode(String str) {
		try {
			if (str == null) {
				str = "DEFAULT";
			}
			return str.getBytes(DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {

		}
		return encode(null);
	}
}
