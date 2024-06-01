package com.example.mynoteskeepingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.mynoteskeepingapp.Adapter.NotesAdapter;
import com.example.mynoteskeepingapp.activity.InsertNotesActivity;
import com.example.mynoteskeepingapp.model.Notes;
import com.example.mynoteskeepingapp.viewmodel.NotesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    NotesViewModel notesViewModel;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    NotesAdapter notesAdapter;

    TextView nofilter,hightolow,lowtohigh;
    List<Notes> allFilterNotes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar_color));
        // Optional: Set light or dark status bar icons
        WindowInsetsControllerCompat windowInsetsController = ViewCompat.getWindowInsetsController(window.getDecorView());
        if (windowInsetsController != null) {
            windowInsetsController.setAppearanceLightStatusBars(false); // false for light icons on dark background, true for dark icons on light background
        }

        nofilter = findViewById(R.id.noFilter);
        hightolow = findViewById(R.id.highToLow);
        lowtohigh = findViewById(R.id.lowToHigh);

        nofilter.setBackgroundResource(R.drawable.filter_selected_shape);

        recyclerView = findViewById(R.id.notesRecycler);
        notesViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this.getApplication()).create(NotesViewModel.class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nofilter.setOnClickListener(v -> {
            loadData(0);
            nofilter.setBackgroundResource(R.drawable.filter_selected_shape);
            hightolow.setBackgroundResource(R.drawable.filter_btn_shape);
            lowtohigh.setBackgroundResource(R.drawable.filter_btn_shape);
        });
        hightolow.setOnClickListener(v -> {
            loadData(1);
            nofilter.setBackgroundResource(R.drawable.filter_btn_shape);
            hightolow.setBackgroundResource(R.drawable.filter_selected_shape);
            lowtohigh.setBackgroundResource(R.drawable.filter_btn_shape);
        });
        lowtohigh.setOnClickListener(v -> {
            loadData(2);
            nofilter.setBackgroundResource(R.drawable.filter_btn_shape);
            hightolow.setBackgroundResource(R.drawable.filter_btn_shape);
            lowtohigh.setBackgroundResource(R.drawable.filter_selected_shape);
        });

        floatingActionButton = findViewById(R.id.notesAddBtn);
        floatingActionButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this,InsertNotesActivity.class));
        });

        notesViewModel.getAllNotes.observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(List<Notes> notes) {
                allFilterNotes = notes;
                setNotesAdapter(notes);
            }
        });

    }

    private void loadData(int i) {
        if(i == 0){
            notesViewModel.getAllNotes.observe(this, new Observer<List<Notes>>() {
                @Override
                public void onChanged(List<Notes> notes) {
                    allFilterNotes = notes;
                    setNotesAdapter(notes);
                }
            });
        } else if (i == 1) {
            notesViewModel.highToLow.observe(this, new Observer<List<Notes>>() {
                @Override
                public void onChanged(List<Notes> notes) {
                    allFilterNotes = notes;
                    setNotesAdapter(notes);
                }
            });
        } else if (i == 2) {
            notesViewModel.lowToHigh.observe(this, new Observer<List<Notes>>() {
                @Override
                public void onChanged(List<Notes> notes) {
                    allFilterNotes = notes;
                    setNotesAdapter(notes);
                }
            });
        }
    }

    public void setNotesAdapter(List<Notes> notes){
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        notesAdapter = new NotesAdapter(MainActivity.this,notes);
        recyclerView.setAdapter(notesAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_notes, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView =(SearchView)menuItem.getActionView();
        assert searchView != null;
        searchView.setQueryHint("Search Notes here...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                notesFilter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void notesFilter(String newText) {
        ArrayList<Notes> filterNotes = new ArrayList<>();
        for (Notes notes: this.allFilterNotes) {
            if(notes.notesTitle.contains(newText) || notes.notesSubTitle.contains(newText)){
                filterNotes.add(notes);
            }
            this.notesAdapter.searchNotes(filterNotes);
        }
    }
}
