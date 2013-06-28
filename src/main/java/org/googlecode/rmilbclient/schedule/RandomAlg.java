package org.googlecode.rmilbclient.schedule;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @author zhongfeng
 *
 * @param <E>
 */
public class RandomAlg<E> implements Iterable<E> {

	private final static Random random = new Random();
	List<E> coll;
	
	public RandomAlg(List<E> coll) { this.coll = coll; }

	public Iterator<E> iterator() {
		return new Iterator<E>() {
			public boolean hasNext() {
				return true;
			}
			public E next() {
				E ret = null;
				if (coll != null) {
					int length = coll.size();
					int rIndex = random.nextInt(length);
					ret = coll.get(rIndex);
				}
				return ret;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}