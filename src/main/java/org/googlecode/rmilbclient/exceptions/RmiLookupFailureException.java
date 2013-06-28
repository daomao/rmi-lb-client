package org.googlecode.rmilbclient.exceptions;

/**
 * @author zhongfeng
 *
 */
@SuppressWarnings("serial")
public class RmiLookupFailureException extends RuntimeException {

	/**
	 * Constructor for RemoteLookupFailureException.
	 * @param msg the detail message
	 */
	public RmiLookupFailureException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for RemoteLookupFailureException.
	 * @param msg message
	 * @param cause the root cause from the remoting API in use
	 */
	public RmiLookupFailureException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}