package ru.tohaman.rubicsguide.database;

/**
 * Created by User on 23.05.2017.
 */

public class DBSchema {
    public static final class BaseTable {
        public static final String NAME = "baseTable"; //позволит получать значение как BaseTable.NAME, вернет baseTable
        public static final class Cols {
            public static final String PHASE = "phase";
            public static final String ID = "id";                       //Возможно нужны только phase, id и comment
/*
            public static final String TITLE = "title";                 //остальные пока сделал, надо проверить нужны ли
            public static final String ICON = "icon";                   //если нет, то удалить
            public static final String DESCRIPTION = "description";     //т.к. хранить нужно только коммент к этапу,
            public static final String URL = "url";                     //остальное в базе хранить не надо
*/
            public static final String COMMENT = "comment";
        }
    }
}
