package com.bookstore.ui.contract;

import com.bookstore.ui.model.PaymentInfo;
import java.math.BigDecimal;

public interface Payable {
    BigDecimal getTotal();
    boolean processPayment(PaymentInfo payment);
}
