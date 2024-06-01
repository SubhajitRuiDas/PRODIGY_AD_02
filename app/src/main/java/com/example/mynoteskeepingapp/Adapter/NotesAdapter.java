package com.example.mynoteskeepingapp.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynoteskeepingapp.MainActivity;
import com.example.mynoteskeepingapp.R;
import com.example.mynoteskeepingapp.activity.UpdateNotesActivity;
import com.example.mynoteskeepingapp.model.Notes;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    MainActivity mainActivity;
    List<Notes> notes;
    List<Notes> allNotesItem;
    public NotesAdapter(MainActivity mainActivity, List<Notes> notes) {
        this.mainActivity = mainActivity;
        this.notes = notes;
        allNotesItem = new ArrayList<>(notes);
    }

    public void searchNotes(List<Notes> filteredNotesName){
        this.notes = filteredNotesName;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(mainActivity).inflate(R.layout.notes_recycleview_layoutscreen,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.title.setText(notes.get(position).notesTitle);
        holder.subTitle.setText(notes.get(position).notesSubTitle);
        holder.date.setText(notes.get(position).notesDate);
        if(notes.get(position).notesPriority.equals("1")){
            holder.priority.setBackgroundResource(R.drawable.green_shape);
        } else if (notes.get(position).notesPriority.equals("2")) {
            holder.priority.setBackgroundResource(R.drawable.yellow_shape);
        }else if (notes.get(position).notesPriority.equals("3")) {
            holder.priority.setBackgroundResource(R.drawable.red_shape);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mainActivity, UpdateNotesActivity.class);

            intent.putExtra("ID",notes.get(position).id);
            intent.putExtra("TITLE",notes.get(position).notesTitle);
            intent.putExtra("SUBTITLE",notes.get(position).notesSubTitle);
            intent.putExtra("PRIORITY",notes.get(position).notesPriority);
            intent.putExtra("NOTE",notes.get(position).notes);
            mainActivity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class NotesViewHolder extends RecyclerView.ViewHolder{
        TextView title,subTitle,date;
        View priority;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notesTitleShow);
            subTitle = itemView.findViewById(R.id.notesSubtitleShow);
            date = itemView.findViewById(R.id.notesDateShow);
            priority = itemView.findViewById(R.id.notesPriority);
        }
    }
}
