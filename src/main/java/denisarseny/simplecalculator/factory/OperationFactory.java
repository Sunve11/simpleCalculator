package denisarseny.simplecalculator.factory;

import denisarseny.simplecalculator.operation.BinaryOperation;

public abstract class OperationFactory {
    public abstract BinaryOperation createOperation();
}
