# SQLiteORM

**SQLiteORM helps developers using SQLite in Android easier!** It's an ORM-like tool for faster development.

## How to install
### in gradle
Add it in your root build.gradle at the end of repositories:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  ```
  then add the depenceny
  ```
  dependencies {
	        compile 'com.github.mimrahe:sqliteorm:v0.9.1'
	}
  ```
  ### in maven
  ```xml
  <repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
  ```
  then add the dependency
  ```xml
  <dependency>
	    <groupId>com.github.mimrahe</groupId>
	    <artifactId>sqliteorm</artifactId>
	    <version>v0.9.1</version>
	</dependency>
  ```
### other ways
see [SQLiteORM on jitpack](https://jitpack.io/#mimrahe/sqliteorm)

## How to use
### create version file
this ORM looks for sql files in assets folder of project. so create a new folder in assets and name it "database".
create a new file with extension ".sql" for each version of database.
for example for version 1 name file "1.sql".

### place sql statements in the file
for example if you want to create a new table in version 1 of database place this lines in it:
```sql
CREATE TABLE IF NOT EXISTS notes (_id INTEGER PRIMARY KEY, note VARCHAR(250) NOT NULL);
```
**Define sql file for each version of database**:

for example if your database version updated to 2 so create a sql file in "assets/database" and name it 2.sql

**Modify database via version files**:

for example if you want to create a new table and add new field to existed table do:
```sql
CREATE TABLE IF NOT EXISTS types (_id INTEGER PRIMARY KEY, type VARCHAR(50) NOT NULL);
ALTER TABLE notes ADD COLUMN type_id INTEGER DEFAULT 0;
```
### define a model for each table
models extend `ir.mimrahe.sqliteorm.ModelAbstract` abstract class:
```java
package ir.mimrahe.sqliteorm;

import java.util.HashMap;

// this abstract class was defined in sqliteorm library
// so do not create this class
public abstract class ModelAbstract {
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

    /**
     * sets if and returns instance of model
     * @param id id of table
     * @return instance of model
     */
    public abstract ModelAbstract setId(Integer id);

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
```
for example for table "notes":
```java
public class NoteModel extends ModelAbstract {
    public Integer id;
    public String note, dirtyNote;

    public enum Columns{
        ID("_id"),
        Note("note");

        private String colName;

        Columns(String colName){
            this.colName = colName;
        }

        public String getColName(){
            return colName;
        }
    }

    NoteModel(){}

    NoteModel(String note){
        this.note = note;
    }

    NoteModel(Integer id, String note){
        this.id = id;
        this.note = note;
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

    public static ArrayList<NoteModel> findAll(){
        ArrayList<NoteModel> notes = new ArrayList<>();
        Cursor result = DatabaseSingleton.getInstance().findAll((new NoteModel()).getTableName());

        try {
            if(result.moveToFirst()){
                do {
                    Integer id = result.getInt(result.getColumnIndex(Columns.ID.getColName()));
                    String note = result.getString(result.getColumnIndex(Columns.Note.getColName()));
                    notes.add(new NoteModel(id, note));
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

        return insertFields;
    }

    @Override
    public HashMap<String, Object> getUpdateFields() {
        HashMap<String, Object> updateFields = new HashMap<>();
        // Note: use dirty values here!
        updateFields.put(Columns.Note.getColName(), getDirtyNote());

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
        return new NoteModel(getNote());
    }

    @Override
    public NoteModel getInstance() {
        return this;
    }

    @Override
    public String toString() {
        return "id: " + getId() + ", note: " + getNote();
    }
}
```
when you want to update a field of model new value gets in `dirty`. for example if you update `note` field new values gets in `note` and 
`dirtyNote`.

**define a `dirty` prefixed variable for fields that will be update**

**`getUpdateFields` shoud place values of dirty fields in HashMap value places**

### import sqliteorm in your class
```java
import ir.mimrahe.sqliteorm;
```

### init database
```java
String databaseName = "myDatabase";
int databaseVersion = 1;
DatabaseSingleton.init(getApplicationContext(), databaseName, databaseVersion);
```

### use model for CRUD operations
```java
NoteModel note1 = new NoteModel("call Ali today");
// note1.save(); or
note1.saveAndSetId(); // use this if you want to update

note1.setNote("call Ali today at 19:00");
note1.update();

note1.setNote("call Ali today at 19:00 and say hello").update();

NoteModel note2 = new NoteModel("go shopping");
note2.savAndSetId();

NoteModel note3 = note2.copy();
note3.save();

for(NoteModel note: NoteModel.findAll()){
    Log.e("all notes", note.toString());
}
note1.delete();
note2.delete();
```

### close database
close database and release resources
```java
    @Override
    protected void onDestroy() {
        DatabaseSingleton.closeDatabase();
        super.onDestroy();
    }
```

## Define new functions in models


## License
