package me.mahakagg.notesroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import static me.mahakagg.notesroom.MainActivity.EXTRA_DATA_ID;
import static me.mahakagg.notesroom.MainActivity.EXTRA_DATA_UPDATE_TITLE;
import static me.mahakagg.notesroom.MainActivity.EXTRA_DATA_UPDATE_CONTENT;
import static me.mahakagg.notesroom.MainActivity.EXTRA_DATA_UPDATE_DUEDAY;
import static me.mahakagg.notesroom.MainActivity.EXTRA_DATA_UPDATE_FINISHED;

public class AddNoteActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY_ID = "me.mahakagg.notesroom.REPLY_ID";
    public static final String EXTRA_REPLY_TITLE = "me.mahakagg.notesroom.REPLY_TITLE";
    public static final String EXTRA_REPLY_CONTENT = "me.mahakagg.notesroom.REPLY_CONTEXT";
    public static final String EXTRA_REPLY_DUEDAY = "me.mahakagg.notesroom.REPLY_DUEDAY";
    public static final String EXTRA_REPLY_FINISHED = "me.mahakagg.notesroom.REPLY_FINISHED";

    private EditText mTitleEditText;
    private EditText mContentEditText;
    private EditText mDueEditText;
    private Switch DateSwitch;
    private CheckBox mFinished;
    private Button save_btn;
    private NoteViewModel viewModel;
    private Boolean finised;

    private TextView text;
    public static String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        mTitleEditText = findViewById(R.id.titleEditText);
        mContentEditText = findViewById(R.id.contentEditText);
        mDueEditText = findViewById(R.id.dateEditText);
        DateSwitch = findViewById(R.id.DateSwitch);
        mFinished = findViewById(R.id.finished_CheckBox);
        text = findViewById(R.id.textView);
        text.setText(tag);
        viewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        DateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", "" + isChecked);
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
// edit mothed
        int id = -1;
        final Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String title = extras.getString(EXTRA_DATA_UPDATE_TITLE, "");
            String content = extras.getString(EXTRA_DATA_UPDATE_CONTENT, "");
            String dueday = extras.getString(EXTRA_DATA_UPDATE_DUEDAY, "");
            boolean finished = extras.getBoolean(EXTRA_DATA_UPDATE_FINISHED);
            if (!title.isEmpty()) {
                mTitleEditText.setText(title);
                mContentEditText.setText(content);
                mDueEditText.setText(dueday);
                mFinished.setChecked(finished);

                mTitleEditText.setSelection(title.length());
                mTitleEditText.requestFocus();
            }
        }

        save_btn = findViewById(R.id.save_button);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mTitleEditText.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String title = mTitleEditText.getText().toString();
                    String content = mContentEditText.getText().toString();
                    String dueday = mDueEditText.getText().toString();
                    Boolean finished = getfinish();
                    replyIntent.putExtra(EXTRA_REPLY_TITLE, title);
                    replyIntent.putExtra(EXTRA_REPLY_CONTENT, content);
                    replyIntent.putExtra(EXTRA_REPLY_DUEDAY, dueday);
                    replyIntent.putExtra(EXTRA_REPLY_FINISHED, finished);
                    if (extras != null && extras.containsKey(EXTRA_DATA_ID)) {
                        int id = extras.getInt(EXTRA_DATA_ID, -1);
                        if (id != -1) {
                            replyIntent.putExtra(EXTRA_REPLY_ID, id);
                        }
                    }
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });

    }

    public void processDatePickerResult ( int year, int month, int day){
        String year_text = Integer.toString(year);
        String month_text = Integer.toString(month + 1);
        String day_text = Integer.toString(day);
        String dateMessage = ("Due day: " + year_text + "-" + month_text + "-" + day_text);
        mDueEditText.setText(dateMessage);
    }

    public void AddDueDate(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }

    public Boolean getfinish() {
        if (mFinished.isChecked()) {
            finised = true;
        } else {
            finised = false;
        }
        Log.v("Finished", "" + finised);
        return finised;
    }
/*
    public void onSaveClick(View view){
        Note note = new Note(0, mTitleEditText.getText().toString(), mContentEditText.getText().toString(), mDueEditText.getText().toString(), getfinish());
        viewModel.insert(note);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
*/
    public void onClearClick(View view) {
        mTitleEditText.setText("");
        mContentEditText.setText("");
        mDueEditText.setText("");
        mFinished.setChecked(false);
    }
}
