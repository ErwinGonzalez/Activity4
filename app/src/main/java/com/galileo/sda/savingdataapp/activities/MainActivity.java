package com.galileo.sda.savingdataapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.galileo.sda.savingdataapp.R;
import com.galileo.sda.savingdataapp.adapter.NoteAdapter;
import com.galileo.sda.savingdataapp.database.NoteDAO;
import com.galileo.sda.savingdataapp.database.NoteModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.R.attr.inset;


public class MainActivity extends AppCompatActivity implements NoteAdapter.handleClicked{

    public NoteDAO notes;
    public RecyclerView rv;
    public NoteAdapter noteAd;
    public RecyclerView.LayoutManager manager;
    public ArrayList<NoteModel> noteList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        notes = new NoteDAO(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.faButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInsertDialog(true,-1);
            }
        });
        rv =(RecyclerView)findViewById(R.id.mainRecView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        noteList = notes.getAllNotes();
        noteAd = new NoteAdapter(noteList);
        noteAd.setCallback(this);
        rv.setAdapter(noteAd);
    }
    @Override
    public void onResume(){
        super.onResume();
        updateList();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,Settings.class));
        }

        return super.onOptionsItemSelected(item);
    }
    public void showInsertDialog(final boolean insert, final int position){
        LayoutInflater li = LayoutInflater.from(this);
        View dialog_view = li.inflate(R.layout.dialog_layout,null);
        final EditText title = (EditText)dialog_view.findViewById(R.id.etTitle);
        final EditText content = (EditText)dialog_view.findViewById(R.id.etContent);
        if(!insert){
            NoteModel n = noteList.get(position);
            title.setText(n.getTitle());
            content.setText(n.getContent());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Note")
                .setView(dialog_view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String timestamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).format(Calendar.getInstance().getTime());
                        if(insert)
                            notes.insertNote(title.getText().toString(),content.getText().toString(),timestamp);
                        else{
                            notes.updateNote(title.getText().toString(),content.getText().toString(),timestamp
                                    ,noteList.get(position).getId());}
                        updateList();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void updateList(){
        noteList = notes.getAllNotes();
        noteAd.clearData(noteList);
        rv.setAdapter(noteAd);
        noteAd.notifyDataSetChanged();
    }

    @Override
    public void editClicked(int position) {
        showInsertDialog(false,position);
    }

    @Override
    public void deleteClicked(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Confirmation")
                .setMessage("You are about to delete the note, proceed?")
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NoteModel n = noteList.get(position);
                        notes.deleteNote(n.getId());
                        updateList();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}