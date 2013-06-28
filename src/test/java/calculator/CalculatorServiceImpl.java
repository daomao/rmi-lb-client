package calculator;

/**
 * An implementation of the Calculator service.
 */
public class CalculatorServiceImpl implements CalculatorService {

	public double add(double n1, double n2) {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return n1 + n2;
	}

	public double subtract(double n1, double n2) {
		return n1 - n2;
	}

	public double multiply(double n1, double n2) {
		return n1 * n2;
	}

	public double divide(double n1, double n2) {
		return n1 / n2;
	}
}
