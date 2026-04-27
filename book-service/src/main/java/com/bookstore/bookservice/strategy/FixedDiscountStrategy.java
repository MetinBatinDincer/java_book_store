package com.bookstore.bookservice.strategy;

import java.math.BigDecimal;

public class FixedDiscountStrategy implements DiscountStrategy {

    private final BigDecimal discountAmount;

    public FixedDiscountStrategy(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public BigDecimal apply(BigDecimal totalPrice) {
        BigDecimal result = totalPrice.subtract(discountAmount);
        return result.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : result;
    }

    @Override
    public String getName() {
        return "FIXED_" + discountAmount + "TL";
    }
}
