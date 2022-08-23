package kr.re.keti.mobiussampleapp_v25;

/**
 * Created by DowonYoon on 2017-06-21.
 */

import android.provider.BaseColumns;

public final class DataBases {

    public static final class CreateDB implements BaseColumns {
        public static final String USERID = "userid";
        public static final String NAME = "name";
        public static final String AGE = "age";
        public static final String GENDER = "gender";

        //
        public static final String INFO = "info";
        public static final String MEDICINE = "medicine";
        public static final String NUMBER = "number";


        public static final String _TABLENAME0 = "usertable";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+" integer primary key autoincrement, "
                +USERID+" text not null , "
                +NAME+" text not null , "
                +AGE+" integer not null , "
                +GENDER+" text not null , "
                +NUMBER+" integer not null , "
                +MEDICINE+" text not null , "
                +INFO+" text not null );";
    }
}