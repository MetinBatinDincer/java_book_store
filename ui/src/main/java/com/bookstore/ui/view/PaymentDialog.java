package com.bookstore.ui.view;

import com.bookstore.ui.auth.SessionManager;
import com.bookstore.ui.model.PaymentInfo;
import com.bookstore.ui.util.CardValidator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.math.BigDecimal;
import java.util.Optional;

public class PaymentDialog {

    private final BigDecimal total;

    public PaymentDialog(BigDecimal total) {
        this.total = total;
    }

    /** Diyaloğu gösterir; kullanıcı onaylarsa PaymentInfo döner. */
    public Optional<PaymentInfo> show() {
        Dialog<PaymentInfo> dialog = new Dialog<>();
        dialog.setTitle("Ödeme");
        dialog.setHeaderText(String.format("Ödenecek tutar: %.2f ₺", total));

        ButtonType payType = new ButtonType("Ödemeyi Tamamla", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(payType, ButtonType.CANCEL);

        // Alanlar
        TextField     holderField = new TextField();
        holderField.setPromptText("Kart Üzerindeki İsim");

        TextField     cardField   = new TextField();
        cardField.setPromptText("XXXX XXXX XXXX XXXX");

        TextField     expiryField = new TextField();
        expiryField.setPromptText("AA/YY");
        expiryField.setMaxWidth(80);

        TextField     cvvField    = new TextField();
        cvvField.setPromptText("CVV");
        cvvField.setMaxWidth(70);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11px;");
        errorLabel.setVisible(false);

        CheckBox saveCardBox = new CheckBox("Kartı kaydet (sonraki alışverişlerde kullan)");
        saveCardBox.setVisible(SessionManager.getInstance().isLoggedIn());

        // Kayıtlı kart varsa ön doldur
        SessionManager session = SessionManager.getInstance();
        if (session.isLoggedIn() && session.getCurrentUser().hasSavedCard()) {
            holderField.setText(session.getCurrentUser().getSavedCardHolder());
            cardField.setText(session.getCurrentUser().getMaskedCard());
            expiryField.setText(session.getCurrentUser().getSavedCardExpiry());
            cardField.setEditable(false);
            Label savedNote = new Label("✔  Kayıtlı kart kullanılıyor");
            savedNote.setStyle("-fx-text-fill: #16a34a; -fx-font-size: 11px;");
        }

        // Kart numarası otomatik formatlama
        cardField.textProperty().addListener((obs, old, val) -> {
            if (!val.equals(CardValidator.format(val)) && cardField.isEditable()) {
                String formatted = CardValidator.format(val);
                cardField.setText(formatted);
                cardField.positionCaret(formatted.length());
            }
        });

        // MM/YY otomatik /
        expiryField.textProperty().addListener((obs, old, val) -> {
            String digits = val.replaceAll("\\D", "");
            if (digits.length() >= 3) {
                String formatted = digits.substring(0, 2) + "/" + digits.substring(2, Math.min(4, digits.length()));
                if (!formatted.equals(val)) {
                    expiryField.setText(formatted);
                    expiryField.positionCaret(formatted.length());
                }
            }
        });

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(12);
        grid.setPadding(new Insets(20, 24, 8, 24));
        grid.setMinWidth(380);

        grid.addRow(0, label("Kart Sahibi *"), holderField);
        GridPane.setHgrow(holderField, Priority.ALWAYS);

        grid.addRow(1, label("Kart Numarası *"), cardField);
        GridPane.setHgrow(cardField, Priority.ALWAYS);

        HBox expiryRow = new HBox(12, expiryField, label("CVV *"), cvvField);
        expiryRow.setAlignment(Pos.CENTER_LEFT);
        grid.addRow(2, label("Son Kullanma *"), expiryRow);

        grid.add(errorLabel, 0, 3, 2, 1);
        if (SessionManager.getInstance().isLoggedIn()) {
            grid.add(saveCardBox, 0, 4, 2, 1);
        }

        dialog.getDialogPane().setContent(grid);

        // Ödeme düğmesini devre dışı bırak, validasyona göre aç
        javafx.scene.Node payBtn = dialog.getDialogPane().lookupButton(payType);
        payBtn.setDisable(true);

        Runnable validate = () -> {
            boolean rawCard = session.isLoggedIn() && session.getCurrentUser().hasSavedCard();
            boolean cardOk  = rawCard || CardValidator.isValidLuhn(cardField.getText());
            boolean expOk   = CardValidator.isValidExpiry(expiryField.getText());
            boolean cvvOk   = CardValidator.isValidCvv(cvvField.getText());
            boolean nameOk  = !holderField.getText().trim().isEmpty();
            payBtn.setDisable(!(cardOk && expOk && cvvOk && nameOk));
            if (!cardOk && !cardField.getText().isBlank()) {
                errorLabel.setText("Geçersiz kart numarası");
                errorLabel.setVisible(true);
            } else if (!expOk && !expiryField.getText().isBlank()) {
                errorLabel.setText("Geçersiz veya süresi dolmuş tarih (AA/YY)");
                errorLabel.setVisible(true);
            } else {
                errorLabel.setVisible(false);
            }
        };

        holderField.textProperty().addListener((o, a, b) -> validate.run());
        cardField.textProperty().addListener((o, a, b)   -> validate.run());
        expiryField.textProperty().addListener((o, a, b) -> validate.run());
        cvvField.textProperty().addListener((o, a, b)    -> validate.run());
        validate.run();

        dialog.setResultConverter(btn -> {
            if (btn != payType) return null;
            PaymentInfo info = new PaymentInfo();
            info.setCardHolder(holderField.getText().trim());
            info.setCardNumber(cardField.getText());
            info.setExpiry(expiryField.getText());
            info.setCvv(cvvField.getText());
            info.setSaveCard(saveCardBox.isSelected());
            return info;
        });

        return dialog.showAndWait();
    }

    private Label label(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: #374151; -fx-font-size: 12px;");
        return l;
    }
}
