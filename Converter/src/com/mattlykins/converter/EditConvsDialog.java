package com.mattlykins.converter;

import java.io.IOException;

import com.mattlykins.converter.dbContract.dBase;
import com.mattlykins.dblibrary.DatabaseHelper;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditConvsDialog extends Dialog implements android.view.View.OnClickListener {

    // Handle when user try to set two unique columns with the same values

    final EditText etEditConvsFrom, etEditConvsTo, etEditConvsMultiBy, etEditConvsOffset,
            etEditConvsSpecial;
    Button bEditConvsOK, bEditConvsCancel, bEditConvsDelete;
    final int index;
    DatabaseHelper dbHelper;
    CustomCursorAdapter ccAdapter;

    public EditConvsDialog(Context context, int ViewId, String title, Cursor c,
            CustomCursorAdapter adapter) {
        super(context);
        this.setContentView(ViewId);
        this.setTitle(title);

        ccAdapter = adapter;

        dbHelper = new DatabaseHelper(context);
        try {
            dbHelper.createDataBase();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dbHelper.openDataBase();

        etEditConvsFrom = (EditText) this.findViewById(R.id.etEditConvsFrom);
        etEditConvsTo = (EditText) this.findViewById(R.id.etEditConvsTo);
        etEditConvsMultiBy = (EditText) this.findViewById(R.id.etEditConvsMultiBy);
        etEditConvsOffset = (EditText) this.findViewById(R.id.etEditConvsOffset);
        etEditConvsSpecial = (EditText) this.findViewById(R.id.etEditConvsSpecial);

        bEditConvsOK = (Button) this.findViewById(R.id.bEditConvsOK);
        bEditConvsCancel = (Button) this.findViewById(R.id.bEditConvsCancel);
        bEditConvsDelete = (Button) this.findViewById(R.id.bEditConvsDelete);

        bEditConvsOK.setOnClickListener(this);
        bEditConvsCancel.setOnClickListener(this);
        bEditConvsDelete.setOnClickListener(this);

        index = c.getInt(dBase.NDEX_ID);

        etEditConvsFrom.setText(c.getString(dBase.NDEX_JOINED_FROM));
        etEditConvsTo.setText(c.getString(dBase.NDEX_JOINED_TO));
        etEditConvsMultiBy.setText(c.getString(dBase.NDEX_JOINED_MULTI));
        etEditConvsOffset.setText(c.getString(dBase.NDEX_JOINED_OFFSET));
        etEditConvsSpecial.setText(c.getString(dBase.NDEX_JOINED_SPECIAL));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bEditConvsOK:

//                final String tempEtSymbol = etEditConvsFrom.getText().toString();
//                final String tempEtName = etEditConvsTo.getText().toString();
//                final String tempEtType = etEditConvsMultiBy.getText().toString();
//
//                String[] colData = new String[] { tempEtSymbol, tempEtName, tempEtType };
//
//                dbHelper.Update_ByID(dBase.TN_CONVS, index, dBase.CN_Convs, colData);
//
//                ccAdapter.changeCursor(dbHelper.getAllRows(dBase.TN_CONVS));
                this.dismiss();
                break;

            case R.id.bEditConvsCancel:
                this.dismiss();
                break;

            case R.id.bEditConvsDelete:
                dbHelper.Delete_ByID(dBase.TN_CONVS, index);
                ccAdapter.changeCursor(dbHelper.getAllRows(dBase.TN_CONVS));
                this.dismiss();
                break;
        }

    }
}
