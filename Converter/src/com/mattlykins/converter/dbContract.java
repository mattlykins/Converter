package com.mattlykins.converter;

import android.provider.BaseColumns;

public final class dbContract {
    public dbContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class dBase implements BaseColumns {
        // primary key is the same for all tables
        public static final int NDEX_ID = 0;

        // Set up column names and index valus for UNITS Table
        public static final String TN_UNITS = "UNITS";
        public static final String CN_UNITS_SYMBOL = "SYMBOL";
        public static final String CN_UNITS_NAME = "NAME";
        public static final String CN_UNITS_TYPE = "TYPE";

        public static final String[] CN_UNITS = new String[] { CN_UNITS_SYMBOL, CN_UNITS_NAME,
                CN_UNITS_TYPE };

        public static final int NDEX_UNITS_SYMBOL = 1;
        public static final int NDEX_UNITS_NAME = 2;
        public static final int NDEX_UNITS_TYPE = 3;

        // Set up column names and index values for CONVS Table
        public static final String TN_CONVS = "CONVS";
        public static final String CN_CONVS_FROM = "FROMUNIT";
        public static final String CN_CONVS_TO = "TOUNIT";
        public static final String CN_CONVS_MULTI = "MULTIPLYBY";
        public static final String CN_CONVS_OFFSET = "OFFSET";
        public static final String CN_CONVS_SPECIAL = "SPECIAL";

        public static final String[] CN_CONVS = new String[] { CN_CONVS_FROM, CN_CONVS_TO,
                CN_CONVS_MULTI, CN_CONVS_OFFSET, CN_CONVS_SPECIAL };

        public static final int NDEX_CONVS_FROM = 1;
        public static final int NDEX_CONVS_TO = 2;
        public static final int NDEX_CONVS_MUTLI = 3;
        public static final int NDEX_CONVS_OFFSET = 4;
        public static final int NDEX_CONVS_SPECIAL = 5;

        // Set up index values for Joined Cursor
        public static final int NDEX_JOINED_FROM = 1;
        public static final int NDEX_JOINED_TO = 2;
        public static final int NDEX_JOINED_MULTI = 3;
        public static final int NDEX_JOINED_OFFSET = 4;
        public static final int NDEX_JOINED_SPECIAL = 5;

        // Column name arrays for the adapter and listview
        public final static String[] COLUMNS_UNITS = new String[] { CN_UNITS_SYMBOL, CN_UNITS_NAME,
                CN_UNITS_TYPE };

        // View ids arrays for the adapter and listview
        public final static int[] VIEW_IDS_UNITS = new int[] { R.id.tvListSymbol, R.id.tvListName,
                R.id.tvListType };

        public final static int[] VIEW_IDS_CONVS = new int[] { R.id.tvListFrom, R.id.tvListTo,
                R.id.tvListMultiplyBy };

//        public final static String QUERYCONVS = "SELECT C._ID AS _id, U1._ID, U1.SYMBOL, U2._ID, U2.SYMBOL, C.MULTI, C.OFFSET, C.SPECIAL FROM CONVS C "
//                + "LEFT JOIN UNITS U1 ON U1._ID=C.FROMID " + "LEFT JOIN UNITS U2 ON U2._ID=C.TOID";
        

        public final static String QUERYTEST = "SELECT _ID, FROMID FROM CONVS";

    }
}
