package denisarseny.simplecalculator.command;

import denisarseny.simplecalculator.viewmodel.CalculatorViewModel;

public final class EqualsCommand extends AbstractCalculatorCommand {

    private boolean historyEntryAdded;

    public EqualsCommand(CalculatorViewModel viewModel) {
        super(viewModel);
    }

    @Override
    protected void doExecute() {
        String expressionBefore = viewModel.getDisplayValue();
        viewModel.equalsPressed();
        String after = viewModel.getDisplayValue();
        historyEntryAdded = !expressionBefore.isEmpty()
                && !expressionBefore.equals(after)
                && !"Ошибка".equals(after);
    }

    @Override
    public void undo() {
        super.undo();
        if (historyEntryAdded) {
            viewModel.removeLastHistoryEntry();
            historyEntryAdded = false;
        }
    }
}
