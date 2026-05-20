package denisarseny.simplecalculator.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Модель конфигурации приложения (JSON).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppConfig {

    private WindowSettings window = WindowSettings.defaults();
    private String theme = "dark";
    private AccessibilitySettings accessibility = new AccessibilitySettings();
    private FontSettings font = FontSettings.defaults();
    private SoundSettings sound = new SoundSettings();
    private DatabaseSettings database = new DatabaseSettings();

    public WindowSettings getWindow() {
        return window;
    }

    public void setWindow(WindowSettings window) {
        this.window = window != null ? window : WindowSettings.defaults();
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public AccessibilitySettings getAccessibility() {
        return accessibility;
    }

    public void setAccessibility(AccessibilitySettings accessibility) {
        this.accessibility = accessibility != null ? accessibility : new AccessibilitySettings();
    }

    public FontSettings getFont() {
        return font;
    }

    public void setFont(FontSettings font) {
        this.font = font != null ? font : FontSettings.defaults();
    }

    public SoundSettings getSound() {
        return sound;
    }

    public void setSound(SoundSettings sound) {
        this.sound = sound != null ? sound : new SoundSettings();
    }

    public DatabaseSettings getDatabase() {
        return database;
    }

    public void setDatabase(DatabaseSettings database) {
        this.database = database != null ? database : new DatabaseSettings();
    }

    public static AppConfig defaults() {
        return new AppConfig();
    }
}
