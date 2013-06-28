package org.googlecode.rmilbclient.exceptions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.rmi.ConnectException;
import java.rmi.ConnectIOException;
import java.rmi.NoSuchObjectException;
import java.rmi.StubNotFoundException;
import java.rmi.UnknownHostException;

import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.NO_RESPONSE;
import org.omg.CORBA.SystemException;
import org.springframework.remoting.RemoteLookupFailureException;

/**
 * this class copy from spring,modified by zhongfeng
 * @author zhongfeng
 *
 */
public class RmiExceptionUtils {

	private static final String ORACLE_CONNECTION_EXCEPTION = "com.evermind.server.rmi.RMIConnectionException";
	/**
	 * Determine whether the given RMI exception indicates a connect failure.
	 */
	public static boolean isConnectFailure(Throwable ex) {
		if (ex instanceof InvocationTargetException) {
			ex = ((InvocationTargetException) ex).getTargetException();
		}
		return (ex instanceof ConnectException
				|| ex instanceof ConnectIOException
				|| ex instanceof UnknownHostException
				|| ex instanceof NoSuchObjectException
				|| ex instanceof StubNotFoundException
				|| ex instanceof UndeclaredThrowableException
				|| ex instanceof RemoteLookupFailureException
				|| ex instanceof RmiLookupFailureException
				|| isCorbaConnectFailure(ex.getCause()) || ORACLE_CONNECTION_EXCEPTION
				.equals(ex.getClass().getName()));
	}

	/**
	 * Check whether the given RMI exception root cause indicates a CORBA
	 * connection failure.
	 * <p>
	 * This is relevant on the IBM JVM, in particular for WebSphere EJB clients.
	 * <p>
	 * See the <a href=
	 * "http://www.redbooks.ibm.com/Redbooks.nsf/RedbookAbstracts/tips0243.html"
	 * >IBM website</code> for details.
	 * 
	 * @param ex
	 *            the RMI exception to check
	 */
	private static boolean isCorbaConnectFailure(Throwable ex) {
		return ((ex instanceof COMM_FAILURE || ex instanceof NO_RESPONSE) && ((SystemException) ex).completed == CompletionStatus.COMPLETED_NO);
	}

}
