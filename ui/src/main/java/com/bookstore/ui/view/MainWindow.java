package com.bookstore.ui.view;

import com.bookstore.ui.BookStoreApp;
import com.bookstore.ui.auth.SessionManager;
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

    private static final int WIDTH  = 1400;
    private static final int HEIGHT = 820;

    private final Stage          stage;
    private final BookApiService apiService;
    private final BookListView   bookListView;
    private final SalesView      salesView;
    private final ProfileView    profileView;
    private final StackPane      contentArea = new StackPane();
    private final SessionManager session     = SessionManager.getInstance();

    private Button activeNavBtn;
    private Label  headerUserLabel;

    public MainWindow(Stage stage) {
        this.stage        = stage;
        this.apiService   = new BookApiService();
        this.bookListView = new BookListView(apiService);
        this.salesView    = new SalesView(apiService);
        this.profileView  = new ProfileView();
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setTop(buildHeader());
        root.setLeft(buildSidebar());
        root.setCenter(buildContentArea());
        root.setBottom(buildStatusBar());

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add(
                BookStoreApp.class.getResource("style.css").toExternalForm());

        stage.setTitle("BookStore — Dijital Kitap Mağazası");
        stage.setScene(scene);
        stage.setMinWidth(1100);
        stage.setMinHeight(680);
        stage.show();
    }

    // ── Header ───────────────────────────────────────────────────────────

    private HBox buildHeader() {
        Label logoIcon = new Label("📚");
        logoIcon.getStyleClass().add("header-logo-icon");
        StackPane logoBox = new StackPane(logoIcon);
        logoBox.getStyleClass().add("header-logo-box");

        Label title    = new Label("BookStore");
        Label subtitle = new Label("Dijital Kitap Mağazası");
        title.getStyleClass().add("app-title");
        subtitle.getStyleClass().add("app-subtitle");
        VBox titleBox = new VBox(1, title, subtitle);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Circle avatar = new Circle(16);
        avatar.getStyleClass().add("header-avatar");

        headerUserLabel = new Label(session.getDisplayName());
        headerUserLabel.getStyleClass().add("header-username");

        String btnStyle = "-fx-background-color: rgba(255,255,255,0.15); -fx-text-fill: #e2e8f0; " +
                "-fx-background-radius: 6px; -fx-padding: 4px 12px; -fx-cursor: hand; -fx-font-size: 11px;";

        HBox userBadge;
        if (session.isGuest()) {
            Button loginBtn = new Button("Giriş Yap");
            loginBtn.setStyle(btnStyle);
            loginBtn.setOnAction(e -> openLoginDialog());
            userBadge = new HBox(8, loginBtn);
        } else {
            Button logoutBtn = new Button("Çıkış");
            logoutBtn.setStyle(btnStyle);
            logoutBtn.setOnAction(e -> logout());
            userBadge = new HBox(8, avatar, headerUserLabel, logoutBtn);
        }
        userBadge.setAlignment(Pos.CENTER);
        userBadge.getStyleClass().add("header-user-badge");

        HBox header = new HBox(14, logoBox, titleBox, spacer, userBadge);
        header.getStyleClass().add("app-header");
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }

    // ── Sidebar ──────────────────────────────────────────────────────────

    private VBox buildSidebar() {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");

        if (session.isAdmin()) {
            Label adminSection = new Label("YÖNETİM");
            adminSection.getStyleClass().add("sidebar-section-label");
            adminSection.setPadding(new Insets(0, 20, 6, 20));

            Button booksBtn = navButton("📚   Kitaplar");
            booksBtn.setOnAction(e -> navigate(booksBtn, bookListView));

            sidebar.getChildren().addAll(adminSection, booksBtn);

            Label salesSection = new Label("SATIŞ");
            salesSection.getStyleClass().add("sidebar-section-label");
            salesSection.setPadding(new Insets(16, 20, 6, 20));

            Button salesBtn = navButton("🛒   Satış Ekranı");
            salesBtn.setOnAction(e -> navigate(salesBtn, salesView));

            sidebar.getChildren().addAll(salesSection, salesBtn);
            setActive(booksBtn);
        } else {
            Label salesSection = new Label("SATIŞ");
            salesSection.getStyleClass().add("sidebar-section-label");
            salesSection.setPadding(new Insets(0, 20, 6, 20));

            Button salesBtn = navButton("🛒   Satış Ekranı");
            salesBtn.setOnAction(e -> navigate(salesBtn, salesView));
            sidebar.getChildren().addAll(salesSection, salesBtn);
            setActive(salesBtn);
        }

        if (!session.isGuest()) {
            Label profileSection = new Label("HESAP");
            profileSection.getStyleClass().add("sidebar-section-label");
            profileSection.setPadding(new Insets(16, 20, 6, 20));

            Button profileBtn = navButton("👤   Profilim");
            profileBtn.setOnAction(e -> navigate(profileBtn, profileView));
            sidebar.getChildren().addAll(profileSection, profileBtn);
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label versionLbl = new Label("BookStore v1.0");
        versionLbl.getStyleClass().add("sidebar-footer-text");
        Label courseLbl  = new Label("© 2026 · TBL324");
        courseLbl.getStyleClass().add("sidebar-footer-text");
        VBox footer = new VBox(3, versionLbl, courseLbl);
        footer.getStyleClass().add("sidebar-footer");

        sidebar.getChildren().addAll(spacer, footer);
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
        if (session.isAdmin()) {
            contentArea.getChildren().add(bookListView);
        } else {
            contentArea.getChildren().add(salesView);
        }
        contentArea.getStyleClass().add("content-area");
        return contentArea;
    }

    private void navigate(Button btn, Node view) {
        setActive(btn);
        contentArea.getChildren().setAll(view);
        if (view instanceof SalesView sv)       sv.loadCatalog();
        else if (view instanceof BookListView b) b.loadBooks();
        else if (view instanceof ProfileView pv) pv.refresh();
    }

    // ── Status Bar ───────────────────────────────────────────────────────

    private HBox buildStatusBar() {
        Label dot = new Label("●");
        dot.setStyle("-fx-text-fill: #22c55e; -fx-font-size: 9px;");
        Label status = new Label("Bağlı · localhost:8080");
        status.getStyleClass().add("status-text");

        String userInfo = session.isGuest() ? "Misafir" :
                (session.isAdmin() ? "Yönetici" : "Müşteri") + " — " + session.getDisplayName();
        Label userLbl = new Label(userInfo);
        userLbl.getStyleClass().add("status-text");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label version = new Label("BookStore v1.0 — TBL324 İleri Java");
        version.getStyleClass().add("status-text");

        HBox bar = new HBox(6, dot, status, new Label("·"), userLbl, spacer, version);
        bar.getStyleClass().add("status-bar");
        bar.setAlignment(Pos.CENTER_LEFT);
        return bar;
    }

    // ── Giriş / Çıkış ────────────────────────────────────────────────────

    private void openLoginDialog() {
        LoginView loginView = new LoginView(stage, s -> new MainWindow(s).show());
        loginView.showAsDialog();
    }

    private void logout() {
        session.logout();
        new MainWindow(stage).show();
    }
}
