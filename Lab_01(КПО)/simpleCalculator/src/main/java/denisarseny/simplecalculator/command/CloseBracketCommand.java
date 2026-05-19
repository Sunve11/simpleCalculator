package denisarseny.simplecalculator.command;

import denisarseny.simplecalculator.viewmodel.CalculatorViewModel;

public final class CloseBracketCommand extends AbstractCalculatorCommand {

    public CloseBracketCommand(CalculatorViewModel viewModel) {
        super(viewModel);
    }

    @Override
    protected void doExecute() {
        viewModel.closeBracketPressed();
    }
}
