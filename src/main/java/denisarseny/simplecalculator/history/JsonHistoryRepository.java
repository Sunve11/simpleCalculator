package denisarseny.simplecalculator.history;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonHistoryRepository implements HistoryRepository {
    private static final Pattern ENTRY_PATTERN = Pattern.compile(
            "\\{\\s*\"timestamp\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"\\s*,\\s*\"expression\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"\\s*,\\s*\"result\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"\\s*\\}"
    );
    private final Path historyPath;

    public JsonHistoryRepository() {
        this(Paths.get(System.getProperty("user.home"), ".simpleCalculator", "history.json"));
    }

    public JsonHistoryRepository(Path historyPath) {
        this.historyPath = historyPath;
    }

    @Override
    public synchronized void append(HistoryEntry entry) {
        List<HistoryEntry> entries = readAll();
        entries.add(entry);
        writeAll(entries);
    }

    @Override
    public synchronized List<HistoryEntry> readAll() {
        ensureFile();
        try {
            String raw = Files.readString(historyPath, StandardCharsets.UTF_8);
            return parse(raw);
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось прочитать историю", e);
        }
    }

    @Override
    public synchronized void clear() {
        writeAll(new ArrayList<>());
    }

    private void writeAll(List<HistoryEntry> entries) {
        ensureFile();
        String json = toJson(entries);
        try {
            Files.writeString(historyPath, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось сохранить историю", e);
        }
    }

    private void ensureFile() {
        try {
            Path parent = historyPath.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            if (!Files.exists(historyPath)) {
                Files.writeString(historyPath, "[]", StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось подготовить файл истории", e);
        }
    }

    private List<HistoryEntry> parse(String json) {
        List<HistoryEntry> result = new ArrayList<>();
        if (json == null || json.isBlank()) {
            return result;
        }

        Matcher matcher = ENTRY_PATTERN.matcher(json);
        while (matcher.find()) {
            String timestamp = unescapeJson(matcher.group(1));
            String expression = unescapeJson(matcher.group(2));
            String value = unescapeJson(matcher.group(3));
            result.add(new HistoryEntry(timestamp, expression, value));
        }
        return result;
    }

    private String toJson(List<HistoryEntry> entries) {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        for (int i = 0; i < entries.size(); i++) {
            HistoryEntry entry = entries.get(i);
            json.append("  {")
                    .append("\"timestamp\":\"").append(escapeJson(entry.getTimestamp())).append("\",")
                    .append("\"expression\":\"").append(escapeJson(entry.getExpression())).append("\",")
                    .append("\"result\":\"").append(escapeJson(entry.getResult())).append("\"")
                    .append("}");
            if (i < entries.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("]");
        return json.toString();
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String unescapeJson(String value) {
        return value.replace("\\\"", "\"").replace("\\\\", "\\");
    }
}
