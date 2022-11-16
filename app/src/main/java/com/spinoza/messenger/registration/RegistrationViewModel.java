package com.spinoza.messenger.registration;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spinoza.messenger.ConstantStrings;
import com.spinoza.messenger.FirebaseResult;
import com.spinoza.messenger.users.User;

public class RegistrationViewModel extends ViewModel {

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersReference;

    private MutableLiveData<FirebaseResult> registrationResult = new MutableLiveData<>();

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
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersReference = firebaseDatabase.getReference(ConstantStrings.TABLE_USERS);
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
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
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
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            setRegistrationResult(
                                    FirebaseResult.Type.ERROR_REGISTRATION,
                                    e.getMessage(),
                                    null
                            );
                        }
                    });
        }
    }
}