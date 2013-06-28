package org.googlecode.rmilbclient;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhongfeng
 * 
 */
public class StubMonitorService {

	private static final Logger logger = LoggerFactory
			.getLogger(StubMonitorService.class);

	public static final long DEFAULF_PERIOD = 600L;

	public static final long DEFAULT_MIN_PERIOD = 10L;

	public static final long DEFAULT_MAX_PERIOD = 3600L;

	/**
	 * 监控频率，默认是600s一次
	 */
	private long monitorPeriod = DEFAULF_PERIOD;

	/**
	 * 定时监控cached stub的服务
	 */
	private ScheduledExecutorService monitorService;

	private StubManager stubMgr;

	/**
	 * @param monitorPeriod
	 * @param stubMgr
	 */
	public StubMonitorService(StubManager stubMgr) {
		this(DEFAULF_PERIOD, stubMgr);
	}

	/**
	 * @param monitorPeriod
	 * @param stubMgr
	 */
	public StubMonitorService(long monitorPeriod, StubManager stubMgr) {
		this.monitorPeriod = monitorPeriod;
		this.stubMgr = stubMgr;
	}

	/**
	 * @param monitorPeriod
	 * @return
	 */
	private static long checkPeriod(long monitorPeriod) {
		if(monitorPeriod < DEFAULT_MIN_PERIOD)
			monitorPeriod = DEFAULT_MIN_PERIOD;
		else if(monitorPeriod > DEFAULT_MAX_PERIOD)
			monitorPeriod = DEFAULT_MAX_PERIOD;
		return monitorPeriod;
	}

	/**
	 * 延迟初始化 一个 ScheduledThreadPool,每10分钟定时检测下CacheStub的状态
	 */
	public synchronized void startMonitor() {
		monitorPeriod = checkPeriod(monitorPeriod);
		if (monitorService == null) {
			if (logger.isDebugEnabled())
				logger.debug("Build MonitorService,monitor period is {}",
						monitorPeriod);
			monitorService = Executors.newScheduledThreadPool(1);
			monitorService.scheduleAtFixedRate(new ScheduleMonitor(stubMgr),
					monitorPeriod, monitorPeriod, TimeUnit.SECONDS);
		}
	}

	public long getMonitorPeriod() {
		return monitorPeriod;
	}

	public void setMonitorPeriod(long monitorPeriod) {
		this.monitorPeriod = monitorPeriod;
	}

	public StubManager getStubMgr() {
		return stubMgr;
	}

	public void setStubMgr(StubManager stubMgr) {
		this.stubMgr = stubMgr;
	}
}
