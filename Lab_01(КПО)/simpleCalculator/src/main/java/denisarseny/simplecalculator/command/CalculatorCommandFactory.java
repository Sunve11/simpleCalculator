package denisarseny.simplecalculator.command;

import denisarseny.simplecalculator.viewmodel.CalculatorViewModel;

/**
 * Создаёт команды для действий калькулятора.
 */
public final class CalculatorCommandFactory {

    private final CalculatorViewModel viewModel;

    public CalculatorCommandFactory(CalculatorViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public CalculatorCommand appendDigit(String digit) {
        return new AppendDigitCommand(viewModel, digit);
    }

    public CalculatorCommand appendOperator(String operator) {
        return new AppendOperatorCommand(viewModel, operator);
    }

    public CalculatorCommand equals() {
        return new EqualsCommand(viewModel);
    }

    public CalculatorCommand clearDisplay() {
        return new ClearDisplayCommand(viewModel);
    }

    public CalculatorCommand dot() {
        return new DotCommand(viewModel);
    }

    public CalculatorCommand sign() {
        return new SignCommand(viewModel);
    }

    public CalculatorCommand openBracket() {
        return new OpenBracketCommand(viewModel);
    }

    public CalculatorCommand closeBracket() {
        return new CloseBracketCommand(viewModel);
    }

    public CalculatorCommand showHistory() {
        return new ShowHistoryCommand(viewModel);
    }

    public CalculatorCommand clearHistory() {
        return new ClearHistoryCommand(viewModel);
    }
}
