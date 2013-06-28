package org.googlecode.rmilbclient.exceptions;

/**
 * @author zhongfeng
 *
 */
@SuppressWarnings("serial")
public class RmiInvokeAsynExecuteException extends RuntimeException {

	/**
	 * Constructor for RemoteLookupFailureException.
	 * @param msg the detail message
	 */
	public RmiInvokeAsynExecuteException(String msg) {
		super(msg);
	}

	/**
	 * 
	 */
	public RmiInvokeAsynExecuteException() {
		super();

	}

	/**
	 * @param cause
	 */
	public RmiInvokeAsynExecuteException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor for RemoteLookupFailureException.
	 * @param msg message
	 * @param cause the root cause from the remoting API in use
	 */
	public RmiInvokeAsynExecuteException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}