package com.mattlykins.converter;

import java.io.IOException;

import com.mattlykins.converter.dbContract.dBase;
import com.mattlykins.dblibrary.DatabaseHelper;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper myDbHelper = new DatabaseHelper(this);
        
        try {

            myDbHelper.createDataBase();

        }
        catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        myDbHelper.openDataBase();

        Cursor c = myDbHelper.getAllRows(dBase.TN_UNITS);

        String adMessage = "";

        c.moveToFirst();
        while (c.isAfterLast() == false) {
            adMessage = adMessage + c.getString(dBase.NDEX_UNITS_SYMBOL) + CONSTANT.SPACE
                    + c.getString(dBase.NDEX_UNITS_NAME) + CONSTANT.SPACE
                    + c.getString(dBase.NDEX_UNITS_TYPE) + "\n";
            c.moveToNext();
        }

        c.close();

        PopUp p = new PopUp(this, "Database Values", adMessage);

        c = myDbHelper.getAllRows(dBase.TN_CONVS);

        adMessage = "";

        c.moveToFirst();
        while (c.isAfterLast() == false) {
            adMessage = adMessage + c.getString(dBase.NDEX_CONVS_FROMID) + CONSTANT.SPACE
                    + c.getString(dBase.NDEX_CONVS_TOID) + CONSTANT.SPACE
                    + c.getString(dBase.NDEX_CONVS_MUTLI) + CONSTANT.SPACE
                    + c.getString(dBase.NDEX_CONVS_OFFSET) + CONSTANT.SPACE
                    + c.getString(dBase.NDEX_CONVS_SPECIAL) + "\n";
            c.moveToNext();
        }

        c.close();

        p = new PopUp(this, "Database Values", adMessage);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
