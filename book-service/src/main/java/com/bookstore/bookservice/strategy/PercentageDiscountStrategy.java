package com.bookstore.bookservice.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PercentageDiscountStrategy implements DiscountStrategy {

    private final BigDecimal percentage;

    public PercentageDiscountStrategy(BigDecimal percentage) {
        this.percentage = percentage;
    }

    @Override
    public BigDecimal apply(BigDecimal totalPrice) {
        BigDecimal discount = totalPrice.multiply(percentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return totalPrice.subtract(discount);
    }

    @Override
    public String getName() {
        return "PERCENTAGE_" + percentage + "%";
    }
}
