package com.mattlykins.converter;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

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
        Cursor cAll = dbHelper.getAllRows(dBase.TN_CONVS, dBase.CN_CONVS_FROM);
        if (cAll == null) {
            return;
        }
        // return if null
        cAll.moveToFirst();

        while (cAll.moveToNext()) {

            Integer currentIndex = cAll.getInt(dBase.NDEX_ID);
            String currentFrom = cAll.getString(dBase.NDEX_CONVS_FROM);
            String currentTo = cAll.getString(dBase.NDEX_CONVS_TO);
            Double currentMultiply = cAll.getDouble(dBase.NDEX_CONVS_MUTLI);
            Double currentOffset = cAll.getDouble(dBase.NDEX_CONVS_OFFSET);
            String currentSpecial = cAll.getString(dBase.NDEX_CONVS_SPECIAL);

            // Check to see if the currentTo has Froms that take it to other Tos
            Cursor cSecond = dbHelper.Query(true, dBase.TN_CONVS, null, dBase.CN_CONVS_FROM + "=?",
                    new String[] { currentTo });
            if (cSecond == null || cSecond.getCount() == 0) {
                cAll.moveToNext();
                continue;
            }

            lc.LogConvs(cSecond, "cSecond");
            cSecond.moveToFirst();

            while (cSecond.moveToNext()) {

                Double secondMultiply = cSecond.getDouble(dBase.NDEX_CONVS_MUTLI);
                Double secondOffset = cSecond.getDouble(dBase.NDEX_CONVS_OFFSET);
                String secondSpecial = cSecond.getString(dBase.NDEX_CONVS_SPECIAL);
                String secondTo = cSecond.getString(dBase.NDEX_CONVS_TO);

                if (currentFrom.equals(secondTo)) {
                    continue;
                }

                // Check if the current From already connects to the second
                // level Tos.
                Cursor cExists = dbHelper.Query(true, dBase.TN_CONVS, null, dBase.CN_CONVS_FROM
                        + "=? AND " + dBase.CN_CONVS_TO + "=?", new String[] { currentFrom,
                        secondTo });

                if (cExists == null || cExists.getCount() == 0) {

                    // If the conversion is possible but does not exist, add it.
                    Double newMultiply = currentMultiply * secondMultiply;
                    Double newOffset = currentOffset + secondOffset;
                    String newSpecial = "";
                    if (currentSpecial.isEmpty() && secondSpecial.isEmpty()) {
                        newSpecial = "";
                    }
                    else if (currentSpecial.equals(secondSpecial)
                            && currentSpecial.equals("INVERSE")) {
                        newSpecial = currentSpecial;
                    }
                    else {
                        newSpecial = currentSpecial + ";" + secondSpecial;
                    }

                    try {
                        dbHelper.Insert(dBase.TN_CONVS, dBase.CN_CONVS, new String[] { currentFrom,
                                secondTo, String.valueOf(newMultiply), String.valueOf(newOffset),
                                newSpecial });
                    }
                    catch (SQLException sqlex) {
                        // TODO Auto-generated catch block
                        sqlex.printStackTrace();
                        PopUp p = new PopUp(myContext, "PROBLEM!!!", "Could not add conversion");
                        return;
                    }
                    convsAdded++;

                    cExists.close();
                }
                else {
                    lc.LogConvs(cExists, "cExists");
                    cExists.moveToFirst();
                    cExists.close();
                }

                // // Verify multiplicative factors
                // if (cExists.getDouble(dBase.NDEX_CONVS_MUTLI) /
                // (currentMultiply * secondMultiply) > 0.01) {
                // PopUp p = new PopUp(myContext, "PROBLEM", String.format(
                // "Bad Multi:%E\n%s To %s:%E\n%s to %s:%E",
                // cExists.getDouble(dBase.NDEX_CONVS_MUTLI), currentFrom,
                // currentTo,
                // currentMultiply, cSecond.getString(dBase.NDEX_CONVS_FROM),
                // cSecond.getString(dBase.NDEX_CONVS_TO), secondMultiply));
                // }
            }
            cSecond.close();
        }

        if (convsAdded > 0) {
            PopUp p = new PopUp(myContext, "Conversions Added", String.format(
                    "%d conversions have been added.", convsAdded));
        }
        cAll.close();
    }

    public void createInverses() {
        Cursor cAll = dbHelper.getAllRows(dBase.TN_CONVS, dBase.CN_CONVS_FROM);
        if (cAll == null) {
            return;
        }
        // return if null
        cAll.moveToFirst();

        while (cAll.moveToNext()) {

            final Integer currentIndex = cAll.getInt(dBase.NDEX_ID);
            final String currentFrom = cAll.getString(dBase.NDEX_CONVS_FROM);
            final String currentTo = cAll.getString(dBase.NDEX_CONVS_TO);
            final Double currentMultiply = cAll.getDouble(dBase.NDEX_CONVS_MUTLI);
            final Double currentOffset = cAll.getDouble(dBase.NDEX_CONVS_OFFSET);
            final String currentSpecial = cAll.getString(dBase.NDEX_CONVS_SPECIAL);

            // Check to see if inverse exists
            Cursor cInverse = dbHelper
                    .Query(true, dBase.TN_CONVS, null, dBase.CN_CONVS_FROM + "=? AND "
                            + dBase.CN_CONVS_TO + "=?", new String[] { currentTo, currentFrom });

            if (cInverse == null || cInverse.getCount() == 0) {
                // Create inverse conversions. For now special only has INVERSE
                // (cm to cm-1) which is still correct
                final Double invMultiply = 1 / currentMultiply;
                final Double invOffset = -1 * currentOffset;
                final String invFrom = currentTo;
                final String invTo = currentFrom;
                final String invSpecial = currentSpecial;

                dbHelper.Insert(dBase.TN_CONVS, dBase.CN_CONVS, new String[] { invFrom, invTo,
                        String.valueOf(invMultiply), String.valueOf(invOffset), invSpecial });
            }

            cInverse.close();
        }
        cAll.close();
    }

    public boolean verifyMultiFactors() {

        Cursor cAll = dbHelper.getAllRows(dBase.TN_CONVS, dBase.CN_CONVS_FROM);
        if (cAll == null) {
            return false;
        }
        // return if null
        cAll.moveToFirst();

        while (cAll.moveToNext()) {

            
            Convs currentConv = getConvs(cAll);
            
         // Check to see if the currentTo has Froms that take it to other Tos
            Cursor cSecond = dbHelper.Query(true, dBase.TN_CONVS, null, dBase.CN_CONVS_FROM + "=?",
                    new String[] { currentConv.getTo() });
            if (cSecond == null || cSecond.getCount() == 0) {
                cAll.moveToNext();
                continue;
            }

            lc.LogConvs(cSecond, "cSecond");
            cSecond.moveToFirst();

            while (cSecond.moveToNext()) {

                Convs secondConv = getConvs(cSecond);
                
                
                if (currentConv.getFrom().equals(secondConv.getTo())) {
                    continue;
                }

                // Check if the current From already connects to the second
                // level Tos.
                Cursor cExists = dbHelper.Query(true, dBase.TN_CONVS, null, dBase.CN_CONVS_FROM
                        + "=? AND " + dBase.CN_CONVS_TO + "=?", new String[] { currentConv.getFrom(),
                        secondConv.getTo() });

        }
        return false;
    }

    public Convs getConvs(Cursor c) {
        Integer cIndex = c.getInt(dBase.NDEX_ID);
        String cFrom = c.getString(dBase.NDEX_CONVS_FROM);
        String cTo = c.getString(dBase.NDEX_CONVS_TO);
        Double cMultiply = c.getDouble(dBase.NDEX_CONVS_MUTLI);
        Double cOffset = c.getDouble(dBase.NDEX_CONVS_OFFSET);
        String cSpecial = c.getString(dBase.NDEX_CONVS_SPECIAL);

        Convs cConv = new Convs(cIndex, cFrom, cTo, cMultiply, cOffset, cSpecial);

        return cConv;
    }
}
