package learn.zero.say.notes.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Date;

import learn.zero.say.notes.Model.Notes;
import learn.zero.say.notes.R;
import learn.zero.say.notes.ViewModels.NotesViewModel;
import learn.zero.say.notes.databinding.ActivityUpdateNotesBinding;

public class UpdateNotesActivity extends AppCompatActivity {

    ActivityUpdateNotesBinding binding;
    NotesViewModel notesViewModel;
    String priority ="1";
    //s = string , i = int
    String   stitle, ssubtitle, snotes, spriority;
    int iid;
    //Ads
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUpdateNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        iid = getIntent().getIntExtra("id",0);
        stitle = getIntent().getStringExtra("title");
        ssubtitle = getIntent().getStringExtra("subtitle");
        spriority = getIntent().getStringExtra("priority");
        snotes = getIntent().getStringExtra("note");

        binding.updateTitle.setText(stitle);
        binding.updateSubtitle.setText(ssubtitle);
        binding.updateNotes.setText(snotes);

        //Ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        loadAds();


        //Show the priority of previous
        if (spriority.equals("1")){
            binding.greenPriority.setImageResource(R.drawable.ic_done);
        } else if (spriority.equals("2")){
            binding.yellowPriority.setImageResource(R.drawable.ic_done);
        }else if (spriority.equals("3")){
            binding.redPriority.setImageResource(R.drawable.ic_done);
        }

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

        binding.updateNoteBtn.setOnClickListener(v -> {

            String title = binding.updateTitle.getText().toString();
            String subtitle = binding.updateSubtitle.getText().toString();
            String notes = binding.updateNotes.getText().toString();

            UpdateNotes(title, subtitle, notes);

        });
    }

    private void UpdateNotes(String title, String subtitle, String notes) {

        //Date View
        Date date = new Date();
        CharSequence sequence = DateFormat.format("MMM dd, yyyy HH:mm:ss", date.getTime());

        Notes updateNotes = new Notes();

        updateNotes.id =iid;
        updateNotes.notesTitle = title;
        updateNotes.notesSubtitle = subtitle;
        updateNotes.notes = notes;
        //Date View
        updateNotes.notesDate = sequence.toString();
        //priority
        updateNotes.notesPriority = priority;

        notesViewModel.updateNote(updateNotes);

        Toast.makeText(this,"Notes Updated Successfully", Toast.LENGTH_SHORT).show();

        finish();

    }

    //Ads InterstitialAdLoadCallback
    void loadAds() {

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, getString(R.string.MainView_BackPress_IntAds), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });
    }


    @Override
    public void onBackPressed() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(UpdateNotesActivity.this);

            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    mInterstitialAd = null;
                    UpdateNotesActivity.super.onBackPressed();
                }
            });
        } else
        {
            super.onBackPressed();
        }
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(@Nullable MenuItem item) {

        if (item.getItemId() ==R.id.ic_delete) {
            BottomSheetDialog sheetDialog= new BottomSheetDialog(UpdateNotesActivity.this,
                    R.style.BottomSheetStyle);

            View view = LayoutInflater. from (UpdateNotesActivity.this).
                    inflate(R.layout.delete_bottom, (LinearLayout) findViewById(R.id.bottomSheet));

            sheetDialog.setContentView(view);

            TextView yes, no;

            yes= view.findViewById(R.id.delete_yes);
            no = view.findViewById(R.id.delete_no);

            yes.setOnClickListener(v -> {
                notesViewModel.deleteNote (iid);
                finish();
            });

            no.setOnClickListener(v -> {
                sheetDialog.dismiss();

            });

            sheetDialog.show();

        }



        return true;
    }



}
