package com.mattlykins.converter;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.mattlykins.converter.dbContract.dBase;
import com.mattlykins.dblibrary.DatabaseHelper;

public class dbIntegrity {

    DatabaseHelper dbHelper;
    Context myContext;
    LogCursor lc;
    Integer convsAdded;

    public dbIntegrity(Context context) {
        myContext = context;
        lc = new LogCursor();
        convsAdded = 0;

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
        Cursor cAll = dbHelper.getAllRows(dBase.TN_CONVS,dBase.CN_CONVS_FROM);
        if (cAll == null) {
            return;
        }
        // return if null
        cAll.moveToFirst();

        while (cAll.isAfterLast() == false) {

            Integer currentIndex = cAll.getInt(dBase.NDEX_ID);
            String currentFrom = cAll.getString(dBase.NDEX_CONVS_FROM);
            String currentTo = cAll.getString(dBase.NDEX_CONVS_TO);
            Double currentMultiply = cAll.getDouble(dBase.NDEX_CONVS_MUTLI);
            Double currentOffset = cAll.getDouble(dBase.NDEX_CONVS_OFFSET);
            String currentSpecial = cAll.getString(dBase.NDEX_CONVS_SPECIAL);

            // Check to see if the currentTo has Froms that take it to other Tos
            Cursor cFrom = dbHelper.Query(true, dBase.TN_CONVS, null, dBase.CN_CONVS_FROM + "=?",
                    new String[] { currentTo });
            if (cFrom == null || cFrom.getCount() == 0) {
                cAll.moveToNext();
                continue;
            }

            lc.LogConvs(cFrom, "cFrom");
            cFrom.moveToFirst();

            while (cFrom.isAfterLast() == false) {

                Double fromMultiply = cFrom.getDouble(dBase.NDEX_CONVS_MUTLI);
                Double fromOffset = cFrom.getDouble(dBase.NDEX_CONVS_OFFSET);
                String fromSpecial = cFrom.getString(dBase.NDEX_CONVS_SPECIAL);
                String testTo = cFrom.getString(dBase.NDEX_CONVS_TO);

                // Check if the current From also connects to the resultant Tos.
                Cursor cTo = dbHelper.Query(true, dBase.TN_CONVS, null, dBase.CN_CONVS_FROM
                        + "=? AND " + dBase.CN_CONVS_TO + "=?",
                        new String[] { currentFrom, testTo });

                if (cTo == null || cTo.getCount() == 0) {

                    // If the conversion is possible but not available, add it.
                    Double newMultiply = currentMultiply * fromMultiply;
                    Double newOffset = currentOffset + fromOffset;
                    String newSpecial = "";
                    if (currentSpecial.isEmpty() && fromSpecial.isEmpty()) {
                        newSpecial = "";
                    }
                    else if (currentSpecial.equals(fromSpecial) && currentSpecial.equals("INVERSE")) {
                        newSpecial = "";
                    }
                    else {
                        newSpecial = currentSpecial + fromSpecial;
                    }

                    try {
						dbHelper.Insert(dBase.TN_CONVS, dBase.CN_CONVS, new String[] { currentFrom,
						        testTo, String.valueOf(newMultiply), String.valueOf(newOffset),
						        newSpecial });
					} catch (SQLException sqlex) {
						// TODO Auto-generated catch block
						sqlex.printStackTrace();
						PopUp p = new PopUp(myContext, "PROBLEM!!!", "Could not add conversion");
						return;
					}
                    convsAdded++;

                    cFrom.moveToNext();
                    continue;
                }
                lc.LogConvs(cTo, "cTo");

                cTo.moveToFirst();

                // Verify multiplicative factors
                if (cTo.getDouble(dBase.NDEX_CONVS_MUTLI) != currentMultiply * fromMultiply) {
                    PopUp p = new PopUp(myContext, "PROBLEM", "Bad Multi");
                }

                cFrom.moveToNext();
            }
            cAll.moveToNext();
        }
        
        PopUp p = new PopUp(myContext, "Conversion Added", String.format("%i conversions have been added.", convsAdded));

    }
}
