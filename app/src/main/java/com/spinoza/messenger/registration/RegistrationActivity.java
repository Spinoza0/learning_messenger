package com.spinoza.messenger.registration;

import static com.spinoza.messenger.R.string.registration_success;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.spinoza.messenger.ConstantStrings;
import com.spinoza.messenger.FirebaseResult;
import com.spinoza.messenger.R;
import com.spinoza.messenger.users.User;
import com.spinoza.messenger.users.UsersActivity;

public class RegistrationActivity extends AppCompatActivity {

    private RegistrationViewModel viewModel;
    private EditText editTextEMail;
    private EditText editTextPassword;
    private EditText editTextName;
    private EditText editTextLastname;
    private EditText editTextAge;
    private Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        viewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        initViews();

        String email = getIntent().getStringExtra(ConstantStrings.EXTRA_EMAIL);
        editTextEMail.setText(email);

        setOnClickListeners();
        setObservers();
    }

    void setObservers() {
        viewModel.getRegistrationResult().observe(this,
                registrationResult -> {
                    switch (registrationResult.getType()) {
                        case SUCCESS:
                            viewModel.setRegistrationResult(
                                    FirebaseResult.Type.NONE,
                                    "",
                                    null
                            );
                            Toast.makeText(
                                    RegistrationActivity.this,
                                    registration_success,
                                    Toast.LENGTH_SHORT).show();
                            startActivity(
                                    UsersActivity.newIntent(RegistrationActivity.this,
                                            registrationResult.getFirebaseUser().getUid()
                                    )
                            );
                            finish();
                            break;
                        case ERROR_DATA_EMPTY:
                            Toast.makeText(
                                    RegistrationActivity.this,
                                    R.string.error_data_empty,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case ERROR_REGISTRATION:
                            Toast.makeText(
                                    RegistrationActivity.this,
                                    registrationResult.getText(),
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case ERROR_DATABASE_CONNECT:
                            Toast.makeText(
                                    RegistrationActivity.this,
                                    R.string.error_connecting_database,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case ERROR_REGISTRATION_NO_UID:
                            Toast.makeText(
                                    RegistrationActivity.this,
                                    R.string.registration_error,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                });
    }

    private void setOnClickListeners() {
        buttonSignUp.setOnClickListener(view ->
                viewModel.signUp(new User(
                                "",
                                editTextName.getText().toString(),
                                editTextLastname.getText().toString(),
                                editTextAge.getText().toString(), false
                        ),
                        editTextEMail.getText().toString(),
                        editTextPassword.getText().toString()
                )
        );
    }

    public static Intent newIntent(Context context, String email) {
        Intent intent = new Intent(context, RegistrationActivity.class);
        intent.putExtra(ConstantStrings.EXTRA_EMAIL, email.trim());
        return intent;
    }

    private void initViews() {
        editTextEMail = findViewById(R.id.editTextEMail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextLastname = findViewById(R.id.editTextLastname);
        editTextAge = findViewById(R.id.editTextAge);
        buttonSignUp = findViewById(R.id.buttonSignUp);
    }
}