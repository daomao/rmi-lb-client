package org.googlecode.rmilbclient.normal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.util.ArrayList;
import java.util.List;

import org.googlecode.rmilbclient.StubComponent;
import org.googlecode.rmilbclient.StubStatus;
import org.googlecode.rmilbclient.schedule.LbSchedule;
import org.googlecode.rmilbclient.schedule.RandomLbSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.RemoteLookupFailureException;



@Deprecated
public class NDCachedStubManager {
	private static Logger logger = LoggerFactory
			.getLogger(NDCachedStubManager.class.getName());

	private List<StubComponent> stubArr;

	private boolean lookupStubOnStartup = true;

	private RMIClientSocketFactory registryClientSocketFactory;

	private LbSchedule<StubComponent> schedule;

	private List<String> serviceUrls;

	/**
	 * 
	 */
	public NDCachedStubManager() {

	}

	/**
	 * @param serviceUrls
	 */
	public NDCachedStubManager(List<String> serviceUrls) {
		this(false, null, serviceUrls);
	}

	/**
	 * @param lookupStubOnStartup
	 * @param serviceUrls
	 */
	public NDCachedStubManager(boolean lookupStubOnStartup,
			List<String> serviceUrls) {
		this(lookupStubOnStartup, null, serviceUrls);
	}

	/**
	 * @param lookupStubOnStartup
	 * @param registryClientSocketFactory
	 * @param serviceUrls
	 */
	public NDCachedStubManager(boolean lookupStubOnStartup,
			RMIClientSocketFactory registryClientSocketFactory,
			List<String> serviceUrls) {
		this.lookupStubOnStartup = lookupStubOnStartup;
		this.registryClientSocketFactory = registryClientSocketFactory;
		this.serviceUrls = serviceUrls;
		init();
	}

	public void init() throws RemoteLookupFailureException {
		stubArr = new ArrayList<StubComponent>();
		for (String serviceUrl : serviceUrls) {
			StubComponent stubComp = new StubComponent();
			stubComp.setServiceUrl(serviceUrl);
			stubArr.add(stubComp);
		}
		prepare();
	}

	/**
	 * Fetches RMI stub on startup, if necessary.
	 * 
	 * @throws RemoteLookupFailureException
	 * 
	 * @throws RemoteLookupFailureException
	 *             if RMI stub creation failed
	 * @see #setLookupStubOnStartup
	 * @see #lookupStub
	 */
	private void prepare() throws RemoteLookupFailureException {
		// Cache RMI stub on initialization?
		if (this.lookupStubOnStartup) {
			updateStubsStatus();
		}
	}

	/**
	 * Create the RMI stub, typically by looking it up.
	 * <p>
	 * Called on interceptor initialization if "cacheStub" is "true"; else
	 * called for each invocation by {@link #getStub()}.
	 * <p>
	 * The default implementation looks up the service URL via
	 * <code>java.rmi.Naming</code>. This can be overridden in subclasses.
	 * 
	 * @return the RMI stub to store in this interceptor
	 * @throws RemoteLookupFailureException
	 *             if RMI stub creation failed
	 * @see #setCacheStub
	 * @see java.rmi.Naming#lookup
	 */
	private Remote lookupStub(String serviceUrl)
			throws RemoteLookupFailureException {
		try {
			Remote stub = null;
			if (this.registryClientSocketFactory != null) {
				// RMIClientSocketFactory specified for registry access.
				// Unfortunately, due to RMI API limitations, this means
				// that we need to parse the RMI URL ourselves and perform
				// straight LocateRegistry.getRegistry/Registry.lookup calls.
				URL url = new URL(null, serviceUrl, new DummyURLStreamHandler());
				String protocol = url.getProtocol();
				if (protocol != null && !"rmi".equals(protocol)) {
					throw new MalformedURLException("Invalid URL scheme '"
							+ protocol + "'");
				}
				String host = url.getHost();
				int port = url.getPort();
				String name = url.getPath();
				if (name != null && name.startsWith("/")) {
					name = name.substring(1);
				}
				Registry registry = LocateRegistry.getRegistry(host, port,
						this.registryClientSocketFactory);
				stub = registry.lookup(name);
			} else {
				// Can proceed with standard RMI lookup API...
				stub = Naming.lookup(serviceUrl);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Located RMI stub with URL [" + serviceUrl + "]");
			}
			return stub;
		} catch (MalformedURLException ex) {
			throw new RemoteLookupFailureException("Service URL [" + serviceUrl
					+ "] is invalid", ex);
		} catch (NotBoundException ex) {
			throw new RemoteLookupFailureException(
					"Could not find RMI service [" + serviceUrl
							+ "] in RMI registry", ex);
		} catch (RemoteException ex) {
			throw new RemoteLookupFailureException("Lookup of RMI stub failed",
					ex);
		}
	}

