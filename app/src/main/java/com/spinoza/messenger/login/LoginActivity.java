package com.spinoza.messenger.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.spinoza.messenger.FirebaseResult;
import com.spinoza.messenger.R;
import com.spinoza.messenger.registration.RegistrationActivity;
import com.spinoza.messenger.resetpassword.ResetPasswordActivity;
import com.spinoza.messenger.users.UsersActivity;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel viewModel;

    private EditText editTextEMail;
    private EditText editTextPassword;
    private Button buttonSignIn;
    private TextView textViewForgotPassword;
    private TextView textViewRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        initViews();

        setOnClickListeners();
        setObservers();
    }

    private void setOnClickListeners() {
        textViewRegister.setOnClickListener(view ->
                startActivity(RegistrationActivity.newIntent(
                        LoginActivity.this,
                        editTextEMail.getText().toString())
                )
        );

        textViewForgotPassword.setOnClickListener(view ->
                startActivity(ResetPasswordActivity.newIntent(
                        LoginActivity.this,
                        editTextEMail.getText().toString())
                )
        );

        buttonSignIn.setOnClickListener(view -> viewModel.login(
                editTextEMail.getText().toString(),
                editTextPassword.getText().toString()
        ));
    }

    private void setObservers() {
        viewModel.getLoginResult().observe(this,
                loginResult -> {
                    switch (loginResult.getType()) {
                        case SUCCESS:
                            viewModel.setLoginResult(
                                    FirebaseResult.Type.NONE,
                                    "",
                                    null
                            );
                            startActivity(UsersActivity.newIntent(
                                            LoginActivity.this,
                                            loginResult.getFirebaseUser().getUid()
                                    )
                            );
                            finish();
                            break;
                        case ERROR_DATA_EMPTY:
                            Toast.makeText(
                                    LoginActivity.this,
                                    R.string.error_data_empty,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case ERROR_LOGIN:
                            Toast.makeText(
                                    LoginActivity.this,
                                    loginResult.getText(),
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case ERROR_DATABASE_CONNECT:
                            Toast.makeText(
                                    LoginActivity.this,
                                    R.string.error_connecting_database,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                });
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    private void initViews() {
        editTextEMail = findViewById(R.id.editTextEMail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        textViewRegister = findViewById(R.id.textViewRegister);
    }

}