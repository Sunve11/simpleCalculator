package denisarseny.simplecalculatormvvm.view;

import denisarseny.simplecalculatormvvm.viewmodel.CalculatorViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/** View: привязка к ViewModel и события UI. */
public class MainViewController {

    @FXML
    private TextField display;

    private final CalculatorViewModel viewModel = new CalculatorViewModel();

    @FXML
    private void initialize() {
        display.textProperty().bind(viewModel.displayProperty());
    }

    @FXML
    private void handleNumber(javafx.event.ActionEvent event) {
        viewModel.appendDigit(((Button) event.getSource()).getText());
    }

    @FXML
    private void handleOperation(javafx.event.ActionEvent event) {
        viewModel.appendOperator(((Button) event.getSource()).getText());
    }

    @FXML
    private void handleEquals() {
        viewModel.equalsPressed();
    }

    @FXML
    private void handleClear() {
        viewModel.clear();
    }

    @FXML
    private void handleDot() {
        viewModel.dotPressed();
    }

    @FXML
    private void handleSign() {
        viewModel.signPressed();
    }

    @FXML
    private void handleOpenBracket() {
        viewModel.openBracketPressed();
    }

    @FXML
    private void handleCloseBracket() {
        viewModel.closeBracketPressed();
    }
}
