package com.mattlykins.converter;

import java.io.IOException;

import com.mattlykins.converter.dbContract.dBase;
import com.mattlykins.dblibrary.DatabaseHelper;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        DatabaseHelper myDbHelper = new DatabaseHelper(this);
//        
//        try {
//
//            myDbHelper.createDataBase();
//
//        }
//        catch (IOException ioe) {
//
//            throw new Error("Unable to create database");
//
//        }
//
//        myDbHelper.openDataBase();
//
//        Cursor c = myDbHelper.getAllRows(dBase.TN_UNITS);
//
//        String adMessage = "";
//
//        c.moveToFirst();
//        while (c.isAfterLast() == false) {
//            adMessage = adMessage + c.getString(dBase.NDEX_UNITS_SYMBOL) + CONSTANT.SPACE
//                    + c.getString(dBase.NDEX_UNITS_NAME) + CONSTANT.SPACE
//                    + c.getString(dBase.NDEX_UNITS_TYPE) + "\n";
//            c.moveToNext();
//        }
//
//        c.close();
//
//        PopUp p = new PopUp(this, "Database Values", adMessage);
//
//        c = myDbHelper.getAllRows(dBase.TN_CONVS);
//
//        adMessage = "";
//
//        c.moveToFirst();
//        while (c.isAfterLast() == false) {
//            adMessage = adMessage + c.getString(dBase.NDEX_CONVS_FROMID) + CONSTANT.SPACE
//                    + c.getString(dBase.NDEX_CONVS_TOID) + CONSTANT.SPACE
//                    + c.getString(dBase.NDEX_CONVS_MUTLI) + CONSTANT.SPACE
//                    + c.getString(dBase.NDEX_CONVS_OFFSET) + CONSTANT.SPACE
//                    + c.getString(dBase.NDEX_CONVS_SPECIAL) + "\n";
//            c.moveToNext();
//        }
//
//        c.close();
//
//        p = new PopUp(this, "Database Values", adMessage);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_view_db:
                final Intent intent = new Intent(this,ViewDB.class);
                
                AlertDialog.Builder b = new AlertDialog.Builder(this);
                b.setTitle("Example");
                final CharSequence[] types = {"UNITS", "CONVS"};
                b.setItems(types, new DialogInterface.OnClickListener() 
                {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) 
                    {
                        switch(which)
                        {
                        case 0:
                            intent.putExtra(CONSTANT.WHICH_TABLE, dBase.TN_UNITS);
                            startActivity(intent);
                            break;
                        case 1:
                            intent.putExtra(CONSTANT.WHICH_TABLE, dBase.TN_CONVS);
                            startActivity(intent);
                            break;
                        
                        }
                    }
                });
                b.show(); 
                break;
        }
        return super.onOptionsItemSelected(item);
        
    }

}
