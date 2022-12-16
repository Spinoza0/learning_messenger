package com.spinoza.messenger.users;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spinoza.messenger.ConstantStrings;

import java.util.ArrayList;
import java.util.List;

public class UsersViewModel extends ViewModel {

    private final FirebaseAuth auth;
    private final DatabaseReference usersReference;

    private final MutableLiveData<FirebaseUser> user = new MutableLiveData<>();
    private final MutableLiveData<List<User>> users = new MutableLiveData<>();

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    public LiveData<List<User>> getUsers() {
        return users;
    }

    public UsersViewModel() {
        auth = FirebaseAuth.getInstance();
        usersReference = FirebaseDatabase.getInstance().getReference(ConstantStrings.TABLE_USERS);
        auth.addAuthStateListener(firebaseAuth -> user.setValue(firebaseAuth.getCurrentUser()));

        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser != null) {
                    List<User> usersFromDb = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null && !user.getId().equals(currentUser.getUid())) {
                            usersFromDb.add(user);
                        }
                    }
                    users.setValue(usersFromDb);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void logout() {
        setUserOnline(false);
        auth.signOut();
    }

    public void setUserOnline(boolean isOnline) {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            usersReference
                    .child(firebaseUser.getUid())
                    .child(ConstantStrings.TABLE_KEY_ONLINE)
                    .setValue(isOnline);
        }
    }
}
