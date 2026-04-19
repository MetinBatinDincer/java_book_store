package com.bookstore.ui.view;

import com.bookstore.ui.model.Book;
import com.bookstore.ui.service.BookApiService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookListView extends BaseView {

    static final List<String> GENRES = List.of(
        "Edebiyat", "Çocuk", "Eğitim & Başvuru", "Ders ve Sınav",
        "Kurgu Dışı", "Yabancı Kitaplar", "Kişisel Gelişim",
        "Bilim & Teknoloji", "Tarih", "Diğer"
    );

    private final BookApiService          apiService;
    private final ObservableList<Book>    books;
    private final FilteredList<Book>      filteredBooks;
    private final TableView<Book>         tableView;
    private final Label                   statusLabel;
    private final Label                   countBadge;
    private final ProgressIndicator       spinner;
    private       Task<List<Book>>        activeTask;

    @Override protected void buildUI() {}
    @Override public void refresh() { loadBooks(); }

    public BookListView(BookApiService apiService) {
        this.apiService    = apiService;
        this.books         = FXCollections.observableArrayList();
        this.filteredBooks = new FilteredList<>(books, b -> true);
        this.tableView     = buildTable();
        this.statusLabel   = new Label("Hazır.");
        this.countBadge    = new Label("0 kitap");
        this.spinner       = new ProgressIndicator();

        spinner.setMaxSize(22, 22);
        spinner.setVisible(false);
        countBadge.getStyleClass().add("badge-blue");

        Label pageTitle    = new Label("Kitap Yönetimi");
        Label pageSubtitle = new Label("Sistemdeki tüm kitapları görüntüleyin, ekleyin ve düzenleyin");
        pageTitle.getStyleClass().add("page-title");
        pageSubtitle.getStyleClass().add("page-subtitle");
        VBox titleBox = new VBox(4, pageTitle, pageSubtitle);

        VBox card = new VBox(buildCardHeader(), buildToolbar(), tableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        card.getStyleClass().add("card");

        VBox wrapper = new VBox(16, titleBox, card);
        VBox.setVgrow(card, Priority.ALWAYS);

        setCenter(wrapper);
        setBottom(buildStatusBar());
        BorderPane.setMargin(wrapper, new Insets(0, 0, 8, 0));

        loadBooks();
    }

    private HBox buildCardHeader() {
        Label title = new Label("Kitap Listesi");
        title.getStyleClass().add("card-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(8, title, spacer, countBadge);
        header.getStyleClass().add("card-header");
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }

    private HBox buildToolbar() {
        TextField searchField = new TextField();
        searchField.setPromptText("🔍  Başlık, yazar veya ISBN ara...");
        searchField.getStyleClass().add("search-field");
        searchField.textProperty().addListener((obs, old, val) -> applyFilter(val));

        Button refreshBtn = new Button("↻   Yenile");
        refreshBtn.getStyleClass().add("btn-secondary");
        refreshBtn.setOnAction(e -> loadBooks());

        Button newBtn = new Button("➕  Yeni Kitap");
        newBtn.getStyleClass().add("btn-primary");
        newBtn.setOnAction(e -> showBookForm(null));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox toolbar = new HBox(8, searchField, spacer, spinner, newBtn, refreshBtn);
        toolbar.getStyleClass().add("toolbar-area");
        toolbar.setAlignment(Pos.CENTER_LEFT);
        return toolbar;
    }

    @SuppressWarnings("unchecked")
    private TableView<Book> buildTable() {
        TableColumn<Book, Long>       idCol     = new TableColumn<>("#");
        TableColumn<Book, String>     titleCol  = new TableColumn<>("Başlık");
        TableColumn<Book, String>     authorCol = new TableColumn<>("Yazar");
        TableColumn<Book, String>     genreCol  = new TableColumn<>("Tür");
        TableColumn<Book, String>     isbnCol   = new TableColumn<>("ISBN");
        TableColumn<Book, BigDecimal> priceCol  = new TableColumn<>("Fiyat (₺)");
        TableColumn<Book, Integer>    stockCol  = new TableColumn<>("Stok");

        idCol.setCellValueFactory(c    -> c.getValue().idProperty().asObject());
        titleCol.setCellValueFactory(c  -> c.getValue().titleProperty());
        authorCol.setCellValueFactory(c -> c.getValue().authorProperty());
        genreCol.setCellValueFactory(c  -> c.getValue().genreProperty());
        isbnCol.setCellValueFactory(c   -> c.getValue().isbnProperty());
        priceCol.setCellValueFactory(c  -> c.getValue().priceProperty());
        stockCol.setCellValueFactory(c  -> c.getValue().stockQuantityProperty().asObject());

        idCol.setPrefWidth(45);    idCol.setMinWidth(40);
        titleCol.setPrefWidth(220);
        authorCol.setPrefWidth(160);
        genreCol.setPrefWidth(130);
        isbnCol.setPrefWidth(130);
        priceCol.setPrefWidth(95);
        stockCol.setPrefWidth(65);

        genreCol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String v, boolean empty) {
                super.updateItem(v, empty);
                if (empty || v == null || v.isBlank()) { setGraphic(null); setText(null); return; }
                Label badge = new Label(v);
                badge.getStyleClass().add("genre-badge");
                setGraphic(badge);
                setText(null);
            }
        });

        priceCol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(BigDecimal v, boolean empty) {
                super.updateItem(v, empty);
                if (empty || v == null) { setText(null); setStyle(""); return; }
                setText(v.toPlainString() + " ₺");
                setStyle("-fx-alignment: center-right; -fx-font-weight: bold;");
            }
        });

        stockCol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer v, boolean empty) {
                super.updateItem(v, empty);
                if (empty || v == null) { setText(null); setStyle(""); return; }
                setText(String.valueOf(v));
                if (v == 0) {
                    setStyle("-fx-text-fill: #9ca3af; -fx-alignment: center;");
                } else if (v <= 3) {
                    setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold; -fx-alignment: center;");
                } else {
                    setStyle("-fx-text-fill: #16a34a; -fx-font-weight: bold; -fx-alignment: center;");
                }
            }
        });

        TableColumn<Book, Void> actionsCol = new TableColumn<>("İşlemler");
        actionsCol.setPrefWidth(145);
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn   = new Button("✎ Düzenle");
            private final Button deleteBtn = new Button("✕ Sil");
            {
                editBtn.getStyleClass().add("btn-secondary");
                deleteBtn.getStyleClass().add("btn-danger");
                editBtn.setStyle("-fx-font-size: 11px; -fx-padding: 5px 10px;");
            }
            @Override
            protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Book book = getTableRow().getItem();
                    editBtn.setOnAction(e -> showBookForm(book));
                    deleteBtn.setOnAction(e -> confirmDelete(book));
                    setGraphic(new HBox(4, editBtn, deleteBtn));
                }
            }
        });

        TableView<Book> tv = new TableView<>(filteredBooks);
        tv.getColumns().addAll(idCol, titleCol, authorCol, genreCol, isbnCol, priceCol, stockCol, actionsCol);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tv.setPlaceholder(new Label("Henüz kitap eklenmemiş."));
        VBox.setVgrow(tv, Priority.ALWAYS);
        return tv;
    }

    private HBox buildStatusBar() {
        statusLabel.getStyleClass().add("status-text");
        HBox bar = new HBox(statusLabel);
        bar.getStyleClass().add("status-bar");
        bar.setAlignment(Pos.CENTER_LEFT);
        return bar;
    }

    private void applyFilter(String val) {
        String lower = val == null ? "" : val.toLowerCase();
        filteredBooks.setPredicate(b ->
            lower.isBlank()
            || (b.getTitle()  != null && b.getTitle().toLowerCase().contains(lower))
            || (b.getAuthor() != null && b.getAuthor().toLowerCase().contains(lower))
            || (b.getIsbn()   != null && b.getIsbn().toLowerCase().contains(lower))
        );
        updateCount();
    }

    private void updateCount() {
        int shown = filteredBooks.size(), total = books.size();
        countBadge.setText(shown == total ? total + " kitap" : shown + " / " + total);
    }

    public void loadBooks() {
        if (activeTask != null) activeTask.cancel();
        statusLabel.setText("Yükleniyor...");
        spinner.setVisible(true);

        Task<List<Book>> task = new Task<>() {
            @Override protected List<Book> call() throws Exception {
                return apiService.findAll();
            }
        };

        task.setOnSucceeded(e -> {
            books.setAll(task.getValue());
            updateCount();
            statusLabel.setText("Son güncelleme: "
                + LocalTime.now().truncatedTo(ChronoUnit.SECONDS));
            spinner.setVisible(false);
        });

        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            String msg = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
            statusLabel.setText("Hata: " + msg);
            spinner.setVisible(false);
            if (ex instanceof java.net.ConnectException) showConnectionError();
        });

        activeTask = task;
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    private void showConnectionError() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Bağlantı Hatası");
        alert.setHeaderText("Backend servise ulaşılamıyor.");
        alert.setContentText(
            "http://localhost:8080 adresinde book-service'in\n" +
            "çalıştığından emin olun, ardından Yenile'ye tıklayın.");
        alert.showAndWait();
    }

    public TableView<Book> getTableView() { return tableView; }

    private void showBookForm(Book existing) {
        boolean isNew = existing == null;
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle(isNew ? "Yeni Kitap" : "Kitap Düzenle");
        dialog.setHeaderText(isNew ? "Yeni kitap ekle" : "Kitap bilgilerini düzenle");

        ButtonType saveType = new ButtonType("Kaydet", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(16));

        TextField titleF  = new TextField(isNew ? "" : nvl(existing.getTitle()));
        TextField authorF = new TextField(isNew ? "" : nvl(existing.getAuthor()));
        TextField isbnF   = new TextField(isNew ? "" : nvl(existing.getIsbn()));
        TextField priceF  = new TextField(isNew ? "" : (existing.getPrice() != null ? existing.getPrice().toPlainString() : ""));
        TextField stockF  = new TextField(isNew ? "" : (existing.getStockQuantity() != null ? String.valueOf(existing.getStockQuantity()) : ""));

        ComboBox<String> genreCombo = new ComboBox<>();
        genreCombo.getItems().addAll(GENRES);
        genreCombo.setEditable(false);
        genreCombo.setPromptText("Tür seçin...");
        genreCombo.setPrefWidth(240);
        if (!isNew && existing.getGenre() != null) genreCombo.setValue(existing.getGenre());

        for (TextField tf : List.of(titleF, authorF, isbnF, priceF, stockF)) {
            tf.setPrefWidth(240);
        }

        grid.addRow(0, new Label("Başlık *"),    titleF);
        grid.addRow(1, new Label("Yazar *"),     authorF);
        grid.addRow(2, new Label("Tür"),         genreCombo);
        grid.addRow(3, new Label("ISBN"),        isbnF);
        grid.addRow(4, new Label("Fiyat (₺) *"), priceF);
        grid.addRow(5, new Label("Stok *"),      stockF);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(titleF::requestFocus);

        dialog.setResultConverter(btn -> {
            if (btn != saveType) return null;
            try {
                Book b = isNew ? new Book() : existing;
                b.setTitle(titleF.getText().trim());
                b.setAuthor(authorF.getText().trim());
                b.setIsbn(isbnF.getText().trim().isEmpty() ? null : isbnF.getText().trim());
                b.setPrice(new java.math.BigDecimal(priceF.getText().trim()));
                b.setStockQuantity(Integer.parseInt(stockF.getText().trim()));
                b.setGenre(genreCombo.getValue());
                return b;
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Geçersiz değer: " + ex.getMessage()).showAndWait();
                return null;
            }
        });

        dialog.showAndWait().ifPresent(book -> {
            Task<Book> task = new Task<>() {
                @Override protected Book call() throws Exception {
                    return isNew ? apiService.create(book) : apiService.update(existing.getId(), book);
                }
            };
            task.setOnSucceeded(e -> loadBooks());
            task.setOnFailed(e -> new Alert(Alert.AlertType.ERROR,
                    "Kayıt hatası: " + task.getException().getMessage()).showAndWait());
            new Thread(task) {{ setDaemon(true); }}.start();
        });
    }

    private void confirmDelete(Book book) {
        if (book == null) return;
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "\"" + book.getTitle() + "\" silinsin mi?", ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Kitabı Sil");
        confirm.setHeaderText("Bu işlem geri alınamaz.");
        confirm.showAndWait().filter(b -> b == ButtonType.YES).ifPresent(b -> {
            Task<Void> task = new Task<>() {
                @Override protected Void call() throws Exception {
                    apiService.deleteById(book.getId()); return null;
                }
            };
            task.setOnSucceeded(e -> loadBooks());
            task.setOnFailed(e -> new Alert(Alert.AlertType.ERROR,
                    "Silme hatası: " + task.getException().getMessage()).showAndWait());
            new Thread(task) {{ setDaemon(true); }}.start();
        });
    }

    private String nvl(String s) { return s != null ? s : ""; }
}
