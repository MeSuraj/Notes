package learn.zero.say.notes.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.util.Date;

import learn.zero.say.notes.Model.Notes;
import learn.zero.say.notes.R;
import learn.zero.say.notes.ViewModels.NotesViewModel;
import learn.zero.say.notes.databinding.ActivityInsertNoteBinding;

public class InsertNoteActivity extends AppCompatActivity {

    ActivityInsertNoteBinding binding;
    String title, subtitle, notes;
    NotesViewModel notesViewModel;
    String priority = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);

        //priority view
        binding.greenPriority.setOnClickListener(v -> {

            binding.greenPriority.setImageResource(R.drawable.ic_done);
            binding.yellowPriority.setImageResource(0);
            binding.redPriority.setImageResource(0);
            priority = "1";
        } );

        binding.yellowPriority.setOnClickListener(v -> {

            binding.greenPriority.setImageResource(0);
            binding.yellowPriority.setImageResource(R.drawable.ic_done);
            binding.redPriority.setImageResource(0);
            priority = "2";
        } );

        binding.redPriority.setOnClickListener(v -> {
            binding.greenPriority.setImageResource(0);
            binding.yellowPriority.setImageResource(0);
            binding.redPriority.setImageResource(R.drawable.ic_done);
            priority = "3";
        } );

        //Notes Done Button
        binding.doneNoteBtn.setOnClickListener(v ->{

        title = binding.notesTitle.getText().toString();
        subtitle = binding.notesSubtitle.getText().toString();
        notes = binding.notesData.getText().toString();

        CreateNotes(title, subtitle, notes);
        });
    }

    private void CreateNotes (String title, String subtitle, String notes) {

        //Date View
        Date date = new Date();
        CharSequence sequence = DateFormat.format("MMM dd, yyyy HH:mm:ss", date.getTime());

        Notes notes1 = new Notes();
        notes1.notesTitle = title;
        notes1.notesSubtitle = subtitle;
        notes1.notes = notes;
        //Date View
        notes1.notesDate = sequence.toString();
        //priority
        notes1.notesPriority = priority;
        notesViewModel.insertNote(notes1);


        Toast.makeText(this,"Notes Created Successfully", Toast.LENGTH_SHORT).show();

        finish();



    }


}