package org.googlecode.rmilbclient.exceptions;

/**
 * @author zhongfeng
 *
 */
@SuppressWarnings("serial")
public class InvokeRawExecuteException extends RuntimeException {

	/**
	 * Constructor for RemoteLookupFailureException.
	 * @param msg the detail message
	 */
	public InvokeRawExecuteException(String msg) {
		super(msg);
	}

	/**
	 * 
	 */
	public InvokeRawExecuteException() {
		super();

	}

	/**
	 * @param cause
	 */
	public InvokeRawExecuteException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor for RemoteLookupFailureException.
	 * @param msg message
	 * @param cause the root cause from the remoting API in use
	 */
	public InvokeRawExecuteException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}