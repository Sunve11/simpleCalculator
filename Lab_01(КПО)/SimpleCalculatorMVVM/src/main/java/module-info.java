module denisarseny.simplecalculatormvvm {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    opens denisarseny.simplecalculatormvvm.view to javafx.fxml;

    exports denisarseny.simplecalculatormvvm;
    exports denisarseny.simplecalculatormvvm.view;
    exports denisarseny.simplecalculatormvvm.viewmodel;
    exports denisarseny.simplecalculatormvvm.model;
}
