/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity implements OnClickListener, OnKeyListener {
  ConstraintLayout constraintLayout;
  ImageView logoImageView;
  EditText usernameEditText;
  EditText passwordEditText;
  TextView switchSignUpLogIn;
  Button signupButton;
  boolean signUpEnabled = true;

  public void viewUsers() {
    Intent intent = new Intent(getApplicationContext(), UsersActivity.class);
    startActivity(intent);
  }

  @Override
  public boolean onKey(View v, int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
      signUp(v);
    }
    return false;
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.switchSignUpLogIn) {
      if (signUpEnabled) {
        switchSignUpLogIn.setText("Or Sign Up");
        signupButton.setText("Log In");
        signUpEnabled = false;
      } else {
        switchSignUpLogIn.setText("Or Log In");
        signupButton.setText("Sign Up");
        signUpEnabled = true;
      }
    } else if (view.getId() == R.id.constraintLayout || view.getId() == R.id.logoImageView) {
      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      if (getCurrentFocus() != null) {
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

      }
    }
  }

  public void signUp(final View view) {
    String userText = usernameEditText.getText().toString();
    String passText = passwordEditText.getText().toString();

    if (userText.matches("") || passText.matches("")) {
      Toast.makeText(MainActivity.this, "Username and password are required.", Toast.LENGTH_LONG).show();
    } else {
      if (signUpEnabled) {

        ParseUser user = new ParseUser();
        user.setUsername(userText);
        user.setPassword(passText);
        user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
            if (e == null) {
              Log.i("Signup", "Successful");
              viewUsers();
            } else {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
          }
        });
      } else {
        ParseUser.logInInBackground(userText, passText, new LogInCallback() {
          @Override
          public void done(ParseUser parseUser, ParseException e) {
            if (parseUser != null && e == null) {
              Log.i("Log in", "Successful");
              viewUsers();
            } else {
              Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }
    }
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	setTitle("Quarantime");

	constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
	logoImageView = (ImageView) findViewById(R.id.logoImageView);
    usernameEditText = (EditText) findViewById(R.id.usernameEditText);
    passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    switchSignUpLogIn = (TextView) findViewById(R.id.switchSignUpLogIn);
    signupButton = (Button) findViewById(R.id.signupButton);

    constraintLayout.setOnClickListener(this);
    logoImageView.setOnClickListener(this);
    passwordEditText.setOnKeyListener(this);

    if (ParseUser.getCurrentUser() != null) viewUsers();

	ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }
}