package com.mattlykins.converter;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;

import com.mattlykins.converter.dbContract.dBase;
import com.mattlykins.dblibrary.DatabaseHelper;

public class dbIntegrity {

    DatabaseHelper dbHelper;
    Context myContext;
    LogCursor lc;

    public dbIntegrity(Context context) {
        myContext = context;
        lc = new LogCursor();

        dbHelper = new DatabaseHelper(context);
        try {
            dbHelper.createDataBase();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dbHelper.openDataBase();

    }

    public void checkIntegrity() {
        Cursor cAll = dbHelper.getAllRows(dBase.TN_CONVS);
        if (cAll == null) {
            return;
        }
        // return if null
        cAll.moveToFirst();

        Integer currentIndex = cAll.getInt(dBase.NDEX_ID);
        String currentFrom = cAll.getString(dBase.NDEX_CONVS_FROM);
        String currentTo = cAll.getString(dBase.NDEX_CONVS_TO);

        // Check to see if the currentTo has Froms that take it to other Tos
        Cursor cFrom = dbHelper.Query(true, dBase.TN_CONVS, null, dBase.CN_CONVS_FROM + "=?",
                new String[] { currentTo });
        if (cFrom == null) {
            return;
        }       
        
        lc.LogConvs(cFrom, "cFrom");
        cFrom.moveToFirst();
        cFrom.moveToNext();

        String testTo = cFrom.getString(dBase.NDEX_CONVS_TO);

        // Check if the current From also connects to the resultant Tos.
        Cursor cTo = dbHelper.Query(true, dBase.TN_CONVS, null, dBase.CN_CONVS_FROM + "=? AND "
                + dBase.CN_CONVS_TO + "=?", new String[] { currentFrom, testTo });
        if (cTo == null) {
            return;
        }
        lc.LogConvs(cTo, "cTo");

    }

}
