package denisarseny.simplecalculator.factory;

import denisarseny.simplecalculator.operation.BinaryOperation;
import denisarseny.simplecalculator.operation.MultiplicationOperation;

public class MultiplicationFactory extends OperationFactory {
    @Override
    public BinaryOperation createOperation() {
        return new MultiplicationOperation();
    }
}
