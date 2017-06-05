package ru.tohaman.rubicsguide.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import ru.tohaman.rubicsguide.database.DBSchema.BaseTable;
import ru.tohaman.rubicsguide.listpager.ListPager;

/**
 * Created by User on 23.05.2017.
 */

public class DBCursorWrapper extends CursorWrapper {
    public DBCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ListPager getLP () {
        int id = getInt (getColumnIndex(BaseTable.Cols.ID));
        String phase = getString(getColumnIndex(BaseTable.Cols.PHASE));
        String comment = getString(getColumnIndex(BaseTable.Cols.COMMENT));

        ListPager lp = new ListPager(id, phase, comment);
        return lp;
    }
    //
}
