package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    EditText userName,passWord;
    TextView btn_view;
    Button loginBtn;
    ProgressBar load;
    Database mydb;
    ArrayList<String> Username,Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = findViewById(R.id.email_edit_text);
        passWord = findViewById(R.id.password_edit_text);
        loginBtn = findViewById(R.id.login_btn);
        btn_view = findViewById(R.id.signup_text_view_btn);
        load = findViewById(R.id.progress_bar2);
        mydb = new Database(LoginActivity.this);
        Username = new ArrayList<>();
        Password = new ArrayList<>();
        loginBtn.setOnClickListener(v -> login());
        btn_view.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
        });
    }

    public void login(){
        changeInProgress(false);
        String username = userName.getText().toString().trim();
        String password = passWord.getText().toString().trim();
        Cursor cursor = mydb.readAccount(username,password);
        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            userName.setError("Invalid email format");
            return;
        }
        if (password.isEmpty()) {
            passWord.setError("Password is required");
            return;
        }
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }else {
            changeInProgress(true);
            Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Bundle bundle =  new Bundle();
                    bundle.putString("username",username);
                    intent.putExtra("myuserName",bundle);
                    startActivity(intent);
                    finish();
                }
            },1000);

        }


    }
    void changeInProgress(boolean inProgress){
        if(inProgress){
            load.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }else{
            load.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }


}