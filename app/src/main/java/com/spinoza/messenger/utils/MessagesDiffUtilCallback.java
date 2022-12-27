package com.spinoza.messenger.utils;

import androidx.recyclerview.widget.DiffUtil;

import com.spinoza.messenger.data.Message;

import java.util.List;

public class MessagesDiffUtilCallback extends DiffUtil.Callback {
    private final List<Message> oldList;
    private final List<Message> newList;

    public MessagesDiffUtilCallback(List<Message> oldList, List<Message> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Message oldItem = oldList.get(oldItemPosition);
        Message newItem = newList.get(newItemPosition);
        return oldItem.getText().equals(newItem.getText());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Message oldItem = oldList.get(oldItemPosition);
        Message newItem = newList.get(newItemPosition);
        return oldItem.getSenderId().equals(newItem.getSenderId()) &&
                oldItem.getReceiverId().equals(newItem.getReceiverId());
    }
}
