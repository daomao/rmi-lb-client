package org.googlecode.rmilbclient;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhongfeng
 * 
 */
public class RmiLbServiceConfig<E> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 31328307955440178L;

	/**
	 * RMI Service Url 列表，对等服务
	 */
	private List<String> serviceUrls;

	/**
	 * service interface
	 */
	private Class<E> serviceInterface;

	/**
	 * 是否启动时初始化stub
	 */
	private boolean lookupStubOnStartup = false;

	/**
	 * monitor 检测 间隔
	 */
	private long monitorPeriod = StubMonitorService.DEFAULF_PERIOD;
	
	/**
	 * RMI 调用超时时间，由于RMI使用的是JVM级别的 sun.rmi.transport.tcp.responseTimeout
	 * 来指定，缺陷是不能每个服务指定单独的超时时间,时间单位：毫秒
	 */
	private long timeout = -1;

	/**
	 * 
	 */
	public RmiLbServiceConfig() {
	}
	
	public RmiLbServiceConfig(List<String> serviceUrls,
			Class<E> serviceInterface) {
		this(serviceUrls, serviceInterface, false,
				StubMonitorService.DEFAULF_PERIOD,-1);
	}

	public RmiLbServiceConfig(List<String> serviceUrls,
			Class<E> serviceInterface, boolean lookupStubOnStartup,
			long monitorPeriod,long timeout) {
		setServiceUrls(serviceUrls);
		setServiceInterface(serviceInterface);
		this.lookupStubOnStartup = lookupStubOnStartup;
		this.monitorPeriod = monitorPeriod;
		this.timeout = timeout;
	}

	public List<String> getServiceUrls() {
		return serviceUrls;
	}

	public void setServiceUrls(List<String> serviceUrls) {
		if (serviceUrls == null || serviceUrls.isEmpty())
			throw new IllegalArgumentException(
					" 'serviceUrls' must not be null or empty");
		this.serviceUrls = serviceUrls;
	}

	public Class<E> getServiceInterface() {
		return serviceInterface;
	}

	public void setServiceInterface(Class<E> serviceInterface) {
		if (serviceInterface != null && !serviceInterface.isInterface()) {
			throw new IllegalArgumentException(
					"'serviceInterface' must be an interface");
		}
		this.serviceInterface = serviceInterface;
	}

	public boolean isLookupStubOnStartup() {
		return lookupStubOnStartup;
	}

	public void setLookupStubOnStartup(boolean lookupStubOnStartup) {
		this.lookupStubOnStartup = lookupStubOnStartup;
	}

	public long getMonitorPeriod() {
		return monitorPeriod;
	}

	public void setMonitorPeriod(long monitorPeriod) {
		this.monitorPeriod = monitorPeriod;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (lookupStubOnStartup ? 1231 : 1237);
		result = prime * result
				+ (int) (monitorPeriod ^ (monitorPeriod >>> 32));
		result = prime
				* result
				+ ((serviceInterface == null) ? 0 : serviceInterface.hashCode());
		result = prime * result
				+ ((serviceUrls == null) ? 0 : serviceUrls.hashCode());
		result = prime * result + (int) (timeout ^ (timeout >>> 32));
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RmiLbServiceConfig other = (RmiLbServiceConfig) obj;
		if (lookupStubOnStartup != other.lookupStubOnStartup)
			return false;
		if (monitorPeriod != other.monitorPeriod)
			return false;
		if (serviceInterface == null) {
			if (other.serviceInterface != null)
				return false;
		} else if (!serviceInterface.equals(other.serviceInterface))
			return false;
		if (serviceUrls == null) {
			if (other.serviceUrls != null)
				return false;
		} else if (!serviceUrls.equals(other.serviceUrls))
			return false;
		if (timeout != other.timeout)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RmiLbServiceConfig [lookupStubOnStartup=" + lookupStubOnStartup
				+ ", monitorPeriod=" + monitorPeriod + ", serviceInterface="
				+ serviceInterface + ", serviceUrls=" + serviceUrls
				+ ", timeout=" + timeout + "]";
	}

}
