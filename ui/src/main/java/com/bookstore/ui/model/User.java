package com.bookstore.ui.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private Long   id;
    private String name;
    private String email;
    private String role;
    private String password;

    // Saved card (in-memory; MongoDB'ye taşınacak ilerleyen aşamada)
    private String savedCardHolder;
    private String savedCardNumber;
    private String savedCardExpiry;

    public User() {}

    public User(Long id, String name, String email, String role) {
        this.id    = id;
        this.name  = name;
        this.email = email;
        this.role  = role;
    }

    // ── Getters / Setters ──────────────────────────────────────────────────

    public Long   getId()       { return id; }
    public void   setId(Long id){ this.id = id; }

    public String getName()           { return name; }
    public void   setName(String n)   { this.name = n; }

    public String getEmail()          { return email; }
    public void   setEmail(String e)  { this.email = e; }

    public String getRole()           { return role; }
    public void   setRole(String r)   { this.role = r; }

    public String getPassword()            { return password; }
    public void   setPassword(String p)    { this.password = p; }

    public String getSavedCardHolder()             { return savedCardHolder; }
    public void   setSavedCardHolder(String v)     { this.savedCardHolder = v; }

    public String getSavedCardNumber()             { return savedCardNumber; }
    public void   setSavedCardNumber(String v)     { this.savedCardNumber = v; }

    public String getSavedCardExpiry()             { return savedCardExpiry; }
    public void   setSavedCardExpiry(String v)     { this.savedCardExpiry = v; }

    public boolean hasSavedCard() {
        return savedCardNumber != null && !savedCardNumber.isBlank();
    }

    public String getMaskedCard() {
        if (!hasSavedCard()) return null;
        String digits = savedCardNumber.replaceAll("\\s", "");
        int len = digits.length();
        return "**** **** **** " + digits.substring(Math.max(0, len - 4));
    }
}
