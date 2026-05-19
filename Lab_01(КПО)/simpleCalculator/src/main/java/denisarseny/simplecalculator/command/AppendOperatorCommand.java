package denisarseny.simplecalculator.command;

import denisarseny.simplecalculator.viewmodel.CalculatorViewModel;

public final class AppendOperatorCommand extends AbstractCalculatorCommand {

    private final String operator;

    public AppendOperatorCommand(CalculatorViewModel viewModel, String operator) {
        super(viewModel);
        this.operator = operator;
    }

    @Override
    protected void doExecute() {
        viewModel.appendOperator(operator);
    }
}
