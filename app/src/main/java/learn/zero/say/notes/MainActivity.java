package learn.zero.say.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;



import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import learn.zero.say.notes.Activity.InsertNoteActivity;
import learn.zero.say.notes.Activity.PrivacyPolicy;
import learn.zero.say.notes.Adapter.NotesAdapter;
import learn.zero.say.notes.Model.Notes;
import learn.zero.say.notes.ViewModels.NotesViewModel;

public class MainActivity extends AppCompatActivity {


    //Add Button fab
    FloatingActionButton newNotesBtn;

    NotesViewModel notesViewModel;
    RecyclerView notesRecycler;
    NotesAdapter adapter;

    TextView nofilter, hightolow, lowtohigh;
    List<Notes> filterAllNotesList;

    //Dark and Day Mode
    SharedPreferences sharedPreferences;
    boolean isDarkModeOn;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Add Button fab
        newNotesBtn = findViewById(R.id.newNotesBtn);
        notesRecycler = findViewById(R.id.notesRecyclerView);

        //Filter
        nofilter= findViewById(R.id.nofilter);
        hightolow = findViewById(R.id.hightolow);
        lowtohigh = findViewById(R.id.lowtohigh);

        nofilter.setBackgroundResource(R.drawable.filter_selected);

        nofilter.setOnClickListener(v ->{
            loadData(0);
            hightolow.setBackgroundResource(0);
            lowtohigh.setBackgroundResource(0);
            nofilter.setBackgroundResource(R.drawable.filter_selected);
        });

        hightolow.setOnClickListener(v ->{
            loadData(1);
            hightolow.setBackgroundResource(R.drawable.filter_selected);
            lowtohigh.setBackgroundResource(0);
            nofilter.setBackgroundResource(0);
        });

        lowtohigh.setOnClickListener(v ->{
            loadData(2);
            hightolow.setBackgroundResource(0);
            lowtohigh.setBackgroundResource(R.drawable.filter_selected);
            nofilter.setBackgroundResource(0);
        });


        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);

        newNotesBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, InsertNoteActivity.class));
        });

        notesViewModel.getAllNotes.observe(this, new Observer<List<Notes>>() {
            @Override
            public  void onChanged(List<Notes> notes) {
                setAdapter(notes);
                filterAllNotesList = notes;
            }
        });
    }

    private void   loadData(int i) {
        if (i == 0) {
            notesViewModel.getAllNotes.observe(this, new Observer<List<Notes>>() {
                @Override
                public  void onChanged(List<Notes> notes) {
                    setAdapter(notes);
                    filterAllNotesList = notes;
                }
            });

        } else if (i== 1){
            notesViewModel.hightolow.observe(this, new Observer<List<Notes>>() {
                @Override
                public  void onChanged(List<Notes> notes) {
                    setAdapter(notes);
                    filterAllNotesList = notes;
                }
            });
        }else if (i == 2){
            notesViewModel.lowtohigh.observe(this, new Observer<List<Notes>>() {
                @Override
                public  void onChanged(List<Notes> notes) {
                    setAdapter(notes);
                    filterAllNotesList = notes;
                }
            });
        }
    }

    public void setAdapter(List<Notes> notes) {
        notesRecycler.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        adapter =new NotesAdapter(MainActivity.this, notes);
        notesRecycler.setAdapter(adapter);
    }
        //For Search notes And Day And Night Mode
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        //Day And Night Mode
        sharedPreferences
                = getSharedPreferences(
                "sharedPrefs", MODE_PRIVATE);
        editor
                = sharedPreferences.edit();
        isDarkModeOn
                = sharedPreferences
                .getBoolean(
                        "isDarkModeOn", false);

        final MenuItem nightMode = menu.findItem(R.id.night_mode);
        final MenuItem dayMode = menu.findItem(R.id.day_mode);

        if (isDarkModeOn) {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_YES);
            dayMode.setVisible(true);
            nightMode.setVisible(false);
        } else {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_NO);
            dayMode.setVisible(false);
            nightMode.setVisible(true);

        }

        nightMode.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_YES);

                editor.putBoolean(
                        "isDarkModeOn", true);
                editor.apply();
                Toast.makeText(getApplicationContext(), "Dark Mode On ", Toast.LENGTH_SHORT).show();
                dayMode.setVisible(true);
                nightMode.setVisible(false);
                return true;
            }
        });

        dayMode.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_NO);
                editor.putBoolean(
                        "isDarkModeOn", false);
                editor.apply();
                Toast.makeText(getApplicationContext(), "Dark Mode Off", Toast.LENGTH_SHORT).show();
                dayMode.setVisible(false);
                nightMode.setVisible(true);
                return true;

            }
        });


        // For Search Notes
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query){
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText){
                NotesFilter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void NotesFilter(String newText) {
        Log.e("#####", "Notes Filter" +newText);

        ArrayList<Notes > FilterNames = new ArrayList<>();

        //Search
        for (Notes notes:this.filterAllNotesList){
            if(notes.notesTitle.contains(newText) || notes.notesSubtitle.contains(newText)){
                FilterNames.add(notes);
            }
        }
        this.adapter.searchNotes(FilterNames);
    }



    //Contact Us,  PrivacyPolicy And Share App
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.contactUs:
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                // only email apps should handle this
                String[] to = {getString(R.string.ContactUsMailId)};
                intent.putExtra(Intent.EXTRA_EMAIL, to);
                startActivity(Intent.createChooser(intent, "Contact us!"));
                return true;

            case R.id.privacyPolicy:
                startActivity(new Intent(getApplicationContext(), PrivacyPolicy.class));
                return true;

            case R.id.share_app:
                String appUrl = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                Intent sharing = new Intent(Intent.ACTION_SEND);
                sharing.setType("text/plain");
                sharing.putExtra(Intent.EXTRA_SUBJECT, "Download Now");
                sharing.putExtra(Intent.EXTRA_TEXT, appUrl);
                startActivity(Intent.createChooser(sharing, "Share via"));

            default:
                return super.onOptionsItemSelected(item);

        }

    }



}