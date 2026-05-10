package com.bookstore.bookservice.strategy;

import java.math.BigDecimal;

// Strategy Pattern — indirim davranışını soyutlar.
// NoDiscount, PercentageDiscount ve FixedDiscount bu interface'i implement eder.
public interface DiscountStrategy {
    BigDecimal apply(BigDecimal totalPrice);
    String getName();
}
