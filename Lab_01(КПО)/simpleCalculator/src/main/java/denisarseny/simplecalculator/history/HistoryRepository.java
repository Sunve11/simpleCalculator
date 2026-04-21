package denisarseny.simplecalculator.history;

import java.util.List;

public interface HistoryRepository {
    void append(HistoryEntry entry);

    List<HistoryEntry> readAll();

    void clear();
}
