package com.pritesh.notes.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pritesh.notes.Adapter.NotesAdapter;
import com.pritesh.notes.Globals;
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


//        notesAdapter = new NotesAdapter(this, noteList);
//        recyclerView.setAdapter(notesAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchData();
    }

    private void fetchData() {
        Globals.getDatabaseReference().child(getString(R.string.notes))
                .child(Globals.getAuth().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();

                noteList=new ArrayList<>();
                for (DataSnapshot snapshot:dataSnapshots){
                    noteList.add(new Note(snapshot.getKey(),snapshot.child("created").getValue().toString(),snapshot.child("title").getValue().toString(),snapshot.child("description").getValue().toString()));
                }
                notesAdapter=new NotesAdapter(getBaseContext(),noteList);
                recyclerView.setAdapter(notesAdapter);
                notesAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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