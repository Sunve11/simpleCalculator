package denisarseny.simplecalculator.view;

import denisarseny.simplecalculator.command.CalculatorCommandFactory;
import denisarseny.simplecalculator.command.CommandInvoker;
import denisarseny.simplecalculator.viewmodel.CalculatorViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * View: передаёт нажатия в {@link CommandInvoker} (паттерн Command).
 */
public class HelloController {

    @FXML
    private TextField display;

    private CalculatorViewModel viewModel;
    private CommandInvoker commandInvoker;
    private CalculatorCommandFactory commandFactory;

    @FXML
    private void initialize() {
        viewModel = new CalculatorViewModel(this::showHistoryError);
        commandInvoker = new CommandInvoker();
        commandFactory = new CalculatorCommandFactory(viewModel);
        display.textProperty().bind(viewModel.displayProperty());
    }

    @FXML
    private void handleNumber(javafx.event.ActionEvent event) {
        String digit = ((Button) event.getSource()).getText();
        commandInvoker.execute(commandFactory.appendDigit(digit));
    }

    @FXML
    private void handleOperation(javafx.event.ActionEvent event) {
        String operator = ((Button) event.getSource()).getText();
        commandInvoker.execute(commandFactory.appendOperator(operator));
    }

    @FXML
    private void handleEquals() {
        commandInvoker.execute(commandFactory.equals());
    }

    @FXML
    private void handleClear() {
        commandInvoker.execute(commandFactory.clearDisplay());
    }

    @FXML
    private void handleDot() {
        commandInvoker.execute(commandFactory.dot());
    }

    @FXML
    private void handleSign() {
        commandInvoker.execute(commandFactory.sign());
    }

    @FXML
    private void handleOpenBracket() {
        commandInvoker.execute(commandFactory.openBracket());
    }

    @FXML
    private void handleCloseBracket() {
        commandInvoker.execute(commandFactory.closeBracket());
    }

    @FXML
    private void handleShowHistory() {
        commandInvoker.execute(commandFactory.showHistory());
    }

    @FXML
    private void handleClearHistory() {
        commandInvoker.execute(commandFactory.clearHistory());
    }

    @FXML
    private void handleUndo() {
        commandInvoker.undo();
    }

    @FXML
    private void handleRedo() {
        commandInvoker.redo();
    }

    private void showHistoryError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
