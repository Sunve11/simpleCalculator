package denisarseny.simplecalculator.history;

import javafx.stage.Stage;
import java.util.List;

public abstract class HistoryWindowFactory {
    public abstract Stage createWindow(List<HistoryEntry> entries);
}
