package org.googlecode.rmilbclient.loadbalance;

import java.util.List;

public class RoundRobinLoadBalancer<E> extends LoadBalancerSupport<E> {
	private int counter = -1;

	@Override
	protected synchronized E doSelect(List<E> processors) {
		 int size = processors.size();
	        if (++counter >= size) {
	            counter = 0;
	        }
	        return processors.get(counter);
	}

}
