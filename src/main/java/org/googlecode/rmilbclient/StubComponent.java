package org.googlecode.rmilbclient;

import java.rmi.Remote;

import org.springframework.remoting.RemoteLookupFailureException;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

/**
 * @author zhongfeng
 * 
 */
public class StubComponent {

	/**
	 * 状态
	 */
	private StubStatus status = StubStatus.UN_INIT;

	/**
	 * Remote Stub 对象
	 */
	private Object stub;

	/**
	 * RMI Service URL
	 */
	private String serviceUrl;

	/**
	 * 服务接口
	 */
	private Class<?> serviceInterface;

	/**
	 * 
	 */
	public StubComponent() {
	}

	/**
	 * @param status
	 * @param stub
	 */
	public StubComponent(StubStatus status, Remote stub) {
		this.status = status;
		this.stub = stub;
	}

	public void refreshStub() throws RemoteLookupFailureException {
		Object stub = null;
		try {
			stub = buildStub();
			setStub(stub);
			setStatus(StubStatus.ACTIVE);
		} catch (RemoteLookupFailureException e) {
			setStatus(StubStatus.BROKEN);
			throw e;
		}
	}

	/**
	 * @return
	 */
	private Object buildStub() {
		RmiProxyFactoryBean remoteStub = new RmiProxyFactoryBean();
		remoteStub.setLookupStubOnStartup(true);
		remoteStub.setRefreshStubOnConnectFailure(true);
		remoteStub.setCacheStub(true);
		remoteStub.setServiceUrl(serviceUrl);
		remoteStub.setServiceInterface(serviceInterface);
		remoteStub.afterPropertiesSet();
		return remoteStub.getObject();
	}

	public StubStatus getStatus() {
		return status;
	}

	public void setStatus(StubStatus status) {
		this.status = status;
	}

	public Object getStub() {
		return stub;
	}

	public void setStub(Object stub) {
		this.stub = stub;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public Class<?> getServiceInterface() {
		return serviceInterface;
	}

	public void setServiceInterface(Class<?> serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	@Override
	public String toString() {
		return "StubComponent [serviceInterface=" + serviceInterface
				+ ", serviceUrl=" + serviceUrl + ", status=" + status + "]";
	}

}
