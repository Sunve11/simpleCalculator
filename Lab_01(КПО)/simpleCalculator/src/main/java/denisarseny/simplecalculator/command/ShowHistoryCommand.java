package denisarseny.simplecalculator.command;

import denisarseny.simplecalculator.viewmodel.CalculatorViewModel;

/**
 * Команда без отмены: только открывает окно истории.
 */
public final class ShowHistoryCommand implements CalculatorCommand {

    private final CalculatorViewModel viewModel;

    public ShowHistoryCommand(CalculatorViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void execute() {
        viewModel.showHistory();
    }

    @Override
    public void undo() {
        // окно истории не откатывается
    }

    @Override
    public void redo() {
        execute();
    }
}
