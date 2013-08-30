package com.mattlykins.converter;

import java.io.IOException;

import com.mattlykins.converter.dbContract.dBase;
import com.mattlykins.dblibrary.DatabaseHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

/**
 * Class handles the display of the unit and conversion data
 * 
 */
public class ViewDB extends Activity implements OnItemClickListener {

    ListView list;
    Context context;
    DatabaseHelper mydbHelper;
    CustomCursorAdapter ccAdapter;
    SimpleCursorAdapter scAdapter;
    String whichTable;
    Spinner spinTable;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_db);
        context = this;
        list = (ListView) findViewById(R.id.list);
        
        spinTable = (Spinner)findViewById(R.id.spinTable);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,dBase.TN_TABLES_STRING);
        spinTable.setAdapter(spinnerAdapter);
        
        spinTable.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                whichTable = spinTable.getItemAtPosition(arg2).toString();
                
                if( whichTable.equals(dBase.TN_UNITS))
                {
                    unitsSelected();
                    //scAdapter.changeCursor(mydbHelper.getAllRows(dBase.TN_UNITS));
                }
                else if( whichTable.equals(dBase.TN_CONVS))
                {
                    convsSelected();
                    //ccAdapter.changeCursor(mydbHelper.getAllRows(dBase.TN_CONVS));
                }
                
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                
            }
        });
        

    }

//    private void getWhichTable(Bundle savedInstanceState) {
//        if (savedInstanceState == null) {
//            Bundle extras = getIntent().getExtras();
//            if (extras == null) {
//                @SuppressWarnings("unused")
//                PopUp p = new PopUp(this, "ERROR!!!!", "ViewDB received no bundle");
//            }
//            else {
//                whichTable = extras.getString(CONSTANT.WHICH_TABLE);
//            }
//        }
//        else {
//            whichTable = (String) savedInstanceState.getSerializable(CONSTANT.WHICH_TABLE);
//        }
//    }

    private void unitsSelected(){
        cursor = mydbHelper.getAllRows(whichTable);

        scAdapter = new SimpleCursorAdapter(this, R.layout.list_row_units, cursor,
                dBase.COLUMNS_UNITS, dBase.VIEW_IDS_UNITS,
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        list.setAdapter(scAdapter);
        list.setOnItemClickListener(this);
    }
    
    private void convsSelected(){
        cursor = mydbHelper.getAllRows(whichTable);
        ccAdapter = new CustomCursorAdapter(this, R.layout.list_row_convs, cursor);
        list.setAdapter(ccAdapter);
//        list.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                Cursor c = (Cursor) arg0.getItemAtPosition(arg2);
//                EditConvsDialog ecd = new EditConvsDialog(context, R.layout.edit_convs,
//                        "Edit Conversion", c, ccAdapter);
//                ecd.show();                
//            }
//        });
    }
    
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        openDatabase();
        
        whichTable = dBase.TN_UNITS;

        //Cursor cursor = null;

        if (whichTable.equals(dBase.TN_UNITS)) {

//            cursor = mydbHelper.getAllRows(whichTable);
//
//            scAdapter = new SimpleCursorAdapter(this, R.layout.list_row_units, cursor,
//                    dBase.COLUMNS_UNITS, dBase.VIEW_IDS_UNITS,
//                    SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
//
//            list.setAdapter(scAdapter);
//            list.setOnItemClickListener(this);
        }
        else {

            // cursor = mydbHelper.sqlQuery(dBase.QUERYCONVS);
//            cursor = mydbHelper.getAllRows(whichTable);
//            ccAdapter = new CustomCursorAdapter(this, R.layout.list_row_convs, cursor);
//            list.setAdapter(ccAdapter);
//            list.setOnItemClickListener(this);
        }

        // cursor.moveToFirst();
        // while (cursor.isAfterLast() == false) {
        // Log.d("FERRET",
        // cursor.getString(0) + " " + cursor.getString(1) + " " +
        // cursor.getString(2)
        // + " " + String.valueOf(cursor.getDouble(3)) + "\n");
        // cursor.moveToNext();
        // }

    }

    /**
     * Method uses the DatabaseHelper class to create, if necessary, and open
     * the database
     */
    public void openDatabase() {
        mydbHelper = new DatabaseHelper(this);
        try {
            mydbHelper.createDataBase();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mydbHelper.openDataBase();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Cursor c = (Cursor) arg0.getItemAtPosition(arg2);

        if (whichTable.equals(dBase.TN_UNITS)) {
            EditUnitsDialog eud = new EditUnitsDialog(context, R.layout.edit_units, CONSTANT.EDIT
                    + " " + dBase.TN_UNITS, c, scAdapter);
            eud.show();
        }
        else {
            EditConvsDialog ecd = new EditConvsDialog(context, R.layout.edit_convs,
                    "Edit Conversion", c, ccAdapter);
            ecd.show();
        }

    }
}
