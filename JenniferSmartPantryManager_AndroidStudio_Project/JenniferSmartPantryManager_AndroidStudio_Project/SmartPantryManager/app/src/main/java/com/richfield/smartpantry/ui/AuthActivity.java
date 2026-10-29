package com.richfield.smartpantry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.richfield.smartpantry.R;

public class AuthActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private TextView title;
    private TextView subtitle;
    private TextView modeToggle;
    private Button submitButton;
    private boolean creatingAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            openMain();
            return;
        }

        setContentView(R.layout.activity_auth);
        title = findViewById(R.id.auth_title);
        subtitle = findViewById(R.id.auth_subtitle);
        emailLayout = findViewById(R.id.auth_email_layout);
        passwordLayout = findViewById(R.id.auth_password_layout);
        emailInput = findViewById(R.id.auth_email_input);
        passwordInput = findViewById(R.id.auth_password_input);
        submitButton = findViewById(R.id.auth_submit);
        modeToggle = findViewById(R.id.auth_mode_toggle);

        submitButton.setOnClickListener(view -> authenticate());
        modeToggle.setOnClickListener(view -> {
            creatingAccount = !creatingAccount;
            updateMode();
        });
        updateMode();
    }

    private void updateMode() {
        title.setText(creatingAccount
                ? R.string.auth_signup_title : R.string.auth_login_title);
        subtitle.setText(creatingAccount
                ? R.string.auth_signup_subtitle : R.string.auth_login_subtitle);
        submitButton.setText(creatingAccount
                ? R.string.auth_create_account : R.string.auth_login);
        modeToggle.setText(creatingAccount
                ? R.string.auth_switch_to_login : R.string.auth_switch_to_signup);
        emailLayout.setError(null);
        passwordLayout.setError(null);
    }

    private void authenticate() {
        String email = emailInput.getText() == null
                ? "" : emailInput.getText().toString().trim();
        String password = passwordInput.getText() == null
                ? "" : passwordInput.getText().toString();
        emailLayout.setError(null);
        passwordLayout.setError(null);

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError(getString(R.string.auth_email_required));
            return;
        }
        if (password.isEmpty()) {
            passwordLayout.setError(getString(R.string.auth_password_required));
            return;
        }
        if (creatingAccount && password.length() < 6) {
            passwordLayout.setError(getString(R.string.auth_password_too_short));
            return;
        }

        setLoading(true);
        if (creatingAccount) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> finishAuthentication(task.isSuccessful(),
                            task.getException()));
        } else {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> finishAuthentication(task.isSuccessful(),
                            task.getException()));
        }
    }

    private void finishAuthentication(boolean successful, Exception error) {
        setLoading(false);
        if (successful) {
            openMain();
            return;
        }
        Toast.makeText(this, authErrorMessage(error), Toast.LENGTH_LONG).show();
    }

    private String authErrorMessage(Exception error) {
        if (error instanceof FirebaseAuthUserCollisionException) {
            return getString(R.string.auth_email_in_use);
        }
        if (error instanceof FirebaseAuthWeakPasswordException) {
            return getString(R.string.auth_weak_password);
        }
        if (error instanceof FirebaseAuthInvalidCredentialsException
                || error instanceof FirebaseAuthInvalidUserException) {
            return getString(R.string.auth_invalid_credentials);
        }
        return getString(R.string.auth_failed);
    }

    private void setLoading(boolean loading) {
        submitButton.setEnabled(!loading);
        modeToggle.setEnabled(!loading);
        submitButton.setText(loading ? R.string.auth_loading
                : (creatingAccount ? R.string.auth_create_account : R.string.auth_login));
    }

    private void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
