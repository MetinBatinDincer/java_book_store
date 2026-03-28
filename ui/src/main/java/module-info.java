module com.bookstore.ui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;

    opens com.bookstore.ui to javafx.fxml;
    opens com.bookstore.ui.model to com.fasterxml.jackson.databind, javafx.base;
    opens com.bookstore.ui.dto   to com.fasterxml.jackson.databind;
    opens com.bookstore.ui.view to javafx.fxml;

    exports com.bookstore.ui;
}
