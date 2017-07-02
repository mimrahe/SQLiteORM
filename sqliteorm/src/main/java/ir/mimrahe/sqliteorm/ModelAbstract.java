package ir.mimrahe.sqliteorm;

import android.database.Cursor;
import android.util.Log;

import java.util.HashMap;

public abstract class ModelAbstract {
    /**
     * sets if and returns instance of model
     * @param id id of table
     * @return instance of model
     */
    public abstract ModelAbstract setId(Integer id);

    /**
     * @return fields will be inserted in table
     */
    public abstract HashMap<String, Object> getInsertFields();

    /**
     * @return fields will be updated
     */
    public abstract HashMap<String, Object> getUpdateFields();

    /**
     * @return table name of model
     */
    public abstract String getTableName();

    /**
     * @return primary key field name of table
     */
    public abstract String getPKName();

    /**
     * @return primaty key field value
     */
    public abstract String getPKValue();

    /**
     * checks if new value is dirty for update
     * @param newValue new value of field
     * @param oldValue old value of field
     */
    public boolean isDirty(Object newValue, Object oldValue){
        return oldValue != null && !newValue.equals(oldValue);
    }

    /**
     * @return copied instance of model
     */
    public abstract ModelAbstract copy();

    /**
     * @return instance of model
     */
    public abstract ModelAbstract getInstance();

    public Cursor findWithPrimaryKey(){
        String whereClause = getPKName() + " = ?";
        String[] whereArgs = new String[]{getPKValue()};
        return DatabaseSingleton.getInstance().find(getTableName(), whereClause, whereArgs);
    }

    /**
     * save model in table
     * @return id of newly created row
     */
    public long save(){
        return DatabaseSingleton.getInstance().insert(getInstance());
    }

    /**
     * saves model in table and set id for model
     */
    public void saveAndSetId(){
        long id = save();
        setId((int) id);
    }

    /**
     * update model in table
     * @return the number of rows affected always equals 1
     */
    public int update(){
        String whereClause = getPKName() + " = ?";
        String[] whereArgs = new String[]{getPKValue()};
        return DatabaseSingleton.getInstance().update(getInstance(), whereClause, whereArgs);
    }

    /**
     * deletes model from table
     */
    public void delete(){
        String whereClause = getPKName() + " = ?";
        String[] whereArgs = new String[]{getPKValue()};
        DatabaseSingleton.getInstance().delete(getTableName(), whereClause, whereArgs);
    }
}
