package ir.mimrahe.sqliteormshowcase;

import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import ir.mimrahe.sqliteorm.DatabaseSingleton;
import ir.mimrahe.sqliteorm.ModelAbstract;

public class NoteModel extends ModelAbstract {
    private Integer id;
    private String note, dirtyNote;
    private Boolean myFlag, dirtyMyFlag;

    public enum Columns{
        ID("_id"),
        Note("note"),
        MyFlag("my_flag");

        private String colName;

        Columns(String colName){
            this.colName = colName;
        }

        public String getColName(){
            return colName;
        }
    }

    NoteModel(){}

    NoteModel(String note, Boolean myFlag){
        this.note = note;
        this.myFlag = myFlag;
    }

    NoteModel(Integer id, String note, Boolean myFlag){
        this.id = id;
        this.note = note;
        this.myFlag = myFlag;
    }

    public Integer getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public String getDirtyNote() {
        return dirtyNote;
    }

    public Boolean getMyFlag() {
        return myFlag;
    }

    public Boolean getDirtyMyFlag() {
        return dirtyMyFlag;
    }

    @Override
    public NoteModel setId(Integer id) {
        this.id = id;
        return this;
    }

    public NoteModel setNote(String note) {
        if (isDirty(note, this.note)){
            dirtyNote = note;
        }
        this.note = note;

        return this;
    }

    public NoteModel setMyFlag(Boolean myFlag) {
        if (isDirty(myFlag, this.myFlag)){
            dirtyMyFlag = myFlag;
        }
        this.myFlag = myFlag;

        return this;
    }

    public static ArrayList<NoteModel> findAll(){
        ArrayList<NoteModel> notes = new ArrayList<>();
        Cursor result = DatabaseSingleton.getInstance().findAll((new NoteModel()).getTableName());

        try {
            if(result.moveToFirst()){
                do {
                    Integer id = result.getInt(result.getColumnIndex(Columns.ID.getColName()));
                    String note = result.getString(result.getColumnIndex(Columns.Note.getColName()));
                    Integer myFlag = result.getInt(result.getColumnIndex(Columns.MyFlag.getColName()));
                    Log.e("in find all", myFlag.toString());
                    notes.add(new NoteModel(id, note, myFlag == 1));
                } while(result.moveToNext());
            }
        } catch (Exception e){
            Log.e("note model", "find all error");
        } finally {
            if (result != null && !result.isClosed()) {
                result.close();
            }
        }

        return notes;
    }

    @Override
    public HashMap<String, Object> getInsertFields() {
        HashMap<String, Object> insertFields = new HashMap<>();

        insertFields.put(Columns.Note.getColName(), getNote());
        insertFields.put(Columns.MyFlag.getColName(), getMyFlag());

        return insertFields;
    }

    @Override
    public HashMap<String, Object> getUpdateFields() {
        HashMap<String, Object> updateFields = new HashMap<>();

        updateFields.put(Columns.Note.getColName(), getDirtyNote());
        updateFields.put(Columns.MyFlag.getColName(), getDirtyMyFlag());

        return updateFields;
    }

    @Override
    public String getTableName() {
        return "notes";
    }

    @Override
    public String getPKName() {
        return Columns.ID.getColName();
    }

    @Override
    public String getPKValue() {
        return getId().toString();
    }

    @Override
    public NoteModel copy() {
        return new NoteModel(getNote(), getMyFlag());
    }

    @Override
    public NoteModel getInstance() {
        return this;
    }

    @Override
    public String toString() {
        return "id: " + getId() + ", note: " + getNote() + ", my flag: " + getMyFlag();
    }
}
