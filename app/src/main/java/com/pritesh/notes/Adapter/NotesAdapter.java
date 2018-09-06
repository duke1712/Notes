package com.pritesh.notes.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pritesh.notes.Activity.CreateNoteActivity;
import com.pritesh.notes.Activity.MainActivity;
import com.pritesh.notes.Interface.SelectListener;
import com.pritesh.notes.Models.Note;
import com.pritesh.notes.R;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>{
    private Context context;
    private List<Note> noteList;
    private boolean isSelected = false;
    private int selectedCount = 0;
    private SelectListener selectListener;
    public NotesAdapter(Context context, List<Note> noteList, SelectListener selectListener) {
        this.context = context;
        this.noteList = noteList;
        this.selectListener=selectListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tvTitle.setText(noteList.get(position).getTitle());
        holder.tvCreated.setText(noteList.get(position).getCreated());
        holder.tvDescription.setText(noteList.get(position).getDescription());
//        holder.cardView.setCardBackgroundColor(R.drawable.card_selector);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if(isSelected){
                    if(holder.cardView.isSelected()) {
                        selectedCount--;
                        holder.cardView.setCardBackgroundColor(context.getColor(android.R.color.white));
                        holder.cardView.setSelected(false);
                        selectListener.selected(false,position);
                        if(selectedCount==0){
                            isSelected=false;
                        }
                    }else{
                        selectedCount++;
                        holder.cardView.setSelected(true);
                        holder.cardView.setCardBackgroundColor(context.getColor(R.color.cardBackground));
                        selectListener.selected(true,position);
                    }


                }else{
                Note note = noteList.get(position);
                Intent intent = new Intent(context, CreateNoteActivity.class);
                intent.putExtra(context.getString(R.string.title),note.getTitle());
                intent.putExtra(context.getString(R.string.id),note.getId());
                intent.putExtra(context.getString(R.string.description),note.getTitle());
                intent.putExtra(context.getString(R.string.created),note.getTitle());
                MainActivity mainActivity = ((MainActivity)((Activity)context));

                context.startActivity(intent);

                mainActivity.overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);
                }
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("NewApi")
            @Override
            public boolean onLongClick(View v) {
                if(!isSelected) {
                    holder.cardView.setSelected(true);
                    isSelected = true;
                    holder.cardView.setCardBackgroundColor(context.getColor(R.color.cardBackground));
                    selectedCount = 1;
                    selectListener.selected(true,position);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle, tvDescription, tvCreated;
        private CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvCreated = itemView.findViewById(R.id.tvCreated);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
