package denisarseny.simplecalculator.view;

import denisarseny.simplecalculator.resources.AppResources;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Диалоговое окно из ресурсов: строки, изображение, шрифт, опционально видео.
 */
public final class AboutDialog {

    private final Stage owner;
    private MediaPlayer mediaPlayer;

    public AboutDialog(Stage owner) {
        this.owner = owner;
    }

    public void show() {
        AppResources res = AppResources.get();

        Label header = new Label(res.getString("about.header"));
        header.setFont(res.loadAppFont(20));

        Label version = new Label(res.getString("about.version"));
        version.setFont(res.loadAppFont(13));

        Label patterns = new Label(res.getString("about.patterns"));
        patterns.setWrapText(true);
        patterns.setFont(res.loadAppFont(12));

        ImageView logo = new ImageView(res.getStaticImage("/denisarseny/simplecalculator/images/logo.png"));
        logo.setFitHeight(48);
        logo.setFitWidth(48);
        logo.setPreserveRatio(true);

        TextField authorField = new TextField();
        authorField.setPromptText("Автор (ввод в диалоге)");
        authorField.setFont(res.loadAppFont(12));

        VBox videoBox = new VBox(8);
        videoBox.setAlignment(Pos.CENTER);
        Media media = res.tryLoadIntroVideo();
        if (media != null) {
            mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);
            mediaView.setFitWidth(280);
            mediaView.setPreserveRatio(true);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
            videoBox.getChildren().add(mediaView);
        } else {
            Label noVideo = new Label(res.getString("video.missing"));
            noVideo.setWrapText(true);
            noVideo.setFont(res.loadAppFont(11));
            videoBox.getChildren().add(noVideo);
        }

        Button okButton = new Button(res.getString("dialog.ok"));
        okButton.setDefaultButton(true);
        okButton.setOnAction(e -> close(stageOf(okButton)));

        Button cancelButton = new Button(res.getString("dialog.cancel"));
        cancelButton.setCancelButton(true);
        cancelButton.setOnAction(e -> close(stageOf(cancelButton)));

        HBox buttons = new HBox(10, okButton, cancelButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        buttons.setPadding(new Insets(8, 0, 0, 0));

        VBox center = new VBox(10, header, version, patterns, authorField, videoBox, buttons);
        center.setPadding(new Insets(16));
        center.setAlignment(Pos.CENTER_LEFT);

        BorderPane root = new BorderPane();
        root.setLeft(logo);
        BorderPane.setMargin(logo, new Insets(16, 12, 16, 16));
        root.setCenter(center);

        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(res.getString("about.title"));
        dialog.getIcons().addAll(res.loadWindowIcons());
        dialog.setScene(new Scene(root, 420, 380));
        dialog.setOnHidden(e -> stopMedia());
        dialog.showAndWait();
    }

    private static Stage stageOf(Button button) {
        return (Stage) button.getScene().getWindow();
    }

    private void close(Stage dialog) {
        stopMedia();
        dialog.close();
    }

    private void stopMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }
}
