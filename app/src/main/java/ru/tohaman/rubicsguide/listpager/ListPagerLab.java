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
 * Created by Toha on 21.05.2017. Синглетный класс, при первом вызове создающий сиглет, хранящмй всю информацию о всех фазах
 * в вие коллекции объектов типа ListPager, данные считывает из SQLite базы, где хранятся комменты к каждому этапу фазы.
 * если в базе нет записи, она создается со значениями по-умолчанию (0,"").
 */

public class ListPagerLab {
    private static ListPagerLab sListPagerLab;
    private static Context mContext;
    private SQLiteDatabase mDatabase;
    private List<ListPager> mListPagers;

    private ListPagerLab(Context context){
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

        mPhase = "ADVF2L";
        mTitles = mContext.getResources().getStringArray(R.array.advf2l_title);
        mResId = getResID(R.array.advf2l_icon);
        Descr = getResID(R.array.advf2l_descr);
        mUrl = mContext.getResources().getStringArray(R.array.advf2l_url);
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
        mTitles = mContext.getResources().getStringArray(R.array.scramblegen_title);
        mResId = getResID(R.array.scramblegen_icon);
        Descr = getResID(R.array.scramblegen_descr);
        mUrl = mContext.getResources().getStringArray(R.array.scramblegen_url);
        PhaseInit (mPhase, mTitles, mResId, Descr, mUrl);

        mPhase = "AZBUKA";
        mTitles = new String[]{"CURRENTAZBUKA","CUSTOMAZBUKA"};
        mResId = new int[] {0,0,0,0};
        Descr = new int[] {0,0,0,0};
        mUrl = new String[]{"",""};
        PhaseInit (mPhase, mTitles, mResId, Descr, mUrl);

        mPhase = "PLLTEST";
        mTitles = mContext.getResources().getStringArray(R.array.pll_test_phases);
        mResId = getResID(R.array.pll_test_icon);
        Descr = getResID(R.array.pll_test_descr);
        mUrl = mContext.getResources().getStringArray(R.array.pll_test_url);
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
        mContext = context;
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
        ListPager mPagerList = null;

        for (ListPager listPager : mListPagers){
            if (phase.equals(listPager.getPhase())& id==listPager.getId()) {
                    mPagerList = listPager;
                }
        }
        return mPagerList;
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
        int[] idx = null;
        if (tArray.length() != 0){
            int count = tArray.length();
            idx = new int[count];
            for (int i = 0; i < idx.length; i++) {
                idx[i] = tArray.getResourceId(i, 0);
            }
            tArray.recycle();
        }
        return idx;
    }

    public String[] getMaximAzbuka() {
        String[] azbuka = new String[54];
        azbuka[0] = "М";    azbuka[1] = "Л";    azbuka[2] = "Л";
        azbuka[3] = "М";    azbuka[4] = "-";     azbuka[5] = "К";
        azbuka[6] = "И";    azbuka[7] = "И";    azbuka[8] = "К";

        azbuka[9] = "С";    azbuka[10] = "С";   azbuka[11] = "О";
        azbuka[12] = "Р";   azbuka[13] = "-";    azbuka[14] = "О";
        azbuka[15] = "Р";   azbuka[16] = "П";   azbuka[17] = "П";

        azbuka[18] = "А";   azbuka[19] = "А";   azbuka[20] = "Б";
        azbuka[21] = "Г";   azbuka[22] = "-";    azbuka[23] = "Б";
        azbuka[24] = "Г";   azbuka[25] = "В";   azbuka[26] = "В";

        azbuka[27] = "У";   azbuka[28] = "Ц";   azbuka[29] = "Ц";
        azbuka[30] = "У";   azbuka[31] = "-";    azbuka[32] = "Х";
        azbuka[33] = "Ф";   azbuka[34] = "Ф";   azbuka[35] = "Х";

        azbuka[36] = "Э";    azbuka[37] = "Ш";    azbuka[38] = "Ш";
        azbuka[39] = "Э";    azbuka[40] = "-";    azbuka[41] = "Я";
        azbuka[42] = "Ю";    azbuka[43] = "Ю";    azbuka[44] = "Я";

        azbuka[45] = "Е";    azbuka[46] = "Е";    azbuka[47] = "Ё";
        azbuka[48] = "З";    azbuka[49] = "-";    azbuka[50] = "Ё";
        azbuka[51] = "З";    azbuka[52] = "Ж";    azbuka[53] = "Ж";

        return azbuka;
    }

