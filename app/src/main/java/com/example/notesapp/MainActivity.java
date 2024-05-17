package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ListNote listNote;
    MyDay myDay;

    Important important;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        listNote = new ListNote();
        myDay = new MyDay();
        important = new Important();
        Intent myIntent = getIntent();
        Bundle myBundle = myIntent.getBundleExtra("myuserName");
        String userName = myBundle.getString("username");
        System.out.println(userName);
        Bundle bundle = new Bundle();
        bundle.putString("username", userName);
        listNote.setArguments(bundle);
        important.setArguments(bundle);
        myDay.setArguments(bundle);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.notes){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,listNote).commit();
                }
                if(item.getItemId() == R.id.myday){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,myDay).commit();
                }
                if(item.getItemId() == R.id.important){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,important).commit();
                }

                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.notes);
    }
}