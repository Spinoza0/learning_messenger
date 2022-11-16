package com.spinoza.messenger.resetpassword;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.spinoza.messenger.ConstantStrings;
import com.spinoza.messenger.FirebaseResult;
import com.spinoza.messenger.R;

public class ResetPasswordActivity extends AppCompatActivity {

    private ResetPasswordViewModel viewModel;
    private EditText editTextMail;
    private Button buttonResetPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        viewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);
        initViews();

        String email = getIntent().getStringExtra(ConstantStrings.EXTRA_EMAIL);
        editTextMail.setText(email);

        setOnClickListeners();
        setObservers();
    }


    private void setOnClickListeners() {
        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.resetPassword(editTextMail.getText().toString());
            }
        });
    }

    void setObservers() {
        viewModel.getResetResult().observe(this, new Observer<FirebaseResult>() {
            @Override
            public void onChanged(FirebaseResult resetResult) {
                switch (resetResult.getType()) {
                    case SUCCESS:
                        Toast.makeText(ResetPasswordActivity.this,
                                R.string.email_sent,
                                Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case ERROR_DATA_EMPTY:
                        Toast.makeText(ResetPasswordActivity.this,
                                R.string.error_data_empty,
                                Toast.LENGTH_SHORT).show();
                        break;
                    case ERROR_RESET:
                        Toast.makeText(ResetPasswordActivity.this,
                                resetResult.getText(),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case ERROR_DATABASE_CONNECT:
                        Toast.makeText(ResetPasswordActivity.this,
                                R.string.error_connecting_database,
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public static Intent newIntent(Context context, String email) {
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        intent.putExtra(ConstantStrings.EXTRA_EMAIL, email.trim());
        return intent;
    }

    private void initViews() {
        editTextMail = findViewById(R.id.editTextMail);
        buttonResetPassword = findViewById(R.id.buttonResetPassword);
    }
}