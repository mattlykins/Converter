package com.mattlykins.converter;

import com.mattlykins.converter.dbContract.dBase;

import android.database.Cursor;
import android.util.Log;

public class LogCursor {

    public LogCursor() {

    }

    public void LogConvs(Cursor cursor, String PreText) {
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            Log.d("FERRET", PreText + ": " +
                    cursor.getString(dBase.NDEX_CONVS_FROM) + " "
                            + cursor.getString(dBase.NDEX_CONVS_TO) + " " + 
                           String.valueOf(cursor.getDouble(dBase.NDEX_CONVS_MUTLI)) + " " +
                           cursor.getString(dBase.NDEX_CONVS_OFFSET) + " " +
                           cursor.getString(dBase.NDEX_CONVS_SPECIAL) + "\n");
            cursor.moveToNext();
        }
    }

}
