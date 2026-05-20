package denisarseny.simplecalculator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import denisarseny.simplecalculator.Calculator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Загрузка config.json: сначала пользовательский файл, иначе копия шаблона из JAR.
 */
public final class ConfigLoader {

    private static final String CONFIG_DIR = ".simpleCalculator";
    private static final String CONFIG_FILE = "config.json";
    private static final String DEFAULT_RESOURCE = "/denisarseny/simplecalculator/config/app-config.json";

    private final ObjectMapper mapper = new ObjectMapper();

    public ConfigLoadResult load() {
        Path userConfig = Path.of(System.getProperty("user.home"), CONFIG_DIR, CONFIG_FILE);

        try {
            if (!Files.exists(userConfig)) {
                copyDefaultConfig(userConfig);
            }
            AppConfig config = readFile(userConfig);
            validate(config);
            return new ConfigLoadResult(config, userConfig, false, null);
        } catch (ConfigLoadException e) {
            return fallback(userConfig, e.getMessage());
        } catch (IOException e) {
            return fallback(userConfig, "Не удалось прочитать config.json: " + e.getMessage());
        }
    }

    private AppConfig readFile(Path path) throws IOException, ConfigLoadException {
        try {
            return mapper.readValue(path.toFile(), AppConfig.class);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new ConfigLoadException("Неверный формат JSON в " + path, e);
        }
    }

    private void copyDefaultConfig(Path target) throws IOException, ConfigLoadException {
        try (InputStream in = Calculator.class.getResourceAsStream(DEFAULT_RESOURCE)) {
            if (in == null) {
                throw new ConfigLoadException("Шаблон конфигурации не найден в ресурсах приложения");
            }
            Files.createDirectories(target.getParent());
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void validate(AppConfig config) throws ConfigLoadException {
        if (config.getWindow() == null) {
            throw new ConfigLoadException("Отсутствует секция \"window\"");
        }
        if (config.getWindow().getWidth() < 280 || config.getWindow().getHeight() < 400) {
            throw new ConfigLoadException("Слишком малый размер окна (width/height)");
        }
        String theme = config.getTheme();
        if (theme == null || theme.isBlank()) {
            config.setTheme("dark");
        } else if (!theme.equalsIgnoreCase("light") && !theme.equalsIgnoreCase("dark")) {
            throw new ConfigLoadException("Параметр theme должен быть \"light\" или \"dark\"");
        }
    }

    private ConfigLoadResult fallback(Path attemptedPath, String errorDetail) {
        AppConfig defaults = AppConfig.defaults();
        String message = "Ошибка конфигурации: " + errorDetail
                + System.lineSeparator()
                + "Используются настройки по умолчанию."
                + System.lineSeparator()
                + "Файл: " + attemptedPath;
        try (InputStream in = Calculator.class.getResourceAsStream(DEFAULT_RESOURCE)) {
            if (in != null) {
                defaults = mapper.readValue(in, AppConfig.class);
            }
        } catch (IOException ignored) {
            // keep hardcoded defaults
        }
        return new ConfigLoadResult(defaults, attemptedPath, true, message);
    }
}
