package com.example.mynoteskeepingapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mynoteskeepingapp.Dao.NotesDao;
import com.example.mynoteskeepingapp.database.NotesDatabase;
import com.example.mynoteskeepingapp.model.Notes;

import java.util.List;

public class NotesRepository {
    private NotesDao notesDao;
    private LiveData<List<Notes>> getAllNotes;

    private LiveData<List<Notes>> hightolow;
    private LiveData<List<Notes>> lowtohigh;
    public NotesRepository(Application application){
        NotesDatabase notesDatabase = NotesDatabase.getDatabaseInstance(application);
        notesDao = notesDatabase.notesDao();
        getAllNotes = notesDao.getAllNotes();
        hightolow = notesDao.highToLow();
        lowtohigh = notesDao.lowToHigh();
    }

    public LiveData<List<Notes>> getAllNotesData(){
        return getAllNotes;
    }
    public LiveData<List<Notes>> getHightolow(){
        return hightolow;
    }
    public LiveData<List<Notes>> getLowtohigh(){
        return lowtohigh;
    }
    public void insertNotes(Notes notes){
        notesDao.insertNotes(notes);
    }

    public void deleteNotes(int id){
        notesDao.deleteNotes(id);
    }

    public void updateNotes(Notes notes){
        notesDao.updateNotes(notes);
    }
}
