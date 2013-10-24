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

    // public void checkIntegrity() {
    // Cursor cAll = dbHelper.getAllRows(dBase.TN_CONVS, dBase.CN_CONVS_FROM);
    // if (cAll == null) {
    // return;
    // }
    // // return if null
    // cAll.moveToFirst();
    //
    // while (cAll.moveToNext()) {
    //
    // Convs currentConv = getConvs(cAll);
    //
    // // Check to see if the currentTo has Froms that take it to other Tos
    // Cursor cSecond = dbHelper.Query(true, dBase.TN_CONVS, null,
    // dBase.CN_CONVS_FROM + "=?",
    // new String[] { currentConv.getTo() });
    // if (cSecond == null || cSecond.getCount() == 0) {
    // cAll.moveToNext();
    // continue;
    // }
    //
    // lc.LogConvs(cSecond, "cSecond");
    // cSecond.moveToFirst();
    //
    // while (cSecond.moveToNext()) {
    //
    // Convs secondConv = getConvs(cSecond);
    //
    // if (currentConv.getFrom().equals(secondConv.getTo())) {
    // continue;
    // }
    //
    // // Check if the current From already connects to the second
    // // level Tos.
    // Cursor cExists = dbHelper.Query(true, dBase.TN_CONVS, null,
    // dBase.CN_CONVS_FROM
    // + "=? AND " + dBase.CN_CONVS_TO + "=?",
    // new String[] { currentConv.getFrom(), secondConv.getTo() });
    //
    // if (cExists == null || cExists.getCount() == 0) {
    //
    // // If the conversion is possible but does not exist, add it.
    // Double newMultiply = currentConv.getMulti() * secondConv.getMulti();
    // Double newOffset = currentConv.getOffset() + secondConv.getOffset();
    // String newSpecial = "";
    // if (currentConv.getSpecial().isEmpty() &&
    // secondConv.getSpecial().isEmpty()) {
    // newSpecial = "";
    // }
    // else if (currentConv.getSpecial().equals(secondConv.getSpecial())
    // && currentConv.getSpecial().equals("INVERSE")) {
    // newSpecial = currentConv.getSpecial();
    // }
    // else {
    // newSpecial = currentConv.getSpecial() + ";" + secondConv.getSpecial();
    // }
    //
    // try {
    // dbHelper.Insert(
    // dBase.TN_CONVS,
    // dBase.CN_CONVS,
    // new String[] { currentConv.getFrom(), secondConv.getTo(),
    // String.valueOf(newMultiply), String.valueOf(newOffset),
    // newSpecial });
    // }
    // catch (SQLException sqlex) {
    // // TODO Auto-generated catch block
    // sqlex.printStackTrace();
    // PopUp p = new PopUp(myContext, "PROBLEM!!!", "Could not add conversion");
    // return;
    // }
    // convsAdded++;
    //
    // cExists.close();
    // }
    // else {
    // lc.LogConvs(cExists, "cExists");
    // cExists.moveToFirst();
    // cExists.close();
    // }
    //
    // // // Verify multiplicative factors
    // // if (cExists.getDouble(dBase.NDEX_CONVS_MUTLI) /
    // // (currentMultiply * secondMultiply) > 0.01) {
    // // PopUp p = new PopUp(myContext, "PROBLEM", String.format(
    // // "Bad Multi:%E\n%s To %s:%E\n%s to %s:%E",
    // // cExists.getDouble(dBase.NDEX_CONVS_MUTLI), currentFrom,
    // // currentTo,
    // // currentMultiply, cSecond.getString(dBase.NDEX_CONVS_FROM),
    // // cSecond.getString(dBase.NDEX_CONVS_TO), secondMultiply));
    // // }
    // }
    // cSecond.close();
    // }
    //
    // if (convsAdded > 0) {
    // PopUp p = new PopUp(myContext, "Conversions Added", String.format(
    // "%d conversions have been added.", convsAdded));
    // }
    // cAll.close();
    // }

    public void checkIntegrity() {
        Cursor cAll = dbHelper.getAllRows(dBase.TN_CONVS, dBase.CN_CONVS_FROM);
        if (cAll == null) {
            return;
        }
        // return if null
        cAll.moveToFirst();

        while (cAll.moveToNext()) {
            Convs currentConv = getConvs(cAll);
            extendConvs(currentConv);
        }
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

    // Used to extend the conversion space of a unit
    public void extendConvs(Convs conv) {

        // Query db for convs to extend the input conv
        Cursor c = dbHelper.Query(true, dBase.TN_CONVS, null, dBase.CN_CONVS_FROM + "=?",
                new String[] { conv.getTo() });
        if (c == null || c.getCount() == 0) {
            // No convs to extend the input
            return;
        }
        else {

            c.moveToFirst();

            while (c.moveToNext()) {
                Convs secondConv = getConvs(c);

                // Check to see if the potential extended conv already exists
                Cursor cExists = dbHelper.Query(true, dBase.TN_CONVS, null, dBase.CN_CONVS_FROM
                        + "=? AND " + dBase.CN_CONVS_TO + "=?", new String[] { conv.getFrom(),
                        secondConv.getTo() });

                // null cursor means that the potential conv does not exist
                if (cExists == null || cExists.getCount() == 0) {

                    Convs newConv = new Convs();

                    newConv.setFrom(conv.getFrom());
                    newConv.setTo(secondConv.getTo());
                    newConv.setSpecial(conv.getSpecial() + ";" + secondConv.getSpecial());
                    newConv.setMulti(conv.getMulti() * secondConv.getMulti());
                    newConv.setOffset(conv.getOffset() + secondConv.getOffset());

                    // Call routine to add conv object to db
                    if (addConv(newConv) == 0) {
                        Log.d("FERRET", "Conversion added\n");

                    }
                }
                else if (cExists.getCount() == 1) {
                    // Move to first and only
                    cExists.moveToFirst();
                    Convs existConv = getConvs(cExists);
                    
                    double oldFactor = existConv.getMulti();
                    double newFactor = conv.getMulti() * secondConv.getMulti();
                    
                    double epsilon = 0.01;

                    // If the conversion already exists, check that the values
                    // are correct

                    if (!conv.getFrom().equals(existConv.getFrom())) {

                        // Bad news!
                    }
                    else if (!secondConv.getTo().equals(existConv.getTo())) {

                        // Bad news !
                    }
                    else if (Math.abs((oldFactor - newFactor)/oldFactor) > epsilon) {

                        // Fix it!
                        Log.d("FERRET",
                                "MULTI DOESN'T MATCH::" + conv.getFrom() + ":" + conv.getMulti()
                                        + " and " + secondConv.getTo() + ":"
                                        + secondConv.getMulti() + " does not match:" + existConv.getMulti() + "\n");
                        
                        PopUp p = new PopUp(myContext,"PROBLEM","MULTI DOESN'T MATCH");

                    }

                }
                else {
                    // WTF!
                }
            }
        }

    }

    public int addConv(Convs conv) {

        try {
            dbHelper.Insert(dBase.TN_CONVS, dBase.CN_CONVS,
                    new String[] { conv.getFrom(), conv.getTo(), String.valueOf(conv.getMulti()),
                            String.valueOf(conv.getOffset()), conv.getSpecial() });
        }
        catch (SQLException sqlex) {
            sqlex.printStackTrace();
            PopUp p = new PopUp(myContext, "PROBLEM!!!", "Could not add conversion");
            return -1;
        }
        return 0;
    }
}
