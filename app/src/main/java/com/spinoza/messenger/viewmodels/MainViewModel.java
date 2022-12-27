package com.spinoza.messenger.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.spinoza.messenger.data.FirebaseResult;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<FirebaseResult> loginResult = new MutableLiveData<>();
    private final FirebaseAuth auth;

    public LiveData<FirebaseResult> getLoginResult() {
        return loginResult;
    }


    public void setLoginResult(FirebaseResult.Type type, String text, FirebaseUser firebaseUser) {
        loginResult.setValue(new FirebaseResult(type, text, firebaseUser));
    }


    public MainViewModel() {
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(firebaseAuth -> {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                setLoginResult(FirebaseResult.Type.SUCCESS, "", firebaseUser);
            }
        });
    }

    public void login(String emailFromDialog, String passwordFromDialog) {
        String email = emailFromDialog.trim();
        String password = passwordFromDialog.trim();
        if (email.isEmpty() || password.isEmpty()) {
            setLoginResult(FirebaseResult.Type.ERROR_DATA_EMPTY, "", null);
        } else {
            if (auth == null) {
                setLoginResult(FirebaseResult.Type.ERROR_DATABASE_CONNECT, "", null);
            } else {
                auth.signOut();
                auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(authResult -> setLoginResult(
                                FirebaseResult.Type.SUCCESS,
                                "",
                                authResult.getUser()
                        )).addOnFailureListener(e -> setLoginResult(
                                FirebaseResult.Type.ERROR_LOGIN,
                                e.getMessage(),
                                null
                        ));
            }
        }
    }
}
