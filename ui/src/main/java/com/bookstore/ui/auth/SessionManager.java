package com.bookstore.ui.auth;

import com.bookstore.ui.model.User;

public class SessionManager {

    private static final SessionManager INSTANCE = new SessionManager();
    private User currentUser;

    private SessionManager() {}

    public static SessionManager getInstance() { return INSTANCE; }

    public void login(User user)  { this.currentUser = user; }
    public void logout()          { this.currentUser = null; }
    public User getCurrentUser()  { return currentUser; }
    public boolean isLoggedIn()   { return currentUser != null; }
    public boolean isGuest()      { return currentUser == null; }

    public boolean isAdmin() {
        return currentUser != null && "ADMIN".equals(currentUser.getRole());
    }

    public String getDisplayName() {
        if (currentUser == null) return "Misafir";
        return currentUser.getName() != null ? currentUser.getName() : currentUser.getEmail();
    }

    public Long getUserId() {
        return currentUser != null ? currentUser.getId() : 1L;
    }
}
