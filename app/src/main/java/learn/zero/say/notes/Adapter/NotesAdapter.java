package learn.zero.say.notes.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import learn.zero.say.notes.Activity.UpdateNotesActivity;
import learn.zero.say.notes.MainActivity;
import learn.zero.say.notes.Model.Notes;
import learn.zero.say.notes.R;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.notesViewholder> {

    MainActivity mainActivity;
    List<Notes> notes;
    List<Notes> allNotesItem;

    public NotesAdapter (MainActivity mainActivity, List<Notes> notes) {
        this.mainActivity = mainActivity;
        this.notes = notes;
        //search for notes
        allNotesItem = new ArrayList(notes);

    }
    //search for notes
    public void searchNotes(List<Notes> filteredName) {

        this.notes =filteredName;
        notifyDataSetChanged();
    }

    @Override
    public notesViewholder onCreateViewHolder (ViewGroup parent, int viewType) {

        return new notesViewholder(LayoutInflater.from(mainActivity).inflate(R.layout.item_notes, parent, false));
    }

    @Override
    public void onBindViewHolder(NotesAdapter.notesViewholder holder, int position) {

        Notes note = notes.get(position);

        switch (note.notesPriority) {
            case "1":
                holder.notesPriority.setBackgroundResource(R.drawable.green_shape);
                break;
            case "2":
                holder.notesPriority.setBackgroundResource(R.drawable.yellow_shape);
                break;
            case "3":
                holder.notesPriority.setBackgroundResource(R.drawable.red_shape);
                break;
        }


        holder.title.setText(note.notesTitle);
        holder.notes.setText(note.notes);
        holder.notesData.setText(note.notesDate);

        //update note
        holder.itemView.setOnClickListener(v ->{

            Intent intent = new Intent(mainActivity, UpdateNotesActivity.class);
            intent.putExtra("id", note.id);
            intent.putExtra("title", note.notesTitle);
            intent.putExtra("subtitle", note.notesSubtitle);
            intent.putExtra("priority", note.notesPriority);
            intent.putExtra("note", note.notes);

            mainActivity.startActivity(intent);
        });

    }

      @Override
    public int getItemCount() {
        return notes.size();
    }

    static class notesViewholder extends RecyclerView.ViewHolder {

        TextView title,notes, notesData;
        View notesPriority;

        public notesViewholder(View itemView) {
            super(itemView);


            title = itemView.findViewById(R.id.notesTitle);
            notes = itemView.findViewById(R.id.notesData);
            notesData = itemView.findViewById(R.id.notesDate);
            notesPriority = itemView.findViewById(R.id.notesPriority);
        }

    }


}
