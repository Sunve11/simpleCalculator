package denisarseny.simplecalculator.history.decorator;

import denisarseny.simplecalculator.history.HistoryEntry;
import denisarseny.simplecalculator.history.HistoryRepository;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Декоратор: логирует обращения к хранилищу (удобно при пошаговой отладке лабы №5).
 */
public final class LoggingHistoryRepositoryDecorator extends HistoryRepositoryDecorator {

    private static final Logger LOG = Logger.getLogger(LoggingHistoryRepositoryDecorator.class.getName());

    public LoggingHistoryRepositoryDecorator(HistoryRepository wrappee) {
        super(wrappee);
    }

    @Override
    public void append(HistoryEntry entry) {
        LOG.log(Level.INFO, "History.append: {0} = {1}", new Object[]{entry.getExpression(), entry.getResult()});
        super.append(entry);
    }

    @Override
    public List<HistoryEntry> readAll() {
        LOG.log(Level.FINE, "History.readAll");
        List<HistoryEntry> entries = super.readAll();
        LOG.log(Level.FINE, "History.readAll -> {0} записей", entries.size());
        return entries;
    }

    @Override
    public void clear() {
        LOG.log(Level.INFO, "History.clear");
        super.clear();
    }

    @Override
    public void removeLast() {
        LOG.log(Level.INFO, "History.removeLast");
        super.removeLast();
    }

    @Override
    public void replaceAll(List<HistoryEntry> entries) {
        LOG.log(Level.INFO, "History.replaceAll: {0} записей", entries.size());
        super.replaceAll(entries);
    }
}
