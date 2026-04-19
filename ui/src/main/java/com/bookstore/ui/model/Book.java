package com.bookstore.ui.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.beans.property.*;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private Integer stockQuantity;
    private String genre;

    public Book() {}

    // ── Getter / Setter ──────────────────────────────────────────────────────
    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }

    public String getTitle()                   { return title; }
    public void setTitle(String title)         { this.title = title; }

    public String getAuthor()                  { return author; }
    public void setAuthor(String author)       { this.author = author; }

    public String getIsbn()                    { return isbn; }
    public void setIsbn(String isbn)           { this.isbn = isbn; }

    public BigDecimal getPrice()               { return price; }
    public void setPrice(BigDecimal price)     { this.price = price; }

    public Integer getStockQuantity()                      { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity)    { this.stockQuantity = stockQuantity; }

    public String getGenre()                   { return genre; }
    public void setGenre(String genre)         { this.genre = genre; }

    // ── JavaFX Property erişimcileri ─────────────────────────────────────────
    public LongProperty idProperty() {
        if (id == null) return new SimpleLongProperty(0L);
        return new SimpleLongProperty(id);
    }
    public StringProperty titleProperty()      { return new SimpleStringProperty(title); }
    public StringProperty authorProperty()     { return new SimpleStringProperty(author); }
    public StringProperty isbnProperty()       { return new SimpleStringProperty(isbn); }
    public StringProperty genreProperty()      { return new SimpleStringProperty(genre); }
    public ObjectProperty<BigDecimal> priceProperty() {
        return new SimpleObjectProperty<>(price);
    }
    public IntegerProperty stockQuantityProperty() {
        return new SimpleIntegerProperty(stockQuantity == null ? 0 : stockQuantity.intValue());
    }

    @Override
    public String toString() {
        return "Book{id=%d, title='%s', author='%s'}".formatted(id, title, author);
    }
}
