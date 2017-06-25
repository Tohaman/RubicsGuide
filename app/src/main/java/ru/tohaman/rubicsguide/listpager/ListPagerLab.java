package ru.tohaman.rubicsguide.listpager;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ru.tohaman.rubicsguide.R;
import ru.tohaman.rubicsguide.database.BaseHelper;
import ru.tohaman.rubicsguide.database.DBCursorWrapper;
import ru.tohaman.rubicsguide.database.DBSchema;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Toha on 21.05.2017.
 */

public class ListPagerLab {
    private static ListPagerLab sListPagerLab;
    private static Context mContext;
    private SQLiteDatabase mDatabase;
    private List<ListPager> mListPagers;

    private ListPagerLab(Context context){
        ListPager mListPager;
        mContext = context.getApplicationContext();
        mDatabase = new BaseHelper(mContext).getWritableDatabase();
        mListPagers = new ArrayList<>();

        String mPhase = "PLL";
        String[] mTitles = mContext.getResources().getStringArray(R.array.pll_title);
        int [] mResId = getResID(R.array.pll_icon);
        int [] Descr = getResID(R.array.pll_descr);
        String[] mUrl = mContext.getResources().getStringArray(R.array.pll_url);
        PhaseInit (mPhase, mTitles, mResId, Descr, mUrl);

        mPhase = "OLL";
        mTitles = mContext.getResources().getStringArray(R.array.oll_title);
        mResId = getResID(R.array.oll_icon);
        Descr = getResID(R.array.oll_descr);
        mUrl = mContext.getResources().getStringArray(R.array.oll_url);
        PhaseInit (mPhase, mTitles, mResId, Descr, mUrl);

        mPhase = "CROSS";
        mTitles = mContext.getResources().getStringArray(R.array.cross_title);
        mResId = getResID(R.array.cross_icon);
        Descr = getResID(R.array.cross_descr);
        mUrl = mContext.getResources().getStringArray(R.array.cross_url);
        PhaseInit (mPhase, mTitles, mResId, Descr, mUrl);

        mPhase = "ACCEL";
        mTitles = mContext.getResources().getStringArray(R.array.accel_title);
        mResId = getResID(R.array.accel_icon);
        Descr = getResID(R.array.accel_descr);
        mUrl = mContext.getResources().getStringArray(R.array.accel_url);
        PhaseInit (mPhase, mTitles, mResId, Descr, mUrl);

        mPhase = "F2L";
        mTitles = mContext.getResources().getStringArray(R.array.f2l_title);
        mResId = getResID(R.array.f2l_icon);
        Descr = getResID(R.array.f2l_descr);
        mUrl = mContext.getResources().getStringArray(R.array.f2l_url);
        PhaseInit (mPhase, mTitles, mResId, Descr, mUrl);

        mPhase = "BASIC";
        mTitles = mContext.getResources().getStringArray(R.array.basic_title);
        mResId = getResID(R.array.basic_icon);
        Descr = getResID(R.array.basic_descr);
        mUrl = mContext.getResources().getStringArray(R.array.basic_url);
        PhaseInit (mPhase, mTitles, mResId, Descr, mUrl);

        mPhase = "BEGIN";
        mTitles = mContext.getResources().getStringArray(R.array.begin_title);
        mResId = getResID(R.array.begin_icon);
        Descr = getResID(R.array.begin_descr);
        mUrl = mContext.getResources().getStringArray(R.array.begin_url);
        PhaseInit (mPhase, mTitles, mResId, Descr, mUrl);

        mPhase = "BLIND";
        mTitles = mContext.getResources().getStringArray(R.array.blind_title);
        mResId = getResID(R.array.blind_icon);
        Descr = getResID(R.array.blind_descr);
        mUrl = mContext.getResources().getStringArray(R.array.blind_url);
        PhaseInit (mPhase, mTitles, mResId, Descr, mUrl);

        mPhase = "BLINDACC";
        mTitles = mContext.getResources().getStringArray(R.array.blindacc_title);
        mResId = getResID(R.array.blindacc_icon);
        Descr = getResID(R.array.blindacc_descr);
        mUrl = mContext.getResources().getStringArray(R.array.blindacc_url);
        PhaseInit (mPhase, mTitles, mResId, Descr, mUrl);

        mPhase = "SCRAMBLEGEN";
        mTitles = new String[]{"Scramble", "ScrambleLength", "ChkBufRebro", "ChkBufUgol"};
        mResId = new int[] {0,0,0,0};                       //что-то укажем, не будем использовать
        Descr = new int[] {0,0,0,0};
        mUrl = new String[]{"U R U' R' F", "14","1","1"};   //значения, если их еще нет в базе (комментах).
        PhaseInit (mPhase, mTitles, mResId, Descr, mUrl);

    }

