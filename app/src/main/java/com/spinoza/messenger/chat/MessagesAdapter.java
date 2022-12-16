package com.spinoza.messenger.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.spinoza.messenger.R;

import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private enum ViewType {
        MY_MESSAGE,
        OTHER_MESSAGE
    }


    private final String currentUserId;
    private List<Message> messages = new ArrayList<>();

    public MessagesAdapter(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public void setMessages(List<Message> messages) {
        MessagesDiffUtilCallback diffUtilCallback =
                new MessagesDiffUtilCallback(this.messages, messages);
        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(diffUtilCallback);
        this.messages = messages;
        productDiffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(
                        (viewType == ViewType.MY_MESSAGE.ordinal()) ?
                                R.layout.my_message_item :
                                R.layout.other_message_item,
                        parent,
                        false
                );
        return new MessageViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return (messages.get(position).getSenderId().equals(currentUserId)) ?
                ViewType.MY_MESSAGE.ordinal() :
                ViewType.OTHER_MESSAGE.ordinal();
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.textViewMessage.setText(messages.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewMessage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
        }
    }
}
