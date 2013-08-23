package com.mattlykins.converter;

import java.io.IOException;

import com.mattlykins.converter.dbContract.dBase;
import com.mattlykins.dblibrary.DatabaseHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWhichTable(savedInstanceState);

        setContentView(R.layout.activity_view_db);
        context = this;
        list = (ListView) findViewById(R.id.list);

    }

    private void getWhichTable(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                @SuppressWarnings("unused")
                PopUp p = new PopUp(this, "ERROR!!!!", "ViewDB received no bundle");
            }
            else {
                whichTable = extras.getString(CONSTANT.WHICH_TABLE);
            }
        }
        else {
            whichTable = (String) savedInstanceState.getSerializable(CONSTANT.WHICH_TABLE);
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        openDatabase();

        Cursor cursor = null;

        if (whichTable.equals(dBase.TN_UNITS)) {

            String tableName = whichTable; // Get this from the intent
            cursor = mydbHelper.getAllRows(tableName);

            scAdapter = new SimpleCursorAdapter(this, R.layout.list_row_units, cursor,
                    dBase.COLUMNS_UNITS, dBase.VIEW_IDS_UNITS,
                    SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

            list.setAdapter(scAdapter);
            list.setOnItemClickListener(this);
        }
        else {

            cursor = mydbHelper.sqlQuery(dBase.QUERYCONVS);
            ccAdapter = new CustomCursorAdapter(this, R.layout.list_row_convs, cursor);
            list.setAdapter(ccAdapter);
            list.setOnItemClickListener(this);
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
            EditUnitsDialog eud = new EditUnitsDialog(context, R.layout.edit_units, "Edit Units",
                    c, scAdapter);
            eud.show();
        }
        else{
            EditConvsDialog ecd = new EditConvsDialog(context, R.layout.edit_convs, "Edit Conversion",
                    c, ccAdapter);
            ecd.show();
        }
        
    }
}
