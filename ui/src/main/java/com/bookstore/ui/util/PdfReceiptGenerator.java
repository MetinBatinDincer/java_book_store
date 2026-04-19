package com.bookstore.ui.util;

import com.bookstore.ui.model.CartItem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/** Harici kütüphane gerektirmeden saf Java ile PDF makbuzu üretir. */
public class PdfReceiptGenerator {

    private PdfReceiptGenerator() {}

    public static void generate(File file, List<CartItem> items,
                                BigDecimal total, String buyer) throws IOException {
        Files.write(file.toPath(), buildPdf(items, total, buyer));
    }

    private static byte[] buildPdf(List<CartItem> items, BigDecimal total, String buyer) {
        String cs = buildContentStream(items, total, buyer);
        byte[] csBytes = cs.getBytes(StandardCharsets.US_ASCII);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int[] offsets = new int[5];

        w(out, "%PDF-1.4\n");

        offsets[0] = out.size();
        w(out, "1 0 obj\n<</Type /Catalog /Pages 2 0 R>>\nendobj\n");

        offsets[1] = out.size();
        w(out, "2 0 obj\n<</Type /Pages /Kids [3 0 R] /Count 1>>\nendobj\n");

        offsets[2] = out.size();
        w(out, "3 0 obj\n<</Type /Page /Parent 2 0 R /MediaBox [0 0 595 842]"
                + " /Contents 4 0 R /Resources <</Font <</F1 5 0 R>>>>>>\nendobj\n");

        offsets[3] = out.size();
        w(out, "4 0 obj\n<</Length " + csBytes.length + ">>\nstream\n");
        try { out.write(csBytes); } catch (IOException ignored) {}
        w(out, "\nendstream\nendobj\n");

        offsets[4] = out.size();
        w(out, "5 0 obj\n<</Type /Font /Subtype /Type1 /BaseFont /Courier>>\nendobj\n");

        int xref = out.size();
        w(out, "xref\n0 6\n0000000000 65535 f \n");
        for (int off : offsets) w(out, String.format("%010d 00000 n \n", off));
        w(out, "trailer\n<</Size 6 /Root 1 0 R>>\nstartxref\n" + xref + "\n%%EOF\n");

        return out.toByteArray();
    }

    private static String buildContentStream(List<CartItem> items,
                                              BigDecimal total, String buyer) {
        List<String> lines = new ArrayList<>();
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        lines.add("DIJITAL KITAP SATIS MAKBUZU");
        lines.add("==========================================");
        lines.add("");
        lines.add("Tarih   : " + now);
        lines.add("Musteri : " + ascii(buyer));
        lines.add("");
        lines.add(String.format("%-28s %4s  %10s", "KITAP", "ADET", "TOPLAM"));
        lines.add("------------------------------------------");
        for (CartItem ci : items) {
            String title = truncate(ascii(ci.getBook().getTitle()), 28);
            lines.add(String.format("%-28s %4d  %8.2f TL",
                    title, ci.getQuantity(), ci.subtotal()));
        }
        lines.add("------------------------------------------");
        lines.add(String.format("%-33s %8.2f TL", "GENEL TOPLAM:", total));
        lines.add("==========================================");
        lines.add("");
        lines.add("Bu belge dijital makbuz niteligi tasir.");
        lines.add("TBL324 - Ileri Java Uygulamalari");
        lines.add("Kocaeli Universitesi - 2026");

        StringBuilder sb = new StringBuilder("BT\n/F1 10 Tf\n12 TL\n50 790 Td\n");
        for (String line : lines) sb.append("(").append(escape(line)).append(") Tj T*\n");
        sb.append("ET\n");
        return sb.toString();
    }

    private static String ascii(String s) {
        if (s == null) return "";
        return s.replace("ş","s").replace("Ş","S").replace("ı","i").replace("İ","I")
                .replace("ğ","g").replace("Ğ","G").replace("ü","u").replace("Ü","U")
                .replace("ö","o").replace("Ö","O").replace("ç","c").replace("Ç","C");
    }

    private static String escape(String s) {
        return s.replace("\\","\\\\").replace("(","\\(").replace(")","\\)");
    }

    private static String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 1) + ".";
    }

    private static void w(ByteArrayOutputStream out, String text) {
        try { out.write(text.getBytes(StandardCharsets.US_ASCII)); }
        catch (IOException ignored) {}
    }
}
