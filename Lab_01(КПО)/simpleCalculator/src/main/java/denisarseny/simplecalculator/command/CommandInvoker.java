package denisarseny.simplecalculator.command;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Invoker: выполняет команды и управляет стеками отмены/повтора.
 */
public final class CommandInvoker {

    private final Deque<CalculatorCommand> undoStack = new ArrayDeque<>();
    private final Deque<CalculatorCommand> redoStack = new ArrayDeque<>();

    public void execute(CalculatorCommand command) {
        command.execute();
        if (commandSupportsUndo(command)) {
            undoStack.push(command);
            redoStack.clear();
        }
    }

    public void undo() {
        if (undoStack.isEmpty()) {
            return;
        }
        CalculatorCommand command = undoStack.pop();
        command.undo();
        redoStack.push(command);
    }

    public void redo() {
        if (redoStack.isEmpty()) {
            return;
        }
        CalculatorCommand command = redoStack.pop();
        command.redo();
        undoStack.push(command);
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    private static boolean commandSupportsUndo(CalculatorCommand command) {
        return !(command instanceof ShowHistoryCommand);
    }
}
