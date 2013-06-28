package org.googlecode.rmilbclient.loadbalance;

import java.util.ArrayList;
import java.util.List;

public abstract class WeightedLoadBalancer<E> extends LoadBalancerSupport<E> {
	private List<Integer> distributionRatioList = new ArrayList<Integer>();
    private List<DistributionRatio> runtimeRatios = new ArrayList<DistributionRatio>();
    
    public WeightedLoadBalancer(List<E> processors,List<Integer> distributionRatios) {
    	addProcessors(processors);
    	List<Integer> disGCDRatios = computeGCD(distributionRatios);
        deepCloneDistributionRatios(disGCDRatios);
        loadRuntimeRatios(disGCDRatios);
        doStart();
    }
    
    private List<Integer> computeGCD(List<Integer> distributionRatios)
    {
    	List<Integer> tmp = new ArrayList<Integer>();
    	int gcd = GCDAlg.gcdN(distributionRatios, distributionRatios.size());
    	for(Integer ratio :distributionRatios)
    		tmp.add(ratio/gcd);
    	return tmp;
    }
    
    
    private void deepCloneDistributionRatios(List<Integer> distributionRatios) {
        for (Integer value : distributionRatios) {
            this.distributionRatioList.add(value);
        }
    }
    
    private void doStart(){
        if (getProcessors().size() != getDistributionRatioList().size()) {
            throw new IllegalArgumentException("Loadbalacing with " + getProcessors().size()
                + " should match number of distributions " + getDistributionRatioList().size());
        }
    }

    private void loadRuntimeRatios(List<Integer> distributionRatios) {
        int position = 0;
        for (int value : distributionRatios) {
            runtimeRatios.add(new DistributionRatio(position++, value));
        }
    }
    
    protected boolean isRuntimeRatiosZeroed() {
        boolean cleared = true;
        
        for (DistributionRatio runtimeRatio : runtimeRatios) {
            if (runtimeRatio.getRuntimeWeight() > 0) {
                cleared = false;
            }
        }        
        return cleared; 
    }
    
    protected void resetRuntimeRatios() {
        for (DistributionRatio runtimeRatio : runtimeRatios) {
            runtimeRatio.setRuntimeWeight(runtimeRatio.getDistributionWeight());
        }
    }

    public List<Integer> getDistributionRatioList() {
        return distributionRatioList;
    }

    public void setDistributionRatioList(List<Integer> distributionRatioList) {
        this.distributionRatioList = distributionRatioList;
    }

    public List<DistributionRatio> getRuntimeRatios() {
        return runtimeRatios;
    }

    public void setRuntimeRatios(ArrayList<DistributionRatio> runtimeRatios) {
        this.runtimeRatios = runtimeRatios;
    } 

}
