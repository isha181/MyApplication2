package com.ashi.myapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailLoginActivity extends AppCompatActivity implements View.OnClickListener {

private FirebaseAuth mAuth;
private TextInputEditText editEmail;
private TextInputEditText editPassword;
private ProgressDialog statusDialog;

@SuppressLint("WrongViewCast")
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnRegister = findViewById(R.id.btnRegister);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        TextView textForgot = findViewById(R.id.textForgot);

        // code to connect to firebase authentication
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // btn event listeners


        btnRegister.setOnClickListener(this);
        textForgot.setOnClickListener(this);

        }

    private void setSupportActionBar(Toolbar toolbar) {

    }

    @Override
public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
        }

@Override
public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
        return true;
        }

        return super.onOptionsItemSelected(item);
        }

@Override
public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        }

private void updateUI(FirebaseUser currentUser) {
        hideDialog(statusDialog);
        if (currentUser != null) {
        startActivity(new Intent(this,MainActivity.class));
        finish();
        } else {
        // error msg
        }
        }

@Override
public void onClick(View v) {
        switch (v.getId()) {

        case R.id.btnRegister:
        gotoRegister();
        break;
        case R.id.textForgot:
        gotoForgot();
        break;
        }
        }

private void gotoForgot() {
//        startActivity(new Intent(this,ForgotActivity.class));
        }

private void gotoRegister() {
        startActivity(new Intent(this, EmailLoginActivity.class));
        }

private void processLogin() {

        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
        Snackbar.make(editEmail, "email or password cannot be empty", Snackbar.LENGTH_INDEFINITE).show();
        } else if (email.length() < 10 || password.length() < 8) {
        Snackbar.make(editEmail, "email or password length invalid", Snackbar.LENGTH_INDEFINITE).show();
        } else {
        statusDialog = showDialog("authenticating with server");
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
@Override
public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user);
        } else {
        updateUI(null);
        }
        }
        });
        }
        }

public ProgressDialog showDialog(String msg) {
        Context context;
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle(msg);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
        }

public void hideDialog(ProgressDialog dialog) {
        if (dialog != null) {
        if (dialog.isShowing()) {
        dialog.dismiss();
        }
        }
        }

        }


