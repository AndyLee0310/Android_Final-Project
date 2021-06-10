package me.mahakagg.notesroom;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> mAllNotes;

    NoteRepository(Application application){
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        noteDao = db.noteDao();
        mAllNotes = noteDao.getAllNotes();
    }

    LiveData<List<Note>> getAllNotes(){
        return mAllNotes;
    }

    public void insert(Note note){
        new InsertAsyncTask(noteDao).execute(note);
    }

    public void update(Note note) {
        new updateAsycTask(noteDao).execute(note);
    }

    public void delete(Note note) {
        new deleteAsyncTask(noteDao).execute(note);
    }

    private static class InsertAsyncTask extends AsyncTask<Note, Void, Void>{

        private NoteDao mAsyncTaskDao;

        InsertAsyncTask(NoteDao noteDao) {
            mAsyncTaskDao = noteDao;
        }

        @Override
        protected Void doInBackground(final Note... notes) {
            mAsyncTaskDao.insert(notes[0]);
            return null;
        }
    }

    private static class updateAsycTask extends AsyncTask<Note, Void, Void>{
        private NoteDao mAsyncTaskDao;

        updateAsycTask(NoteDao noteDao) {
            mAsyncTaskDao = noteDao;
        }

        @Override
        protected Void doInBackground(final Note... notes) {
            mAsyncTaskDao.update(notes[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Note, Void, Void>{
        private NoteDao mAsyncTaskDao;

        deleteAsyncTask(NoteDao noteDao) {
            mAsyncTaskDao = noteDao;
        }

        @Override
        protected Void doInBackground(final Note... notes) {
            mAsyncTaskDao.deleteNote(notes[0]);
            return null;
        }
    }
}
