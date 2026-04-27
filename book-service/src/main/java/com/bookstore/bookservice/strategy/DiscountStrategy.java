package com.bookstore.bookservice.strategy;

import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal apply(BigDecimal totalPrice);
    String getName();
}
