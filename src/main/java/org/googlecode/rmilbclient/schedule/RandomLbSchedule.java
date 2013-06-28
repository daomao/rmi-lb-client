package org.googlecode.rmilbclient.schedule;

import java.util.Iterator;
import java.util.List;

import org.googlecode.rmilbclient.StubComponent;



/**
 * @author zhongfeng
 *
 */
public class RandomLbSchedule implements LbSchedule<StubComponent> {

	private Iterator<StubComponent> iter;

	private RandomAlg<StubComponent> alg;

	/**
	 * @param iter
	 * @param slaveLbSch
	 */
	public RandomLbSchedule(List<StubComponent> stubArr) {
		this.alg = new RandomAlg<StubComponent>(stubArr);
		this.iter = alg.iterator();
	}

	public StubComponent nextHost() {
		return iter.next();
	}

}
