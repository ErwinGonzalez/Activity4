package com.galileo.sda.savingdataapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by EEVGG on 16/06/2017.
 */

public class NoteDAO {
    public static final String TAG = "NoteDAO";

    private SQLiteDatabase db;
    private DBHandler dbh;
    private Context ctx;

    private String[] columns = {
            DBHandler.NOTE_ID,
            DBHandler.NOTE_TITLE,
            DBHandler.NOTE_CONTENT,
            DBHandler.NOTE_TIMESTAMP
    };
    public NoteDAO(Context context){
        this.ctx = context;
        dbh = new DBHandler(context);
        try{
            open();
        }catch (SQLException e) {
            Log.e(TAG, "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() {
        db = dbh.getWritableDatabase();
    }
    public void close(){
        dbh.close();
    }
    public NoteModel insertNote(String Title, String Content, String Time){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHandler.NOTE_TITLE,Title);
        contentValues.put(DBHandler.NOTE_CONTENT,Content);
        contentValues.put(DBHandler.NOTE_TIMESTAMP,Time);
        long id =db.insert(DBHandler.NOTE_TABLE,null,contentValues);
        Cursor c = db.query (DBHandler.NOTE_TABLE, columns,DBHandler.NOTE_ID+" = "+id,null,null,null,null);
        c.moveToFirst();
        NoteModel newNote = cursorToNote(c);
        c.close();
        return newNote;
    }
    public void deleteNote(NoteModel note){
        long id = note.getId();
        db.delete(DBHandler.NOTE_TABLE,DBHandler.NOTE_ID+" = "+id,null);
    }
    private NoteModel cursorToNote(Cursor cursor){
        NoteModel note = new NoteModel();
        note.setId(cursor.getLong(0));
        note.setTitle(cursor.getString(1));
        note.setContent(cursor.getString(2));
        note.setDatetime(cursor.getString(3));
        return note;
    }
    public void deleteAllNotes(){
        db = dbh.getWritableDatabase();
        db.execSQL("DELETE FROM "+DBHandler.NOTE_TABLE);
    }
    public ArrayList<NoteModel> getAllNotes(){
        ArrayList<NoteModel> list = new ArrayList<NoteModel>();
        Cursor c = db.query(DBHandler.NOTE_TABLE,columns,null,null,null,null,null);
        if(c!=null) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                NoteModel note = cursorToNote(c);
                list.add(note);
                c.moveToNext();
            }
            c.close();
        }
        return  list;
    }
    public void updateNote(String title,String content, String time,long id){
        /*TODO update the note with the specified id, using the new parameters
         *make use of ContentValues and the database update method
         */
    }
    public void deleteNote(long id) {
        /*TODO delete the note with the specified id, you can use either
         * the database query or delete method
        */
    }
}
