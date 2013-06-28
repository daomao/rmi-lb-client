package org.googlecode.rmilbclient.normal;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.rmi.ConnectException;
import java.rmi.ConnectIOException;
import java.rmi.NoSuchObjectException;
import java.rmi.StubNotFoundException;
import java.rmi.UnknownHostException;

import org.googlecode.rmilbclient.RmiProxy;
import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.NO_RESPONSE;
import org.omg.CORBA.SystemException;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.RemoteLookupFailureException;



/**
 * 
 * @author zhongfeng
 */
@Deprecated
public class NDRmiProxy implements InvocationHandler, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(RmiProxy.class.getName());

	private static final String ORACLE_CONNECTION_EXCEPTION = "com.evermind.server.rmi.RMIConnectionException";

	protected NDRmiProxyFactory proxyFactory;

	/**
	 * @param proxyFactory
	 */
	public NDRmiProxy(NDRmiProxyFactory proxyFactory) {
		this.proxyFactory = proxyFactory;
	}

	public Object invoke(Object proxyObj, Method method, Object[] args)
			throws Throwable {
		try {
			return doInvoke(method, args);
		} catch (Throwable ex) {
			if (isConnectFailure(ex)) {
				return handleRemoteConnectFailure(method, args, ex);
			} else {
				throw ex;
			}
		}
	}

	private Object handleRemoteConnectFailure(Method m, Object[] args,
			Throwable ex) throws Throwable {
		logger.error("Could not connect to RMI service - retrying", ex);
		getStubMgr().updateStubsStatus();
		return doInvoke(m, args);
	}

	private Object doInvoke(Method method, Object[] args) throws Throwable {
		Object ret = null;
		Object stub = getStubMgr().getStub();
		if (stub != null)
			try {
				ret = method.invoke(stub, args);
			} catch (Throwable e) {
				throw e;
			}

		return ret;
	}

	/**
	 * Determine whether the given RMI exception indicates a connect failure.
	 * <p>
	 * Treats RMI's ConnectException, ConnectIOException, UnknownHostException,
	 * NoSuchObjectException and StubNotFoundException as connect failure, as
	 * well as Oracle's OC4J
	 * <code>com.evermind.server.rmi.RMIConnectionException</code> (which
	 * doesn't derive from from any well-known RMI connect exception).
	 * 
	 * @param ex
	 *            the RMI exception to check
	 * @return whether the exception should be treated as connect failure
	 * @see java.rmi.ConnectException
	 * @see java.rmi.ConnectIOException
	 * @see java.rmi.UnknownHostException
	 * @see java.rmi.NoSuchObjectException
	 * @see java.rmi.StubNotFoundException
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

	public NDRmiProxyFactory getProxyFactory() {
		return proxyFactory;
	}

	public void setProxyFactory(NDRmiProxyFactory proxyFactory) {
		this.proxyFactory = proxyFactory;
	}

	public NDCachedStubManager getStubMgr() {
		return proxyFactory.getStubMgr();
	}

}
