module denisarseny.simplecalculator {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens denisarseny.simplecalculator.view to javafx.fxml;

    exports denisarseny.simplecalculator;
    exports denisarseny.simplecalculator.view;
    exports denisarseny.simplecalculator.viewmodel;
    exports denisarseny.simplecalculator.model;
    exports denisarseny.simplecalculator.command;
}