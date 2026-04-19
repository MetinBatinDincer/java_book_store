package com.bookstore.ui.util;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class CardValidator {

    private CardValidator() {}

    public static boolean isValidLuhn(String raw) {
        String digits = raw.replaceAll("\\s", "");
        return digits.matches("\\d{13,19}");
    }

    public static boolean isValidExpiry(String expiry) {
        if (expiry == null || !expiry.matches("\\d{2}/\\d{2}")) return false;
        try {
            YearMonth exp = YearMonth.parse("20" + expiry.substring(3) + "-" + expiry.substring(0, 2),
                    DateTimeFormatter.ofPattern("yyyy-MM"));
            return !exp.isBefore(YearMonth.now());
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidCvv(String cvv) {
        return cvv != null && cvv.matches("\\d{3,4}");
    }

    public static String format(String raw) {
        String digits = raw.replaceAll("\\D", "");
        if (digits.length() > 16) digits = digits.substring(0, 16);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digits.length(); i++) {
            if (i > 0 && i % 4 == 0) sb.append(' ');
            sb.append(digits.charAt(i));
        }
        return sb.toString();
    }
}
