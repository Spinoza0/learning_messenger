package com.spinoza.messenger.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spinoza.messenger.data.ConstantStrings;
import com.spinoza.messenger.data.FirebaseResult;
import com.spinoza.messenger.data.User;

public class RegistrationViewModel extends ViewModel {

    private final FirebaseAuth auth;
    private final DatabaseReference usersReference;

    private final MutableLiveData<FirebaseResult> registrationResult = new MutableLiveData<>();

    public LiveData<FirebaseResult> getRegistrationResult() {
        return registrationResult;
    }

    public void setRegistrationResult(
            FirebaseResult.Type type,
            String text,
            FirebaseUser firebaseUser) {
        this.registrationResult.setValue(new FirebaseResult(type, text, firebaseUser));
    }

    public RegistrationViewModel() {
        auth = FirebaseAuth.getInstance();
        usersReference = FirebaseDatabase.getInstance().getReference(ConstantStrings.TABLE_USERS);
    }

    public void signUp(User user, String emailFromDialog, String passwordFromDialog) {
        String email = User.trimString(emailFromDialog);
        String password = User.trimString(passwordFromDialog);
        if (email.isEmpty() ||
                password.isEmpty() ||
                user.getName().isEmpty() ||
                user.getLastname().isEmpty() ||
                (user.getAge() == 0)) {
            setRegistrationResult(FirebaseResult.Type.ERROR_DATA_EMPTY, "", null);
        } else if (auth == null) {
            setRegistrationResult(
                    FirebaseResult.Type.ERROR_DATABASE_CONNECT,
                    "",
                    null
            );
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser firebaseUser = authResult.getUser();
                        if (firebaseUser != null) {
                            user.setId(firebaseUser.getUid());
                            usersReference.child(user.getId()).setValue(user);
                            setRegistrationResult(
                                    FirebaseResult.Type.SUCCESS,
                                    "",
                                    firebaseUser
                            );
                        } else {
                            setRegistrationResult(
                                    FirebaseResult.Type.ERROR_REGISTRATION_NO_UID,
                                    "",
                                    null
                            );
                        }
                    }).addOnFailureListener(e -> setRegistrationResult(
                            FirebaseResult.Type.ERROR_REGISTRATION,
                            e.getMessage(),
                            null
                    ));
        }
    }
}