    // Инициализация фазы, с заданными массивами Заголовков, Иконок, Описаний, ютуб-ссылок
    private void PhaseInit (String mPhase, String[] mTitles, int [] mResId, int [] Descr, String[] mUrl){
        ListPager mListPager;
        for (int i = 0; i < mTitles.length; i++) {
            mListPager = getListPagerFromBase(i,mPhase);
            if (mListPager == null) {
                mListPager = new ListPager(mPhase, i, mTitles[i], mResId[i], Descr[i], mUrl[i],"");
                addLPtoBase(mListPager);
            } else {
                mListPager.setDescription (Descr[i]);
                mListPager.setUrl (mUrl[i]);
                mListPager.setIcon (mResId[i]);
                mListPager.setTitle(mTitles[i]);
            }
            mListPagers.add (mListPager);
        }
    }

    // Создаем синглет, т.е. класс с закрытым конструктором. Открыт метод get, который
    // создает экземпляр, только если он еще не создан, в противном случае возвращает, то что уже создано
    public static ListPagerLab get(Context context) {
        if (sListPagerLab == null) {
            sListPagerLab = new ListPagerLab(context);
        }
        return sListPagerLab;
    }

    //возвращает из ListPagerLab список ListPager'ов с заданной фазой
    public List<ListPager> getPhaseList(String Phase){
        List<ListPager> sPagerLists = new ArrayList<>();
        for (ListPager listPager : mListPagers) {
            if (Phase.equals(listPager.getPhase())) {
                sPagerLists.add(listPager);
            }
        }
        return sPagerLists;
    }


    //возвращает из ListPagerLab один ListPager с заданными фазой и номером
    public ListPager getPhaseItem(int id, String phase) {
        ListPager sPagerList = null;

        for (ListPager listPager : mListPagers){
            if (phase.equals(listPager.getPhase())& id==listPager.getId()) {
                    sPagerList = listPager;
                }
        }
        return sPagerList;
    }

    //обновляем элемент ListPagerLab (свой комментарий)
    public void updateListPager(ListPager listPager) {
        int i = 0;  //вот такой интересный for для для всех элементов mListPagers
        // Обновляем элемент ListPager в синглете mListPagers
        for (ListPager lp : mListPagers) {
            if (listPager.getPhase().equals(lp.getPhase())& listPager.getId()==lp.getId()){
                mListPagers.set(i, listPager);
            }
            i++;
        }
        // Обновляем коммент в базе, если его нет, то создаем
        if (getListPagerFromBase(listPager.getId(),listPager.getPhase()) == null) {
            addLPtoBase(listPager);
        } else {
            updateLPtoBase(listPager);
        }

    }

    private static ContentValues getContentValues (ListPager LP) {
        ContentValues values = new ContentValues();
        values.put (DBSchema.BaseTable.Cols.ID, LP.getStringId());
        values.put (DBSchema.BaseTable.Cols.PHASE, LP.getPhase());
        values.put (DBSchema.BaseTable.Cols.COMMENT, LP.getComment());
        return values;
    }

    private DBCursorWrapper queryLPs (String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                DBSchema.BaseTable.NAME,
                null,   // columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null,   // groupBy
                null,   // having
                null    // orderBy
        );
        return new DBCursorWrapper(cursor);
    }

    // получить комментарий для заданных фазы и номера из базы в виде объекта ListPager (с пустыми остальными полями)
    public ListPager getListPagerFromBase (int id, String phase) {
        DBCursorWrapper cursor = queryLPs (DBSchema.BaseTable.Cols.ID +
                " =? and " + DBSchema.BaseTable.Cols.PHASE + " = ?",
                new String[] {String.valueOf(id), phase});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getLP();
        } finally {
            cursor.close();
        }
    }

    public void addLPtoBase (ListPager lp) {
        ContentValues values = getContentValues (lp);
        mDatabase.insert(DBSchema.BaseTable.NAME,null,values);
    }

    public void updateLPtoBase (ListPager lp) {
        ContentValues values = getContentValues(lp);
        mDatabase.update(DBSchema.BaseTable.NAME, values,
                DBSchema.BaseTable.Cols.ID +
                        " =? and " + DBSchema.BaseTable.Cols.PHASE + " = ?",
                new String[] {lp.getStringId(), lp.getPhase()});
    }

    // возвращает массив целых числел, являющихся ссылками на ресурсы входящего массива (заданного тк же ссылкой)
    // т.е. на входе ссылка на <string-array name="ххх"> , а на выходе массив ссылок на элементы этого массива
    public static int[] getResID(int mId) {
        TypedArray tArray = mContext.getResources().obtainTypedArray(mId);
        int count = tArray.length();
        int[] idx = new int[count];
        for (int i =0; i < idx.length; i++) {
            idx[i] = tArray.getResourceId(i,0);
        }
        tArray.recycle();
        return idx;
    }
}
