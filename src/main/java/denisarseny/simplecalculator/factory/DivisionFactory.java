package denisarseny.simplecalculator.factory;

import denisarseny.simplecalculator.operation.BinaryOperation;
import denisarseny.simplecalculator.operation.DivisionOperation;

public class DivisionFactory extends OperationFactory {
    @Override
    public BinaryOperation createOperation() {
        return new DivisionOperation();
    }
}
