package com.example.novelsocial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.novelsocial.databinding.ActivityLoginBinding;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());

        // Root of the layout
        View view = binding.getRoot();
        setContentView(view);

        // Check if there's a user logged in
        // If there is, take them to MainActivity
        if(ParseUser.getCurrentUser() != null) {
            goToMainActivity();
        }

        EditText username = binding.etUsername;
        EditText password = binding.etPassword;
        Button signInButton = binding.btSignIn;
        Button createAccountButton = binding.btCreateAccount;

        signInButton.setOnClickListener((View v) -> {
            if (!(username.getText().toString().isEmpty()) && !(password.getText().toString().isEmpty())) {
                logInUser(username, password);
            }
            else {
                Toast.makeText(getApplicationContext(), "Check your username or password", Toast.LENGTH_SHORT).show();
            }
        });

        createAccountButton.setOnClickListener((View v) -> goToSignUpActivity());
    }

    private void logInUser(EditText username, EditText password) {
        ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), (ParseUser user, ParseException e) -> {
            if (e == null && user != null) {
                Toast.makeText(getApplicationContext(), "Signed In Successfully", Toast.LENGTH_SHORT).show();
                goToMainActivity();
            }
            else {
                assert e != null;
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error Logging In, Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}