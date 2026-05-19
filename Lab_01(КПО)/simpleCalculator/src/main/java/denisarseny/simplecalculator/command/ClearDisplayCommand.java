package denisarseny.simplecalculator.command;

import denisarseny.simplecalculator.viewmodel.CalculatorViewModel;

public final class ClearDisplayCommand extends AbstractCalculatorCommand {

    public ClearDisplayCommand(CalculatorViewModel viewModel) {
        super(viewModel);
    }

    @Override
    protected void doExecute() {
        viewModel.clear();
    }
}
