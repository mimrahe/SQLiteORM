package ir.mimrahe.sqliteormshowcase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import ir.mimrahe.sqliteorm.DatabaseSingleton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseSingleton.init(getApplicationContext(), "orm_test", 1);

        NoteModel note1 = new NoteModel("note 1");
        note1.saveAndSetId();

        Log.e("note 1", note1.toString());

        note1.setNote("new note 1").update();

        Log.e("new note 1", note1.toString());

        NoteModel note2 = new NoteModel("note 2");
        note2.saveAndSetId();

        Log.e("note 2", note2.toString());

        for(NoteModel note: NoteModel.findAll()){
            Log.e("all notes", note.toString());
        }
    }
}
