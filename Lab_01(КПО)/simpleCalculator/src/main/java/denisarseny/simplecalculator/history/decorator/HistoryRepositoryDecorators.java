package denisarseny.simplecalculator.history.decorator;

import denisarseny.simplecalculator.history.HistoryRepository;
import denisarseny.simplecalculator.history.JsonHistoryRepository;

/**
 * Сборка цепочки декораторов: Logging → Caching → JsonHistoryRepository.
 */
public final class HistoryRepositoryDecorators {

    private HistoryRepositoryDecorators() {
    }

    public static HistoryRepository createDefault() {
        HistoryRepository core = new JsonHistoryRepository();
        HistoryRepository cached = new CachingHistoryRepositoryDecorator(core);
        return new LoggingHistoryRepositoryDecorator(cached);
    }
}
