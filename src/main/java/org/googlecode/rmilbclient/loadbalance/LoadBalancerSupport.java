package org.googlecode.rmilbclient.loadbalance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LoadBalancerSupport<E> implements LoadBalancer<E> {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	private final List<E> processors = new CopyOnWriteArrayList<E>();

	public void addProcessor(E processor) {
		processors.add(processor);
	}

	public void addProcessors(Collection<E> processors) {
		this.processors.addAll(processors);
	}

	public void removeProcessor(E processor) {
		processors.remove(processor);
	}

	public List<E> getProcessors() {
		return processors;
	}

	public List<E> next() {
		if (!hasNext()) {
			return null;
		}
		return new ArrayList<E>(processors);
	}

	public boolean hasNext() {
		return processors.size() > 0;
	}

	public E select() {
		List<E> list = getProcessors();
		E processor = null;
		if (!list.isEmpty()) {
			processor = doSelect(list);
			if (processor == null) {
				throw new IllegalStateException(
						"No processors could be chosen to process ");
			}
		}
		return processor;
	}

	public E select(String key) {
		return select();
	}

	protected abstract E doSelect(List<E> processors);

}
