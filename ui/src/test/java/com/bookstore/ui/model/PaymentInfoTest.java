package com.bookstore.ui.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentInfoTest {

    @Test
    void defaultConstructor_saveCardIsFalse() {
        PaymentInfo info = new PaymentInfo();
        assertFalse(info.isSaveCard());
    }

    @Test
    void settersAndGetters_storeValues() {
        PaymentInfo info = new PaymentInfo();
        info.setCardHolder("Ahmet Yılmaz");
        info.setCardNumber("4111111111111111");
        info.setExpiry("12/27");
        info.setCvv("123");
        info.setSaveCard(true);

        assertEquals("Ahmet Yılmaz",       info.getCardHolder());
        assertEquals("4111111111111111",   info.getCardNumber());
        assertEquals("12/27",              info.getExpiry());
        assertEquals("123",                info.getCvv());
        assertTrue(info.isSaveCard());
    }

    @Test
    void setSaveCard_false_remainsFalse() {
        PaymentInfo info = new PaymentInfo();
        info.setSaveCard(true);
        info.setSaveCard(false);
        assertFalse(info.isSaveCard());
    }

    @Test
    void nullCardHolder_doesNotThrow() {
        PaymentInfo info = new PaymentInfo();
        assertDoesNotThrow(() -> info.setCardHolder(null));
        assertNull(info.getCardHolder());
    }
}