	public Object getStub(){
		Object stub = null;
		if (schedule != null)
			stub = schedule.nextHost().getStub();
		else {
			updateStubsStatus();
			if (schedule != null)
				stub = schedule.nextHost().getStub();
		}
		return stub;
	}

	public synchronized boolean updateStubsStatus() throws RemoteLookupFailureException,
			IllegalStateException {
		boolean flag = false;
		int failCount = 0;
		if (stubArr == null || stubArr.size() == 0)
			throw new IllegalStateException(
					"StubArr need to init,try to excute init method first");
		for (StubComponent stubComp : stubArr) {
			StubStatus oldStatus = stubComp.getStatus();
			try {
				refreshStub(stubComp);
				if (!oldStatus.equals(StubStatus.ACTIVE)) {
					stubComp.setStatus(StubStatus.ACTIVE);
					flag = true;
				}
			} catch (RemoteLookupFailureException ex) {
				if (logger.isErrorEnabled())
					logger.error("Lookup of RMI stub failed,serviceUrl is "
							+ stubComp.getServiceUrl());
				if(logger.isDebugEnabled())
					logger.debug("Lookup of RMI stub failed,serviceUrl is "
							+ stubComp.getServiceUrl(), ex);
				failCount++;
				if (!oldStatus.equals(StubStatus.BROKEN)) {
					stubComp.setStatus(StubStatus.BROKEN);
					flag = true;
				}
			}
		}
		if (failCount == stubArr.size())
			throw new RemoteLookupFailureException("Lookup of RMI stub failed "
					+ stubArr.toString());
		if (flag)
			updateLBSchedule();
		return flag;
	}

	private void refreshStub(StubComponent stubComp)
			throws RemoteLookupFailureException {
		initStubComponent(stubComp);
	}

	/**
	 * @param stubComp
	 * @throws RemoteLookupFailureException
	 */
	private void initStubComponent(StubComponent stubComp)
			throws RemoteLookupFailureException {
		Remote remoteObj = null;
		try {
			remoteObj = lookupStub(stubComp.getServiceUrl());
			stubComp.setStub(remoteObj);
			stubComp.setStatus(StubStatus.ACTIVE);
		} catch (RemoteLookupFailureException e) {
			stubComp.setStatus(StubStatus.BROKEN);
			throw e;
		}
	}

	private void updateLBSchedule() {
		List<StubComponent> liveStubComp = new ArrayList<StubComponent>(0);
		for (StubComponent stubComp : stubArr) {

			if (stubComp.getStatus().equals(StubStatus.ACTIVE)) {
				liveStubComp.add(stubComp);
			}
			if (liveStubComp.size() > 0)
				schedule = new RandomLbSchedule(liveStubComp);
			else
				schedule = null;
		}
	}

	/**
	 * Dummy URLStreamHandler that's just specified to suppress the standard
	 * <code>java.net.URL</code> URLStreamHandler lookup, to be able to use the
	 * standard URL class for parsing "rmi:..." URLs.
	 */
	private static class DummyURLStreamHandler extends URLStreamHandler {

		protected URLConnection openConnection(URL url) throws IOException {
			throw new UnsupportedOperationException();
		}
	}

	public boolean isLookupStubOnStartup() {
		return lookupStubOnStartup;
	}

	public void setLookupStubOnStartup(boolean lookupStubOnStartup) {
		this.lookupStubOnStartup = lookupStubOnStartup;
	}

	public RMIClientSocketFactory getRegistryClientSocketFactory() {
		return registryClientSocketFactory;
	}

	public void setRegistryClientSocketFactory(
			RMIClientSocketFactory registryClientSocketFactory) {
		this.registryClientSocketFactory = registryClientSocketFactory;
	}

	public List<String> getServiceUrls() {
		return serviceUrls;
	}

	public void setServiceUrls(List<String> serviceUrls) {
		this.serviceUrls = serviceUrls;
	}

}
