package com.pritesh.notes.Activity;

import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.pritesh.notes.Globals;
import com.pritesh.notes.Models.Note;
import com.pritesh.notes.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateNoteActivity extends AppCompatActivity {
    Note note;
    private EditText etTitle, etDescription;
    private String id, title, description;
    private ProgressBar progressBar;
    private MenuItem doneMenu;
    private final String TAG = "CREATE_NOTE_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        id = getIntent().getStringExtra(getString(R.string.id));
        title = getIntent().getStringExtra(getString(R.string.title));
        description = getIntent().getStringExtra(getString(R.string.description));
        initialize();

    }

    private void initialize() {
        etDescription=findViewById(R.id.etDescription);
        etTitle=findViewById(R.id.etTitle);
        progressBar = findViewById(R.id.progressBar);
        if(id!=null && !id.equalsIgnoreCase("")){
            etTitle.setText(title);
            etDescription.setText(description);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);
        doneMenu = menu.findItem(R.id.done);
        doneMenu.setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.done:
                if(etTitle.getText().length() == 0 && etDescription.getText().length() == 0){
                    Toast.makeText(getBaseContext(), R.string.enter_title_description,Toast.LENGTH_SHORT).show();
                }else {
                    if(Globals.isNetworkAvailable(getBaseContext()))
                        createNote();
                    else
                        Toast.makeText(getBaseContext(),getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }
                break;
            case android.R.id.home:
                if(etTitle.getText().length() != 0 && etDescription.getText().length() != 0) {
                    if(Globals.isNetworkAvailable(getBaseContext())) {
                        createNote();
                    }else{
                        Toast.makeText(getBaseContext(),getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);
    }

    private void createNote() {
        note=new Note(null,getDate(),etTitle.getText().toString().trim(),etDescription.getText().toString().trim());
        progressBar.setVisibility(View.VISIBLE);
        if(id!=null && !id.equalsIgnoreCase("")){
            Map<String,Object> updateObject = new HashMap<>();
            updateObject.put(getString(R.string.title),etTitle.getText().toString().trim());
            updateObject.put(getString(R.string.description),etDescription.getText().toString().trim());
            updateObject.put(getString(R.string.created_small),getDate());
            Globals.getDatabaseReference()
                    .child(getString(R.string.notes))
                    .child(Globals.getAuth().getCurrentUser().getUid()).child(id)
                    .updateChildren(updateObject)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    finish();
                    progressBar.setVisibility(View.GONE);
                }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }else {
            Globals.getDatabaseReference()
                    .child(getString(R.string.notes))
                    .child(Globals.getAuth().getCurrentUser().getUid()).push().setValue(note)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            finish();
                            progressBar.setVisibility(View.GONE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private String getDate(){
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
        Log.d(TAG,simpleDateFormat.format(date));
        return simpleDateFormat.format(date);
    }
}
