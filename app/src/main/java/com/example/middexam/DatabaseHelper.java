package com.example.middexam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StudentDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "students";

    private static final String COL_ID = "id";
    private static final String COL_NAME = "student_name";
    private static final String COL_STUDENT_ID = "student_id";
    private static final String COL_MAJOR = "major";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_STUDENT_ID + " TEXT UNIQUE, " +
                COL_MAJOR + " TEXT)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // ADD
    public boolean addStudent(String name, String studentId, String major) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_NAME, name);
        values.put(COL_STUDENT_ID, studentId);
        values.put(COL_MAJOR, major);

        long result = db.insert(TABLE_NAME, null, values);

        return result != -1;
    }

    // VIEW
    public Cursor getAllStudents() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_NAME,
                null
        );
    }

    // EDIT
    public boolean updateStudent(String studentId, String name, String major) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_NAME, name);
        values.put(COL_MAJOR, major);

        int result = db.update(
                TABLE_NAME,
                values,
                COL_STUDENT_ID + "=?",
                new String[]{studentId}
        );

        return result > 0;
    }

    // DELETE
    public boolean deleteStudent(String studentId) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                TABLE_NAME,
                COL_STUDENT_ID + "=?",
                new String[]{studentId}
        );

        return result > 0;
    }
}