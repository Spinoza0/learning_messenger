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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.spinoza.messenger.ConstantStrings;
import com.spinoza.messenger.R;
import com.spinoza.messenger.users.User;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private TextView textViewTitle;
    private View viewOnLineStatus;
    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private ImageView imageViewSendMessage;

    private ChatViewModel viewModel;
    private ChatViewModelFactory viewModelFactory;
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

        viewModelFactory = new ChatViewModelFactory(currentUserId, otherUserId);
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
        viewModel.getMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                messagesAdapter.setMessages(messages);
            }
        });
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    Toast.makeText(
                                    ChatActivity.this,
                                    errorMessage,
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        viewModel.getMessageSent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean sent) {
                if (sent) {
                    editTextMessage.setText("");
                }
            }
        });
        viewModel.getOtherUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                String userInfo = String.format("%s %s", user.getName(), user.getLastname());
                textViewTitle.setText(userInfo);
                int backgroundResId;
                if (user.isOnline()) {
                    backgroundResId = R.drawable.circle_green;
                } else {
                    backgroundResId = R.drawable.circle_red;
                }
                Drawable background = ContextCompat.getDrawable(
                        ChatActivity.this,
                        backgroundResId
                );
                viewOnLineStatus.setBackground(background);
            }
        });
    }

    private void setOnClickListeners() {
        imageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message(
                        editTextMessage.getText().toString().trim(),
                        currentUserId,
                        otherUserId);
                viewModel.sentMessage(message);
            }
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