module denisarseny.simplecalculator {
    requires java.desktop;
    requires java.logging;
    requires com.fasterxml.jackson.databind;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens denisarseny.simplecalculator.view to javafx.fxml;

    exports denisarseny.simplecalculator;
    exports denisarseny.simplecalculator.view;
    exports denisarseny.simplecalculator.viewmodel;
    exports denisarseny.simplecalculator.model;
    exports denisarseny.simplecalculator.command;
    exports denisarseny.simplecalculator.history.decorator;
    exports denisarseny.simplecalculator.config;
}