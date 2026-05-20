package denisarseny.simplecalculator;

import denisarseny.simplecalculator.config.ConfigLoadResult;
import denisarseny.simplecalculator.config.ConfigLoader;
import denisarseny.simplecalculator.config.ThemeManager;
import denisarseny.simplecalculator.config.WindowSettings;
import denisarseny.simplecalculator.resources.AppResources;
import denisarseny.simplecalculator.view.HelloController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class Calculator extends Application {

    private ConfigLoadResult configLoadResult;

    @Override
    public void start(Stage stage) throws IOException {
        AppResources resources = AppResources.get();
        resources.initClickSound();

        configLoadResult = new ConfigLoader().load();
        if (configLoadResult.hasWarning()) {
            showConfigWarning(configLoadResult.userMessage());
        }

        var config = configLoadResult.config();
        resources.applyConfig(config);

        FXMLLoader fxmlLoader = new FXMLLoader(Calculator.class.getResource("hello-view.fxml"));
        WindowSettings window = config.getWindow();
        Scene scene = new Scene(fxmlLoader.load(), window.getWidth(), window.getHeight());
        ThemeManager.apply(scene, config);

        HelloController controller = fxmlLoader.getController();
        controller.bindStage(stage);
        controller.bindConfig(configLoadResult);
        controller.applyResources();
        controller.applyConfig(config);

        stage.setTitle(resources.getString("app.title"));
        stage.getIcons().addAll(resources.loadWindowIcons());
        stage.setMinWidth(window.getMinWidth());
        stage.setMinHeight(window.getMinHeight());
        stage.setScene(scene);
        stage.show();
    }

    private void showConfigWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Конфигурация");
        alert.setHeaderText("Проблема при загрузке config.json");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void stop() {
        AppResources.get().dispose();
    }
}
