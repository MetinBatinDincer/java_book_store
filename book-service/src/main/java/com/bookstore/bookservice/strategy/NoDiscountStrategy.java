package com.bookstore.bookservice.strategy;

import java.math.BigDecimal;

// DiscountStrategy'yi implement eder; indirim uygulamaz, fiyatı aynen döner.
public class NoDiscountStrategy implements DiscountStrategy {

    @Override
    public BigDecimal apply(BigDecimal totalPrice) {
        return totalPrice;
    }

    @Override
    public String getName() {
        return "NO_DISCOUNT";
    }
}
