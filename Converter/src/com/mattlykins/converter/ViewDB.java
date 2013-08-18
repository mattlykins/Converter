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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ViewDB extends Activity implements OnItemClickListener {

    ListView list;
    Context context;

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

        DatabaseHelper mydbHelper = new DatabaseHelper(this);
        try {
            mydbHelper.createDataBase();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mydbHelper.openDataBase();

        boolean lgUnits = true;
        Cursor cursor = null;

        if (lgUnits) {

            String tableName = dBase.TN_UNITS; // Get this from the intent
            cursor = mydbHelper.getAllRows(tableName);

            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_row_units,
                    cursor, dBase.COLUMNS_UNITS, dBase.VIEW_IDS_UNITS,
                    SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

            list.setAdapter(adapter);
            list.setOnItemClickListener(this);
        }
        else {

            cursor = mydbHelper.sqlQuery(dBase.QUERYCONVS);
            CustomCursorAdapter adapter = new CustomCursorAdapter(this, R.layout.list_row_convs,
                    cursor);
            list.setAdapter(adapter);
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

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Cursor c = (Cursor) arg0.getItemAtPosition(arg2);
        // Intent intent = new Intent(context, AddToDB.class);
        //
        int index = c.getInt(dBase.NDEX_ID);
        String tempSymbol = c.getString(dBase.NDEX_UNITS_SYMBOL);
        String tempName = c.getString(dBase.NDEX_UNITS_NAME);
        String tempType = c.getString(dBase.NDEX_UNITS_TYPE);
        
        Log.d("FERRET", tempSymbol+ " " +tempName+ " " +tempType+ "\n");

        Dialog d = new Dialog(arg0.getContext());
        d.setContentView(R.layout.edit_units);
        d.setTitle("Edit Unit");
        
        EditText etEditUnitsSymbol = (EditText) d.findViewById(R.id.etEditUnitsSymbol);
        EditText etEditUnitsName = (EditText) d.findViewById(R.id.etEditUnitsName);
        EditText etEditUnitsType = (EditText) d.findViewById(R.id.etEditUnitsType);

        etEditUnitsSymbol.setText(tempSymbol);
        etEditUnitsName.setText(tempName);
        etEditUnitsType.setText(tempType);

        d.show();
    }

}