    public String[] getMyAzbuka() {
        String[] azbuka = new String[54];
        azbuka[0] = "М";    azbuka[1] = "Л";    azbuka[2] = "Л";
        azbuka[3] = "М";    azbuka[4] = "-";     azbuka[5] = "К";
        azbuka[6] = "И";    azbuka[7] = "И";    azbuka[8] = "К";

        azbuka[9] = "Р";    azbuka[10] = "Р";   azbuka[11] = "Н";
        azbuka[12] = "П";   azbuka[13] = "-";    azbuka[14] = "Н";
        azbuka[15] = "П";   azbuka[16] = "О";   azbuka[17] = "О";

        azbuka[18] = "А";   azbuka[19] = "А";   azbuka[20] = "Б";
        azbuka[21] = "Г";   azbuka[22] = "-";    azbuka[23] = "Б";
        azbuka[24] = "Г";   azbuka[25] = "В";   azbuka[26] = "В";

        azbuka[27] = "С";   azbuka[28] = "Ф";   azbuka[29] = "Ф";
        azbuka[30] = "С";   azbuka[31] = "-";    azbuka[32] = "У";
        azbuka[33] = "Т";   azbuka[34] = "Т";   azbuka[35] = "У";

        azbuka[36] = "Ц";    azbuka[37] = "Х";    azbuka[38] = "Х";
        azbuka[39] = "Ц";    azbuka[40] = "-";    azbuka[41] = "Ш";
        azbuka[42] = "Ч";    azbuka[43] = "Ч";    azbuka[44] = "Ш";

        azbuka[45] = "Д";    azbuka[46] = "Д";    azbuka[47] = "Е";
        azbuka[48] = "З";    azbuka[49] = "-";    azbuka[50] = "Е";
        azbuka[51] = "З";    azbuka[52] = "Ж";    azbuka[53] = "Ж";

        return azbuka;
    }

    public String[] getScrambleManagement() {
        String[] azbuka = new String[54];

                                                                        azbuka[0] = " ";     azbuka[1] = " ";    azbuka[2] = " ";
                                                                        azbuka[3] = " ";     azbuka[4] = " ";    azbuka[5] = " ";
                                                                        azbuka[6] = " ";     azbuka[7] = " ";    azbuka[8] = " ";

        azbuka[9]  = " ";   azbuka[10] = " ";   azbuka[11] = " ";       azbuka[18] = " ";   azbuka[19] = " ";    azbuka[20] = "↑";       azbuka[27] = " ";   azbuka[28] = " ";   azbuka[29] = " ";
        azbuka[12] = " ";   azbuka[13] = " ";   azbuka[14] = " ";       azbuka[21] = " ";   azbuka[22] = " ";    azbuka[23] = " ";       azbuka[30] = " ";   azbuka[31] = " ";   azbuka[32] = " ";
        azbuka[15] = " ";   azbuka[16] = " ";   azbuka[17] = " ";       azbuka[24] = " ";   azbuka[25] = " ";    azbuka[26] = "↓";       azbuka[33] = " ";   azbuka[34] = " ";   azbuka[35] = " ";

                                                                        azbuka[45] = "←";   azbuka[46] = "M'";   azbuka[47] = "→";
                                                                        azbuka[48] = "←";   azbuka[49] = " ";   azbuka[50] = "→";
                                                                        azbuka[51] = "←";   azbuka[52] = "M";   azbuka[53] = "→";


        azbuka[36] = " ";    azbuka[37] = " ";    azbuka[38] = " ";
        azbuka[39] = " ";    azbuka[40] = " ";    azbuka[41] = " ";
        azbuka[42] = " ";    azbuka[43] = " ";    azbuka[44] = " ";

        return azbuka;
    }


    public String[] getCustomAzbuka() {
        String[] azbuka = new String [54];
        ListPager mListPager = getPhaseItem (0,"AZBUKA");
        if (mListPager.getComment().equals("")) {
            azbuka = getMaximAzbuka();
        } else {
            azbuka = mListPager.getComment().split(" ");
        }
        return azbuka;
    }

    public void setCustomAzbuka(String[] azbuka) {
        String st = "";
        for (int i=0; i<azbuka.length;i++) {
            st = st + azbuka[i] + " ";
        }
        ListPager mListPager = getPhaseItem (0,"AZBUKA");
        mListPager.setComment(st);
        updateListPager(mListPager);
    }
    public void saveCustomAzbuka() {
        ListPager mListPager = getPhaseItem (0,"AZBUKA");
        ListPager mLP = new ListPager(1,"AZBUKA",mListPager.getComment());
        updateListPager(mLP);
    }

    public String[] loadCustomAzbuka () {
        String[] azbuka = new String [54];
        ListPager mListPager = getPhaseItem (1,"AZBUKA");
        if (mListPager.getComment().equals("")) {
            azbuka = getMaximAzbuka();
        } else {
            azbuka = mListPager.getComment().split(" ");
        }
        return azbuka;
    }
}
