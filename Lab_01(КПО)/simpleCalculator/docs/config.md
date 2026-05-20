# Конфигурационный файл приложения (лабораторная)

## Формат и расположение

Используется **JSON** — удобно читать и редактировать без перекомпиляции.

| Файл | Назначение |
|------|------------|
| `src/main/resources/.../config/app-config.json` | шаблон в JAR |
| `%USERPROFILE%\.simpleCalculator\config.json` | **рабочий файл** (создаётся при первом запуске) |

После изменения `config.json` **перезапустите** приложение.

Открыть файл: меню **Справка → Открыть config.json**.

## Вариант 1 (слабовидящие)

Параметр `"theme": "light"` или `"dark"` переключает светлый / затемнённый интерфейс.

Дополнительно в `"accessibility"`:

| Параметр | Тип | Описание |
|----------|-----|----------|
| `variant` | int | Номер варианта задания (1) |
| `highContrast` | boolean | Утолщённые границы, контраст |
| `largeFonts` | boolean | Крупнее шрифт на дисплее и кнопках |
| `largeButtons` | boolean | Увеличенная высота/ширина кнопок |

## Секции конфигурации

### `window`

| Параметр | Тип | По умолчанию | Эффект |
|----------|-----|--------------|--------|
| `width` | number | 380 | Ширина окна |
| `height` | number | 640 | Высота окна |
| `minWidth` | number | 320 | Минимальная ширина |
| `minHeight` | number | 540 | Минимальная высота |

### `theme`

`"light"` | `"dark"` — подключается `theme-light.css` или `theme-dark.css`.

### `font`

| Параметр | Тип | Эффект |
|----------|-----|--------|
| `family` | string | Запасной шрифт, если TTF не загрузился |
| `useResourceFont` | boolean | Загружать TTF из resources |
| `resourcePath` | string | Путь к TTF относительно resources |
| `displaySize` | number | Размер шрифта дисплея |
| `titleSize` | number | Заголовок |
| `buttonSize` | number | Кнопки клавиатуры |

### `sound`

| `enabled` | boolean | Звук нажатия кнопок |

### `database`

Для калькулятора **не используется** (`enabled: false`). Параметры зарезервированы:

| Параметр | Описание |
|----------|----------|
| `jdbcUrl` | URL подключения; `${user.home}` подставляется |
| `username`, `password` | Учётные данные |

## Обработка ошибок

| Ситуация | Поведение |
|----------|-----------|
| Файл отсутствует | Копируется шаблон из JAR |
| Неверный JSON | Диалог с предупреждением, настройки по умолчанию |
| Неверный `theme` | Сообщение об ошибке, fallback |
| Слишком маленькое окно | Ошибка валидации, fallback |

Классы: `ConfigLoader`, `ConfigLoadException`, `ConfigLoadResult`.

## Сборка (командная строка)

```bash
cd simpleCalculator
mvnw clean package
mvnw javafx:run
```

Конфигурация попадает в JAR; пользовательский `config.json` лежит вне JAR и не требует пересборки.

## Пример: светлая тема для слабовидящих

```json
{
  "theme": "light",
  "accessibility": {
    "highContrast": true,
    "largeFonts": true,
    "largeButtons": true
  },
  "font": {
    "displaySize": 36,
    "buttonSize": 24
  }
}
```

Сохраните файл, перезапустите калькулятор.
