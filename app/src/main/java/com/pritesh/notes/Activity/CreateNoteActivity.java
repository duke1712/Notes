package com.pritesh.notes.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.pritesh.notes.Globals;
import com.pritesh.notes.Note;
import com.pritesh.notes.R;

public class CreateNoteActivity extends AppCompatActivity {
    Note note;
    private EditText etTitle, etDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        initialize();
    }

    private void initialize() {
        etDescription=findViewById(R.id.etDescription);
        etTitle=findViewById(R.id.etTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.done:
                note=new Note(null,"02-03-2018",etTitle.getText().toString(),etDescription.getText().toString());
                createNote();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createNote() {

        Globals.getDatabaseReference()
                .child(getString(R.string.notes))
                .child(Globals.getAuth().getCurrentUser().getUid()).push().setValue(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
