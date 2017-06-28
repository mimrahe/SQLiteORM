package ir.mimrahe.sqliteorm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper implements DatabaseHelperInterface {
    private Context mContext;
    private int mDbVersion;

    public static SQLiteDatabase database;

    public DatabaseHelper(Context context, String dbName, int dbVersion) {
        super(context, dbName, null, dbVersion);
        mContext = context;
        mDbVersion = dbVersion;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        modifyDatabase(db, 1, mDbVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        modifyDatabase(db, oldVersion + 1, newVersion);
    }

    /**
     * reads database version files and modifies database
     * @param db database instance
     * @param oldVersion older version of database
     * @param newVersion newer version of database
     */
    private void modifyDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
        for (int i = oldVersion; i <= newVersion; i++) {
            for (String modification : compileScheme(i)) {
                db.execSQL(modification);
                Log.e("database modify", modification);
            }
        }
    }

    protected Context getContext() {
        return mContext;
    }

    public SQLiteDatabase getDatabase() {
        if (database == null) {
            database = getWritableDatabase();
        }
        return database;
    }

    @Override
    public synchronized void close() {
        database = null;
        super.close();
    }

    /**
     * reads database version file
     * @param databaseVersion version of database
     * @return array of database version file lines
     */
    private ArrayList<String> compileScheme(int databaseVersion) {
        ArrayList<String> modifications = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getContext().getAssets().open("database/" + databaseVersion + ".sql")));

            String line = reader.readLine();

            while (line != null) {
                modifications.add(line);
                line = reader.readLine();
            }

        } catch (IOException e) {
            Log.e("scheme compile", "error while reading file");
        }

        return modifications;
    }

    public Cursor find(String tableName, String selection, String[] selectionArgs,
                       String groupBy, String having, String orderBy, String limit) {
        return getDatabase().query(tableName, null, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public Cursor find(String tableName, String selection, String[] selectionArgs) {
        return find(tableName, selection, selectionArgs, null, null, null, null);
    }

    public Cursor findAll(String tableName){
        return find(tableName, null, null, null, null, null, null);
    }

    public long insert(ModelAbstract object) {
        ContentValues values = new ContentValues();

        HashMap<String, Object> fields = object.getInsertFields();

        for (HashMap.Entry entry : fields.entrySet()) {
            if (entry.getValue() == null)
                return -1;

            if (entry.getValue() instanceof Integer) {
                values.put(entry.getKey().toString(), (Integer) entry.getValue());
            } else if (entry.getValue() instanceof String) {
                values.put(entry.getKey().toString(), (String) entry.getValue());
            } else if (entry.getValue() instanceof Boolean){
                values.put(entry.getKey().toString(), (Boolean) entry.getKey());
            }
        }

        return getDatabase().insert(object.getTableName(), null, values);
    }

    public int update(ModelAbstract object, String whereClause, String[] whereArgs) {
        ContentValues values = new ContentValues();

        HashMap<String, Object> fields = object.getUpdateFields();

        if (fields == null)
            return 0;

        for (HashMap.Entry entry : fields.entrySet()) {
            if (entry.getValue() == null)
                continue;

            if (entry.getValue() instanceof Integer) {
                values.put(entry.getKey().toString(), (Integer) entry.getValue());
            } else if (entry.getValue() instanceof String) {
                values.put(entry.getKey().toString(), (String) entry.getValue());
            } else if (entry.getValue() instanceof Boolean){
                values.put(entry.getKey().toString(), (Boolean) entry.getKey());
            }
        }

        if (values.size() == 0)
            return 0;

        return getDatabase().update(object.getTableName(), values, whereClause, whereArgs);
    }

    public void delete(String tableName, String whereClause, String[] whereArgs) {
        getDatabase().delete(tableName, whereClause, whereArgs);
    }
}
