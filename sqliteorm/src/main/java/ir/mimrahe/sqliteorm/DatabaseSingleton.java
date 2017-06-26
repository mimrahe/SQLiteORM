package ir.mimrahe.sqliteorm;

import android.content.Context;

public class DatabaseSingleton {
    private static DatabaseHelper instance;
    private static Context context;
    private static String dbName;
    private static int dbVersion;

    public static void init(Context appContext, String theDbName, int theDbVersion){
        context = appContext;
        dbName = theDbName;
        dbVersion = theDbVersion;
    }

    public static DatabaseHelper getInstance() {
        if (instance == null){
            instance = new DatabaseHelper(context, dbName, dbVersion);
        }
        return instance;
    }

    public static void closeDatabase(){
        if (instance != null) {
            instance.close();
            instance = null;
        }
    }
}
