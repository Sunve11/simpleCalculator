package denisarseny.simplecalculator.history;

import java.util.List;

public interface HistoryRepository {
    void append(HistoryEntry entry);

    List<HistoryEntry> readAll();

    void clear();

    void removeLast();

    void replaceAll(List<HistoryEntry> entries);
}
