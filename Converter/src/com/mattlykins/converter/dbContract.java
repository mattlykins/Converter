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

        public static final int NDEX_UNITS_SYMBOL = 1;
        public static final int NDEX_UNITS_NAME = 2;
        public static final int NDEX_UNITS_TYPE = 3;

        // Set up column names and index valus for CONVS Table
        public static final String TN_CONVS = "CONVS";
        public static final String CN_CONVS_FROMID = "FROMID";
        public static final String CN_CONVS_TOID = "TOID";
        public static final String CN_CONVS_MULTI = "MULTI";
        public static final String CN_CONVS_OFFSET = "OFFSET";
        public static final String CN_CONVS_SPECIAL = "SPECIAL";

        public static final int NDEX_CONVS_FROMID = 1;
        public static final int NDEX_CONVS_TOID = 2;
        public static final int NDEX_CONVS_MUTLI = 3;
        public static final int NDEX_CONVS_OFFSET = 4;
        public static final int NDEX_CONVS_SPECIAL = 5;

        // Column name arrays for the adapter and listview
        final static String[] COLUMNS_UNITS = new String[] { CN_UNITS_SYMBOL, CN_UNITS_NAME,
                CN_UNITS_TYPE };
        final static String[] COLUMNS_CONVS = new String[] { CN_CONVS_FROMID, CN_CONVS_TOID,
                CN_CONVS_MULTI, CN_CONVS_OFFSET, CN_CONVS_SPECIAL };

        // View ids arrays for the adapter and listview
        final static int[] VIEW_IDS_UNITS = new int[] { R.id.tvListSymbol, R.id.tvListName,
                R.id.tvListType };        
        final static int[] VIEW_IDS_CONVS = new int[] { R.id.tvListFrom, R.id.tvListTo,
            R.id.tvListMultiplyBy, R.id.tvListOffset, R.id.tvListSpecial};

    }
}
