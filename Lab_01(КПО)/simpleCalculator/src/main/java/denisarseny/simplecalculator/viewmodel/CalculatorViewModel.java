package denisarseny.simplecalculator.viewmodel;

import denisarseny.simplecalculator.history.HistoryEntry;
import denisarseny.simplecalculator.history.HistoryRepository;
import denisarseny.simplecalculator.history.HistoryWindowFactory;
import denisarseny.simplecalculator.history.JsonHistoryRepository;
import denisarseny.simplecalculator.history.TextAreaHistoryWindowFactory;
import denisarseny.simplecalculator.model.ExpressionEvaluator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * ViewModel: состояние экрана; действия вызываются через паттерн Command.
 * Factory Method остаётся в {@link ExpressionEvaluator}.
 */
public class CalculatorViewModel {

    private final StringProperty display = new SimpleStringProperty("");
    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();
    private final HistoryRepository historyRepository = new JsonHistoryRepository();
    private final HistoryWindowFactory historyWindowFactory = new TextAreaHistoryWindowFactory();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Consumer<String> historyErrorSink;
    private boolean startNewExpression = true;

    public CalculatorViewModel(Consumer<String> historyErrorSink) {
        this.historyErrorSink = historyErrorSink;
    }

    public StringProperty displayProperty() {
        return display;
    }

    public CalculatorSnapshot captureState() {
        return new CalculatorSnapshot(display.get(), startNewExpression);
    }

    public void restoreState(CalculatorSnapshot snapshot) {
        display.set(snapshot.display());
        startNewExpression = snapshot.startNewExpression();
    }

    public String getDisplayValue() {
        return display.get();
    }

    public void removeLastHistoryEntry() {
        historyRepository.removeLast();
    }

    public List<HistoryEntry> readHistory() {
        return new ArrayList<>(historyRepository.readAll());
    }

    public void restoreHistory(List<HistoryEntry> entries) {
        historyRepository.replaceAll(entries);
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
            String resultText = evaluator.formatResult(result);
            display.set(resultText);
            historyRepository.append(
                    new HistoryEntry(
                            LocalDateTime.now().format(dateTimeFormatter),
                            expression,
                            resultText
                    )
            );
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

    public void showHistory() {
        try {
            historyWindowFactory.createWindow(historyRepository.readAll()).show();
        } catch (Exception e) {
            historyErrorSink.accept("Не удалось открыть историю");
        }
    }

    public void clearHistory() {
        try {
            historyRepository.clear();
        } catch (Exception e) {
            historyErrorSink.accept("Не удалось очистить историю");
        }
    }
}
