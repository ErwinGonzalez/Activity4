package com.galileo.sda.savingdataapp.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.Manifest;

import com.galileo.sda.savingdataapp.R;
import com.galileo.sda.savingdataapp.database.NoteDAO;
import com.galileo.sda.savingdataapp.database.NoteModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class Settings extends AppCompatActivity{
    private CheckBox saveLocation;
    private SharedPreferences preferences;
    private NoteDAO noteDAO;
    private static String TAG ="Settings Activity";
    private static final int REQUEST_WRITE_STORAGE=112;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        noteDAO = new NoteDAO(this);
        saveLocation =(CheckBox)findViewById(R.id.savelocationcheck);
        Button saveButton = (Button) findViewById(R.id.savelocationbutton);
        Button deleteButton = (Button) findViewById(R.id.deletedatabutton);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        /*TODO set a onCheckedChangedListener on the checkbox
        * then save the value to the shared preferences
        * Finally check the value from the preferences, so the
        * checkbox can be checked or not
        * this is done by checkbox.setChecked(true)*/
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestWriteStorage();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
            }
        });

    }
    private void requestWriteStorage(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && saveLocation.isChecked()){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE_STORAGE);
            }
        }else {
            saveData();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantRestuls){
        if(requestCode==REQUEST_WRITE_STORAGE){
            if(grantRestuls[0]==PackageManager.PERMISSION_GRANTED){
                saveData();
            }
        }
    }
    private void saveData() {
        try {
            String filename = "Notes.txt";
            File file;
            //As in java, use a FileOutputStream to write data
            FileOutputStream fos;
            if (saveLocation.isChecked() && Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                //set save location external
                file = new File(Environment.getExternalStorageDirectory(),filename);
            } else {
                //set save location internal
                file = new File(getApplicationContext().getFilesDir(),filename);
            }
            fos = new FileOutputStream(file);
            //use DAO method to get notes as a list
            ArrayList<NoteModel> notes = noteDAO.getAllNotes();
            String out="";
            //for every note get the String Value and append to output
            for(NoteModel note:notes)
                out+=note.toString()+"\n";
            //when done write to FOS
            fos.write(out.getBytes());
            //always close FOS after done using
            fos.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void deleteData() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Warning");
        alertDialogBuilder.setMessage("You are about to delete all data, proceed?");

        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                noteDAO.deleteAllNotes();
            }
        });
        alertDialogBuilder.setNegativeButton(android.R.string.no,new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
