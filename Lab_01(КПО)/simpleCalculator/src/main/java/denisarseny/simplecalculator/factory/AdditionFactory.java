package denisarseny.simplecalculator.factory;

import denisarseny.simplecalculator.operation.AdditionOperation;
import denisarseny.simplecalculator.operation.BinaryOperation;

public class AdditionFactory extends OperationFactory {
    @Override
    public BinaryOperation createOperation() {
        return new AdditionOperation();
    }
}
