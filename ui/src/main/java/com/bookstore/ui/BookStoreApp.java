package com.bookstore.ui;

import com.bookstore.ui.view.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class BookStoreApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        new MainWindow(primaryStage).show();
    }
}
