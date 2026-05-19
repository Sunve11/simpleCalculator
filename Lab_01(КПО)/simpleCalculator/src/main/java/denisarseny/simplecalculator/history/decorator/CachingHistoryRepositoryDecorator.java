package denisarseny.simplecalculator.history.decorator;

import denisarseny.simplecalculator.history.HistoryEntry;
import denisarseny.simplecalculator.history.HistoryRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Декоратор: кэширует результат {@link #readAll()} до следующей записи/изменения.
 */
public final class CachingHistoryRepositoryDecorator extends HistoryRepositoryDecorator {

    private List<HistoryEntry> cache;
    private boolean cacheValid;

    public CachingHistoryRepositoryDecorator(HistoryRepository wrappee) {
        super(wrappee);
    }

    @Override
    public List<HistoryEntry> readAll() {
        if (!cacheValid) {
            cache = new ArrayList<>(super.readAll());
            cacheValid = true;
        }
        return new ArrayList<>(cache);
    }

    @Override
    public void append(HistoryEntry entry) {
        super.append(entry);
        invalidateCache();
    }

    @Override
    public void clear() {
        super.clear();
        invalidateCache();
    }

    @Override
    public void removeLast() {
        super.removeLast();
        invalidateCache();
    }

    @Override
    public void replaceAll(List<HistoryEntry> entries) {
        super.replaceAll(entries);
        invalidateCache();
    }

    private void invalidateCache() {
        cacheValid = false;
        cache = null;
    }
}
