package denisarseny.simplecalculatormvvm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MvvmCalculatorApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(MvvmCalculatorApp.class.getResource("main-view.fxml"));
        Scene scene = new Scene(loader.load(), 300, 400);
        stage.setTitle("Калькулятор (MVVM)");
        stage.setScene(scene);
        stage.show();
    }
}
