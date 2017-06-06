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

## How to define new functions



## License
