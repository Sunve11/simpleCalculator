package denisarseny.simplecalculator.viewmodel;

/**
 * Снимок состояния калькулятора для отмены команд (Command).
 */
public record CalculatorSnapshot(String display, boolean startNewExpression) {
}
