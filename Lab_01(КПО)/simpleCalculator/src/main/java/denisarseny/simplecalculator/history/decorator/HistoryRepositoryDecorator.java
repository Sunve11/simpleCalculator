package denisarseny.simplecalculator.history.decorator;

import denisarseny.simplecalculator.history.HistoryEntry;
import denisarseny.simplecalculator.history.HistoryRepository;

import java.util.List;

/**
 * Базовый декоратор (Structural: Decorator) — делегирует вызовы обёрнутому репозиторию.
 */
public abstract class HistoryRepositoryDecorator implements HistoryRepository {

    protected final HistoryRepository wrappee;

    protected HistoryRepositoryDecorator(HistoryRepository wrappee) {
        this.wrappee = wrappee;
    }

    @Override
    public void append(HistoryEntry entry) {
        wrappee.append(entry);
    }

    @Override
    public List<HistoryEntry> readAll() {
        return wrappee.readAll();
    }

    @Override
    public void clear() {
        wrappee.clear();
    }

    @Override
    public void removeLast() {
        wrappee.removeLast();
    }

    @Override
    public void replaceAll(List<HistoryEntry> entries) {
        wrappee.replaceAll(entries);
    }

    public HistoryRepository getWrappee() {
        return wrappee;
    }
}
