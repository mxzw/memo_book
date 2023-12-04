package com.bhp.memo_book.ui.register;

/**
 * Class exposing authenticated user details to the UI.
 */
class RegisterInUserView {
    private String displayName;
    private String uid;
    //... other data fields that may be accessible to the UI

    RegisterInUserView(String uid, String displayName) {
        this.uid = uid;
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }

    String getUid() {
        return uid;
    }
}