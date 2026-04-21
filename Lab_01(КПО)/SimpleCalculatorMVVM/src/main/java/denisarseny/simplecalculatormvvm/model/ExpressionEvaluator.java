package denisarseny.simplecalculatormvvm.model;

import java.util.Stack;

/** Модель: вычисление выражения без UI. */
public final class ExpressionEvaluator {

    public double evaluate(String expression) {
        expression = expression.replaceAll("\\s+", "");

        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (Character.isDigit(ch) || (ch == '-' && (i == 0 || expression.charAt(i - 1) == '('))) {
                StringBuilder numBuilder = new StringBuilder();
                numBuilder.append(ch);
                i++;

                while (i < expression.length()
                        && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    numBuilder.append(expression.charAt(i));
                    i++;
                }
                i--;

                numbers.push(Double.parseDouble(numBuilder.toString()));
            } else if (ch == '(') {
                operators.push(ch);
            } else if (ch == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.pop();
            } else if (isOperator(ch)) {
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

    public String formatResult(double result) {
        if (result == (int) result) {
            return String.valueOf((int) result);
        }
        return String.format("%.10f", result).replaceAll("0*$", "").replaceAll("\\.$", "");
    }

    public String getLastNumber(String expression) {
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
            } else if (c == '-' && (i == 0 || !Character.isDigit(expression.charAt(i - 1)) && expression.charAt(i - 1) != ')')) {
                if (foundNumber) {
                    break;
                }
                lastNumber.insert(0, c);
                break;
            } else if (isOperator(c) || c == ' ') {
                break;
            } else {
                break;
            }
        }

        return lastNumber.toString();
    }

    public boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        return !((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'));
    }

    private double applyOperation(char operator, double b, double a) {
        return switch (operator) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> {
                if (b == 0) {
                    throw new ArithmeticException("Деление на ноль");
                }
                yield a / b;
            }
            default -> throw new IllegalArgumentException("Неизвестный оператор: " + operator);
        };
    }
}
