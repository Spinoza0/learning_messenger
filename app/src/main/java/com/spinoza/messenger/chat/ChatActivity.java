package com.spinoza.messenger.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.spinoza.messenger.ConstantStrings;
import com.spinoza.messenger.R;

public class ChatActivity extends AppCompatActivity {

    private TextView textViewTitle;
    private View viewOnLineStatus;
    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private ImageView imageViewSendMessage;

    private ChatViewModel viewModel;
    private MessagesAdapter messagesAdapter;

    private String currentUserId;
    private String otherUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();

        currentUserId = getIntent().getStringExtra(ConstantStrings.EXTRA_CURRENT_USER);
        otherUserId = getIntent().getStringExtra(ConstantStrings.EXTRA_OTHER_USER);

        ChatViewModelFactory viewModelFactory = new ChatViewModelFactory(currentUserId, otherUserId);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(ChatViewModel.class);

        messagesAdapter = new MessagesAdapter(currentUserId);
        recyclerViewMessages.setAdapter(messagesAdapter);

        setObservers();
        setOnClickListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.setUserOnline(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.setUserOnline(true);
    }

    private void setObservers() {
        viewModel.getMessages().observe(this, messages ->
                messagesAdapter.setMessages(messages)
        );
        viewModel.getError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(ChatActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getMessageSent().observe(this, sent -> {
            if (sent) {
                editTextMessage.setText("");
            }
        });
        viewModel.getOtherUser().observe(this, user -> {
            String userInfo = String.format("%s %s", user.getName(), user.getLastname());
            textViewTitle.setText(userInfo);
            Drawable background = ContextCompat.getDrawable(
                    ChatActivity.this,
                    user.isOnline() ? R.drawable.circle_green : R.drawable.circle_red
            );
            viewOnLineStatus.setBackground(background);
        });
    }

    private void setOnClickListeners() {
        imageViewSendMessage.setOnClickListener(view -> {
            Message message = new Message(
                    editTextMessage.getText().toString().trim(),
                    currentUserId,
                    otherUserId);
            viewModel.sentMessage(message);
        });
    }

    private void initViews() {
        textViewTitle = findViewById(R.id.textViewTitle);
        viewOnLineStatus = findViewById(R.id.viewOnLineStatus);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        imageViewSendMessage = findViewById(R.id.imageViewSendMessage);
    }

    public static Intent newIntent(Context context, String current_id, String other_id) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(ConstantStrings.EXTRA_CURRENT_USER, current_id);
        intent.putExtra(ConstantStrings.EXTRA_OTHER_USER, other_id);
        return intent;
    }
}