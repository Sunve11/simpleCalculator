package denisarseny.simplecalculator.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Вариант 1: светлая / затемнённая тема + опции для слабовидящих.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessibilitySettings {

    private int variant = 1;
    private boolean highContrast;
    private boolean largeFonts;
    private boolean largeButtons;

    public int getVariant() {
        return variant;
    }

    public void setVariant(int variant) {
        this.variant = variant;
    }

    public boolean isHighContrast() {
        return highContrast;
    }

    public void setHighContrast(boolean highContrast) {
        this.highContrast = highContrast;
    }

    public boolean isLargeFonts() {
        return largeFonts;
    }

    public void setLargeFonts(boolean largeFonts) {
        this.largeFonts = largeFonts;
    }

    public boolean isLargeButtons() {
        return largeButtons;
    }

    public void setLargeButtons(boolean largeButtons) {
        this.largeButtons = largeButtons;
    }
}
