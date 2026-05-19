package denisarseny.simplecalculator.command;

import denisarseny.simplecalculator.viewmodel.CalculatorViewModel;

public final class DotCommand extends AbstractCalculatorCommand {

    public DotCommand(CalculatorViewModel viewModel) {
        super(viewModel);
    }

    @Override
    protected void doExecute() {
        viewModel.dotPressed();
    }
}
