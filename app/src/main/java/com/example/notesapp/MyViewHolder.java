package com.example.notesapp;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView title,context,compliteDate;
    ImageButton btnMenu;
    ImageView isImportant;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.item_rec_title);
        context = itemView.findViewById(R.id.item_rec_content);
        compliteDate = itemView.findViewById(R.id.date);
        btnMenu = itemView.findViewById(R.id.btn_showsubmenu);
        isImportant = itemView.findViewById(R.id.isImportant_item);
    }

}
