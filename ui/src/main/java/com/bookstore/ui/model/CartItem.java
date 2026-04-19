package com.bookstore.ui.model;

import javafx.beans.property.*;

import java.math.BigDecimal;

public class CartItem {

    private final Book     book;
    private final IntegerProperty quantity = new SimpleIntegerProperty(1);

    public CartItem(Book book) { this.book = book; }

    public Book          getBook()     { return book; }
    public int           getQuantity() { return quantity.get(); }
    public IntegerProperty quantityProperty() { return quantity; }

    public void increment() {
        if (quantity.get() < book.getStockQuantity()) quantity.set(quantity.get() + 1);
    }
    public void decrement() {
        if (quantity.get() > 1) quantity.set(quantity.get() - 1);
    }

    public BigDecimal subtotal() {
        return book.getPrice() == null ? BigDecimal.ZERO
                : book.getPrice().multiply(BigDecimal.valueOf(quantity.get()));
    }
}
