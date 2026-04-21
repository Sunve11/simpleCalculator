package denisarseny.simplecalculatormvvm.viewmodel;

import denisarseny.simplecalculatormvvm.model.ExpressionEvaluator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/** ViewModel: состояние и команды; не зависит от FXML и узлов сцены. */
public class CalculatorViewModel {

    private final StringProperty display = new SimpleStringProperty("");
    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();
    private boolean startNewExpression = true;

    public StringProperty displayProperty() {
        return display;
    }

    public void appendDigit(String digit) {
        if (startNewExpression) {
            display.set(digit);
            startNewExpression = false;
        } else {
            display.set(display.get() + digit);
        }
    }

    public void appendOperator(String operator) {
        String currentText = display.get();
        if (currentText.isEmpty() || "Ошибка".equals(currentText)) {
            return;
        }

        char lastChar = currentText.charAt(currentText.length() - 1);

        if (evaluator.isOperator(lastChar)) {
            display.set(currentText.substring(0, currentText.length() - 1) + operator);
            return;
        }

        if (lastChar == '.') {
            display.set(currentText + "0" + operator);
            return;
        }

        display.set(currentText + operator);
        startNewExpression = false;
    }

    public void equalsPressed() {
        String expression = display.get();
        try {
            double result = evaluator.evaluate(expression);
            display.set(evaluator.formatResult(result));
            startNewExpression = true;
        } catch (Exception e) {
            display.set("Ошибка");
            startNewExpression = true;
        }
    }

    public void clear() {
        display.set("");
        startNewExpression = true;
    }

    public void dotPressed() {
        String currentText = display.get();
        if (currentText.isEmpty() || "Ошибка".equals(currentText)) {
            display.set("0.");
            startNewExpression = false;
            return;
        }

        String lastNumber = evaluator.getLastNumber(currentText);
        if (!lastNumber.contains(".")) {
            display.set(currentText + ".");
        }
        startNewExpression = false;
    }

    public void signPressed() {
        String currentText = display.get();
        if (currentText.isEmpty() || "Ошибка".equals(currentText)) {
            display.set("-");
            startNewExpression = false;
            return;
        }

        String lastNumber = evaluator.getLastNumber(currentText);
        if (!lastNumber.isEmpty()) {
            String newNumber = lastNumber.startsWith("-") ? lastNumber.substring(1) : "-" + lastNumber;
            String withoutLast = currentText.substring(0, currentText.length() - lastNumber.length());
            display.set(withoutLast + newNumber);
        }
        startNewExpression = false;
    }

    public void openBracketPressed() {
        String currentText = display.get();
        if (currentText.isEmpty() || "Ошибка".equals(currentText)) {
            display.set("(");
            startNewExpression = false;
            return;
        }

        char lastChar = currentText.charAt(currentText.length() - 1);
        if (Character.isDigit(lastChar) || lastChar == ')') {
            display.set(currentText + "*(");
        } else {
            display.set(currentText + "(");
        }
        startNewExpression = false;
    }

    public void closeBracketPressed() {
        String currentText = display.get();
        if (currentText.isEmpty() || "Ошибка".equals(currentText)) {
            return;
        }

        int openCount = 0;
        int closeCount = 0;
        for (char c : currentText.toCharArray()) {
            if (c == '(') {
                openCount++;
            }
            if (c == ')') {
                closeCount++;
            }
        }

        if (openCount > closeCount) {
            display.set(currentText + ")");
            startNewExpression = false;
        }
    }
}
