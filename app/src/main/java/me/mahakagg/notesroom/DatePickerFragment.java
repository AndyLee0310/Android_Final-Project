package me.mahakagg.notesroom;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // 建立 DatePickerDialog instance 並回傳
        return new DatePickerDialog(getActivity(), this, year, month, day);    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        AddNoteActivity activity = (AddNoteActivity) getActivity();
        activity.processDatePickerResult(year, month, day);
    }
}
