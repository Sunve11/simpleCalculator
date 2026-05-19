package denisarseny.simplecalculator.history;

import denisarseny.simplecalculator.Calculator;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.util.List;
import java.util.Objects;

public class TextAreaHistoryWindowFactory extends HistoryWindowFactory {
    @Override
    public Stage createWindow(List<HistoryEntry> entries) {
        ListView<String> listView = new ListView<>();
        listView.getStyleClass().add("history-list");
        listView.setItems(FXCollections.observableArrayList(formatLines(entries)));
        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                setWrapText(true);
            }
        });

        Label title = new Label("История вычислений");
        title.getStyleClass().add("history-title");

        VBox root = new VBox(12, title, listView);
        root.getStyleClass().add("history-root");
        VBox.setMargin(listView, new Insets(0));
        VBox.setVgrow(listView, javafx.scene.layout.Priority.ALWAYS);

        Stage stage = new Stage();
        stage.setTitle("История операций");
        Scene scene = new Scene(root, 480, 400);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        scene.getStylesheets().add(
                Objects.requireNonNull(Calculator.class.getResource("history.css")).toExternalForm()
        );
        stage.setScene(scene);
        return stage;
    }

    private List<String> formatLines(List<HistoryEntry> entries) {
        if (entries.isEmpty()) {
            return List.of("История пуста — выполните вычисление с «=»");
        }
        return entries.stream()
                .map(e -> e.getTimestamp() + "   " + e.getExpression() + "  =  " + e.getResult())
                .toList();
    }
}
