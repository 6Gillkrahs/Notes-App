package com.example.notesapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {
    EditText title,content,date;
    Calendar calendar;
    CheckBox isImportant;
    ImageButton addbtn,backbtn;
    Database database;
    ProgressBar load;
    boolean isimportant = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_note);
        title = findViewById(R.id.notes_title_text);
        content = findViewById(R.id.notes_content_text);
        addbtn = findViewById(R.id.check_btn);
        isImportant = findViewById(R.id.isImportant);
        date = findViewById(R.id.notes_date_complete);
        backbtn = findViewById(R.id.btn_back);
        calendar = Calendar.getInstance();
        load = findViewById(R.id.progress_bar3);

        database = new Database(AddNoteActivity.this);
        Intent myIntent = getIntent();
        Bundle myBundle = myIntent.getBundleExtra("myusername");
        String userName = myBundle.getString("username");
        setIsImportant();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR,i);
                calendar.set(Calendar.MONTH,i1);
                calendar.set(Calendar.DAY_OF_MONTH,i2);
                updatelable();
            }
        };
        backbtn.setOnClickListener(v -> {
            onBackPressed();
        });
        date.setOnClickListener(v->dateclick(dateSetListener));
        addbtn.setOnClickListener((view -> {
            String mytitle = title.getText().toString();
            String mycontent = content.getText().toString();
            addnote(userName,mytitle,mycontent,isimportant,date);
        }));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    void dateclick(DatePickerDialog.OnDateSetListener dateSetListener){
        new DatePickerDialog(AddNoteActivity.this,dateSetListener
                ,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onBackPressed() {
        // Tạo một Intent để trả về kết quả cho Fragment ListNote
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    void updatelable(){
        String myFormal = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormal);
        date.setText(dateFormat.format(calendar.getTime()));
    }


    void addnote(String userName,String mytitle,String mycontent,boolean isImportant ,EditText mydate){
        inProgress(false);
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(currentDate);
        if(mytitle.isEmpty()){
            title.setError("Invalid");
            return;
        }
        if(mydate.getText().toString().isEmpty()){
            date.setError("Invalid");
            return;
        }
        if(mycontent.isEmpty()){
            content.setError("Invalid");
            return;
        }
        inProgress(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                database.createNote(mytitle,mycontent,formattedDate, isImportant, mydate.getText().toString(),userName);
                inProgress(false);
            }
        },2000);

    }

    void setIsImportant(){
        isImportant.setOnClickListener((v)->{
            if(isImportant.isChecked()){
                isimportant = true;
            }else{
                isimportant = false;
            }
        });
    }

    void inProgress(boolean inProgress){
        if(inProgress){
            load.setVisibility(View.VISIBLE);
        }else{
            load.setVisibility(View.GONE);
        }
    }
}