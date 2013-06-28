package org.googlecode.rmilbclient.normal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhongfeng
 * 
 */
@Deprecated
public class NDRmiProxyFactory {
	protected static Logger logger = LoggerFactory
			.getLogger(NDRmiProxyFactory.class);

	private final ClassLoader _loader;

	private NDCachedStubManager stubMgr;

	/**
	 * Creates the new proxy factory.
	 */
	public NDRmiProxyFactory() {
		this(Thread.currentThread().getContextClassLoader());
	}

	/**
	 * Creates the new proxy factory.
	 */
	public NDRmiProxyFactory(ClassLoader loader) {
		_loader = loader;
	}

	public <T> T create(Class<T> api, ClassLoader loader) {
		if (api == null)
			throw new NullPointerException(
					"api must not be null for JSONRPCProxyFactory.create()");
		InvocationHandler handler = new NDRmiProxy(this);

		Object proxyObj = Proxy.newProxyInstance(loader, new Class[] { api },
				handler);

		return api.cast(proxyObj);
	}

	public <T> T create(Class<T> api) {
		return create(api, _loader);
	}

	public NDCachedStubManager getStubMgr() {
		return stubMgr;
	}

	public void setStubMgr(NDCachedStubManager stubMgr) {
		this.stubMgr = stubMgr;
	}

}
