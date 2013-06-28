package calculator;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SpringCalculatorServer1 {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting of the Calculator Application exposed as RMI Services...");
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-rmi-1.xml");  
        System.out.println("... Press Enter to Exit...");
        System.in.read();
        System.out.println("Exited...");
        System.exit(0);
    }

}
