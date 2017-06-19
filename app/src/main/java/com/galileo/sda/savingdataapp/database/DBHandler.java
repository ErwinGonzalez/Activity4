package com.galileo.sda.savingdataapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME ="NotesDatabase";

    static final String NOTE_TABLE="NotesTable";
    static final String NOTE_ID="NoteID";
    static final String NOTE_TITLE="NoteTitle";
    static final String NOTE_CONTENT="NoteContent";
    static final String NOTE_TIMESTAMP="NoteTime";

    DBHandler(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS "+NOTE_TABLE+"("
                    +NOTE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +NOTE_TITLE+" STRING,"
                    +NOTE_CONTENT+" STRING,"
                    +NOTE_TIMESTAMP+" STRING);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+NOTE_TABLE);
        onCreate(sqLiteDatabase);
    }
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
}
