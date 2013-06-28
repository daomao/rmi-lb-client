package org.googlecode.rmilbclient.loadbalance;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author zhongfeng
 * 
 * @param <E>
 */
public class ConsistHashLoadBalancer<E> extends WeightedLoadBalancer<E> {

	/**
	 *  默认复制份数
	 */
	private static final int DEFAULT_REPLICATE_NUM = 160;
	
	/**
	 * 一致性hash 计算map
	 */
	private SortedMap<Long, E> nodesMap;
	
	/**
	 * 
	 */
	private int replicateNum = DEFAULT_REPLICATE_NUM;
	
	/**
	 * hash func
	 */
	private Hashing hashFunc = Hashing.MURMUR_HASH;

	public ConsistHashLoadBalancer(List<E> processors,
			List<Integer> distributionRatios) {
		super(processors, distributionRatios);
		init();
	}

	private void init() {
		nodesMap = new TreeMap<Long, E>();
		for (int i = 0; i < getProcessors().size(); i++) {
			E processor = getProcessors().get(i);
			int weight = getDistributionRatioList().get(i);
			for (int j = 0; j < replicateNum * weight; j++) {
				String hashKey = "PROCESSOR-" + i + "-NODE-" + j
						+ processor.toString();
				nodesMap.put(hashFunc.hash(hashKey), processor);
			}
		}
	}

	@Override
	public E select(String key) {
		SortedMap<Long, E> ret = nodesMap.tailMap(hashFunc.hash(key));
		if (ret.isEmpty())
			return nodesMap.get(nodesMap.firstKey());
		return ret.get(ret.firstKey());
	}
	
	@Override
	protected E doSelect(List<E> processors) {
		return null;
	}

}
