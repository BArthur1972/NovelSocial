package com.example.novelsocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.novelsocial.databinding.ActivitySignUpBinding;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySignUpBinding binding = ActivitySignUpBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        // Add the back button in the ActionBar to go back a previous page
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        EditText fullName = binding.etSignUpFullNameField;
        EditText username = binding.etSignUpUsernameField;
        EditText password = binding.etSignUpPasswordField;
        EditText confirmPassword = binding.etConfirmPasswordField;
        Button signUpButton =binding.btSignUp;

        signUpButton.setOnClickListener((View v) -> {
            if (!(username.getText().toString().isEmpty()) && !(password.getText().toString().isEmpty())) {
                signUpUser(fullName ,username, password, confirmPassword);
            } else {
                Toast.makeText(getApplicationContext(), "Check your username or password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Navigate back to search activity when back button is pressed
        if (item.getItemId() == android.R.id.home) {
            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signUpUser(EditText fullName, EditText username, EditText password, EditText confirmPassword) {
        ParseUser user = new ParseUser();
        if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Passwords do not match, Please Try Again", Toast.LENGTH_SHORT).show();
        } else {
            user.put("fullName", fullName.getText().toString());
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