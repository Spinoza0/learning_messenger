package com.spinoza.messenger.users;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.spinoza.messenger.R;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
    private List<User> users = new ArrayList<>();

    private OnUserClickListener onUserClickListener;

    public void setOnUserClickListener(OnUserClickListener onUserClickListener) {
        this.onUserClickListener = onUserClickListener;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.user_item,
                parent,
                false
        );
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        String userInfo = String.format("%s %s, %s",
                user.getName(),
                user.getLastname(),
                user.getAge()
        );
        holder.textViewUserInfo.setText(userInfo);
        int backgroundResId;
        if (user.isOnline()) {
            backgroundResId = R.drawable.circle_green;
        } else {
            backgroundResId = R.drawable.circle_red;
        }
        Drawable background = ContextCompat.getDrawable(
                holder.itemView.getContext(),
                backgroundResId
        );
        holder.viewOnLineStatus.setBackground(background);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onUserClickListener != null) {
                    onUserClickListener.OnUserClick(user);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    interface OnUserClickListener {
        void OnUserClick(User user);
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUserInfo;
        private View viewOnLineStatus;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserInfo = itemView.findViewById(R.id.textViewUserInfo);
            viewOnLineStatus = itemView.findViewById(R.id.viewOnLineStatus);
        }
    }
}
