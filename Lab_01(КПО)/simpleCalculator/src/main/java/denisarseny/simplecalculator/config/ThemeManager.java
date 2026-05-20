package denisarseny.simplecalculator.config;

import denisarseny.simplecalculator.Calculator;
import javafx.scene.Scene;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.util.Objects;

/**
 * Применяет тему и доступность из конфигурации к сцене (без перекомпиляции — из JSON).
 */
public final class ThemeManager {

    private ThemeManager() {
    }

    public static void apply(Scene scene, AppConfig config) {
        var root = scene.getRoot();
        root.getStyleClass().removeIf(c -> c.startsWith("theme-") || c.startsWith("accessibility-"));

        String theme = config.getTheme().equalsIgnoreCase("light") ? "light" : "dark";
        root.getStyleClass().add("theme-" + theme);

        AccessibilitySettings a11y = config.getAccessibility();
        if (a11y.isHighContrast()) {
            root.getStyleClass().add("accessibility-high-contrast");
        }
        if (a11y.isLargeFonts()) {
            root.getStyleClass().add("accessibility-large-fonts");
        }
        if (a11y.isLargeButtons()) {
            root.getStyleClass().add("accessibility-large-buttons");
        }

        scene.getStylesheets().clear();
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        addStylesheet(scene, "calculator.css");
        addStylesheet(scene, "theme-" + theme + ".css");
        if (a11y.isHighContrast() || a11y.isLargeFonts() || a11y.isLargeButtons()) {
            addStylesheet(scene, "theme-accessibility.css");
        }
    }

    private static void addStylesheet(Scene scene, String fileName) {
        String url = Objects.requireNonNull(
                Calculator.class.getResource(fileName),
                "Stylesheet not found: " + fileName
        ).toExternalForm();
        scene.getStylesheets().add(url);
    }
}
