package com.bookstore.ui;

/**
 * Fat-jar başlatıcısı.
 * JavaFX Application sınıfını doğrudan main'den çağırmak modül sistemiyle
 * uyumsuz olabilir; bu yüzden ayrı bir Launcher sınıfı kullanılır.
 */
public class Launcher {
    public static void main(String[] args) {
        BookStoreApp.main(args);
    }
}
