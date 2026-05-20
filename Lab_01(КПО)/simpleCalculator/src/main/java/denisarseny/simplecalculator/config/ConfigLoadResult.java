package denisarseny.simplecalculator.config;

import java.nio.file.Path;

/**
 * Результат загрузки конфигурации: настройки, путь к файлу, предупреждения.
 */
public record ConfigLoadResult(
        AppConfig config,
        Path configPath,
        boolean usedFallback,
        String userMessage
) {
    public boolean hasWarning() {
        return userMessage != null && !userMessage.isBlank();
    }
}
