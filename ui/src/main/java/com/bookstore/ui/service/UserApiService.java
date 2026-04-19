package com.bookstore.ui.service;

import com.bookstore.ui.dto.ApiResponse;
import com.bookstore.ui.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class UserApiService {

    private static final String BASE = "http://localhost:8080/api";
    private static final HttpClient HTTP = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10)).build();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public User login(String email, String password) throws Exception {
        ObjectNode body = MAPPER.createObjectNode();
        body.put("email", email);
        body.put("password", password);

        HttpResponse<String> resp = HTTP.send(
                post(BASE + "/auth/login", MAPPER.writeValueAsString(body)),
                HttpResponse.BodyHandlers.ofString());

        if (resp.statusCode() == 401) throw new Exception("E-posta veya sifre hatali");

        ApiResponse<User> ar = MAPPER.readValue(resp.body(), new TypeReference<>() {});
        return ar.getData();
    }

    public User register(String name, String email, String password) throws Exception {
        ObjectNode body = MAPPER.createObjectNode();
        body.put("name", name);
        body.put("email", email);
        body.put("password", password);
        body.put("role", "CUSTOMER");

        HttpResponse<String> resp = HTTP.send(
                post(BASE + "/users", MAPPER.writeValueAsString(body)),
                HttpResponse.BodyHandlers.ofString());

        if (resp.statusCode() == 409) throw new Exception("Bu e-posta zaten kayitli");

        ApiResponse<User> ar = MAPPER.readValue(resp.body(), new TypeReference<>() {});
        return ar.getData();
    }

    private HttpRequest post(String url, String json) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
    }
}
