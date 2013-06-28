package org.googlecode.rmilbclient.loadbalance;

import java.util.Collection;
import java.util.List;

/**
 * A strategy for load balancing across a number of {@link Processor} instances
 * 
 * @author zhongfeng
 * 
 * @param <E>
 */
public interface LoadBalancer<E> {
	/**
	 * Adds a new processor to the load balancer
	 * 
	 * @param processor
	 *            the processor to be added to the load balancer
	 */
	void addProcessor(E processor);
	
	public void addProcessors(Collection<E> processors) ;

	/**
	 * Removes the given processor from the load balancer
	 * 
	 * @param processor
	 *            the processor to be removed from the load balancer
	 */
	void removeProcessor(E processor);

	/**
	 * Returns the current processors available to this load balancer
	 * 
	 * @return the processors available
	 */
	List<E> getProcessors();

	/**
	 * Returns the selected processor based on load balance algorithm
	 * 
	 * @return the processors
	 */
	E select();
	
	/**
	 * Returns the selected processor based on load balance algorithm
	 * Used by ConsistHash
	 * 
	 * @return the processors
	 */
	E select(String key);
}
