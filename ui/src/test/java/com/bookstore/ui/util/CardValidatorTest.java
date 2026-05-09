package com.bookstore.ui.util;

import org.junit.jupiter.api.Test;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class CardValidatorTest {

    // ── isValidLuhn ───────────────────────────────────────────────────────────

    @Test
    void luhn_validSixteenDigits_returnsTrue() {
        assertTrue(CardValidator.isValidLuhn("4111111111111111"));
    }

    @Test
    void luhn_validWithSpaces_returnsTrue() {
        assertTrue(CardValidator.isValidLuhn("4111 1111 1111 1111"));
    }

    @Test
    void luhn_thirteenDigits_returnsTrue() {
        assertTrue(CardValidator.isValidLuhn("4000000000000"));
    }

    @Test
    void luhn_tooShort_returnsFalse() {
        assertFalse(CardValidator.isValidLuhn("123456789012"));
    }

    @Test
    void luhn_containsLetters_returnsFalse() {
        assertFalse(CardValidator.isValidLuhn("411111111111111X"));
    }

    @Test
    void luhn_empty_returnsFalse() {
        assertFalse(CardValidator.isValidLuhn(""));
    }

    // ── isValidExpiry ─────────────────────────────────────────────────────────

    @Test
    void expiry_futureDate_returnsTrue() {
        YearMonth future = YearMonth.now().plusMonths(6);
        String expiry = future.format(DateTimeFormatter.ofPattern("MM/yy"));
        assertTrue(CardValidator.isValidExpiry(expiry));
    }

    @Test
    void expiry_currentMonth_returnsTrue() {
        YearMonth now = YearMonth.now();
        String expiry = now.format(DateTimeFormatter.ofPattern("MM/yy"));
        assertTrue(CardValidator.isValidExpiry(expiry));
    }

    @Test
    void expiry_pastDate_returnsFalse() {
        assertFalse(CardValidator.isValidExpiry("01/20"));
    }

    @Test
    void expiry_wrongFormat_returnsFalse() {
        assertFalse(CardValidator.isValidExpiry("2025/01"));
    }

    @Test
    void expiry_null_returnsFalse() {
        assertFalse(CardValidator.isValidExpiry(null));
    }

    @Test
    void expiry_empty_returnsFalse() {
        assertFalse(CardValidator.isValidExpiry(""));
    }

    // ── isValidCvv ────────────────────────────────────────────────────────────

    @Test
    void cvv_threeDigits_returnsTrue() {
        assertTrue(CardValidator.isValidCvv("123"));
    }

    @Test
    void cvv_fourDigits_returnsTrue() {
        assertTrue(CardValidator.isValidCvv("1234"));
    }

    @Test
    void cvv_twoDigits_returnsFalse() {
        assertFalse(CardValidator.isValidCvv("12"));
    }

    @Test
    void cvv_containsLetters_returnsFalse() {
        assertFalse(CardValidator.isValidCvv("12A"));
    }

    @Test
    void cvv_null_returnsFalse() {
        assertFalse(CardValidator.isValidCvv(null));
    }

    // ── format ────────────────────────────────────────────────────────────────

    @Test
    void format_rawDigits_addsSpacesEveryFour() {
        assertEquals("4111 1111 1111 1111", CardValidator.format("4111111111111111"));
    }

    @Test
    void format_digitsWithSpaces_normalizesCorrectly() {
        assertEquals("4111 1111 1111 1111", CardValidator.format("4111 1111 1111 1111"));
    }

    @Test
    void format_moreThanSixteenDigits_truncatesToSixteen() {
        String result = CardValidator.format("41111111111111119999");
        assertEquals("4111 1111 1111 1111", result);
    }

    @Test
    void format_shortNumber_noTrailingSpace() {
        String result = CardValidator.format("1234");
        assertEquals("1234", result);
        assertFalse(result.endsWith(" "));
    }
}
