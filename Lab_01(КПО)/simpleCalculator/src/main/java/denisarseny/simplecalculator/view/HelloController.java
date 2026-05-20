package denisarseny.simplecalculator.view;

import denisarseny.simplecalculator.command.CalculatorCommandFactory;
import denisarseny.simplecalculator.command.CommandInvoker;
import denisarseny.simplecalculator.resources.AppResources;
import denisarseny.simplecalculator.resources.ResourceIcons;
import denisarseny.simplecalculator.viewmodel.CalculatorViewModel;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

/**
 * View: Command + загрузка ресурсов (меню, иконки, шрифты, звук, курсоры, анимация, видео).
 */
public class HelloController {

    @FXML
    private MenuBar menuBar;
    @FXML
    private Label titleLabel;
    @FXML
    private ImageView logoView;
    @FXML
    private TextField display;
    @FXML
    private GridPane keypad;
    @FXML
    private Button buttonUndo;
    @FXML
    private Button buttonRedo;
    @FXML
    private Button buttonShowHistory;
    @FXML
    private Button buttonClearHistory;

    private Stage stage;
    private CalculatorViewModel viewModel;
    private CommandInvoker commandInvoker;
    private CalculatorCommandFactory commandFactory;
    private CheckMenuItem soundMenuItem;
    private RotateTransition logoAnimation;
    private MediaPlayer introPlayer;

    @FXML
    private void initialize() {
        viewModel = new CalculatorViewModel(this::showHistoryError);
        commandInvoker = new CommandInvoker();
        commandFactory = new CalculatorCommandFactory(viewModel);
        display.textProperty().bind(viewModel.displayProperty());
        buildMenu();
    }

    public void bindStage(Stage stage) {
        this.stage = stage;
    }

    /** Подключение ресурсов после загрузки FXML */
    public void applyResources() {
        AppResources res = AppResources.get();

        titleLabel.setText(res.getString("app.subtitle"));
        titleLabel.setFont(res.loadAppFont(13));

        logoView.setImage(res.getStaticImage("/denisarseny/simplecalculator/images/logo.png"));
        startLogoAnimation();

        display.setFont(res.loadAppFont(28));

        ResourceIcons.setGraphic(buttonUndo, FontAwesomeSolid.UNDO, 14);
        ResourceIcons.setGraphic(buttonRedo, FontAwesomeSolid.REDO, 14);
        ResourceIcons.setGraphic(buttonShowHistory, FontAwesomeSolid.CLOCK, 14);
        ResourceIcons.setGraphic(buttonClearHistory, FontAwesomeSolid.TRASH, 14);

        Cursor handCursor = Cursor.HAND;
        keypad.setCursor(handCursor);
        for (var node : keypad.getChildren()) {
            if (node instanceof Button button) {
                button.setCursor(handCursor);
            }
        }
        logoView.setCursor(res.loadCursor("/denisarseny/simplecalculator/cursors/pointer.png", 16, 16));
    }

    private void buildMenu() {
        AppResources res = AppResources.get();

        Menu fileMenu = new Menu(res.getString("menu.file"));
        MenuItem exitItem = new MenuItem(res.getString("menu.exit"));
        ResourceIcons.setGraphic(exitItem, FontAwesomeSolid.SIGN_OUT_ALT, 14);
        exitItem.setOnAction(e -> Platform.exit());
        fileMenu.getItems().add(exitItem);

        Menu viewMenu = new Menu(res.getString("menu.view"));
        soundMenuItem = new CheckMenuItem(res.getString("menu.sound.on"));
        soundMenuItem.setSelected(true);
        soundMenuItem.selectedProperty().addListener((obs, oldVal, on) -> {
            res.setSoundEnabled(on);
            soundMenuItem.setText(res.getString(on ? "menu.sound.on" : "menu.sound.off"));
        });
        ResourceIcons.setGraphic(soundMenuItem, FontAwesomeSolid.VOLUME_UP, 14);
        viewMenu.getItems().add(soundMenuItem);

        Menu helpMenu = new Menu(res.getString("menu.help"));
        MenuItem aboutItem = new MenuItem(res.getString("menu.about"));
        ResourceIcons.setGraphic(aboutItem, FontAwesomeSolid.INFO_CIRCLE, 14);
        aboutItem.setOnAction(e -> new AboutDialog(stage).show());

        MenuItem videoItem = new MenuItem(res.getString("menu.video"));
        ResourceIcons.setGraphic(videoItem, FontAwesomeSolid.PLAY_CIRCLE, 14);
        videoItem.setOnAction(e -> openIntroVideo());

        helpMenu.getItems().addAll(aboutItem, videoItem);

        menuBar.getMenus().setAll(fileMenu, viewMenu, helpMenu);
        menuBar.setPadding(new Insets(0, 0, 4, 0));
    }

