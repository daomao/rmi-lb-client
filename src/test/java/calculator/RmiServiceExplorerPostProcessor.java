package calculator;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.remoting.rmi.RmiRegistryFactoryBean;
import org.springframework.remoting.rmi.RmiServiceExporter;

/**
 * @author zhongfeng
 * 
 */
public class RmiServiceExplorerPostProcessor implements BeanPostProcessor,
		ApplicationContextAware {

	private ApplicationContext context;
	private Registry registry;

	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		try {
			autoDistributeRmiService(bean);
		} catch (RemoteException e) {
			throw new RuntimeException(
					"Distribute RMI Service Exception . Bean Name is "
							+ beanName, e);
		}
		return bean;
	}

	/**
	 * @param bean
	 * @throws RemoteException
	 */
	private void autoDistributeRmiService(Object bean) throws RemoteException {
		Class<?>[] interfaces = bean.getClass().getInterfaces();
		if (bean.getClass().isAnnotationPresent(RmiServiceExplorer.class)) {
			RmiServiceExplorer rse = bean.getClass().getAnnotation(
					RmiServiceExplorer.class);
			Class<?> serviceInterface = rse.serviceInterface();
			if (serviceInterface.equals(Object.class)) {
				if (interfaces != null && interfaces.length == 1)
					serviceInterface = interfaces[0];
				else
					throw new RuntimeException(bean.getClass().getName()
							+ " have mutil interfaces or null");
			}
			distributeRmiService(bean, serviceInterface);
		} else {
			for (Class<?> icls : interfaces)
				if (icls.isAnnotationPresent(RmiServiceExplorer.class))
					distributeRmiService(bean, icls);

		}
	}

	/**
	 * @param bean
	 * @param rse
	 * @param serviceInterface
	 * @throws RemoteException
	 */
	private void distributeRmiService(Object bean, Class<?> serviceInterface)
			throws RemoteException {
		RmiServiceExplorer rse = serviceInterface
				.getAnnotation(RmiServiceExplorer.class);
		String serviceName = rse.serviceName();
		if (serviceName.equals("")) {
			serviceName = serviceInterface.getSimpleName();
		}
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setService(bean);
		exporter.setServiceInterface(serviceInterface);
		exporter.setServiceName(serviceName);
		exporter.setRegistry(getRegistry());
		exporter.afterPropertiesSet();
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.context = applicationContext;
		if (registry == null) {
			RmiRegistryFactoryBean factoryBean = context
					.getBean(RmiRegistryFactoryBean.class);
			if (factoryBean != null)
				try {
					factoryBean.afterPropertiesSet();
					setRegistry(factoryBean.getObject());
				} catch (Exception e) {
					throw new RuntimeException("Cannot get registry");
				}
			else
				throw new RuntimeException(
						"Cannot find RmiRegistryFactoryBean config");
		}
	}

	public ApplicationContext getContext() {
		return context;
	}

	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}
}
