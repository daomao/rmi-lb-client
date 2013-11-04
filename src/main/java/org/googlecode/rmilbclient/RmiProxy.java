package org.googlecode.rmilbclient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.googlecode.rmilbclient.exceptions.InvokeRawExecuteException;
import org.googlecode.rmilbclient.exceptions.RmiExceptionUtils;
import org.googlecode.rmilbclient.exceptions.RmiInvokeAsynExecuteException;
import org.googlecode.rmilbclient.exceptions.RmiInvokeTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author zhongfeng
 * 
 */
public class RmiProxy implements InvocationHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(RmiProxy.class);

	private StubManager stubMgr;

	private long timeout = -1;

	private final static ExecutorService EXEC_ASYN_SERVICE = Executors
			.newCachedThreadPool();

	/**
	 * @param stubMgr
	 */
	public RmiProxy(StubManager stubMgr) {
		this(stubMgr, -1);
	}

	/**
	 * @param stubMgr
	 * @param timeout
	 */
	public RmiProxy(StubManager stubMgr, long timeout) {
		this.stubMgr = stubMgr;
		this.timeout = timeout;
	}

	public Object invoke(Object proxyObj, Method method, Object[] args)
			throws Throwable {
		try {
			if (getTimeout() > 0) {
				return invokeAsyn(method, args);
			}
			return doInvoke(method, args);
		} catch (Throwable ex) {
			if( ex instanceof RmiInvokeTimeoutException)
				throw ex;
			return handleConnectinFailure(method, args, ex);
		}
	}

	/**
	 * @param method
	 * @param args
	 * @param ex
	 * @return
	 */
	private Object handleConnectinFailure(Method method, Object[] args,
			Throwable ex) {
		if (RmiExceptionUtils.isConnectFailure(ex)) {
			try {
				return handleRemoteConnectFailure(method, args, ex);
			} catch (Throwable e) {
				throw new InvokeRawExecuteException(ex);
			}
		} else {
			throw new InvokeRawExecuteException(ex);
		}
	}

	private Object handleRemoteConnectFailure(Method m, Object[] args,
			Throwable ex) throws Throwable {
		if (logger.isDebugEnabled())
			logger.debug("Could not connect to RMI service - retrying");
		getStubMgr().updateStubsStatus();
		return doInvoke(m, args);
	}

	private Object doInvoke(Method method, Object[] args) throws Throwable {
		Object ret = null;
		try {
			Object stub = getStubMgr().getStub();
			if (stub != null) {
				ret = method.invoke(stub, args);
			}
		} catch (Throwable ex) {
			throw ex;
		}
		return ret;
	}

	private Object invokeAsyn(final Method method, final Object[] args) {
		Future<Object> futureTask = EXEC_ASYN_SERVICE
				.submit(createTask(method, args));
		try {
			return futureTask.get(getTimeout(), TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			throw new RmiInvokeAsynExecuteException(e);
		} catch (ExecutionException e) {
			throw new RmiInvokeAsynExecuteException(e);
		} catch (TimeoutException e) {
			throw new RmiInvokeTimeoutException(
					"Invoke timeout error,timeout is: " + getTimeout());
		} finally {
			futureTask.cancel(true);
		}
	}

	/**
	 * @param method
	 * @param args
	 * @return
	 */
	private Callable<Object> createTask(final Method method, final Object[] args) {
		return new Callable<Object>() {
			public Object call() throws Exception {
				try {
					return doInvoke(method, args);
				} catch (Throwable ex) {
					return handleConnectinFailure(method, args, ex);
				}
			}
		};
	}

	public StubManager getStubMgr() {
		return stubMgr;
	}

	public void setStubMgr(StubManager stubMgr) {
		this.stubMgr = stubMgr;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

}
