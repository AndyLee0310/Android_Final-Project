package me.mahakagg.notesroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class AddNoteActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY_ID = "me.mahakagg.noteroom.REPLY_ID";
    // public static final String EXTRA_REPLY_TITLE = "me.mahakagg.noteroom.REPLY_TITLE";
    // public static final String EXTRA_REPLY_CONTENT = "me.mahakagg.noteroom.REPLY_CONTENT";
    // public static final String EXTRA_REPLY_DUEDAY = "me.mahakagg.noteroom.REPLY_DUEDAY";
    // public static final String EXTRA_REPLY = "me.mahakagg.noteroom.REPLY";

    private EditText mTitleEditText;
    private EditText mContentEditText;
    private EditText mDueEditText;
    private Switch DateSwitch;
    private NoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        mTitleEditText = findViewById(R.id.titleEditText);
        mContentEditText = findViewById(R.id.contentEditText);
        mDueEditText = findViewById(R.id.dateEditText);
        DateSwitch = findViewById(R.id.DateSwitch);
        viewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        DateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                if (isChecked == true) {
                    mDueEditText.setHint(R.string.addDueDate);
                    mDueEditText.setEnabled(true);
                } else {
                    mDueEditText.setText("");
                    mDueEditText.setHint(R.string.DueDate);
                    mDueEditText.setEnabled(false);
                }
            }
        });
    }


    public void processDatePickerResult(int year, int month, int day) {
        String year_text = Integer.toString(year);
        String month_text = Integer.toString(month + 1);
        String day_text = Integer.toString(day);
        String dateMessage = (year_text + "-" + month_text + "-" + day_text);
        mDueEditText.setText(dateMessage);
    }

    public void AddDueDate(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }

    public void onSaveClick(View view){
        Note note = new Note(0, mTitleEditText.getText().toString(), mContentEditText.getText().toString(), mDueEditText.getText().toString());
        viewModel.insert(note);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onClearClick(View view) {
        mTitleEditText.setText("");
        mContentEditText.setText("");
        mDueEditText.setText("");
    }
}
