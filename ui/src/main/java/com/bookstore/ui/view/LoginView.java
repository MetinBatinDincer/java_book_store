package com.bookstore.ui.view;

import com.bookstore.ui.auth.SessionManager;
import com.bookstore.ui.model.User;
import com.bookstore.ui.service.UserApiService;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class LoginView {

    private static final String STYLE_URL =
            LoginView.class.getResource("/com/bookstore/ui/style.css").toExternalForm();

    private final Stage           stage;
    private final UserApiService  userService = new UserApiService();
    private final SessionManager  session     = SessionManager.getInstance();
    private final Consumer<Stage> onSuccess;   // çağrıldığında ana pencere açılır

    // form alanları
    private TextField     loginEmailField;
    private PasswordField loginPassField;
    private Label         loginErrorLabel;

    private TextField     regNameField;
    private TextField     regEmailField;
    private PasswordField regPassField;
    private Label         regErrorLabel;

    private boolean showingRegister = false;
    private VBox    formCard;

    public LoginView(Stage stage, Consumer<Stage> onSuccess) {
        this.stage     = stage;
        this.onSuccess = onSuccess;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("login-root");
        root.setCenter(buildCard());
        root.setBottom(buildFooter());

        Scene scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add(STYLE_URL);

        stage.setTitle("BookStore — Giriş");
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(500);
        stage.show();
    }

    /** Ana pencere açıkken modal diyalog olarak açar. */
    public void showAsDialog() {
        Stage dialog = new Stage();
        dialog.initOwner(stage);
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);

        LoginView lv = new LoginView(stage, s -> {
            dialog.close();
            onSuccess.accept(s);
        });

        BorderPane root = new BorderPane();
        root.getStyleClass().add("login-root");
        root.setCenter(lv.buildCard());
        root.setBottom(lv.buildFooter());

        Scene scene = new Scene(root, 500, 560);
        scene.getStylesheets().add(STYLE_URL);

        dialog.setTitle("Giriş Yap");
        dialog.setScene(scene);
        dialog.setResizable(false);
        dialog.showAndWait();
    }

    // ── Kart ──────────────────────────────────────────────────────────────

    StackPane buildCard() {
        formCard = new VBox(20);
        formCard.getStyleClass().add("login-card");
        formCard.setMaxWidth(400);

        renderLoginForm();

        StackPane center = new StackPane(formCard);
        center.setAlignment(Pos.CENTER);
        center.getStyleClass().add("login-bg");
        return center;
    }

    private void renderLoginForm() {
        formCard.getChildren().clear();
        showingRegister = false;

        Label logo    = new Label("📚");
        logo.setStyle("-fx-font-size: 42px;");
        Label brand   = new Label("BookStore");
        brand.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #c8a951;");
        Label title   = new Label("Hoş Geldiniz");
        title.getStyleClass().add("login-title");
        Label subtitle = new Label("Hesabınıza giriş yapın");
        subtitle.getStyleClass().add("login-subtitle");
        VBox header = new VBox(4, logo, brand, title, subtitle);
        header.setAlignment(Pos.CENTER);

        loginEmailField = new TextField();
        loginEmailField.setPromptText("E-posta");
        loginEmailField.getStyleClass().add("login-field");

        loginPassField = new PasswordField();
        loginPassField.setPromptText("Şifre");
        loginPassField.getStyleClass().add("login-field");
        loginPassField.setOnAction(e -> doLogin());

        loginErrorLabel = new Label();
        loginErrorLabel.getStyleClass().add("login-error");
        loginErrorLabel.setVisible(false);

        Button loginBtn = new Button("Giriş Yap");
        loginBtn.getStyleClass().add("btn-login");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setOnAction(e -> doLogin());

        Separator sep = new Separator();

        Button guestBtn = new Button("Misafir olarak devam et →");
        guestBtn.getStyleClass().add("btn-guest");
        guestBtn.setMaxWidth(Double.MAX_VALUE);
        guestBtn.setOnAction(e -> continueAsGuest());

        HBox regRow = new HBox(6);
        regRow.setAlignment(Pos.CENTER);
        Label regQ = new Label("Hesabınız yok mu?");
        regQ.getStyleClass().add("login-subtitle");
        Hyperlink regLink = new Hyperlink("Kayıt ol");
        regLink.setOnAction(e -> renderRegisterForm());
        regRow.getChildren().addAll(regQ, regLink);

        Label hint = new Label("Admin: admin1@gmail.com  /  123456");
        hint.getStyleClass().add("login-hint");
        hint.setAlignment(Pos.CENTER);
        hint.setMaxWidth(Double.MAX_VALUE);

        formCard.getChildren().addAll(header, loginEmailField, loginPassField,
                loginErrorLabel, loginBtn, sep, guestBtn, regRow, hint);
    }

    private void renderRegisterForm() {
        formCard.getChildren().clear();
        showingRegister = true;

        Label logo  = new Label("📚");
        logo.setStyle("-fx-font-size: 42px;");
        Label brand2 = new Label("BookStore");
        brand2.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #c8a951;");
        Label title = new Label("Kayıt Ol");
        title.getStyleClass().add("login-title");
        Label sub   = new Label("Yeni hesap oluşturun");
        sub.getStyleClass().add("login-subtitle");
        VBox header = new VBox(4, logo, brand2, title, sub);
        header.setAlignment(Pos.CENTER);

        regNameField  = new TextField();
        regNameField.setPromptText("Ad Soyad");
        regNameField.getStyleClass().add("login-field");

        regEmailField = new TextField();
        regEmailField.setPromptText("E-posta");
        regEmailField.getStyleClass().add("login-field");

        regPassField  = new PasswordField();
        regPassField.setPromptText("Şifre");
        regPassField.getStyleClass().add("login-field");
        regPassField.setOnAction(e -> doRegister());

        regErrorLabel = new Label();
        regErrorLabel.getStyleClass().add("login-error");
        regErrorLabel.setVisible(false);

        Button regBtn = new Button("Kayıt Ol");
        regBtn.getStyleClass().add("btn-login");
        regBtn.setMaxWidth(Double.MAX_VALUE);
        regBtn.setOnAction(e -> doRegister());

        HBox backRow = new HBox(6);
        backRow.setAlignment(Pos.CENTER);
        Label backQ = new Label("Hesabınız var mı?");
        backQ.getStyleClass().add("login-subtitle");
        Hyperlink backLink = new Hyperlink("Giriş yap");
        backLink.setOnAction(e -> renderLoginForm());
        backRow.getChildren().addAll(backQ, backLink);

        formCard.getChildren().addAll(header, regNameField, regEmailField, regPassField,
                regErrorLabel, regBtn, backRow);
    }

    // ── İşlemler ──────────────────────────────────────────────────────────

    private void doLogin() {
        String email = loginEmailField.getText().trim();
        String pass  = loginPassField.getText();
        if (email.isEmpty() || pass.isEmpty()) {
            showLoginError("E-posta ve şifre boş bırakılamaz");
            return;
        }
        runAsync(() -> userService.login(email, pass),
                user -> { session.login(user); onSuccess.accept(stage); },
                err  -> showLoginError(err.getMessage()));
    }

    private void doRegister() {
        String name  = regNameField.getText().trim();
        String email = regEmailField.getText().trim();
        String pass  = regPassField.getText();
        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            showRegError("Tüm alanlar zorunludur");
            return;
        }
        runAsync(() -> userService.register(name, email, pass),
                user -> { session.login(user); onSuccess.accept(stage); },
                err  -> showRegError(err.getMessage()));
    }

    private void continueAsGuest() {
        session.logout();
        onSuccess.accept(stage);
    }

    // ── Yardımcılar ───────────────────────────────────────────────────────

    private <T> void runAsync(ThrowingSupplier<T> task,
                               Consumer<T> onOk,
                               Consumer<Exception> onErr) {
        Task<T> jfxTask = new Task<>() {
            @Override protected T call() throws Exception { return task.get(); }
        };
        jfxTask.setOnSucceeded(e -> Platform.runLater(() -> onOk.accept(jfxTask.getValue())));
        jfxTask.setOnFailed(e -> Platform.runLater(() -> {
            Throwable t = jfxTask.getException();
            onErr.accept(t instanceof Exception ex ? ex : new Exception(t.getMessage()));
        }));
        Thread t = new Thread(jfxTask);
        t.setDaemon(true);
        t.start();
    }

    private void showLoginError(String msg) {
        loginErrorLabel.setText(msg);
        loginErrorLabel.setVisible(true);
    }

    private void showRegError(String msg) {
        regErrorLabel.setText(msg);
        regErrorLabel.setVisible(true);
    }

    HBox buildFooter() {
        Label l = new Label("© 2026 BookStore · TBL324 İleri Java · Kocaeli Üniversitesi");
        l.getStyleClass().add("status-text");
        HBox bar = new HBox(l);
        bar.setAlignment(Pos.CENTER);
        bar.getStyleClass().add("status-bar");
        bar.setPadding(new Insets(8));
        return bar;
    }

    @FunctionalInterface
    interface ThrowingSupplier<T> { T get() throws Exception; }
}
