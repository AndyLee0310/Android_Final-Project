package me.mahakagg.notesroom;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import java.util.List;

/*
 * Answers to homework questions
 *
 * Q1 - What are the advantages of using a Room database?
 * A1 -Creates and manages an Android SQLite database for you.
 * Eliminates a lot of boilerplate code.
 * Using a DAO, provides a mechanism for mapping Java methods to database queries.
 *
 * Q2 - Which of the following are reasons for using a ViewModel?
 * A2 - Often used with LiveData for changeable data that the UI will use or display.
 * Prevents your data from being lost when the app crashes.
 * Acts as a communication center between the Repository and the UI.
 * ViewModel instances survive device configuration changes.
 *
 * Q3 - What is the DAO?
 * A3 - Short for "data access object."
 * A library for managing database queries.
 * An annotated interface that maps Java methods to SQLite queries.
 * A class whose methods run always in the background, not on the main thread.
 * A class that the compiler checks for SQL errors, then uses to generate queries from the annotations.
 *
 * Q4 - What are features of LiveData?
 * A4 - When LiveData is used with Room, data updates automatically if all the intermediate levels also return LiveData (DAO, ViewModel, Repository).
 * Uses the observer pattern and notifies its observers when its data has changed.
 * Is lifecycle aware.
 *
 * */

public class MainActivity extends AppCompatActivity {
    public static final int NEW_NOTE_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_NOTE_ACTIVITY_REQUEST_CODE = 2;
    public static final String EXTRA_DATA_UPDATE_DUEDAY = "extra_dueday_to_be_updated";
    public static final String EXTRA_DATA_UPDATE_CONTENT = "extra_content_to_be_updated";
    public static final String EXTRA_DATA_UPDATE_TITLE = "extra_title_to_be_updated";
    public static final String EXTRA_DATA_UPDATE_ID = "extra_data_id";

    private NoteViewModel mNoteviewModel;
    private Note nowSelectedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });

        // recyclerView setup
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final NoteListAdapter adapter = new NoteListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNoteviewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        mNoteviewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setNotes(notes);
            }
        });

        // recyclerView 滑動
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                try {
                    int position = viewHolder.getAdapterPosition();
                    final Note myNote = adapter.getNoteAtPostion(position);
                    mNoteviewModel.deleteNote(myNote);

                    Snackbar snackbar = Snackbar.make(viewHolder.itemView, R.string.delete_tag,Snackbar.LENGTH_LONG);
                    snackbar.setAction(R.string.undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                mNoteviewModel.insert(myNote);
                            } catch(Exception e) {
                                Log.e("MainActivity", e.getMessage());
                            }
                        }
                    });
                    //Toast.makeText(MainActivity.this, "Deleting...", Toast.LENGTH_LONG).show();
                    snackbar.show();
                } catch(Exception e) {
                    Log.e("MainActivity", e.getMessage());
                }
            }

            @Override
            public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dx, float dy, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dx, dy, actionState, isCurrentlyActive)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.recycler_view_item_swipe_right_background))
                        .addSwipeRightActionIcon(R.drawable.ic_delete_white_24dp)
                        .addSwipeRightLabel(getString(R.string.action_delete))
                        .setSwipeRightLabelColor(Color.WHITE)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.recycler_view_item_swipe_right_background))
                        .addSwipeLeftActionIcon(R.drawable.ic_delete_white_24dp)
                        .addSwipeLeftLabel(getString(R.string.action_delete))
                        .setSwipeLeftLabelColor(Color.WHITE)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dx, dy, actionState, isCurrentlyActive);
            }
        });

        helper.attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteListAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Note note = adapter.getNoteAtPostion(position);
                launchUpdateNoteActivity(note);
            }
        });

    }
/*
    public void onActivityResult(int requestCode, int resultCode, Intent data)  {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            //Note note = new Note(data.getStringExtra(AddNoteActivity.EXTRA_REPLY_TITLE), data.getStringExtra(AddNoteActivity.EXTRA_REPLY_CONTENT), data.getStringExtra(AddNoteActivity.EXTRA_REPLY_DUEDAY));
            //mNoteviewModel.insert(note);
            Log.d("onclick", "onclick");
        } else if (requestCode == UPDATE_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            //String note_title = data.getStringExtra(AddNoteActivity.EXTRA_REPLY_TITLE);
            //String note_content = data.getStringExtra(AddNoteActivity.EXTRA_REPLY_CONTENT);
            //String note_dueday = data.getStringExtra(AddNoteActivity.EXTRA_REPLY_DUEDAY);
            int id = data.getIntExtra(AddNoteActivity.EXTRA_REPLY_ID, -1);
            if (id != -1) {
                Log.d("Test", "id != -1");
                //mNoteviewModel.update(new Note(id, note_title, note_content, note_dueday));
            } else {
                Toast.makeText(this, R.string.unable_to_update, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG).show();
        }
    }
*/
    public void launchUpdateNoteActivity(Note note) {
        Intent intent = new Intent(this, AddNoteActivity.class);
        intent.putExtra(EXTRA_DATA_UPDATE_DUEDAY, note.getDueDay());
        intent.putExtra(EXTRA_DATA_UPDATE_CONTENT, note.getContent());
        intent.putExtra(EXTRA_DATA_UPDATE_TITLE, note.getTitle());
        intent.putExtra(EXTRA_DATA_UPDATE_ID, note.getId());
        startActivityForResult(intent, UPDATE_NOTE_ACTIVITY_REQUEST_CODE);;
    }
}
