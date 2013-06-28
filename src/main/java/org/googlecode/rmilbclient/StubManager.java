package org.googlecode.rmilbclient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.googlecode.rmilbclient.exceptions.RmiLookupFailureException;
import org.googlecode.rmilbclient.schedule.LbSchedule;
import org.googlecode.rmilbclient.schedule.RandomLbSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.RemoteLookupFailureException;


/**
 * @author zhongfeng
 * 
 */
public class StubManager {

	private static Logger logger = LoggerFactory.getLogger(StubManager.class
			.getName());

	private List<StubComponent> stubArr;

	/**
	 * 是否在启动时开启lookup stub 操作
	 */
	private boolean lookupStubOnStartup = false;

	/**
	 * 多节点负载均衡调度器
	 */
	private LbSchedule<StubComponent> lbSchedule;

	private final ReadWriteLock lock = new ReentrantReadWriteLock();

	private final Lock rLock = lock.readLock();

	private final Lock wLock = lock.writeLock();

	/**
	 * 
	 */
	public StubManager() {
	}

	/**
	 * @param stubArr
	 */
	public StubManager(List<StubComponent> stubArr) {
		this(stubArr, false);
	}

	/**
	 * @param stubArr
	 * @param lookupStubOnStartup
	 */
	public StubManager(List<StubComponent> stubArr, boolean lookupStubOnStartup) {
		this.stubArr = stubArr;
		this.lookupStubOnStartup = lookupStubOnStartup;
		init();
	}

	public void init() throws RmiLookupFailureException {
		prepare();
	}

	/**
	 * Fetches RMI stub on startup, if necessary.
	 * 
	 * @throws RmiLookupFailureException
	 * 
	 * @throws RmiLookupFailureException
	 *             if RMI stub creation failed
	 */
	private void prepare() throws RmiLookupFailureException {
		// Cache RMI stub on initialization
		if (this.lookupStubOnStartup) {
			updateStubsStatus();
		}
	}

	/**
	 * 依据负载均衡策略，获取stub
	 * 
	 * @return
	 * @throws RmiLookupFailureException
	 */
	public Object getStub() throws RmiLookupFailureException {
		Object stub = null;
		rLock.lock();
		try {
			if (lbSchedule != null) {
				StubComponent stubComp = lbSchedule.nextHost();
				if (logger.isDebugEnabled())
					logger.debug("Current access service url is :{}", stubComp
							.getServiceUrl());
				stub = stubComp.getStub();
			} else {
				throw new RmiLookupFailureException(
						"No Stub can be used,try to update stub status. ");
			}
		} finally {
			rLock.unlock();
		}
		return stub;
	}

	/**
	 * @throws RmiLookupFailureException
	 */
	public void updateStubsStatus() throws RmiLookupFailureException {
		wLock.lock();
		try {
			logger.info("updateStubsStatus begin");
			if (refreshCachedStubArray())// 如果有状态变化,重新生成lbSchedule
				updateLBSchedule();
		} finally {
			wLock.unlock();
		}
	}

	/**
	 * 检查节点的状态是否有不是StubStatus.ACTIVE
	 * @return 有true,没有false
	 */
	public boolean isExistBrokenNode() {
		wLock.lock();
		try {
			for (StubComponent stubComp : stubArr) {
				if (!stubComp.getStatus().equals(StubStatus.ACTIVE))
					return true;
			}
			return false;
		} finally {
			wLock.unlock();
		}
	}

	/**
	 * 刷新stub，返回true表示有状态变化，false没状态变化
	 * 
	 * @param flag
	 * @return
	 */
	private boolean refreshCachedStubArray() {
		boolean flag = false;
		for (StubComponent stubComp : stubArr) {
			StubStatus oldStatus = stubComp.getStatus();
			try {
				stubComp.refreshStub();
			} catch (RemoteLookupFailureException ex) {
				if (logger.isWarnEnabled()) {
					logger
							.warn(
									"Lookup of RMI stub failed,ServiceURI:{},DetailMsg:{}",
									stubComp.getServiceUrl(), ex.getMessage());
				}
				if (logger.isDebugEnabled())
					logger.debug("Lookup of RMI stub failed,DetailMsg:"
							+ ex.getMessage(), ex);
			} finally {
				if (!oldStatus.equals(stubComp.getStatus())) {
					if (logger.isWarnEnabled())
						logger.warn("Service {} from {} to {} ", new Object[] {
								stubComp.getServiceUrl(), oldStatus,
								stubComp.getStatus() });
					flag = true;
				}
			}
		}
		return flag;
	}

	/**
	 * 重建schedule状态
	 * 
	 * @throws RmiLookupFailureException
	 */
	private void updateLBSchedule() throws RmiLookupFailureException {
		List<StubComponent> liveStubComp = new ArrayList<StubComponent>(0);
		for (StubComponent stubComp : stubArr) {
			if (stubComp.getStatus().equals(StubStatus.ACTIVE)) {
				liveStubComp.add(stubComp);
			}
		}
		if (liveStubComp.size() > 0) {
			if (logger.isWarnEnabled())
				logger.warn("Update LBSchedule,Current StubComponent is {} ",
						stubArr);
			lbSchedule = new RandomLbSchedule(liveStubComp);
		} else {
			if (logger.isErrorEnabled())
				logger
						.error(
								"No RMI Service cant be used,try to check rmi server at {} ",
								stubArr.toString());
			throw new RmiLookupFailureException("Lookup of RMI stub failed "
					+ stubArr.toString());
		}

	}

	public boolean isLookupStubOnStartup() {
		return lookupStubOnStartup;
	}

	public void setLookupStubOnStartup(boolean lookupStubOnStartup) {
		this.lookupStubOnStartup = lookupStubOnStartup;
	}

	public List<StubComponent> getStubArr() {
		return stubArr;
	}

	public void setStubArr(List<StubComponent> stubArr) {
		this.stubArr = stubArr;
	}
}
