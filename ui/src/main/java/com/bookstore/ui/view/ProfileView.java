package com.bookstore.ui.view;

import com.bookstore.ui.model.PaymentInfo;
import com.bookstore.ui.model.User;
import com.bookstore.ui.util.CardValidator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

public class ProfileView extends BaseView {

    public ProfileView() {
        buildUI();
    }

    @Override
    protected void buildUI() {
        Label pageTitle    = new Label("Profilim");
        Label pageSubtitle = new Label("Hesap bilgilerinizi ve ödeme yönteminizi yönetin");
        pageTitle.getStyleClass().add("page-title");
        pageSubtitle.getStyleClass().add("page-subtitle");
        VBox titleBox = new VBox(4, pageTitle, pageSubtitle);

        VBox content = new VBox(20, titleBox, buildProfileCard(), buildCardSection());
        content.setPadding(new Insets(0, 0, 8, 0));

        setCenter(content);
    }

    @Override
    public void refresh() { buildUI(); }

    // ── Profil Kartı ──────────────────────────────────────────────────────

    private VBox buildProfileCard() {
        User u = session.getCurrentUser();

        Circle avatar = new Circle(32);
        avatar.setStyle("-fx-fill: #3b82f6; -fx-stroke: #93c5fd; -fx-stroke-width: 2;");

        String initials = u != null && u.getName() != null && !u.getName().isBlank()
                ? u.getName().substring(0, 1).toUpperCase() : "?";
        Label avatarLabel = new Label(initials);
        avatarLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
        StackPane avatarBox = new StackPane(avatar, avatarLabel);

        Label nameLabel  = new Label(u != null ? nvl(u.getName()) : "Misafir");
        nameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");

        Label emailLabel = new Label(u != null ? nvl(u.getEmail()) : "—");
        emailLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");

        String roleText  = u != null && "ADMIN".equals(u.getRole()) ? "Yönetici" : "Müşteri";
        Label  roleBadge = new Label(roleText);
        roleBadge.setStyle("-fx-background-color: " + (u != null && "ADMIN".equals(u.getRole())
                ? "#fef3c7; -fx-text-fill: #92400e;" : "#dbeafe; -fx-text-fill: #1d4ed8;")
                + " -fx-background-radius: 12px; -fx-padding: 3px 12px; -fx-font-size: 11px; -fx-font-weight: bold;");

        VBox info = new VBox(4, nameLabel, emailLabel, roleBadge);
        info.setAlignment(Pos.CENTER_LEFT);

        HBox header = new HBox(20, avatarBox, info);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20));

        VBox card = new VBox(header);
        card.getStyleClass().add("card");
        return card;
    }

    // ── Kayıtlı Kart Bölümü ───────────────────────────────────────────────

    private VBox buildCardSection() {
        User u = session.getCurrentUser();

        Label title = new Label("Kayıtlı Ödeme Kartı");
        title.getStyleClass().add("card-title");
        HBox cardHeader = new HBox(title);
        cardHeader.getStyleClass().add("card-header");

        VBox body = new VBox(16);
        body.setPadding(new Insets(20));

        if (u == null) {
            Label msg = new Label("Kart bilgisi kaydetmek için giriş yapmalısınız.");
            msg.getStyleClass().add("page-subtitle");
            body.getChildren().add(msg);
        } else if (u.hasSavedCard()) {
            Label cardLabel = new Label("💳  " + u.getMaskedCard());
            cardLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
            Label expiryLabel = new Label("Son Kullanma: " + nvl(u.getSavedCardExpiry()));
            expiryLabel.getStyleClass().add("page-subtitle");
            Label holderLabel = new Label(nvl(u.getSavedCardHolder()));
            holderLabel.getStyleClass().add("page-subtitle");

            Button editBtn = new Button("Kartı Güncelle");
            editBtn.getStyleClass().add("btn-secondary");
            editBtn.setOnAction(e -> showCardForm(u));

            Button removeBtn = new Button("Kartı Kaldır");
            removeBtn.getStyleClass().add("btn-danger");
            removeBtn.setOnAction(e -> {
                u.setSavedCardNumber(null);
                u.setSavedCardExpiry(null);
                u.setSavedCardHolder(null);
                refresh();
            });

            HBox btns = new HBox(10, editBtn, removeBtn);
            body.getChildren().addAll(cardLabel, holderLabel, expiryLabel, btns);
        } else {
            Label noCard = new Label("Kayıtlı kart bulunamadı.");
            noCard.getStyleClass().add("page-subtitle");
            Button addBtn = new Button("+ Kart Ekle");
            addBtn.getStyleClass().add("btn-primary");
            addBtn.setOnAction(e -> showCardForm(u));
            body.getChildren().addAll(noCard, addBtn);
        }

        VBox card = new VBox(cardHeader, body);
        card.getStyleClass().add("card");
        return card;
    }

    private void showCardForm(User u) {
        Dialog<PaymentInfo> dialog = new Dialog<>();
        dialog.setTitle("Kart Bilgisi");
        dialog.setHeaderText("Kayıtlı kart ekle / güncelle");

        ButtonType saveType = new ButtonType("Kaydet", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveType, ButtonType.CANCEL);

        TextField holderF  = new TextField(nvl(u.getSavedCardHolder()));
        holderF.setPromptText("Kart Sahibi");

        TextField cardF    = new TextField();
        cardF.setPromptText("XXXX XXXX XXXX XXXX");
        cardF.textProperty().addListener((obs, old, val) -> {
            String fmt = CardValidator.format(val);
            if (!fmt.equals(val)) { cardF.setText(fmt); cardF.positionCaret(fmt.length()); }
        });

        TextField expiryF  = new TextField(nvl(u.getSavedCardExpiry()));
        expiryF.setPromptText("AA/YY");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(16));
        grid.addRow(0, new Label("Kart Sahibi"), holderF);
        grid.addRow(1, new Label("Kart No"), cardF);
        grid.addRow(2, new Label("Son Kullanma"), expiryF);
        GridPane.setHgrow(holderF, Priority.ALWAYS);
        GridPane.setHgrow(cardF, Priority.ALWAYS);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(btn -> {
            if (btn != saveType) return null;
            PaymentInfo p = new PaymentInfo();
            p.setCardHolder(holderF.getText().trim());
            p.setCardNumber(cardF.getText());
            p.setExpiry(expiryF.getText());
            return p;
        });

        dialog.showAndWait().ifPresent(p -> {
            u.setSavedCardHolder(p.getCardHolder());
            u.setSavedCardNumber(p.getCardNumber());
            u.setSavedCardExpiry(p.getExpiry());
            refresh();
        });
    }

    private String nvl(String s) { return s != null ? s : ""; }
}
