package denisarseny.simplecalculator.config;

/**
 * Ошибка чтения или разбора конфигурационного файла.
 */
public class ConfigLoadException extends Exception {

    public ConfigLoadException(String message) {
        super(message);
    }

    public ConfigLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
