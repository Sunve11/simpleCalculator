package denisarseny.simplecalculator.command;

import denisarseny.simplecalculator.viewmodel.CalculatorSnapshot;
import denisarseny.simplecalculator.viewmodel.CalculatorViewModel;

/**
 * Базовая команда: сохраняет снимок состояния перед первым выполнением.
 */
public abstract class AbstractCalculatorCommand implements CalculatorCommand {

    protected final CalculatorViewModel viewModel;
    private CalculatorSnapshot before;
    private boolean executed;

    protected AbstractCalculatorCommand(CalculatorViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public final void execute() {
        if (!executed) {
            before = viewModel.captureState();
            executed = true;
        }
        doExecute();
    }

    @Override
    public final void redo() {
        doExecute();
    }

    @Override
    public void undo() {
        if (before != null) {
            viewModel.restoreState(before);
        }
    }

    protected abstract void doExecute();
}
