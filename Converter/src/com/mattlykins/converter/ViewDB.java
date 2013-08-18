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

        String tableName = dBase.TN_UNITS; // Get this from the intent
        
       // Cursor cursor = mydbHelper.getAllRows(tableName);
       Cursor cursor = mydbHelper.sqlQuery(dBase.QUERYCONVS);
        
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_row_convs, cursor,
                dBase.COLUMNS_CONVS, dBase.VIEW_IDS_CONVS, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        list.setAdapter(adapter);
        

       list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//        // TODO Auto-generated method stub
//        Cursor c = (Cursor) arg0.getItemAtPosition(arg2);
//        Intent intent = new Intent(context, AddToDB.class);
//
//        int index = c.getInt(dBase.NDEX_ID);
//        String sFrom = c.getString(dBase.NDEX_FROMSYMBOL);
//        String sFromText = c.getString(dBase.NDEX_FROMTEXT);
//        String sTo = c.getString(dBase.NDEX_TOSYMBOL);
//        String sToText = c.getString(dBase.NDEX_TOTEXT);
//        String sMultiBy = c.getString(dBase.NDEX_MULTIBY);
//
//        intent.putExtra("index", index);
//        intent.putExtra("sFrom", sFrom);
//        intent.putExtra("sFromText", sFromText);
//        intent.putExtra("sTo", sTo);
//        intent.putExtra("sToText", sToText);
//        intent.putExtra("sMultiBy", sMultiBy);
//
//        startActivity(intent);
    }

}
