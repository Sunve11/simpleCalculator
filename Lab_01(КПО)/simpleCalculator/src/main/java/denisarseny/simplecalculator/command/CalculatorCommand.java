package denisarseny.simplecalculator.command;

/**
 * Паттерн Command: инкапсулирует действие пользователя с возможностью отмены.
 */
public interface CalculatorCommand {

    void execute();

    void undo();

    void redo();
}
