package denisarseny.simplecalculator;

import denisarseny.simplecalculator.resources.AppResources;
import denisarseny.simplecalculator.view.HelloController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.util.Objects;

public class Calculator extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        AppResources resources = AppResources.get();
        resources.initClickSound();

        Application.setUserAgentStylesheet(BootstrapFX.bootstrapFXStylesheet());

        FXMLLoader fxmlLoader = new FXMLLoader(Calculator.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 360, 620);
        scene.getStylesheets().add(
                Objects.requireNonNull(Calculator.class.getResource("calculator.css")).toExternalForm()
        );

        HelloController controller = fxmlLoader.getController();
        controller.bindStage(stage);
        controller.applyResources();

        stage.setTitle(resources.getString("app.title"));
        stage.getIcons().setAll(resources.loadWindowIcons());
        stage.setMinWidth(320);
        stage.setMinHeight(540);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        AppResources.get().dispose();
    }
}
