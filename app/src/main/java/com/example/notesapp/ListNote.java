package com.example.notesapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class ListNote extends Fragment {
    FloatingActionButton addBtn ;
    String username;
    ImageButton showsubmenu;
    ArrayList<String> title,context,creationDate,compliteDate;
    ArrayList<Integer> isImportant;
    ArrayList<Integer> Id;
    RecyclerView recyclerView ;
    MyAdapter myAdapter;
    Database database ;
    private static final int REQUEST_CODE_ADD_NOTE = 1001;

    public ListNote() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_list_note, container, false);
        addBtn = view.findViewById(R.id.add_note_btn);
        recyclerView = view.findViewById(R.id.recycle_view);
        title = new ArrayList<>();
        context = new ArrayList<>();
        compliteDate = new ArrayList<>();
        isImportant = new ArrayList<>();
        Id = new ArrayList<>();
        database = new Database(getActivity());
        Bundle bundle = getArguments();
        if (bundle != null) {
            username = bundle.getString("username");
        }
        settingrecyclerView();
        StoreData();
        addBtn.setOnClickListener(v -> addclick());
        return view;
    }


    @Override
    public void onResume() {
        StoreData();
        super.onResume();
    }

    public void addclick(){
        Intent intent = new Intent(requireContext(), AddNoteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("username",username);
        intent.putExtra("myusername",bundle);
        startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
    }

    public void StoreData(){
        Cursor cursor = database.ListNote(username);
        if (cursor.getCount() == 0){
            Toast.makeText(getContext(), "You don't have any note ,let's try to create new note", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == Activity.RESULT_OK) {
            myAdapter.notifyDataSetChanged();
        }
    }



}