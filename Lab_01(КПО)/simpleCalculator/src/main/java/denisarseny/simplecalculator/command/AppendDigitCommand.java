package denisarseny.simplecalculator.command;

import denisarseny.simplecalculator.viewmodel.CalculatorViewModel;

public final class AppendDigitCommand extends AbstractCalculatorCommand {

    private final String digit;

    public AppendDigitCommand(CalculatorViewModel viewModel, String digit) {
        super(viewModel);
        this.digit = digit;
    }

    @Override
    protected void doExecute() {
        viewModel.appendDigit(digit);
    }
}
