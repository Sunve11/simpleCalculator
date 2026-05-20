package denisarseny.simplecalculator.resources;

import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Иконки Ikonli (шрифт Font Awesome в JAR) — статический ресурс для меню и кнопок.
 */
public final class ResourceIcons {

    private ResourceIcons() {
    }

    public static FontIcon icon(FontAwesomeSolid glyph, int size) {
        FontIcon icon = new FontIcon(glyph);
        icon.setIconSize(size);
        return icon;
    }

    public static void setGraphic(Button button, FontAwesomeSolid glyph, int size) {
        button.setGraphic(icon(glyph, size));
    }

    public static void setGraphic(MenuItem item, FontAwesomeSolid glyph, int size) {
        item.setGraphic(icon(glyph, size));
    }
}
