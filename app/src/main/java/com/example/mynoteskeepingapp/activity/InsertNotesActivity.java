package com.example.mynoteskeepingapp.activity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mynoteskeepingapp.R;
import com.example.mynoteskeepingapp.databinding.ActivityInsertNotesBinding;
import com.example.mynoteskeepingapp.model.Notes;
import com.example.mynoteskeepingapp.viewmodel.NotesViewModel;

import java.util.Date;

public class InsertNotesActivity extends AppCompatActivity {

    ActivityInsertNotesBinding binding;
    String title,subTitle,notes;
    NotesViewModel notesViewModel;
    String priority = "1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityInsertNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar_color));
        // Optional: Set light or dark status bar icons
        WindowInsetsControllerCompat windowInsetsController = ViewCompat.getWindowInsetsController(window.getDecorView());
        if (windowInsetsController != null) {
            windowInsetsController.setAppearanceLightStatusBars(false); // false for light icons on dark background, true for dark icons on light background
        }
        notesViewModel = new ViewModelProvider.AndroidViewModelFactory(InsertNotesActivity.this.getApplication()).create(NotesViewModel.class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.greenPriority.setOnClickListener(v -> {
            priority = "1";
            binding.greenPriority.setImageResource(R.drawable.baseline_check_24);
            binding.yellowPriority.setImageResource(0);
            binding.redPriority.setImageResource(0);
        });
        binding.yellowPriority.setOnClickListener(v -> {
            priority = "2";
            binding.yellowPriority.setImageResource(R.drawable.baseline_check_24);
            binding.greenPriority.setImageResource(0);
            binding.redPriority.setImageResource(0);
        });
        binding.redPriority.setOnClickListener(v -> {
            priority = "3";
            binding.redPriority.setImageResource(R.drawable.baseline_check_24);
            binding.greenPriority.setImageResource(0);
            binding.yellowPriority.setImageResource(0);
        });

        binding.doneNotesBtn.setOnClickListener(v -> {
            title = binding.notesTitle.getText().toString();
            subTitle = binding.notesSubtitle.getText().toString();
            notes = binding.notesData.getText().toString();

            CreateNotes(title,subTitle,notes);
        });
    }

    private void CreateNotes(String title,String subTitle, String notes){
        Date date = new Date();

        CharSequence sequence = DateFormat.format("MMMM d, yyyy",date.getTime());

        Notes notes1 = new Notes();
        notes1.notesTitle = title;
        notes1.notesSubTitle = subTitle;
        notes1.notesDate = sequence.toString();
        notes1.notes = notes;
        notes1.notesPriority = priority;
        notesViewModel.insertNote(notes1);

        Toast.makeText(this, "Notes added successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}