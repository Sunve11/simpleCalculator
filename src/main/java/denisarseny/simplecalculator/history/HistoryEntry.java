package denisarseny.simplecalculator.history;

public class HistoryEntry {
    private final String timestamp;
    private final String expression;
    private final String result;

    public HistoryEntry(String timestamp, String expression, String result) {
        this.timestamp = timestamp;
        this.expression = expression;
        this.result = result;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getExpression() {
        return expression;
    }

    public String getResult() {
        return result;
    }
}
