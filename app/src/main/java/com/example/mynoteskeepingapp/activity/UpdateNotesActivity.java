package com.example.mynoteskeepingapp.activity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.mynoteskeepingapp.R;
import com.example.mynoteskeepingapp.databinding.ActivityInsertNotesBinding;
import com.example.mynoteskeepingapp.databinding.ActivityUpdateNotesBinding;
import com.example.mynoteskeepingapp.model.Notes;
import com.example.mynoteskeepingapp.viewmodel.NotesViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Date;

public class UpdateNotesActivity extends AppCompatActivity {

    ActivityUpdateNotesBinding binding;
    //String priority = "1";
    String upTitle,upSubTitle,upPriority,upNote;
    NotesViewModel notesViewModel;
    int upId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityUpdateNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar_color));
        // Optional: Set light or dark status bar icons
        WindowInsetsControllerCompat windowInsetsController = ViewCompat.getWindowInsetsController(window.getDecorView());
        if (windowInsetsController != null) {
            windowInsetsController.setAppearanceLightStatusBars(false); // false for light icons on dark background, true for dark icons on light background
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        notesViewModel = new ViewModelProvider.AndroidViewModelFactory(UpdateNotesActivity.this.getApplication()).create(NotesViewModel.class);

        upId = getIntent().getIntExtra("ID",0);
        upTitle = getIntent().getStringExtra("TITLE");
        upSubTitle = getIntent().getStringExtra("SUBTITLE");
        upPriority = getIntent().getStringExtra("PRIORITY");
        upNote = getIntent().getStringExtra("NOTE");

        binding.upNotesTitle.setText(upTitle);
        binding.upNotesSubTitle.setText(upSubTitle);
        binding.upNoteText.setText(upNote);
        // set priority color
        if(upPriority.equals("1")){
            binding.greenPriority.setImageResource(R.drawable.baseline_check_24);
        }else if(upPriority.equals("2")){
            binding.yellowPriority.setImageResource(R.drawable.baseline_check_24);
        } else if (upPriority.equals("3")) {
            binding.redPriority.setImageResource(R.drawable.baseline_check_24);
        }

        binding.greenPriority.setOnClickListener(v -> {
            upPriority = "1";
            binding.greenPriority.setImageResource(R.drawable.baseline_check_24);
            binding.yellowPriority.setImageResource(0);
            binding.redPriority.setImageResource(0);
        });
        binding.yellowPriority.setOnClickListener(v -> {
            upPriority = "2";
            binding.yellowPriority.setImageResource(R.drawable.baseline_check_24);
            binding.greenPriority.setImageResource(0);
            binding.redPriority.setImageResource(0);
        });
        binding.redPriority.setOnClickListener(v -> {
            upPriority = "3";
            binding.redPriority.setImageResource(R.drawable.baseline_check_24);
            binding.greenPriority.setImageResource(0);
            binding.yellowPriority.setImageResource(0);
        });

        binding.updateNotesBtn.setOnClickListener(v -> {
            String updatedTitle = binding.upNotesTitle.getText().toString();
            String updatedSubTitle = binding.upNotesSubTitle.getText().toString();
            String updatedNotes = binding.upNoteText.getText().toString();

            CreateNotes(updatedTitle,updatedSubTitle,updatedNotes);
        });
    }

    private void CreateNotes(String updatedTitle, String updatedSubTitle, String updatedNotes) {
        Date date = new Date();
        CharSequence updatedDate = DateFormat.format("MMMM d, yyyy",date.getTime());
        Notes updatedNote = new Notes();
        updatedNote.id = upId;
        updatedNote.notesTitle = updatedTitle;
        updatedNote.notesSubTitle = updatedSubTitle;
        updatedNote.notesDate = updatedDate.toString();
        updatedNote.notes = updatedNotes;
        updatedNote.notesPriority = upPriority;

        notesViewModel.updateNote(updatedNote);
        Toast.makeText(this, "Notes Updated successfully", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nt_delete){
            BottomSheetDialog sheetDialog = new BottomSheetDialog(UpdateNotesActivity.this
            ,R.style.BottomSheetStyle);

            View view = LayoutInflater.from(UpdateNotesActivity.this).inflate(R.layout.delete_btn_sheet,(LinearLayout)findViewById(R.id.deleteSheet));
            sheetDialog.setContentView(view);
            sheetDialog.show();
            TextView yes,no;
            yes = view.findViewById(R.id.delete_yes);
            no = view.findViewById(R.id.delete_no);
            yes.setOnClickListener(v -> {
                notesViewModel.deleteNote(upId);
                finish();
            });
            no.setOnClickListener(v -> {
                sheetDialog.dismiss();
            });
        }
        return true;
    }
}