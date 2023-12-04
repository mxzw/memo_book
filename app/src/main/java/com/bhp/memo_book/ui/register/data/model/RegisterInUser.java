package com.bhp.memo_book.ui.register.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class RegisterInUser {

    private String userId;
    private String displayName;

    public RegisterInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}