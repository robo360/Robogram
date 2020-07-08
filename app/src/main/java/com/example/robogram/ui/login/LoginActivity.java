package com.example.robogram.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robogram.MainActivity;
import com.example.robogram.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(ParseUser.getCurrentUser() != null){
            goMainActivity();
        }

        // Todo: set up an indeterminate progress bar for showing that the app is making a call on the server.
        // Todo: Remove the keyboard when finishing to typing

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button signupButton = findViewById(R.id.signup);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        //handle login

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //disable the clickability of the signup button
                signupButton.setClickable(false);
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(e != null){
                            Log.i("LoginActivity", e.toString());
                            Snackbar.make(loginButton, "Login Failed.Try again!"+ e, BaseTransientBottomBar.LENGTH_SHORT).show();
                        }else{
                            goMainActivity();
                        }
                    }
                });
            }
        });

        //handle signup
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //disable the clickability of the login button
                loginButton.setClickable(false);
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                ParseUser user = new ParseUser();
                // Set the user's username and password, which can be obtained by a forms
                user.setUsername(username);
                user.setPassword(password);
                user.signUpInBackground( new SignUpCallback(){
                    /**
                     * Override this function with the code you want to run after the signUp is complete.
                     *
                     * @param e The exception raised by the signUp, or {@code null} if it succeeded.
                     */
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.i("LoginActivity", e.toString());
                            Snackbar.make(signupButton, "Sign up Failed.Try again!" + e, BaseTransientBottomBar.LENGTH_SHORT).show();
                        }else{
                            goMainActivity();
                        }
                    }
                });
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
        startActivity(i);
        finish();
    }


}