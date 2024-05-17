package com.example.notesapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

public class Database extends SQLiteOpenHelper {
    private Context context;

    //khai báo name và version của database
    private static final String DATABASE_NAME ="MyNotesapp.db";
    private static final int DATABASE_VERSION =2;
    // khai báo table name và attribute của auth name
    private static final String TABLE_NAME_AUTH ="Auth";
    private static final String COLUNM_ID ="_id";
    private static final String COLUNM_EMAIL ="email";
    private static final String COLUNM_PASSWORD ="password";
    //khai báo table name và attribute của notes table
    private static final String TABLE_NAME_NOTE = "Notes";
    private static final String COLUMN_NOTES_ID = "_id";
    private static final String COLUMN_NOTES_TITLE = "title";
    private static final String COLUMN_NOTES_CONTENT = "content";
    private static final String COLUMN_NOTES_IS_IMPORTANT = "isImportant";
    private static final String COLUMN_NOTES_CREATION_DATE = "creationDate";
    private static final String COLUMN_NOTES_COMPLETION_DATE = "completionDate";

    //tạo database
    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //tạo bảng auth table
        String queryAuth = "CREATE TABLE " + TABLE_NAME_AUTH + "(" + COLUNM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUNM_EMAIL + " TEXT, "
                + COLUNM_PASSWORD + " TEXT);";
        sqLiteDatabase.execSQL(queryAuth);

        //tạo bảng notes table
        String queryNotes = "CREATE TABLE " + TABLE_NAME_NOTE + "(" + COLUMN_NOTES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NOTES_TITLE + " TEXT, "
                + COLUMN_NOTES_CONTENT + " TEXT, "
                + COLUMN_NOTES_IS_IMPORTANT + " INTEGER, "
                + COLUMN_NOTES_CREATION_DATE + " TEXT, "
                + COLUMN_NOTES_COMPLETION_DATE + " TEXT, "
                + COLUNM_EMAIL + " TEXT)";
        sqLiteDatabase.execSQL(queryNotes);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_AUTH);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NOTE);
        onCreate(sqLiteDatabase);
    }

    //hàm tạo account
    void createAccount(String userName,String passWord){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUNM_EMAIL,userName);
        cv.put(COLUNM_PASSWORD,passWord);
        //check xem đã có mail này trong database chưa

        System.out.println(cv);
        long result =  db.insert(TABLE_NAME_AUTH,null,cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Create New Account Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    //hàm đọc account
    Cursor readAccount(String ex, String pass){
            String query = "SELECT * FROM " + TABLE_NAME_AUTH + " WHERE " + COLUNM_EMAIL + " = '" + ex + "' AND " + COLUNM_PASSWORD + " = '" + pass + "'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = null;
            if(db != null){
                cursor = db.rawQuery(query,null);
            }
            System.out.println(cursor);
            return cursor;
    }
    //hàm check
    boolean checkAccount(String user) {
        String query = "SELECT * FROM " + TABLE_NAME_AUTH + " WHERE " + COLUNM_EMAIL + " = '" + user + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        boolean accountExists = false;
        try {
            if (db != null) {
                cursor = db.rawQuery(query, null);
            }
            if (cursor != null && cursor.moveToFirst()) {
                accountExists = true;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        System.out.println(accountExists);
        return accountExists;
    }

    //hàm tạo note
    void createNote(String title, String content , String creationDate , boolean isImportant,String complitionDate,String authName){
        SQLiteDatabase db = this.getWritableDatabase();
        int is = (isImportant==true?1:0);
        System.out.println(is);
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NOTES_TITLE,title);
        cv.put(COLUMN_NOTES_CONTENT,content);
        cv.put(COLUMN_NOTES_CREATION_DATE,creationDate);
        cv.put(COLUMN_NOTES_IS_IMPORTANT,is);
        cv.put(COLUMN_NOTES_COMPLETION_DATE,complitionDate);
        cv.put(COLUNM_EMAIL,authName);
        System.out.println(cv);
        long result = db.insert(TABLE_NAME_NOTE,null,cv);
        System.out.println(result);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Create New Note Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    //hàm đọc note theo username
    Cursor ListNote(String username){
        String query = "SELECT * FROM "+ TABLE_NAME_NOTE + " WHERE " + COLUNM_EMAIL + " = '" + username +"' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        System.out.println(cursor);
        return cursor;
    }
    //hàm xóa note theo id
    public void DeleteNote(String position){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME_NOTE, "_id = ?", new String[]{position});
        if (result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "You deleted", Toast.LENGTH_SHORT).show();
        }
        ((Activity) context).recreate();

    }

    //hàm đánh dấu khi quan trọng
    public void markImportant(String position,String isImportant){
        int isI = 0;
        ContentValues cv = new ContentValues();
        if(isImportant.equals("0") ){
            isI = 1;
        }
        cv.put(COLUMN_NOTES_IS_IMPORTANT,isI);
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.update(TABLE_NAME_NOTE,cv,"_id = ?",new String[]{position});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Changed Successfully!", Toast.LENGTH_SHORT).show();
        }
    }
    //cập nhật lại note bên trong database
     public void updateNote(String title,String contexts,String date,int isImportant,String id){
//         Date currentDate = new Date();
//         SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//         String formattedDate = dateFormat.format(currentDate);
//         if(date.equals(formattedDate)){
//
//         }
         SQLiteDatabase db = this.getWritableDatabase();
         ContentValues cv = new ContentValues();
         cv.put(COLUMN_NOTES_TITLE,title);
         cv.put(COLUMN_NOTES_CONTENT,contexts);
         cv.put(COLUMN_NOTES_IS_IMPORTANT,isImportant);
         cv.put(COLUMN_NOTES_COMPLETION_DATE,date);
         long result = db.update(TABLE_NAME_NOTE,cv,"_id = ?",new String[]{id});
         if(result == -1){
             Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
         }else{
             Toast.makeText(context, "Edit Note Successfully!", Toast.LENGTH_SHORT).show();
         }
     }
     //lấy ra danh sách note có trường important bằng 1
     public Cursor getAllNoteByIsImportant(String userName){
         String query = "SELECT * FROM "+ TABLE_NAME_NOTE + " WHERE "
                 + COLUMN_NOTES_IS_IMPORTANT + " = 1 and " + COLUNM_EMAIL +" = '" +userName + "'" ;
         SQLiteDatabase db = this.getReadableDatabase();
         Cursor cursor = null;
         if (db != null) {
             cursor = db.rawQuery(query, null);
         }
         System.out.println(cursor);
         return cursor;
     }

     public Cursor getAllNoteByCompleteDay(String userName,String date){
         String query = "SELECT * FROM "+ TABLE_NAME_NOTE + " WHERE "
                 + COLUMN_NOTES_COMPLETION_DATE + " = '" + date +"' and " + COLUNM_EMAIL +" = '" +userName + "'" ;
         SQLiteDatabase db = this.getReadableDatabase();
         Cursor cursor = null;
         if (db != null) {
             cursor = db.rawQuery(query, null);
         }
         System.out.println(cursor);
         return cursor;
     }

}
