package calculator;

import java.util.ArrayList;
import java.util.List;

import org.googlecode.rmilbclient.RmiLbServiceConfig;
import org.googlecode.rmilbclient.RmiProxyFactory;


public class MainClient {

	public static void main(String[] args) {
		List<String> serviceUrls = new ArrayList<String>();
		serviceUrls.add("rmi://localhost:8098/CalculatorService");
		//serviceUrls.add("rmi://localhost:8099/CalculatorService");
		RmiLbServiceConfig<CalculatorService> config = new RmiLbServiceConfig<CalculatorService>(
				serviceUrls, CalculatorService.class);
		config.setMonitorPeriod(1000L);//default 600 seconds
		config.setLookupStubOnStartup(false); // default is false;
		config.setTimeout(2000L);
		RmiProxyFactory factory = RmiProxyFactory.getInstance();
		CalculatorService as = factory.create(config);
		for (int i = 0; i < 1000; i++) {
			try {
				long start = System.nanoTime();
				//Thread.sleep(300);
				System.out.println("ret:"+as.add(1, 2));
				System.out.println("ex time:"+(System.nanoTime() - start) / 1000000L);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
