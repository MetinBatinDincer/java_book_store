package com.bookstore.ui;

import com.bookstore.ui.view.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX uygulama giriş noktası.
 * Ana pencereyi oluşturur ve ekrana getirir.
 */
public class BookStoreApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        MainWindow mainWindow = new MainWindow(primaryStage);
        mainWindow.show();
    }
}
