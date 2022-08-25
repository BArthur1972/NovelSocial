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

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText username = findViewById(R.id.et_sign_up_username);
        EditText password = findViewById(R.id.et_sign_up_password);
        EditText confirmPassword = findViewById(R.id.et_confirm_password);
        Button signUpButton = findViewById(R.id.bt_sign_up);

        signUpButton.setOnClickListener((View view) -> {
            if (!(username.getText().toString().isEmpty()) && !(password.getText().toString().isEmpty())) {
                signUpUser(username, password, confirmPassword);
            } else {
                Toast.makeText(getApplicationContext(), "Check your username or password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signUpUser(EditText username, EditText password, EditText confirmPassword) {
        ParseUser user = new ParseUser();
        if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
        } else {
            user.setUsername(username.getText().toString());
            user.setPassword(password.getText().toString());
            user.signUpInBackground((ParseException e) -> {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "Welcome to Novel Social!", Toast.LENGTH_SHORT).show();
                    goToMainActivity();
                } else {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "We encountered an error signing you up, Try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}