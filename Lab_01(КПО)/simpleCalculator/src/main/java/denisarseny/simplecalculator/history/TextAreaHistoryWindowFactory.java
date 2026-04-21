package denisarseny.simplecalculator.history;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class TextAreaHistoryWindowFactory extends HistoryWindowFactory {
    @Override
    public Stage createWindow(List<HistoryEntry> entries) {
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setText(format(entries));

        VBox root = new VBox(textArea);
        Stage stage = new Stage();
        stage.setTitle("История операций");
        stage.setScene(new Scene(root, 420, 360));
        return stage;
    }

    private String format(List<HistoryEntry> entries) {
        if (entries.isEmpty()) {
            return "История пуста";
        }

        StringBuilder sb = new StringBuilder();
        for (HistoryEntry entry : entries) {
            sb.append(entry.getTimestamp())
                    .append(" | ")
                    .append(entry.getExpression())
                    .append(" = ")
                    .append(entry.getResult())
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }
}
