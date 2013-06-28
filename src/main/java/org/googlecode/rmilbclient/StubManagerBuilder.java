package org.googlecode.rmilbclient;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhongfeng
 * 
 */
public class StubManagerBuilder {

	/**
	 * Buid CachedStubManager instance
	 * 
	 * @return
	 */
	public static StubManager buildStubManager(RmiLbServiceConfig<?> config) {

		List<StubComponent> stubArr = new ArrayList<StubComponent>();
		for (String serviceUrl : config.getServiceUrls()) {
			StubComponent stubComp = new StubComponent();
			stubComp.setServiceUrl(serviceUrl);
			stubComp.setServiceInterface(config.getServiceInterface());
			stubArr.add(stubComp);
		}
		StubManager stubMgr = new StubManager(stubArr, config
				.isLookupStubOnStartup());
		return stubMgr;
	}

}
