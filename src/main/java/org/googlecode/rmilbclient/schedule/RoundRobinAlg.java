package org.googlecode.rmilbclient.schedule;

import java.util.Iterator;
import java.util.List;

/**
 * @author zhongfeng
 *
 * @param <E>
 */
public class RoundRobinAlg<E> implements Iterable<E> {
	private List<E> coll;
	private int index = 0;

	public RoundRobinAlg(List<E> coll) {
		this.coll = coll;
	}

	public Iterator<E> iterator() {
		return new Iterator<E>() {
			public boolean hasNext() {
				return true;
			}

			public E next() {
				index = (index + 1) % coll.size();
				E res = coll.get(index);
				return res;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}