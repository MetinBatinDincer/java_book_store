package com.bookstore.ui.service;

import com.bookstore.ui.dto.ApiResponse;
import com.bookstore.ui.model.Book;
import com.bookstore.ui.model.CartItem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class BookApiService {

    private static final String     BASE_URL   = "http://localhost:8080/api/books";
    private static final HttpClient HTTP       = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private static final ObjectMapper MAPPER   = new ObjectMapper();

    public List<Book> findAll() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .header("Accept", "application/json")
                .build();

        String body = HTTP.send(req, HttpResponse.BodyHandlers.ofString()).body();
        ApiResponse<List<Book>> resp = MAPPER.readValue(body,
                new TypeReference<>() {});
        return resp.getData();
    }

    public Book create(Book book) throws Exception {
        String payload = MAPPER.writeValueAsString(book);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();

        String body = HTTP.send(req, HttpResponse.BodyHandlers.ofString()).body();
        ApiResponse<Book> resp = MAPPER.readValue(body, new TypeReference<>() {});
        return resp.getData();
    }

    public void deleteById(Long id) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .header("Accept", "application/json")
                .build();

        HTTP.send(req, HttpResponse.BodyHandlers.discarding());
    }

    /** Kitab\u0131 g\u00fcnceller \u2014 {@code PUT /api/books/{id}} */
    public Book update(Long id, Book book) throws Exception {
        String payload = MAPPER.writeValueAsString(book);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .PUT(HttpRequest.BodyPublishers.ofString(payload))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();
        String body = HTTP.send(req, HttpResponse.BodyHandlers.ofString()).body();
        ApiResponse<Book> resp = MAPPER.readValue(body, new TypeReference<>() {});
        return resp.getData();
    }

    /** Sipari\u015f olu\u015fturur \u2014 {@code POST /api/orders/user/{userId}} */
    public void createOrder(Long userId, List<CartItem> items) throws Exception {
        // Sadece id ve quantity g\u00f6nder
        var nodes = items.stream().map(ci -> {
            ObjectNode node = MAPPER.createObjectNode();
            node.putObject("book").put("id", ci.getBook().getId());
            node.put("quantity", ci.getQuantity());
            return node;
        }).toList();
        String payload = MAPPER.writeValueAsString(nodes);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/orders/user/" + userId))
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();
        HTTP.send(req, HttpResponse.BodyHandlers.discarding());
    }
}