    private void startLogoAnimation() {
        logoAnimation = new RotateTransition(Duration.seconds(4), logoView);
        logoAnimation.setFromAngle(0);
        logoAnimation.setToAngle(360);
        logoAnimation.setCycleCount(RotateTransition.INDEFINITE);
        logoAnimation.setAutoReverse(false);
        logoAnimation.play();
    }

    private void openIntroVideo() {
        AppResources res = AppResources.get();
        Media media = res.tryLoadIntroVideo();
        if (media == null) {
            showHistoryError(res.getString("video.missing"));
            return;
        }
        stopIntroPlayer();
        introPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(introPlayer);
        mediaView.setFitWidth(400);
        mediaView.setPreserveRatio(true);

        Stage videoStage = new Stage();
        videoStage.initOwner(stage);
        videoStage.setTitle(res.getString("menu.video"));
        videoStage.getIcons().addAll(res.loadWindowIcons());
        videoStage.setScene(new javafx.scene.Scene(new javafx.scene.layout.StackPane(mediaView), 420, 260));
        videoStage.setOnHidden(e -> stopIntroPlayer());
        introPlayer.setOnEndOfMedia(introPlayer::stop);
        introPlayer.play();
        videoStage.show();
    }

    private void stopIntroPlayer() {
        if (introPlayer != null) {
            introPlayer.stop();
            introPlayer.dispose();
            introPlayer = null;
        }
    }

    private void onButtonAction(Runnable action) {
        AppResources.get().playClickSound();
        action.run();
    }

    @FXML
    private void handleNumber(javafx.event.ActionEvent event) {
        String digit = ((Button) event.getSource()).getText();
        onButtonAction(() -> commandInvoker.execute(commandFactory.appendDigit(digit)));
    }

    @FXML
    private void handleOperation(javafx.event.ActionEvent event) {
        String operator = ((Button) event.getSource()).getText();
        onButtonAction(() -> commandInvoker.execute(commandFactory.appendOperator(operator)));
    }

    @FXML
    private void handleEquals() {
        onButtonAction(() -> commandInvoker.execute(commandFactory.equals()));
    }

    @FXML
    private void handleClear() {
        onButtonAction(() -> commandInvoker.execute(commandFactory.clearDisplay()));
    }

    @FXML
    private void handleDot() {
        onButtonAction(() -> commandInvoker.execute(commandFactory.dot()));
    }

    @FXML
    private void handleSign() {
        onButtonAction(() -> commandInvoker.execute(commandFactory.sign()));
    }

    @FXML
    private void handleOpenBracket() {
        onButtonAction(() -> commandInvoker.execute(commandFactory.openBracket()));
    }

    @FXML
    private void handleCloseBracket() {
        onButtonAction(() -> commandInvoker.execute(commandFactory.closeBracket()));
    }

    @FXML
    private void handleShowHistory() {
        onButtonAction(() -> commandInvoker.execute(commandFactory.showHistory()));
    }

    @FXML
    private void handleClearHistory() {
        onButtonAction(() -> commandInvoker.execute(commandFactory.clearHistory()));
    }

    @FXML
    private void handleUndo() {
        onButtonAction(commandInvoker::undo);
    }

    @FXML
    private void handleRedo() {
        onButtonAction(commandInvoker::redo);
    }

    private void showHistoryError(String message) {
        AppResources res = AppResources.get();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(res.getString("about.title"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
