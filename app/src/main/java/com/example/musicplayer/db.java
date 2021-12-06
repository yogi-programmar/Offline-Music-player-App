package com.example.musicplayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class db extends SQLiteOpenHelper {


    Context context;
    public  static  final  int DTABASE_VERSION=1;
    public  static  final  String DATABSE_NAME="mudicdb";
    public  static  final  String  TABLE_NAME ="palyerelist";
    public static  final  String Column_ID="id";
    public static  final  String  Column_Title="title";
    public static  final String Column_Album="img";
    public static  final String CREATE_TABLE="CREATE TABLE " + TABLE_NAME +
            "(" + Column_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            Column_Title+  " TEXT ," +
            Column_Album + " TEXT "+ ")" ;


    public db(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABSE_NAME, factory, DTABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public  void addNotes(String title,String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Column_Title,title);
        values.put(Column_Album,description);
        long result= db.insert(TABLE_NAME,null,values);
        db.close();
        if(result==-1){
            Toast.makeText(context,"Not ADD",Toast.LENGTH_SHORT).show();


        }else {
            Toast.makeText(context, "Data Add", Toast.LENGTH_SHORT).show();
        }

    }
    Cursor readAllData(){
        String query="SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase database= this.getReadableDatabase();
        Cursor cursor= null;
        if(database!=null){
            cursor= database.rawQuery(query,null);

        }


        return  cursor;

    }
    public void deleteSingleItem( String id) {
        SQLiteDatabase database= this.getWritableDatabase();

        long results=database.delete(TABLE_NAME,Column_ID+"=?",new String[]{id});
        database.close();
        if (results==-1){
            Toast.makeText(context,"Not Delete",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context," Delete",Toast.LENGTH_SHORT).show();
        }
    }
}

