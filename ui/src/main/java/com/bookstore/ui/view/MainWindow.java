package com.bookstore.ui.view;

import com.bookstore.ui.BookStoreApp;
import com.bookstore.ui.service.BookApiService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class MainWindow {

    private static final int WIDTH  = 1260;
    private static final int HEIGHT = 760;

    private final Stage          stage;
    private final BookApiService apiService;
    private final BookListView   bookListView;
    private final SalesView      salesView;
    private final StackPane      contentArea = new StackPane();

    private Button activeNavBtn;

    public MainWindow(Stage stage) {
        this.stage        = stage;
        this.apiService   = new BookApiService();
        this.bookListView = new BookListView(apiService);
        this.salesView    = new SalesView(apiService);
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setTop(buildHeader());
        root.setLeft(buildSidebar());
        root.setCenter(buildContentArea());
        root.setBottom(buildStatusBar());

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add(
            BookStoreApp.class.getResource("style.css").toExternalForm()
        );

        stage.setTitle("Dijital Kitap Satış ve Otomasyon Sistemi");
        stage.setScene(scene);
        stage.setMinWidth(960);
        stage.setMinHeight(600);
        stage.show();
    }

    // ── Header ───────────────────────────────────────────────────────────

    private HBox buildHeader() {
        // Logo kutusu
        Label logoIcon = new Label("📚");
        logoIcon.getStyleClass().add("header-logo-icon");
        StackPane logoBox = new StackPane(logoIcon);
        logoBox.getStyleClass().add("header-logo-box");

        Label title    = new Label("Dijital Kitap Satış");
        Label subtitle = new Label("ve Otomasyon Sistemi");
        title.getStyleClass().add("app-title");
        subtitle.getStyleClass().add("app-subtitle");
        VBox titleBox = new VBox(1, title, subtitle);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Kullanıcı rozeti
        Circle avatar = new Circle(16);
        avatar.getStyleClass().add("header-avatar");
        Label userName = new Label("Admin");
        userName.getStyleClass().add("header-username");
        HBox userBadge = new HBox(8, avatar, userName);
        userBadge.setAlignment(Pos.CENTER);
        userBadge.getStyleClass().add("header-user-badge");

        HBox header = new HBox(14, logoBox, titleBox, spacer, userBadge);
        header.getStyleClass().add("app-header");
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }

    // ── Sidebar ──────────────────────────────────────────────────────────

    private VBox buildSidebar() {
        Label adminSection = new Label("YÖNETİM");
        adminSection.getStyleClass().add("sidebar-section-label");

        Button booksBtn = navButton("📚   Kitaplar");
        booksBtn.setOnAction(e -> navigate(booksBtn, bookListView));

        Label salesSection = new Label("SATIŞ");
        salesSection.getStyleClass().add("sidebar-section-label");
        salesSection.setPadding(new Insets(16, 0, 0, 0));

        Button salesBtn = navButton("🛒   Satış Ekranı");
        salesBtn.setOnAction(e -> navigate(salesBtn, salesView));

        setActive(booksBtn);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Alt bilgi şeridi
        Label versionLbl = new Label("v1.0.0");
        versionLbl.getStyleClass().add("sidebar-footer-text");
        Label courseLbl = new Label("TBL324 · İleri Java");
        courseLbl.getStyleClass().add("sidebar-footer-text");
        VBox footer = new VBox(3, versionLbl, courseLbl);
        footer.getStyleClass().add("sidebar-footer");

        VBox sidebar = new VBox(adminSection, booksBtn, salesSection, salesBtn, spacer, footer);
        sidebar.getStyleClass().add("sidebar");
        return sidebar;
    }

    private Button navButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("nav-btn");
        return btn;
    }

    private void setActive(Button btn) {
        if (activeNavBtn != null) activeNavBtn.getStyleClass().remove("nav-btn-active");
        btn.getStyleClass().add("nav-btn-active");
        activeNavBtn = btn;
    }

    // ── Content ──────────────────────────────────────────────────────────

    private StackPane buildContentArea() {
        contentArea.getChildren().add(bookListView);
        contentArea.getStyleClass().add("content-area");
        return contentArea;
    }

    private void navigate(Button btn, Node view) {
        setActive(btn);
        contentArea.getChildren().setAll(view);
        if (view instanceof SalesView sv) sv.loadCatalog();
        else if (view instanceof BookListView blv) blv.loadBooks();
    }

    // ── Status Bar ───────────────────────────────────────────────────────

    private HBox buildStatusBar() {
        Label dot = new Label("●");
        dot.setStyle("-fx-text-fill: #22c55e; -fx-font-size: 9px;");
        Label status = new Label("Bağlı · localhost:8080");
        status.getStyleClass().add("status-text");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label version = new Label("TBL324 — İleri Java Uygulamaları");
        version.getStyleClass().add("status-text");

        HBox bar = new HBox(6, dot, status, spacer, version);
        bar.getStyleClass().add("status-bar");
        bar.setAlignment(Pos.CENTER_LEFT);
        return bar;
    }
}
