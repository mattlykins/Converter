package com.mattlykins.converter;

import java.io.IOException;

import com.mattlykins.converter.dbContract.dBase;
import com.mattlykins.dblibrary.DatabaseHelper;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;

public class EditUnitsDialog extends Dialog implements android.view.View.OnClickListener {

    //Handle when user try to set two unique columns with the same values
    
    final EditText etEditUnitsSymbol, etEditUnitsName, etEditUnitsType;
    Button bEditUnitsOK, bEditUnitsCancel, bEditUnitsDelete;
    final int index;
    DatabaseHelper dbHelper;
    SimpleCursorAdapter scAdapter;
    String addOrEdit;

    public EditUnitsDialog(Context context, int ViewId, String title, Cursor c, SimpleCursorAdapter adapter) {
        super(context);
        this.setContentView(ViewId);
        this.setTitle(title);
        
        scAdapter = adapter;
        
        dbHelper = new DatabaseHelper(context);
        try {
            dbHelper.createDataBase();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dbHelper.openDataBase();

        etEditUnitsSymbol = (EditText) this.findViewById(R.id.etEditUnitsSymbol);
        etEditUnitsName = (EditText) this.findViewById(R.id.etEditUnitsName);
        etEditUnitsType = (EditText) this.findViewById(R.id.etEditUnitsType);

        bEditUnitsOK = (Button) this.findViewById(R.id.bEditUnitsOK);
        bEditUnitsCancel = (Button) this.findViewById(R.id.bEditUnitsCancel);
        bEditUnitsDelete = (Button) this.findViewById(R.id.bEditUnitsDelete);

        bEditUnitsOK.setOnClickListener(this);
        bEditUnitsCancel.setOnClickListener(this);
        bEditUnitsDelete.setOnClickListener(this);

        index = c.getInt(dBase.NDEX_ID);

        etEditUnitsSymbol.setText(c.getString(dBase.NDEX_UNITS_SYMBOL));
        etEditUnitsName.setText(c.getString(dBase.NDEX_UNITS_NAME));
        etEditUnitsType.setText(c.getString(dBase.NDEX_UNITS_TYPE));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bEditUnitsOK:

                final String tempEtSymbol = etEditUnitsSymbol.getText().toString();
                final String tempEtName = etEditUnitsName.getText().toString();
                final String tempEtType = etEditUnitsType.getText().toString();

                String[] colData = new String[] { tempEtSymbol, tempEtName, tempEtType };

                
                dbHelper.Update_ByID(dBase.TN_UNITS, index, dBase.CN_UNITS, colData);
                
                scAdapter.changeCursor(dbHelper.getAllRows(dBase.TN_UNITS));
                this.dismiss();
                break;

            case R.id.bEditUnitsCancel:
                this.dismiss();
                break;

            case R.id.bEditUnitsDelete:
                dbHelper.Delete_ByID(dBase.TN_UNITS, index);
                scAdapter.changeCursor(dbHelper.getAllRows(dBase.TN_UNITS));
                this.dismiss();
                break;
        }

    }
}
