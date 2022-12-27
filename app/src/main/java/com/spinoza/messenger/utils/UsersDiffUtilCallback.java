package com.spinoza.messenger.utils;

import androidx.recyclerview.widget.DiffUtil;

import com.spinoza.messenger.data.User;

import java.util.List;

public class UsersDiffUtilCallback extends DiffUtil.Callback {
    private final List<User> oldList;
    private final List<User> newList;

    public UsersDiffUtilCallback(List<User> oldList, List<User> newList) {
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
        User oldItem = oldList.get(oldItemPosition);
        User newItem = newList.get(newItemPosition);
        return oldItem.getId().equals(newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        User oldItem = oldList.get(oldItemPosition);
        User newItem = newList.get(newItemPosition);
        return oldItem.getName().equals(newItem.getName()) &&
                oldItem.getLastname().equals(newItem.getLastname()) &&
                oldItem.getAge() == newItem.getAge() &&
                oldItem.isOnline() == newItem.isOnline();
    }
}
