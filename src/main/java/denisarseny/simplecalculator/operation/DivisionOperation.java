package denisarseny.simplecalculator.operation;

public class DivisionOperation implements BinaryOperation {
    @Override
    public double execute(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Деление на ноль");
        }
        return a / b;
    }
}
