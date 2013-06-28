package org.googlecode.rmilbclient.exceptions;

/**
 * @author zhongfeng
 *
 */
public class RmiInvokeTimeoutException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public RmiInvokeTimeoutException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RmiInvokeTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public RmiInvokeTimeoutException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public RmiInvokeTimeoutException(Throwable cause) {
		super(cause);
	}

}
