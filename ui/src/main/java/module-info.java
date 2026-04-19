module com.bookstore.ui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;

    opens com.bookstore.ui           to javafx.fxml;
    opens com.bookstore.ui.model     to com.fasterxml.jackson.databind, javafx.base;
    opens com.bookstore.ui.dto       to com.fasterxml.jackson.databind;
    opens com.bookstore.ui.view      to javafx.fxml;
    opens com.bookstore.ui.auth      to com.fasterxml.jackson.databind;
    opens com.bookstore.ui.contract  to com.fasterxml.jackson.databind;

    exports com.bookstore.ui;
    exports com.bookstore.ui.auth;
    exports com.bookstore.ui.contract;
    exports com.bookstore.ui.util;
    exports com.bookstore.ui.view;
}
