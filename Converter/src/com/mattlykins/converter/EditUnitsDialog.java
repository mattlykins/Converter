package com.mattlykins.converter;

import com.mattlykins.converter.dbContract.dBase;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditUnitsDialog extends Dialog implements android.view.View.OnClickListener {

    final EditText etEditUnitsSymbol, etEditUnitsName, etEditUnitsType;
    Button bEditUnitsOK, bEditUnitsCancel, bEditUnitsDelete;

    public EditUnitsDialog(Context context, int ViewId, String title, Cursor c) {
        super(context);
        this.setContentView(ViewId);
        this.setTitle(title);

        etEditUnitsSymbol = (EditText) this.findViewById(R.id.etEditUnitsSymbol);
        etEditUnitsName = (EditText) this.findViewById(R.id.etEditUnitsName);
        etEditUnitsType = (EditText) this.findViewById(R.id.etEditUnitsType);

        bEditUnitsOK = (Button) this.findViewById(R.id.bEditUnitsOK);
        bEditUnitsCancel = (Button) this.findViewById(R.id.bEditUnitsCancel);
        bEditUnitsDelete = (Button) this.findViewById(R.id.bEditUnitsDelete);

        bEditUnitsOK.setOnClickListener(this);
        bEditUnitsCancel.setOnClickListener(this);
        bEditUnitsDelete.setOnClickListener(this);

        final int index = c.getInt(dBase.NDEX_ID);

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

                mydbHelper.Update_ByID(dBase.TN_UNITS, index, dBase.CN_UNITS, colData);

                scAdapter.changeCursor(mydbHelper.getAllRows(dBase.TN_UNITS));
                d.dismiss();
                break;

            case R.id.bEditUnitsCancel:
                break;

            case R.id.bEditUnitsDelete:
                break;
        }

    }
}
