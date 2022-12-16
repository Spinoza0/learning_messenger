package com.spinoza.messenger;

import com.google.firebase.auth.FirebaseUser;

public class FirebaseResult {

    public enum Type {
        NONE,
        SUCCESS,
        ERROR_DATA_EMPTY,
        ERROR_DATABASE_CONNECT,
        ERROR_LOGIN,
        ERROR_RESET,
        ERROR_REGISTRATION,
        ERROR_REGISTRATION_NO_UID
    }

    private final Type type;
    private final String text;
    private final FirebaseUser firebaseUser;

    public Type getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public FirebaseResult(Type type, String text, FirebaseUser firebaseUser) {
        this.type = type;
        this.text = text;
        this.firebaseUser = firebaseUser;
    }

}
