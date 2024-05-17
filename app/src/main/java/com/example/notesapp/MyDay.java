package com.example.notesapp;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyDay extends Fragment {
    String username;
    RecyclerView recyclerView ;
    ArrayList<String> title,context,creationDate,compliteDate;
    ArrayList<Integer> isImportant;
    ArrayList<Integer> Id;
    MyAdapter myAdapter;
    Database database ;
    public MyDay() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_day, container, false);
        recyclerView = view.findViewById(R.id.recycle_view_day);
        database = new Database(getActivity());
        title = new ArrayList<>();
        context = new ArrayList<>();
        compliteDate = new ArrayList<>();
        isImportant = new ArrayList<>();
        Id = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            username = bundle.getString("username");
        }
        System.out.println(username);
        settingrecyclerView();
        StoreData();
        return view;
    }
    public void StoreData(){
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(currentDate);
        Cursor cursor = database.getAllNoteByCompleteDay(username,formattedDate);
        if (cursor.getCount() == 0){
            Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                title.add(cursor.getString(1));
                context.add(cursor.getString(2));
//                creationDate.add(cursor.getString(4));
                compliteDate.add(cursor.getString(5));
                isImportant.add(cursor.getInt(3));
                Id.add(cursor.getInt(0));
            }
        }
    }

    public void settingrecyclerView(){
        myAdapter = new MyAdapter(getActivity(),title,context,compliteDate,Id,isImportant);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);
    }
}