package denisarseny.simplecalculator;

import denisarseny.simplecalculator.history.HistoryEntry;
import denisarseny.simplecalculator.history.HistoryRepository;
import denisarseny.simplecalculator.history.HistoryWindowFactory;
import denisarseny.simplecalculator.history.JsonHistoryRepository;
import denisarseny.simplecalculator.history.TextAreaHistoryWindowFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Stack;
import denisarseny.simplecalculator.factory.OperationFactoryResolver;

public class HelloController {

    @FXML
    private TextField display;

    @FXML
    private Button button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    @FXML
    private Button buttonPlus, buttonMinus, buttonMultiply, buttonDivide, buttonEquals, buttonClear;
    @FXML
    private Button buttonDot, buttonSign, buttonOpenBracket, buttonCloseBracket;
    @FXML
    private Button buttonShowHistory, buttonClearHistory;

    private boolean startNewExpression = true; // флаг для начала нового выражения после равно
    private final HistoryRepository historyRepository = new JsonHistoryRepository();
    private final HistoryWindowFactory historyWindowFactory = new TextAreaHistoryWindowFactory();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML
    private void handleNumber(javafx.event.ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String numberText = clickedButton.getText();

        if (startNewExpression) {
            display.setText(numberText);
            startNewExpression = false;
        } else {
            display.setText(display.getText() + numberText);
        }
    }

    @FXML
    private void handleOperation(javafx.event.ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String operator = clickedButton.getText();
        String currentText = display.getText();

        if (currentText.isEmpty() || currentText.equals("Ошибка")) {
            return;
        }

        // Проверяем последний символ
        char lastChar = currentText.charAt(currentText.length() - 1);

        // Если последний символ - оператор, заменяем его
        if (isOperator(lastChar)) {
            String newText = currentText.substring(0, currentText.length() - 1) + operator;
            display.setText(newText);
            return;
        }

        // Если последний символ - точка, добавляем 0 перед оператором
        if (lastChar == '.') {
            display.setText(currentText + "0" + operator);
            return;
        }

        // Нормальное добавление оператора
        display.setText(currentText + operator);
        startNewExpression = false;
    }

    @FXML
    private void handleEquals() {
        String expression = display.getText();

        try {
            double result = evaluateExpression(expression);
            String resultText = formatResult(result);
            display.setText(resultText);
            historyRepository.append(
                    new HistoryEntry(
                            LocalDateTime.now().format(dateTimeFormatter),
                            expression,
                            resultText
                    )
            );

            startNewExpression = true;
        } catch (Exception e) {
            display.setText("Ошибка");
            startNewExpression = true;
        }
    }

    @FXML
    private void handleClear() {
        display.clear();
        startNewExpression = true;
    }

    @FXML
    private void handleDot() {
        String currentText = display.getText();

        if (currentText.isEmpty() || currentText.equals("Ошибка")) {
            display.setText("0.");
            startNewExpression = false;
            return;
        }

        // Проверяем последнее число на наличие точки
        String lastNumber = getLastNumber(currentText);
        if (!lastNumber.contains(".")) {
            display.setText(currentText + ".");
        }
        startNewExpression = false;
    }

    @FXML
    private void handleSign() {
        String currentText = display.getText();

        if (currentText.isEmpty() || currentText.equals("Ошибка")) {
            display.setText("-");
            startNewExpression = false;
            return;
        }

        // Меняем знак у последнего числа
        String lastNumber = getLastNumber(currentText);
        if (!lastNumber.isEmpty()) {
            String newNumber;
            if (lastNumber.startsWith("-")) {
                newNumber = lastNumber.substring(1);
            } else {
                newNumber = "-" + lastNumber;
            }
            String expressionWithoutLastNumber = currentText.substring(0, currentText.length() - lastNumber.length());
            display.setText(expressionWithoutLastNumber + newNumber);
        }
        startNewExpression = false;
    }

    @FXML
    private void handleOpenBracket() {
        String currentText = display.getText();

        if (currentText.isEmpty() || currentText.equals("Ошибка")) {
            display.setText("(");
            startNewExpression = false;
            return;
        }

        char lastChar = currentText.charAt(currentText.length() - 1);
        // Если перед скобкой цифра или закрывающая скобка, добавляем умножение
        if (Character.isDigit(lastChar) || lastChar == ')') {
            display.setText(currentText + "*(");
        } else {
            display.setText(currentText + "(");
        }
        startNewExpression = false;
    }

    @FXML
    private void handleCloseBracket() {
        String currentText = display.getText();

        if (currentText.isEmpty() || currentText.equals("Ошибка")) {
            return;
        }

        // Подсчитываем скобки
        int openCount = 0;
        int closeCount = 0;
        for (char c : currentText.toCharArray()) {
            if (c == '(') openCount++;
            if (c == ')') closeCount++;
        }

        if (openCount > closeCount) {
            display.setText(currentText + ")");
            startNewExpression = false;
        }
    }

    @FXML
    private void handleShowHistory() {
        try {
            historyWindowFactory.createWindow(historyRepository.readAll()).show();
        } catch (Exception e) {
            showHistoryError("Не удалось открыть историю");
        }
    }

    @FXML
    private void handleClearHistory() {
        try {
            historyRepository.clear();
        } catch (Exception e) {
            showHistoryError("Не удалось очистить историю");
        }
    }

    // Метод для вычисления выражения
    private double evaluateExpression(String expression) {
        expression = expression.replaceAll("\\s+", "");

        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (Character.isDigit(ch) || (ch == '-' && (i == 0 || expression.charAt(i-1) == '('))) {
                StringBuilder numBuilder = new StringBuilder();
                numBuilder.append(ch);
                i++;

                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    numBuilder.append(expression.charAt(i));
                    i++;
                }
                i--;

                numbers.push(Double.parseDouble(numBuilder.toString()));
            }
            else if (ch == '(') {
                operators.push(ch);
            }
            else if (ch == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.pop();
            }
            else if (isOperator(ch)) {
                while (!operators.isEmpty() && hasPrecedence(ch, operators.peek())) {
                    numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(ch);
            }
        }

        while (!operators.isEmpty()) {
            numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    private boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        return !((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'));
    }

    private double applyOperation(char operator, double b, double a) {
        return OperationFactoryResolver
                .getFactory(operator)
                .createOperation()
                .execute(a, b);
    }

    private String formatResult(double result) {
        if (result == (int) result) {
            return String.valueOf((int) result);
        }
        return String.format("%.10f", result).replaceAll("0*$", "").replaceAll("\\.$", "");
    }

    private void showHistoryError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String getLastNumber(String expression) {
        StringBuilder lastNumber = new StringBuilder();
        boolean foundNumber = false;

        for (int i = expression.length() - 1; i >= 0; i--) {
            char c = expression.charAt(i);

            if (c == ')' || c == '(') {
                break;
            }

            if (Character.isDigit(c) || c == '.') {
                lastNumber.insert(0, c);
                foundNumber = true;
            }
            else if (c == '-' && (i == 0 || !Character.isDigit(expression.charAt(i-1)) && expression.charAt(i-1) != ')')) {
                if (foundNumber) {
                    break;
                }
                lastNumber.insert(0, c);
                break;
            }
            else if (isOperator(c) || c == ' ') {
                break;
            }
            else {
                break;
            }
        }

        return lastNumber.toString();
    }
}