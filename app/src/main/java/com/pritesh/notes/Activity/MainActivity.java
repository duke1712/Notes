package com.pritesh.notes.Activity;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pritesh.notes.Adapter.NotesAdapter;
import com.pritesh.notes.Globals;
import com.pritesh.notes.Interface.SelectListener;
import com.pritesh.notes.Models.Note;
import com.pritesh.notes.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SelectListener{

    private RecyclerView recyclerView;
    private List<Note> filteredNoteList = new ArrayList<>();
    private List<Note> noteList = new ArrayList<>();
    private NotesAdapter notesAdapter;
    private TextView tvAddNote,noDataView;
    private ProgressBar progressBar;
    private List<Note> selectedNotes = new ArrayList<>();
    private MenuItem deleteMenu;
    private MenuItem sortMenu,logoutMenu;
    private MenuItem searchMenu;
    private SearchView searchView;
    private SearchManager searchManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);//GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);


        notesAdapter = new NotesAdapter(this, filteredNoteList,this);

        recyclerView.setAdapter(notesAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        fetchData();
    }

    private void fetchData() {
        Globals.getDatabaseReference().child(getString(R.string.notes))
                .child(Globals.getAuth().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();

                noteList.clear();
                for (DataSnapshot snapshot:dataSnapshots){
                    noteList.add(new Note(snapshot.getKey(),snapshot.child("created").getValue().toString(),snapshot.child("title").getValue().toString(),snapshot.child("description").getValue().toString()));
                }
                filteredNoteList.clear();
                filteredNoteList.addAll(noteList);
                if(notesAdapter!=null)
                notesAdapter.notifyDataSetChanged();

                if(filteredNoteList.size()==0)
                    noDataView.setVisibility(View.VISIBLE);
                else
                    noDataView.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void initialize() {
        recyclerView = findViewById(R.id.recyclerView);
        tvAddNote = findViewById(R.id.tvAddNote);
        progressBar = findViewById(R.id.progressBar);
        noDataView = findViewById(R.id.noDataView);
        tvAddNote.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvAddNote:
                startActivity(new Intent(getBaseContext(),CreateNoteActivity.class));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);
        deleteMenu=menu.findItem(R.id.delete);
        deleteMenu.setVisible(false);
        sortMenu=menu.findItem(R.id.sort);
        sortMenu.setVisible(true);
        logoutMenu=menu.findItem(R.id.logout);
        logoutMenu.setVisible(true);
        searchMenu=menu.findItem(R.id.action_search);
        searchMenu.setVisible(true);

        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView)searchMenu.getActionView();
        searchView.setQueryHint("Search Notes...");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // This is your adapter that will be filtered
                filter(newText);

                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                // **Here you can get the value "query" which is entered in the search box.**

                Toast.makeText(getApplicationContext(),"searchvalue :"+query,Toast.LENGTH_LONG).show();

                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onCreateOptionsMenu(menu);
    }

//    Filter the  list on the basis of the query string
    private void filter(String newText) {
        if(newText.equalsIgnoreCase("")){
            filteredNoteList.clear();
            filteredNoteList.addAll(noteList);
        }else {
            filteredNoteList.clear();
            for (int i = 0; i < noteList.size(); i++) {
                if (noteList.get(i).getTitle().toLowerCase().contains(newText.toLowerCase()) || noteList.get(i).getDescription().toLowerCase().contains(newText.toLowerCase()))
                    filteredNoteList.add(noteList.get(i));
            }
        }
        notesAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                deleteNotes();
                break;
            case R.id.logout:
                showdialog();
                break;
            case R.id.sort:
                Collections.reverse(filteredNoteList);
                notesAdapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        Globals.getAuth().signOut();
        startActivity(new Intent(this,LoginActivity.class));
        finish();

    }

//    function to delete notes
    private void deleteNotes() {
        progressBar.setVisibility(View.VISIBLE);
//        Map containing id to delete the notes
        Map<String,Object> updateObject = new HashMap<>();
        for(int i = 0; i<selectedNotes.size();i++) {
            updateObject.put(selectedNotes.get(i).getId(),null);
            filteredNoteList.remove(selectedNotes.get(i));
        }

        NotesAdapter.isSelected=false;
        Globals.getDatabaseReference().child(getString(R.string.notes))
                .child(Globals.getAuth().getUid()).updateChildren(updateObject)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                        notesAdapter.notifyDataSetChanged();
                        if(filteredNoteList.size()==0)
                            noDataView.setVisibility(View.VISIBLE);
                        else
                            noDataView.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                if(filteredNoteList.size()==0)
                    noDataView.setVisibility(View.VISIBLE);
                else
                    noDataView.setVisibility(View.GONE);
            }
        });
    }
    void showdialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Are you sure you want to logout?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        logout();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }
    @Override
    public void selected(boolean bool, int pos) {
//        check card selected or not
        if(bool){
//            add selected note to another list
            selectedNotes.add(filteredNoteList.get(pos));
            deleteMenu.setVisible(true);
        }else{
//            remove selected note from list
            selectedNotes.remove(filteredNoteList.get(pos));
            if(selectedNotes.size()==0)
                deleteMenu.setVisible(false);
        }
    }
}