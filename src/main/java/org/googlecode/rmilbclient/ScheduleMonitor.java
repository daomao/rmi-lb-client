package org.googlecode.rmilbclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhongfeng
 * 
 */
class ScheduleMonitor implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(ScheduleMonitor.class);

	private StubManager stubMgr;

	/**
	 * 
	 */
	public ScheduleMonitor() {
	}

	/**
	 * @param stubMgr
	 */
	public ScheduleMonitor(StubManager stubMgr) {
		this.stubMgr = stubMgr;
	}

	public void run() {
		if (logger.isDebugEnabled())
			logger.debug("Start monitor process");
		if (stubMgr.isExistBrokenNode())
			stubMgr.updateStubsStatus();
	}

	public StubManager getStubMgr() {
		return stubMgr;
	}

	public void setStubMgr(StubManager stubMgr) {
		this.stubMgr = stubMgr;
	}
}
