package com.example.novelsocial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Check if there's a user logged in
        // If there is, take them to MainActivity
        if(ParseUser.getCurrentUser() != null) {
            goToMainActivity();
        }

        EditText username = findViewById(R.id.et_username);
        EditText password = findViewById(R.id.et_password);
        Button signInButton = findViewById(R.id.bt_sign_in);
        Button createAccountButton = findViewById(R.id.bt_create_account);

        signInButton.setOnClickListener((View view) -> {
            if (!(username.getText().toString().isEmpty()) && !(password.getText().toString().isEmpty())) {
                logInUser(username, password);
            }
            else {
                Toast.makeText(getApplicationContext(), "Check your username or password", Toast.LENGTH_SHORT).show();
            }
        });

        createAccountButton.setOnClickListener((View view) -> goToSignUpActivity());
    }

    private void logInUser(EditText username, EditText password) {
        ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), (ParseUser user, ParseException e) -> {
            if (e == null && user != null) {
                Toast.makeText(getApplicationContext(), "Signed In Successfully", Toast.LENGTH_SHORT).show();
                goToMainActivity();
            }
            else {
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