package denisarseny.simplecalculator.command;

import denisarseny.simplecalculator.viewmodel.CalculatorViewModel;

public final class OpenBracketCommand extends AbstractCalculatorCommand {

    public OpenBracketCommand(CalculatorViewModel viewModel) {
        super(viewModel);
    }

    @Override
    protected void doExecute() {
        viewModel.openBracketPressed();
    }
}
