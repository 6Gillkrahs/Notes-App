package com.example.notesapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    Database database;
    ArrayList title,contect,isImportant,creationDate,compliteDate,id;
    private static final int REQUEST_CODE_ADD_NOTE = 1002;

    public MyAdapter(Context context, ArrayList title,ArrayList contect,ArrayList compliteDate,ArrayList id,ArrayList isImportant) {
        this.context = context;
        this.title = title;
        this.contect = contect;
        this.isImportant = isImportant;
//        this.creationDate = creationDate;
        this.compliteDate = compliteDate;
        this.id = id;
        database = new Database(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_gidview,parent,false));
    }




    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(String.valueOf(title.get(position)));
        holder.context.setText(String.valueOf(contect.get(position)));
        holder.compliteDate.setText(String.valueOf(compliteDate.get(position)));

        if(isImportant.get(position).equals(1)){
            holder.isImportant.setImageResource(R.drawable.icon_star);

        }

        holder.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.btnMenu);
                popupMenu.inflate(R.menu.sub_menu);
                MenuItem markItem = popupMenu.getMenu().findItem(R.id.menu_item_mark);
                if (isImportant.get(position).equals(1)) {
                    markItem.setTitle("Unmark as Important");
                } else {
                    markItem.setTitle("Mark as Important");
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int i = menuItem.getItemId();
                        if(i == R.id.menu_item_edit){
                            editClick(position);
                        }
                        if(i == R.id.menu_item_mark){
                            markClick(position,String.valueOf(isImportant.get(position)));
                        }
                        if(i == R.id.menu_item_complete){
                            completeclick( position);
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return title.size();
    }
    public void completeclick(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are You Sure?");
        builder.setMessage("Do you want to delete this note");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                database.DeleteNote(String.valueOf(id.get(position)));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }

    public void markClick(int position,String isImportant){
        database.markImportant(String.valueOf(id.get(position)),isImportant);
        ((Activity) context).recreate();
    }

    public void editClick(int position){
        String title_to_edit = String.valueOf(title.get(position));
        String context_to_edit = String.valueOf(contect.get(position));
        String compliteDate_to_edit = String.valueOf(compliteDate.get(position));
        String isImportant_to_edit = String.valueOf(isImportant.get(position));

        Intent intent = new Intent(context, UpdateNoteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("_id", String.valueOf(id.get(position)));
        bundle.putString("title",title_to_edit);
        bundle.putString("context",context_to_edit);
        bundle.putString("date",compliteDate_to_edit);
        bundle.putString("isImportant",isImportant_to_edit);
        intent.putExtra("note", bundle);
        context.startActivity(intent);

    }



}
