package denisarseny.simplecalculator.operation;

public class MultiplicationOperation implements BinaryOperation {
    @Override
    public double execute(double a, double b) {
        return a * b;
    }
}
