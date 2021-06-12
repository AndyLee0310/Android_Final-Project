package me.mahakagg.notesroom;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * The Note entity class
 */
@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String title;

    @NonNull
    private String content;

    @NonNull
    private String dueDay;

    @NonNull
    private Boolean state;

    /**
     * public constructor
     */
    public Note(@NonNull String title_Extra,@NonNull String content_Extra,@NonNull String dueday_Extra, @NonNull Boolean state_Extra) {
        this.title = title_Extra;
        this.content = content_Extra;
        this.dueDay = dueday_Extra;
        this.state = state_Extra;
    }

    /**
     * Instantiates a new Note.
     *
     * @param id      the id
     * @param title   the title
     * @param content the content
     * @param dueDay the DueDay
     * @param state the state
     */
    public Note(int id, @NonNull  String title, @NonNull String content, @NonNull String dueDay, @NonNull Boolean state){
        this.id = id;
        this.title = title;
        this.content = content;
        this.dueDay = dueDay;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    public void setContent(@NonNull String content) {
        this.content = content;
    }


    @NonNull
    public String getDueDay() {
        return dueDay;
    }

    public void setDueDay(@NonNull String DueDay) {
        this.dueDay = DueDay;
    }

    @NonNull
    public Boolean getState() {
        return state;
    }

    public void setState(@NonNull Boolean state) {
        this.state = state;
    }
}
