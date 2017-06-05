package ru.tohaman.rubicsguide.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.tohaman.rubicsguide.database.DBSchema.BaseTable;

/**
 * Created by User on 23.05.2017.
 */

public class BaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "base.db";

    public BaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + BaseTable.NAME + "(" +
                BaseTable.Cols.ID + ", " +
                BaseTable.Cols.PHASE + ", " +
                BaseTable.Cols.COMMENT + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
