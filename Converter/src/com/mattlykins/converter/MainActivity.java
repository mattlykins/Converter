package com.mattlykins.converter;

import java.io.IOException;

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
        myDbHelper = new DatabaseHelper(this);

        try {

            myDbHelper.createDataBase();

        }
        catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        myDbHelper.openDataBase();

        Cursor c = myDbHelper.getAllRows();

        // Set up alert dialog for all three cases (Verified, UnMatched, and
        // WrongValue)
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setCancelable(false);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // here you can add functions
                dialog.cancel();
            }
        });

        adb.setTitle("TABLE UNITS");
        String adMessage = "";
        
        c.moveToFirst();
        while (c.isAfterLast() == false) 
        {
            adMessage = adMessage + c.getString(1) + "\n";
            c.moveToNext();
        }
        
        adb.setMessage(adMessage);
        AlertDialog ad = adb.create();
        ad.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
