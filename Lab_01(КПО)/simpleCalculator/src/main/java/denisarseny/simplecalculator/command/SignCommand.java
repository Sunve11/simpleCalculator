package denisarseny.simplecalculator.command;

import denisarseny.simplecalculator.viewmodel.CalculatorViewModel;

public final class SignCommand extends AbstractCalculatorCommand {

    public SignCommand(CalculatorViewModel viewModel) {
        super(viewModel);
    }

    @Override
    protected void doExecute() {
        viewModel.signPressed();
    }
}
