package com.pritesh.notes.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pritesh.notes.Adapter.NotesAdapter;
import com.pritesh.notes.Note;
import com.pritesh.notes.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private List<Note> noteList;
    private NotesAdapter notesAdapter;
    private TextView tvAddNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);

        noteList=new ArrayList<>();
        noteList.add(new Note("2-2-02","Title","This is a small description"));
        noteList.add(new Note("2-2-02","Title is very long","This is a veryyyyyyyyy long long long long long long long long long long long long long long long long long long long long long long long description"));
        noteList.add(new Note("2-2-02","Title is very long","This is a veryyyyyyyyy long long long long long long long long long long long long long long long long long long long long long long long description"));
        noteList.add(new Note("2-2-02","Title","This is a small description"));
        noteList.add(new Note("2-2-02","Title","This is a veryyyyyyyyy long long long long long long long long long long long long long long long long long long long long long long long description"));
        noteList.add(new Note("2-2-02","Title is very long","This is a small description"));

        notesAdapter = new NotesAdapter(this, noteList);
        recyclerView.setAdapter(notesAdapter);

    }

    private void initialize() {
        recyclerView = findViewById(R.id.recyclerView);
        tvAddNote = findViewById(R.id.tvAddNote);
        tvAddNote.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvAddNote:
                startActivity(new Intent(getBaseContext(),CreateNoteActivity.class));
        }
    }
}