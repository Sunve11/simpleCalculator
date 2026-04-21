package denisarseny.simplecalculator.factory;

import denisarseny.simplecalculator.operation.BinaryOperation;
import denisarseny.simplecalculator.operation.SubtractionOperation;

public class SubtractionFactory extends OperationFactory {
    @Override
    public BinaryOperation createOperation() {
        return new SubtractionOperation();
    }
}
