package com.spinoza.messenger.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spinoza.messenger.R;

import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private enum ViewType {
        MY_MESSAGE,
        OTHER_MESSAGE;
    }


    private String currentUserId;
    private List<Message> messages = new ArrayList<>();

    public MessagesAdapter(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutResId;
        if (viewType == ViewType.MY_MESSAGE.ordinal()) {
            layoutResId = R.layout.my_message_item;
        } else {
            layoutResId = R.layout.other_message_item;
        }
        View view = LayoutInflater.from(parent.getContext()).
                inflate(layoutResId, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        int result;
        if (message.getSenderId().equals(currentUserId)) {
            result = ViewType.MY_MESSAGE.ordinal();
        } else {
            result = ViewType.OTHER_MESSAGE.ordinal();
        }
        return result;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.textViewMessage.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewMessage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
        }
    }
}
