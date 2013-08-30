package com.mattlykins.converter;

import java.io.IOException;

import com.mattlykins.converter.dbContract.dBase;
import com.mattlykins.dblibrary.DatabaseHelper;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddUnitsDialog extends Dialog implements android.view.View.OnClickListener {

    //Handle when user try to set two unique columns with the same values
    
    final EditText etAddUnitsSymbol, etAddUnitsName, etAddUnitsType;
    Button bAddUnitsOK, bAddUnitsCancel, bAddUnitsDelete;
    DatabaseHelper dbHelper;

    public AddUnitsDialog(Context context, int ViewId, String title) {
        super(context);
        this.setContentView(ViewId);
        this.setTitle(title); 
        
        dbHelper = new DatabaseHelper(context);
        try {
            dbHelper.createDataBase();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dbHelper.openDataBase();

        etAddUnitsSymbol = (EditText) this.findViewById(R.id.etEditUnitsSymbol);
        etAddUnitsName = (EditText) this.findViewById(R.id.etEditUnitsName);
        etAddUnitsType = (EditText) this.findViewById(R.id.etEditUnitsType);

        bAddUnitsOK = (Button) this.findViewById(R.id.bEditUnitsOK);
        bAddUnitsCancel = (Button) this.findViewById(R.id.bEditUnitsCancel);
        bAddUnitsDelete = (Button) this.findViewById(R.id.bEditUnitsDelete);
        
        bAddUnitsDelete.setVisibility(View.INVISIBLE);

        bAddUnitsOK.setOnClickListener(this);
        bAddUnitsCancel.setOnClickListener(this);
        
        
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bEditUnitsOK:

                final String tempEtSymbol = etAddUnitsSymbol.getText().toString();
                final String tempEtName = etAddUnitsName.getText().toString();
                final String tempEtType = etAddUnitsType.getText().toString();

                String[] colData = new String[] { tempEtSymbol, tempEtName, tempEtType };
                
                dbHelper.Insert(dBase.TN_UNITS, dBase.CN_UNITS, colData);
                break;

            case R.id.bEditUnitsCancel:
                this.dismiss();
                break;
        }

    }
}
