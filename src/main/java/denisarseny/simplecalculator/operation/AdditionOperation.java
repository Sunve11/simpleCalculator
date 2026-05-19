package denisarseny.simplecalculator.operation;

public class AdditionOperation implements BinaryOperation {
    @Override
    public double execute(double a, double b) {
        return a + b;
    }
}
