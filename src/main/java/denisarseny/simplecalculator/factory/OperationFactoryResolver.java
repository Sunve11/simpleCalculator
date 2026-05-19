package denisarseny.simplecalculator.factory;

public final class OperationFactoryResolver {

    private OperationFactoryResolver() {
    }

    public static OperationFactory getFactory(char operator) {
        return switch (operator) {
            case '+' -> new AdditionFactory();
            case '-' -> new SubtractionFactory();
            case '*' -> new MultiplicationFactory();
            case '/' -> new DivisionFactory();
            default -> throw new IllegalArgumentException("Неизвестный оператор: " + operator);
        };
    }
}
