
package calculator;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * A claculator service server.
 */
public class SpringCalculatorServer {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting of the Calculator Application exposed as RMI Services...");
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-rmi.xml");  
        System.out.println("... Press Enter to Exit...");
        System.in.read();
        System.out.println("Exited...");
        System.exit(0);
    }

}
