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
    final int convIndex;
    String convFrom, convTo;
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

        convIndex = c.getInt(dBase.NDEX_ID);
        convFrom = c.getString(1);
        convTo = c.getString(2);

        etEditConvsFrom.setText(c.getString(1));
        etEditConvsTo.setText(c.getString(2));
        etEditConvsMultiBy.setText(c.getString(3));
        etEditConvsOffset.setText(c.getString(4));
        etEditConvsSpecial.setText(c.getString(5));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bEditConvsOK:

                final String[] tempEtFromSymbol = { etEditConvsFrom.getText().toString() };
                final String[] tempEtToSymbol = { etEditConvsTo.getText().toString() };
                final String tempEtMultiBy = etEditConvsMultiBy.getText().toString();
                final String tempEtOffset = etEditConvsOffset.getText().toString();
                final String tempEtSpecial = etEditConvsSpecial.getText().toString();

                String[] colData = new String[] { convFrom, convTo, tempEtMultiBy, tempEtOffset,
                        tempEtSpecial };

                dbHelper.Update_ByID(dBase.TN_CONVS, convIndex, dBase.CN_CONVS, colData);

                ccAdapter.changeCursor(dbHelper.getAllRows(dBase.TN_CONVS));

                this.dismiss();
                break;

            case R.id.bEditConvsCancel:
                this.dismiss();
                break;

            case R.id.bEditConvsDelete:
                dbHelper.Delete_ByID(dBase.TN_CONVS, convIndex);
                ccAdapter.changeCursor(dbHelper.sqlQuery(dBase.QUERYCONVS));
                this.dismiss();
                break;
        }

    }
}
