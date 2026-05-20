package denisarseny.simplecalculator.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FontSettings {

    private String family = "Segoe UI";
    private boolean useResourceFont = true;
    private String resourcePath = "fonts/Roboto-Regular.ttf";
    private double displaySize = 28;
    private double titleSize = 13;
    private double buttonSize = 20;

    public static FontSettings defaults() {
        return new FontSettings();
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public boolean isUseResourceFont() {
        return useResourceFont;
    }

    public void setUseResourceFont(boolean useResourceFont) {
        this.useResourceFont = useResourceFont;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public double getDisplaySize() {
        return displaySize;
    }

    public void setDisplaySize(double displaySize) {
        this.displaySize = displaySize;
    }

    public double getTitleSize() {
        return titleSize;
    }

    public void setTitleSize(double titleSize) {
        this.titleSize = titleSize;
    }

    public double getButtonSize() {
        return buttonSize;
    }

    public void setButtonSize(double buttonSize) {
        this.buttonSize = buttonSize;
    }
}
