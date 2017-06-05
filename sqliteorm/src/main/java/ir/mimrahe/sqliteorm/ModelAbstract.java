package ir.mimrahe.sqliteorm;

import java.util.HashMap;

public abstract class ModelAbstract {
    public abstract HashMap<String, Object> getInsertFields();

    public abstract HashMap<String, Object> getUpdateFields();

    public abstract String getTableName();

    public abstract String getPKName();

    public abstract String getPKValue();

    public boolean isDirty(Object newValue, Object oldValue){
        return oldValue != null && !newValue.equals(oldValue);
    }

    public abstract ModelAbstract getInstance();

    public long save(){
        return DatabaseSingleton.getInstance().insert(getInstance());
    }

    public int update(){
        String whereClause = getPKName() + " = ?";
        String[] whereArgs = new String[]{getPKValue()};
        return DatabaseSingleton.getInstance().update(getInstance(), whereClause, whereArgs);
    }

    public void delete(){
        String whereClause = getPKName() + " = ?";
        String[] whereArgs = new String[]{getPKValue()};
        DatabaseSingleton.getInstance().delete(getTableName(), whereClause, whereArgs);
    }
}
