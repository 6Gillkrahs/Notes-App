package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class CreateAccountActivity extends AppCompatActivity {
    EditText userName ;
    EditText passWord;
    EditText comfirmPassWord;

    TextView loginBtnTextView ;
    ProgressBar load;

    Button createAccountBtn ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        userName = (EditText) findViewById(R.id.email_edit_text);
        passWord = (EditText) findViewById(R.id.password_edit_text);
        comfirmPassWord = (EditText) findViewById(R.id.comfirm_password_edit_text);
        createAccountBtn = (Button) findViewById(R.id.create_account_btn);
        load = findViewById(R.id.progress_bar);
        loginBtnTextView = findViewById(R.id.login_text_view_btn);
        createAccountBtn.setOnClickListener(v-> createAccount());
        loginBtnTextView.setOnClickListener(v -> loginview());
    }

    void createAccount(){
        changeInProgress(false);
        Database mydb = new Database(CreateAccountActivity.this);

        String username = userName.getText().toString().trim();
        String password = passWord.getText().toString().trim();
        String confirmPassword = comfirmPassWord.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            userName.setError("Invalid email format");
            return;
        }
        if (password.isEmpty()) {
            passWord.setError("Password is required");
            return;
        }
        if (confirmPassword.isEmpty()) {
            comfirmPassWord.setError("Confirm password is required");
            return;
        }
        if (!confirmPassword.equals(password)) {
            Toast.makeText(CreateAccountActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
       if(mydb.checkAccount(username) == true){
           Toast.makeText(CreateAccountActivity.this, "Registered email", Toast.LENGTH_SHORT).show();
           return;
       }
        mydb.createAccount(username, password);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loginview();
            }
        },1000);
        

    }

    void loginview(){
        startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
        finish();
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            load.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }else{
            load.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }
}