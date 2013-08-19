package com.mattlykins.converter;

import java.io.IOException;

import com.mattlykins.converter.dbContract.dBase;
import com.mattlykins.dblibrary.DatabaseHelper;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
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
    boolean lgUnits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_db);
        context = this;
        list = (ListView) findViewById(R.id.list);

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        openDatabase();

        lgUnits = true;
        Cursor cursor = null;

        if (lgUnits) {

            String tableName = dBase.TN_UNITS; // Get this from the intent
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
     * Method uses the DatabaseHelper class to create, if necessary, and open the database
     */
    private void openDatabase() {
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

        final int index = c.getInt(dBase.NDEX_ID);
        final String tempDbSymbol = c.getString(dBase.NDEX_UNITS_SYMBOL);
        final String tempDbName = c.getString(dBase.NDEX_UNITS_NAME);
        final String tempDbType = c.getString(dBase.NDEX_UNITS_TYPE);

        Log.d("FERRET", tempDbSymbol + " " + tempDbName + " " + tempDbType + "\n");

        final Dialog d = new Dialog(arg0.getContext());
        d.setContentView(R.layout.edit_units);
        d.setTitle("Edit Unit");

        final EditText etEditUnitsSymbol = (EditText) d.findViewById(R.id.etEditUnitsSymbol);
        final EditText etEditUnitsName = (EditText) d.findViewById(R.id.etEditUnitsName);
        final EditText etEditUnitsType = (EditText) d.findViewById(R.id.etEditUnitsType);
        
        etEditUnitsSymbol.setText(tempDbSymbol);
        etEditUnitsName.setText(tempDbName);
        etEditUnitsType.setText(tempDbType);

        Button bEditUnitsOK = (Button) d.findViewById(R.id.bEditUnitsOK);
        Button bEditUnitsCancel = (Button) d.findViewById(R.id.bEditUnitsCancel);
        Button bEditUnitsDelete = (Button) d.findViewById(R.id.bEditUnitsDelete);

        bEditUnitsOK.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Find which ID this is and update its values

                final String tempEtSymbol = etEditUnitsSymbol.getText().toString();
                final String tempEtName = etEditUnitsName.getText().toString();
                final String tempEtType = etEditUnitsType.getText().toString();

                String[] colData = new String[] { tempEtSymbol, tempEtName, tempEtType };

                mydbHelper.Update_ByID(dBase.TN_UNITS, index, dBase.CN_UNITS, colData);

                scAdapter.changeCursor(mydbHelper.getAllRows(dBase.TN_UNITS));
                d.dismiss();
            }
        });
        bEditUnitsCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        bEditUnitsDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mydbHelper.Delete_ByID(dBase.TN_UNITS, index);
                
                scAdapter.changeCursor(mydbHelper.getAllRows(dBase.TN_UNITS));
                d.dismiss();
            }
        });
        
        d.show();
    }
}
