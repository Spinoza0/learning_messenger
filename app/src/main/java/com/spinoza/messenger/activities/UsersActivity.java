package com.spinoza.messenger.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.spinoza.messenger.MainActivity;
import com.spinoza.messenger.R;
import com.spinoza.messenger.adapters.UsersAdapter;
import com.spinoza.messenger.data.ConstantStrings;
import com.spinoza.messenger.viewmodels.UsersViewModel;

public class UsersActivity extends AppCompatActivity {

    private UsersViewModel viewModel;
    private UsersAdapter usersAdapter;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        viewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        initViews();

        currentUserId = getIntent().getStringExtra(ConstantStrings.EXTRA_CURRENT_USER);

        setOnClickListeners();
        setObservers();
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

    private void setOnClickListeners() {
        usersAdapter.setOnUserClickListener(user -> startActivity(ChatActivity.newIntent(
                UsersActivity.this,
                currentUserId,
                user.getId())
        ));
    }

    void setObservers() {
        viewModel.getUser().observe(this, firebaseUser -> {
            if (firebaseUser == null) {
                startActivity(MainActivity.newIntent(UsersActivity.this));
                finish();
            }
        });

        viewModel.getUsers().observe(this, users -> usersAdapter.setUsers(users));
    }

    protected void initViews() {
        RecyclerView recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        usersAdapter = new UsersAdapter();
        recyclerViewUsers.setAdapter(usersAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.users_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemLogout) {
            viewModel.logout();
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent newIntent(Context context, String current_id) {
        Intent intent = new Intent(context, UsersActivity.class);
        intent.putExtra(ConstantStrings.EXTRA_CURRENT_USER, current_id);
        return intent;
    }
}