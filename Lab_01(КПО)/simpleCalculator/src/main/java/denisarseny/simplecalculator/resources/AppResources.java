package denisarseny.simplecalculator.resources;

import denisarseny.simplecalculator.config.AppConfig;
import denisarseny.simplecalculator.config.FontSettings;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.text.Font;

import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Централизованная загрузка ресурсов приложения.
 * <p>
 * <b>Статическая</b> загрузка: {@link ResourceBundle}, константные пути, {@link #getStaticImage(String)} —
 * ресурсы кэшируются JVM/класслоадером, подходят для иконок, строк, CSS.
 * <p>
 * <b>Динамическая</b> загрузка: {@link #loadImage(String)}, {@link #loadMedia(String)}, {@link #loadCursor(String)} —
 * создаёт объекты по требованию; для {@link Media} и курсоров нужен {@link #dispose()} (аналог освобождения в WinAPI).
 */
public final class AppResources {

    private static final String BUNDLE = "denisarseny.simplecalculator.messages";
    private static final AppResources INSTANCE = new AppResources();

    private final ResourceBundle bundle;
    private final List<Media> dynamicMedia = new ArrayList<>();
    private final List<Image> dynamicImages = new ArrayList<>();

    private Font appFont;
    private javafx.scene.media.AudioClip clickSound;
    private boolean soundEnabled = true;
    private boolean useResourceFont = true;
    private String fontResourcePath = "/denisarseny/simplecalculator/fonts/Roboto-Regular.ttf";
    private String fallbackFontFamily = "Segoe UI";

    private AppResources() {
        bundle = ResourceBundle.getBundle(BUNDLE, Locale.getDefault());
    }

    public static AppResources get() {
        return INSTANCE;
    }

    /** Статическая строка из messages.properties */
    public String getString(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    public String format(String key, Object... args) {
        return MessageFormat.format(getString(key), args);
    }

    /** Применение параметров из config.json (шрифт, звук). */
    public void applyConfig(AppConfig config) {
        setSoundEnabled(config.getSound().isEnabled());
        FontSettings font = config.getFont();
        useResourceFont = font.isUseResourceFont();
        fallbackFontFamily = font.getFamily() != null ? font.getFamily() : "Segoe UI";
        String path = font.getResourcePath() != null ? font.getResourcePath() : "fonts/Roboto-Regular.ttf";
        fontResourcePath = path.startsWith("/") ? path : "/denisarseny/simplecalculator/" + path;
        appFont = null;
        if (!useResourceFont) {
            appFont = Font.font(fallbackFontFamily, 12);
        }
    }

    /** Статическая картинка (кэш JavaFX Image по URL) */
    public Image getStaticImage(String resourcePath) {
        URL url = resourceUrl(resourcePath);
        if (url == null) {
            throw new IllegalStateException(format("resource.load.error", resourcePath));
        }
        return new Image(url.toExternalForm(), false);
    }

    public List<Image> loadWindowIcons() {
        return List.of(
                getStaticImage("/denisarseny/simplecalculator/icons/app-icon-16.png"),
                getStaticImage("/denisarseny/simplecalculator/icons/app-icon-32.png"),
                getStaticImage("/denisarseny/simplecalculator/icons/app-icon-64.png")
        );
    }

    /** Динамическая загрузка изображения (учёт в dispose) */
    public Image loadImage(String resourcePath) {
        try (InputStream in = openStream(resourcePath)) {
            Image image = new Image(Objects.requireNonNull(in));
            dynamicImages.add(image);
            return image;
        } catch (Exception e) {
            throw new IllegalStateException(format("resource.load.error", resourcePath), e);
        }
    }

    /** Динамическая загрузка шрифта из resources/fonts */
    public Font loadAppFont(double size) {
        if (appFont == null) {
            appFont = loadRobotoFromResources();
            if (appFont == null) {
                appFont = Font.font(fallbackFontFamily, 12);
            }
        }
        return Font.font(appFont.getFamily(), size);
    }

    private Font loadRobotoFromResources() {
        try (InputStream in = openStream(fontResourcePath)) {
            if (in == null) {
                return null;
            }
            return Font.loadFont(in, 12);
        } catch (Exception e) {
            return null;
        }
    }

    /** Статический звук (AudioClip держит поток; освобождается в dispose) */
    public void initClickSound() {
        if (clickSound == null) {
            URL url = resourceUrl("/denisarseny/simplecalculator/media/click.wav");
            if (url != null) {
                clickSound = new javafx.scene.media.AudioClip(url.toExternalForm());
            }
        }
    }

    public void playClickSound() {
        if (soundEnabled && clickSound != null) {
            clickSound.play();
        }
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    /** Динамическое видео (Media — освобождать через dispose) */
    public Media loadMedia(String resourcePath) {
        URL url = resourceUrl(resourcePath);
        if (url == null) {
            return null;
        }
        Media media = new Media(url.toExternalForm());
        dynamicMedia.add(media);
        return media;
    }

    public Media tryLoadIntroVideo() {
        URL url = resourceUrl("/denisarseny/simplecalculator/media/intro.mp4");
        if (url == null) {
            return null;
        }
        Media media = new Media(url.toExternalForm());
        dynamicMedia.add(media);
        return media;
    }

    /** Динамический курсор из PNG */
    public Cursor loadCursor(String resourcePath, double hotspotX, double hotspotY) {
        Image image = loadImage(resourcePath);
        return new ImageCursor(image, hotspotX, hotspotY);
    }

    public URL resourceUrl(String path) {
        String normalized = path.startsWith("/") ? path : "/" + path;
        return AppResources.class.getResource(normalized);
    }

    public InputStream openStream(String path) {
        String normalized = path.startsWith("/") ? path : "/" + path;
        return AppResources.class.getResourceAsStream(normalized);
    }

    /**
     * Освобождение динамических ресурсов (для Media и сброса ссылок).
     * В JavaFX Image/AudioClip сборщик мусора завершит работу после обнуления ссылок.
     */
    public void dispose() {
        dynamicMedia.clear();
        dynamicImages.clear();
        if (clickSound != null) {
            clickSound.stop();
            clickSound = null;
        }
        appFont = null;
    }

}
