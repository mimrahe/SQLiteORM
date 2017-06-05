package ir.mimrahe.sqliteorm;

import android.database.Cursor;

public interface DatabaseHelperInterface {
    long insert(ModelAbstract object);
    int update(ModelAbstract object, String whereClause, String[] whereArgs);
    void delete(String tableName, String whereClause, String[] whereArgs);
    Cursor find(String tableName, String selection, String[] selectionArgs,
                String groupBy, String having, String orderBy, String limit);
}
