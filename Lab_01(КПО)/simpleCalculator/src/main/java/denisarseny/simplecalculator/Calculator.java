package denisarseny.simplecalculator;

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
        Application.setUserAgentStylesheet(BootstrapFX.bootstrapFXStylesheet());

        FXMLLoader fxmlLoader = new FXMLLoader(Calculator.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 340, 580);
        scene.getStylesheets().add(
                Objects.requireNonNull(Calculator.class.getResource("calculator.css")).toExternalForm()
        );

        stage.setTitle("Simple Calculator");
        stage.setMinWidth(320);
        stage.setMinHeight(520);
        stage.setScene(scene);
        stage.show();
    }
}
