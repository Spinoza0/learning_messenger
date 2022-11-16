package com.spinoza.messenger.resetpassword;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.spinoza.messenger.FirebaseResult;

public class ResetPasswordViewModel extends ViewModel {
    private FirebaseAuth auth;
    private MutableLiveData<FirebaseResult> resetResult = new MutableLiveData<>();

    public LiveData<FirebaseResult> getResetResult() {
        return resetResult;
    }

    public void setResetResult(FirebaseResult.Type type, String text) {
        resetResult.setValue(new FirebaseResult(type, text, null));
    }

    public ResetPasswordViewModel() {
        auth = FirebaseAuth.getInstance();
    }

    public void resetPassword(String emailFromDialog) {
        String email = emailFromDialog.trim();
        if (email.isEmpty()) {
            setResetResult(FirebaseResult.Type.ERROR_DATA_EMPTY, "");
        } else if (auth == null) {
            setResetResult(FirebaseResult.Type.ERROR_DATABASE_CONNECT, "");
        } else {
            auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    setResetResult(FirebaseResult.Type.SUCCESS, "");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    setResetResult(FirebaseResult.Type.ERROR_RESET, e.getMessage());
                }
            });
        }
    }
}
