package denisarseny.simplecalculator.command;

import denisarseny.simplecalculator.history.HistoryEntry;
import denisarseny.simplecalculator.viewmodel.CalculatorViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Очищает историю; отмена восстанавливает предыдущий список записей.
 */
public final class ClearHistoryCommand implements CalculatorCommand {

    private final CalculatorViewModel viewModel;
    private List<HistoryEntry> backup;

    public ClearHistoryCommand(CalculatorViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void execute() {
        backup = new ArrayList<>(viewModel.readHistory());
        viewModel.clearHistory();
    }

    @Override
    public void undo() {
        if (backup != null) {
            viewModel.restoreHistory(backup);
            backup = null;
        }
    }

    @Override
    public void redo() {
        execute();
    }
}
