package com.spinoza.messenger.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.spinoza.messenger.FirebaseResult;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<FirebaseResult> loginResult = new MutableLiveData<>();
    private FirebaseAuth auth;

    public LiveData<FirebaseResult> getLoginResult() {
        return loginResult;
    }


    public void setLoginResult(FirebaseResult.Type type, String text, FirebaseUser firebaseUser) {
        loginResult.setValue(new FirebaseResult(type, text, firebaseUser));
    }


    public LoginViewModel() {
        auth = FirebaseAuth.getInstance();
        if (auth != null) {
            auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        setLoginResult(FirebaseResult.Type.SUCCESS, "", firebaseUser);
                    }
                }
            });
        }
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
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                setLoginResult(
                                        FirebaseResult.Type.SUCCESS,
                                        "",
                                        authResult.getUser()
                                );
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                setLoginResult(
                                        FirebaseResult.Type.ERROR_LOGIN,
                                        e.getMessage(),
                                        null
                                );
                            }
                        });
            }
        }
    }
}
