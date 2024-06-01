package com.example.mynoteskeepingapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mynoteskeepingapp.Dao.NotesDao;
import com.example.mynoteskeepingapp.model.Notes;

@Database(entities = {Notes.class},version = 1,exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {
    public abstract NotesDao notesDao();

    public static NotesDatabase INSTANCE;

    public static NotesDatabase getDatabaseInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),NotesDatabase.class,"Notes_Database").
                    allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}
