# Лабораторная: ресурсы приложения (JavaFX)

## Структура ресурсов

```
src/main/resources/denisarseny/simplecalculator/
  messages.properties, messages_ru.properties   — строки (статическая загрузка)
  calculator.css, history.css                   — стили
  icons/app-icon-*.png                          — иконки окна и панели задач
  images/logo.png                               — изображение в шапке
  fonts/Roboto-Regular.ttf                      — шрифт (динамическая загрузка)
  media/click.wav                               — звук нажатия
  media/intro.mp4                               — видео (опционально, динамически)
  cursors/pointer.png                           — курсор для клавиатуры
```

## Статическая vs динамическая загрузка

| Ресурс | Способ | Класс / метод | Обоснование |
|--------|--------|---------------|-------------|
| Строки меню | Статический | `ResourceBundle`, `AppResources.getString()` | Не меняются в runtime, кэш bundle |
| Иконки окна PNG | Статический | `getStaticImage()`, `stage.getIcons()` | Малый размер, загрузка один раз |
| Ikonli (меню, кнопки) | Статический | шрифт в JAR, `ResourceIcons` | Векторные иконки без отдельных файлов |
| CSS | Статический | `scene.getStylesheets().add(url)` | Подключается при старте сцены |
| Шрифт Roboto | Динамический | `Font.loadFont(InputStream)` | TTF большой, загрузка по требованию |
| Звук click.wav | Статический URL | `AudioClip` | Короткий файл, переиспользование |
| Курсор PNG | Динамический | `loadCursor()` → `new Cursor(...)` | Создаётся Image + Cursor, учёт в dispose |
| Видео intro.mp4 | Динамический | `tryLoadIntroVideo()` → `Media` | Тяжёлый ресурс, может отсутствовать |
| Логотип в диалоге | Динамический | `loadImage()` в About | Демонстрация потока InputStream |

## Освобождение ресурсов

`AppResources.dispose()` вызывается в `Calculator.stop()`:

- останавливает `AudioClip`;
- очищает списки `Media` и динамических `Image`;
- `MediaPlayer.dispose()` в `AboutDialog` и окне видео при закрытии.

В JavaFX явного `DeleteObject` нет — снимаются ссылки, для `MediaPlayer` используется `dispose()`.

## Сборка из командной строки

```bash
cd Lab_01(КПО)/simpleCalculator
mvnw clean package
mvnw javafx:run
```

Ресурсы из `src/main/resources` попадают в JAR (`target/simpleCalculator-*.jar`).

## Видео

Положите файл `intro.mp4` в `media/` (например, короткий ролик 320×180).  
Пункт меню **Справка → Видео-вступление** откроет плеер. Без файла показывается сообщение из `messages.properties`.
