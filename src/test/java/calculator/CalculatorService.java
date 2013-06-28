
package calculator;


/**
 * The Calculator service interface.
 */
@RmiServiceExplorer
public interface CalculatorService {

    double add(double n1, double n2);

    double subtract(double n1, double n2);

    double multiply(double n1, double n2);

    double divide(double n1, double n2);
}
