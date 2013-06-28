package org.googlecode.rmilbclient.loadbalance;

import java.util.List;
import java.util.Random;

public class RandomLoadBalancer<E> extends LoadBalancerSupport<E> {
	 private static final Random RANDOM = new Random();

	@Override
	protected synchronized E doSelect(List<E> processors) {
		int size = processors.size();
        if (size == 0) {
            return null;
        } else if (size == 1) {
            // there is only 1
            return processors.get(0);
        }

        // pick a random
        int index = RANDOM.nextInt(size);
        return processors.get(index);
	}

}
