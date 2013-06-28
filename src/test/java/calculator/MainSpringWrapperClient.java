package calculator;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainSpringWrapperClient {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-rmi-wrapper.xml");
		CalculatorService as = (CalculatorService) context.getBean("CalculatorService");
		for (int i = 0; i < 1000; i++) {
			try {
				Thread.sleep(300);
				System.out.println(as.add(1, 2));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
