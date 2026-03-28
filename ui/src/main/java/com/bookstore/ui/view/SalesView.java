package com.bookstore.ui.view;

import com.bookstore.ui.model.Book;
import com.bookstore.ui.model.CartItem;
import com.bookstore.ui.service.BookApiService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class SalesView extends BorderPane {

    private final BookApiService           apiService;
    private final ObservableList<Book>     catalog   = FXCollections.observableArrayList();
    private final FilteredList<Book>       filtered  = new FilteredList<>(catalog, b -> true);
    private final ObservableList<CartItem> cartItems = FXCollections.observableArrayList();
    private final Label                    totalLabel  = new Label("0,00 ₺");
    private final Label                    statusLabel = new Label("Hazır.");
    private final FlowPane                 catalogPane = new FlowPane();
    private       Task<List<Book>>         activeTask;

    private String activeGenre  = "Tümü";
    private String searchText   = "";

    public SalesView(BookApiService apiService) {
        this.apiService = apiService;
        catalogPane.setHgap(14);
        catalogPane.setVgap(14);
        catalogPane.setPadding(new Insets(16));

        Label pageTitle    = new Label("Satış Ekranı");
        Label pageSubtitle = new Label("Kitap seçin, sepete ekleyin ve satın alın");
        pageTitle.getStyleClass().add("page-title");
        pageSubtitle.getStyleClass().add("page-subtitle");
        VBox titleBox = new VBox(4, pageTitle, pageSubtitle);

        ScrollPane scrollPane = new ScrollPane(catalogPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        HBox contentRow = buildContentRow(scrollPane);
        VBox catalogBody = new VBox(buildCatalogToolbar(), contentRow);
        VBox.setVgrow(contentRow, Priority.ALWAYS);
        catalogBody.getStyleClass().add("card");
        VBox.setVgrow(catalogBody, Priority.ALWAYS);

        HBox main = new HBox(16, catalogBody, buildCartPanel());
        HBox.setHgrow(catalogBody, Priority.ALWAYS);
        VBox.setVgrow(main, Priority.ALWAYS);

        VBox wrapper = new VBox(16, titleBox, main);
        VBox.setVgrow(main, Priority.ALWAYS);

        statusLabel.getStyleClass().add("status-text");
        HBox statusBar = new HBox(statusLabel);
        statusBar.getStyleClass().add("status-bar");

        setCenter(wrapper);
        setBottom(statusBar);

        filtered.addListener((javafx.collections.ListChangeListener<Book>) c -> rebuildCatalog());
        cartItems.addListener((javafx.collections.ListChangeListener<CartItem>) c -> recalcTotal());

        loadCatalog();
    }

    // ── Catalog toolbar with search ───────────────────────────────────────

    private HBox buildCatalogToolbar() {
        Label title = new Label("Kitap Kataloğu");
        title.getStyleClass().add("card-title");

        TextField searchField = new TextField();
        searchField.setPromptText("🔍  Kitap ara...");
        searchField.getStyleClass().add("search-field");
        searchField.setPrefWidth(200);
        searchField.textProperty().addListener((obs, old, val) -> {
            searchText = val == null ? "" : val.toLowerCase();
            applyFilter();
        });

        Button refreshBtn = new Button("↻  Yenile");
        refreshBtn.getStyleClass().add("btn-secondary");
        refreshBtn.setOnAction(e -> loadCatalog());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox toolbar = new HBox(8, title, spacer, searchField, refreshBtn);
        toolbar.getStyleClass().add("card-header");
        toolbar.setAlignment(Pos.CENTER_LEFT);
        return toolbar;
    }

    // ── Content row: genre sidebar + book cards ───────────────────────────

    private HBox buildContentRow(ScrollPane scrollPane) {
        VBox genrePanel = buildGenrePanel();
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        HBox row = new HBox(genrePanel, scrollPane);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        VBox.setVgrow(row, Priority.ALWAYS);
        return row;
    }

    private VBox buildGenrePanel() {
        Label sectionLabel = new Label("KATEGORİ");
        sectionLabel.getStyleClass().add("genre-section-label");

        VBox panel = new VBox(sectionLabel);
        panel.getStyleClass().add("genre-panel");

        List<String> genreList = new java.util.ArrayList<>();
        genreList.add("Tümü");
        genreList.addAll(BookListView.GENRES);

        for (String genre : genreList) {
            Button btn = new Button(genre);
            btn.getStyleClass().add("genre-filter-btn");
            if (genre.equals("Tümü")) btn.getStyleClass().add("genre-filter-btn-active");
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setOnAction(e -> {
                panel.getChildren().stream()
                    .filter(n -> n instanceof Button)
                    .forEach(n -> n.getStyleClass().remove("genre-filter-btn-active"));
                btn.getStyleClass().add("genre-filter-btn-active");
                activeGenre = genre;
                applyFilter();
            });
            panel.getChildren().add(btn);
        }

        return panel;
    }

    // ── Filter ────────────────────────────────────────────────────────────

    private void applyFilter() {
        filtered.setPredicate(b -> {
            boolean genreMatch = activeGenre.equals("Tümü")
                || (b.getGenre() != null && b.getGenre().equals(activeGenre));
            boolean searchMatch = searchText.isBlank()
                || (b.getTitle()  != null && b.getTitle().toLowerCase().contains(searchText))
                || (b.getAuthor() != null && b.getAuthor().toLowerCase().contains(searchText));
            return genreMatch && searchMatch;
        });
    }

    // ── Book cards ────────────────────────────────────────────────────────

    private void rebuildCatalog() {
        catalogPane.getChildren().clear();
        for (Book book : filtered) {
            catalogPane.getChildren().add(buildBookCard(book));
        }
        if (filtered.isEmpty() && !catalog.isEmpty()) {
            Label empty = new Label("Bu kategoride kitap bulunamadı.");
            empty.getStyleClass().add("page-subtitle");
            empty.setPadding(new Insets(32));
            catalogPane.getChildren().add(empty);
        }
    }

    private VBox buildBookCard(Book book) {
        // Renkli kapak alanı
        String genre = book.getGenre() != null ? book.getGenre() : "";
        String coverColor = genreCoverColor(genre);

        Label coverIcon = new Label(genreIcon(genre));
        coverIcon.setStyle("-fx-font-size: 28px;");
        StackPane cover = new StackPane(coverIcon);
        cover.setStyle("-fx-background-color: " + coverColor + "; -fx-background-radius: 8px 8px 0 0;");
        cover.setPrefHeight(80);
        cover.setMaxWidth(Double.MAX_VALUE);

        // Kart gövdesi
        Label genreBadge = new Label(genre.isBlank() ? "Genel" : genre);
        genreBadge.getStyleClass().add("book-card-genre");

        Label titleLbl = new Label(book.getTitle());
        titleLbl.getStyleClass().add("book-card-title");
        titleLbl.setWrapText(true);
        titleLbl.setMaxWidth(172);

        Label authorLbl = new Label(book.getAuthor() != null ? "✍  " + book.getAuthor() : "");
        authorLbl.getStyleClass().add("book-card-author");

        Label priceLbl = new Label(book.getPrice() != null ? book.getPrice().toPlainString() + " ₺" : "");
        priceLbl.getStyleClass().add("book-card-price");

        int stock = book.getStockQuantity() == null ? 0 : book.getStockQuantity();
        Label stockLbl = new Label(stock == 0 ? "⊘  Stokta yok" : "● Stok: " + stock);
        stockLbl.getStyleClass().add(stock <= 3 ? "book-card-stock-low" : "book-card-stock-ok");

        Button addBtn = new Button(stock == 0 ? "Stok Yok" : "🛒  Sepete Ekle");
        addBtn.getStyleClass().add("btn-add-cart");
        addBtn.setDisable(stock == 0);
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.setOnAction(e -> addToCart(book));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox body = new VBox(6, genreBadge, titleLbl, authorLbl, priceLbl, stockLbl, spacer, addBtn);
        body.setPadding(new Insets(10, 12, 12, 12));

        VBox card = new VBox(cover, body);
        card.getStyleClass().add("book-card");
        VBox.setVgrow(body, Priority.ALWAYS);
        return card;
    }

    private String genreCoverColor(String genre) {
        return switch (genre) {
            case "Edebiyat"          -> "linear-gradient(from 0% 0% to 100% 100%, #f59e0b, #d97706)";
            case "Çocuk"             -> "linear-gradient(from 0% 0% to 100% 100%, #34d399, #059669)";
            case "Eğitim & Başvuru"  -> "linear-gradient(from 0% 0% to 100% 100%, #60a5fa, #2563eb)";
            case "Ders ve Sınav"     -> "linear-gradient(from 0% 0% to 100% 100%, #818cf8, #4f46e5)";
            case "Kurgu Dışı"        -> "linear-gradient(from 0% 0% to 100% 100%, #f472b6, #db2777)";
            case "Yabancı Kitaplar"  -> "linear-gradient(from 0% 0% to 100% 100%, #38bdf8, #0284c7)";
            case "Kişisel Gelişim"   -> "linear-gradient(from 0% 0% to 100% 100%, #4ade80, #16a34a)";
            case "Bilim & Teknoloji" -> "linear-gradient(from 0% 0% to 100% 100%, #22d3ee, #0891b2)";
            case "Tarih"             -> "linear-gradient(from 0% 0% to 100% 100%, #fb923c, #ea580c)";
            default                  -> "linear-gradient(from 0% 0% to 100% 100%, #a78bfa, #7c3aed)";
        };
    }

    private String genreIcon(String genre) {
        return switch (genre) {
            case "Edebiyat"          -> "✒";
            case "Çocuk"             -> "🎈";
            case "Eğitim & Başvuru"  -> "🎓";
            case "Ders ve Sınav"     -> "📝";
            case "Kurgu Dışı"        -> "🔍";
            case "Yabancı Kitaplar"  -> "🌍";
            case "Kişisel Gelişim"   -> "🌱";
            case "Bilim & Teknoloji" -> "🔬";
            case "Tarih"             -> "🏛";
            default                  -> "📖";
        };
    }

    // ── Cart panel ────────────────────────────────────────────────────────

    private VBox buildCartPanel() {
        Label cartTitle = new Label("🛒  Sepet");
        cartTitle.getStyleClass().add("cart-header-title");

        HBox cartHeader = new HBox(cartTitle);
        cartHeader.getStyleClass().add("cart-header");
        cartHeader.setAlignment(Pos.CENTER_LEFT);

        ListView<CartItem> cartList = new ListView<>(cartItems);
        cartList.setPrefHeight(0);
        VBox.setVgrow(cartList, Priority.ALWAYS);
        cartList.setCellFactory(lv -> new CartCell());
        cartList.setStyle("-fx-background-color: transparent;");
        cartList.setPlaceholder(new Label("Sepet boş."));

        totalLabel.getStyleClass().add("cart-total-amount");
        Label totalText = new Label("Toplam");
        totalText.getStyleClass().add("cart-total-label");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox totalRow = new HBox(totalText, spacer, totalLabel);
        totalRow.getStyleClass().add("cart-total-row");
        totalRow.setAlignment(Pos.CENTER);

        Button buyBtn   = new Button("✔  Satın Al");
        Button clearBtn = new Button("Sepeti Temizle");
        buyBtn.getStyleClass().add("btn-buy");
        clearBtn.getStyleClass().add("btn-clear");
        buyBtn.disableProperty().bind(javafx.beans.binding.Bindings.isEmpty(cartItems));
        clearBtn.disableProperty().bind(javafx.beans.binding.Bindings.isEmpty(cartItems));
        buyBtn.setOnAction(e   -> purchase());
        clearBtn.setOnAction(e -> cartItems.clear());

        VBox buttons = new VBox(8, buyBtn, clearBtn);
        buttons.setPadding(new Insets(12, 16, 16, 16));

        VBox panel = new VBox(cartHeader, cartList, totalRow, buttons);
        panel.getStyleClass().add("cart-panel");
        VBox.setVgrow(cartList, Priority.ALWAYS);
        return panel;
    }

    // ── Cart logic ────────────────────────────────────────────────────────

    private void addToCart(Book book) {
        Optional<CartItem> existing = cartItems.stream()
                .filter(ci -> ci.getBook().getId().equals(book.getId()))
                .findFirst();
        if (existing.isPresent()) {
            existing.get().increment();
            int idx = cartItems.indexOf(existing.get());
            cartItems.set(idx, existing.get());
        } else {
            cartItems.add(new CartItem(book));
        }
        recalcTotal();
    }

    private void recalcTotal() {
        BigDecimal total = cartItems.stream()
                .map(CartItem::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalLabel.setText(total.toPlainString() + " ₺");
    }

    private void purchase() {
        List<CartItem> snapshot = List.copyOf(cartItems);

        Task<Void> task = new Task<>() {
            @Override protected Void call() throws Exception {
                apiService.createOrder(1L, snapshot);
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            cartItems.clear();
            statusLabel.setText("Satış tamamlandı! Stoklar güncellendi.");
            loadCatalog();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Satış Tamamlandı");
            alert.setHeaderText("Satış başarıyla gerçekleşti.");
            alert.setContentText("Sipariş oluşturuldu, stoklar güncellendi.");
            alert.showAndWait();
        });

        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            statusLabel.setText("Hata: " + ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Satış Hatası");
            alert.setHeaderText("Sipariş oluşturulamadı.");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        });

        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    public void loadCatalog() {
        if (activeTask != null) activeTask.cancel();
        statusLabel.setText("Katalog yükleniyor...");

        Task<List<Book>> task = new Task<>() {
            @Override protected List<Book> call() throws Exception {
                return apiService.findAll();
            }
        };

        task.setOnSucceeded(e -> {
            catalog.setAll(task.getValue());
            applyFilter();
            statusLabel.setText(filtered.size() + " / " + catalog.size() + " kitap listelendi.");
        });

        task.setOnFailed(e -> statusLabel.setText(
                "Hata: " + task.getException().getMessage()));

        activeTask = task;
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    // ── Cart cell ─────────────────────────────────────────────────────────

    private class CartCell extends ListCell<CartItem> {
        @Override
        protected void updateItem(CartItem item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) { setGraphic(null); return; }

            Label nameLbl = new Label(item.getBook().getTitle());
            nameLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #1e293b;");
            nameLbl.setMaxWidth(120);

            Label subtotalLbl = new Label(item.subtotal().toPlainString() + " ₺");
            subtotalLbl.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #2563eb;");

            Button minusBtn = new Button("−");
            Button plusBtn  = new Button("+");
            Label  qtyLbl   = new Label(String.valueOf(item.getQuantity()));
            qtyLbl.setStyle("-fx-min-width: 20px; -fx-alignment: center;");

            String btnStyle = "-fx-background-color: #f1f5f9; -fx-background-radius: 4px; " +
                              "-fx-padding: 2px 6px; -fx-cursor: hand;";
            minusBtn.setStyle(btnStyle);
            plusBtn.setStyle(btnStyle);

            minusBtn.setOnAction(e -> { item.decrement(); updateItem(item, false); recalcTotal(); });
            plusBtn.setOnAction(e  -> { item.increment(); updateItem(item, false); recalcTotal(); });

            Button removeBtn = new Button("✕");
            removeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #dc2626; -fx-cursor: hand;");
            removeBtn.setOnAction(e -> { cartItems.remove(item); recalcTotal(); });

            HBox qtyBox = new HBox(4, minusBtn, qtyLbl, plusBtn);
            qtyBox.setAlignment(Pos.CENTER);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox row = new HBox(6, nameLbl, spacer, subtotalLbl, qtyBox, removeBtn);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(4, 8, 4, 8));

            setGraphic(row);
            setStyle("-fx-background-color: transparent;");
        }
    }
}
