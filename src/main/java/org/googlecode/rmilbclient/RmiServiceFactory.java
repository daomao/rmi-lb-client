package org.googlecode.rmilbclient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhongfeng
 *
 */
public class RmiServiceFactory {

	private List<String> regBaseUrls;

	private final static Map<String, Object> REMOTE_SERVICE = new ConcurrentHashMap<String, Object>();

	@SuppressWarnings("unchecked")
	public synchronized <T> T createRemoteService(Class<T> cls, String remoteServiceName) {
		T ret = (T) REMOTE_SERVICE.get(remoteServiceName);
		if (ret == null) {
			RmiLbServiceConfig<T> config = new RmiLbServiceConfig<T>(
					getServiceUrls(remoteServiceName), cls);
			RmiProxyFactory factory = RmiProxyFactory.getInstance();
			ret = factory.create(config);
			REMOTE_SERVICE.put(remoteServiceName, ret);
		}
		return ret;
	}

	private List<String> getServiceUrls(String remoteServiceName) {
		List<String> serviceUrls = new ArrayList<String>();
		for (String baseRegUrl : regBaseUrls)
			serviceUrls.add(baseRegUrl + "/" + remoteServiceName);
		return serviceUrls;
	}

	public List<String> getRegBaseUrls() {
		return regBaseUrls;
	}

	public void setRegBaseUrls(List<String> regBaseUrls) {
		this.regBaseUrls = regBaseUrls;
	}
}
