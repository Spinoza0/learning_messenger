package com.spinoza.messenger.chat;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spinoza.messenger.ConstantStrings;
import com.spinoza.messenger.users.User;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {

    private final MutableLiveData<List<Message>> messages = new MutableLiveData<>();
    private final MutableLiveData<User> otherUser = new MutableLiveData<>();
    private final MutableLiveData<Boolean> messageSent = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference referenceUsers = firebaseDatabase.getReference(
            ConstantStrings.TABLE_USERS
    );
    private final DatabaseReference referenceMessages = firebaseDatabase.getReference(
            ConstantStrings.TABLE_MESSAGES
    );

    private final String currentUserId;

    public LiveData<List<Message>> getMessages() {
        return messages;
    }

    public LiveData<User> getOtherUser() {
        return otherUser;
    }

    public LiveData<Boolean> getMessageSent() {
        return messageSent;
    }

    public LiveData<String> getError() {
        return error;
    }

    public ChatViewModel(String currentUserId, String otherUserId) {
        this.currentUserId = currentUserId;
        referenceUsers.child(otherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                otherUser.setValue(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError errorDb) {
                error.setValue(errorDb.getMessage());
            }
        });
        referenceMessages.child(currentUserId).child(otherUserId).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Message> messageList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            messageList.add(dataSnapshot.getValue(Message.class));
                        }
                        messages.setValue(messageList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError errorDb) {
                        error.setValue(errorDb.getMessage());
                    }
                });
    }

    public void setUserOnline(boolean isOnline) {
        referenceUsers
                .child(currentUserId)
                .child(ConstantStrings.TABLE_KEY_ONLINE)
                .setValue(isOnline);
    }

    public void sentMessage(Message message) {
        referenceMessages
                .child(message.getSenderId())
                .child(message.getReceiverId())
                .push()
                .setValue(message)
                .addOnSuccessListener(unused -> referenceMessages
                        .child(message.getReceiverId())
                        .child(message.getSenderId())
                        .push()
                        .setValue(message)
                        .addOnSuccessListener(unused1 -> messageSent.setValue(true))
                        .addOnFailureListener(e -> error.setValue(e.getMessage())))
                .addOnFailureListener(e -> error.setValue(e.getMessage()));
    }
}
