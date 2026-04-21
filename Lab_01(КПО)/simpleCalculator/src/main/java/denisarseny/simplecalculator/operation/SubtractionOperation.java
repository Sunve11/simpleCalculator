package denisarseny.simplecalculator.operation;

public class SubtractionOperation implements BinaryOperation {
    @Override
    public double execute(double a, double b) {
        return a - b;
    }
}
