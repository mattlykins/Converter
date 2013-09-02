package com.mattlykins.converter;

import java.io.IOException;

import com.mattlykins.converter.dbContract.dBase;
import com.mattlykins.dblibrary.DatabaseHelper;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddConvsDialog extends Dialog implements android.view.View.OnClickListener {

    // Handle when user try to set two unique columns with the same values

    final EditText etEditConvsFrom, etEditConvsTo, etEditConvsMultiBy, etEditConvsOffset,
            etEditConvsSpecial;
    Button bAddConvsOK, bAddConvsCancel, bAddConvsDelete;
    DatabaseHelper dbHelper;
    Context myContext;

    public AddConvsDialog(Context context, int ViewId, String title) {
        super(context);
        this.setContentView(ViewId);
        this.setTitle(title);

        myContext = context;

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

        bAddConvsOK = (Button) this.findViewById(R.id.bEditConvsOK);
        bAddConvsCancel = (Button) this.findViewById(R.id.bEditConvsCancel);
        bAddConvsDelete = (Button) this.findViewById(R.id.bEditConvsDelete);

        //bAddConvsDelete.setVisibility(View.INVISIBLE);

        bAddConvsOK.setText("Add");
        bAddConvsDelete.setText("Clear");

        bAddConvsOK.setOnClickListener(this);
        bAddConvsCancel.setOnClickListener(this);
        bAddConvsDelete.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bEditConvsOK:

                // Verify Symbol, Name and Type before adding
                if (etEditConvsFrom.getText().toString().isEmpty()) {
                    Toast toast = Toast.makeText(myContext, "You must enter a From Symbol.",
                            Toast.LENGTH_LONG);
                    toast.show();
                }
                else if (etEditConvsTo.getText().toString().isEmpty()) {
                    Toast toast = Toast.makeText(myContext, "You must enter a To Symbol.",
                            Toast.LENGTH_LONG);
                    toast.show();
                }
                else if (etEditConvsMultiBy.getText().toString().isEmpty()) {
                    Toast toast = Toast.makeText(myContext, "You must enter a MultiplyBy.",
                            Toast.LENGTH_LONG);
                    toast.show();
                }
                else {

                    final String tempEtFromSymbol = etEditConvsFrom.getText().toString();
                    final String tempEtToSymbol = etEditConvsTo.getText().toString();
                    final String tempEtMultiBy = etEditConvsMultiBy.getText().toString();
                    final String tempEtOffset = etEditConvsOffset.getText().toString();
                    final String tempEtSpecial = etEditConvsSpecial.getText().toString();

                    // test whether a conversion already exists
                    final String selection = "FROMUNIT=? AND TOUNIT=?";
                    Cursor c = dbHelper.Query(true, dBase.TN_CONVS, null, selection, new String[] {
                            tempEtFromSymbol, tempEtToSymbol });

                    LogCursor lc = new LogCursor();
                    lc.LogConvs(c);

                    c.moveToFirst();

                    if (c.getString(dBase.NDEX_CONVS_FROM).equals(tempEtFromSymbol)
                            && c.getString(dBase.NDEX_CONVS_TO).equals(tempEtToSymbol)) {

                        final String message = String.format(
                                "The conversion from %s to %s already exists in the database",
                                tempEtFromSymbol, tempEtToSymbol);
                        @SuppressWarnings("unused")
                        PopUp P = new PopUp(myContext, "Duplicate Conversion", message);

                    }
                    else {

                        String[] colData = new String[] { tempEtFromSymbol, tempEtToSymbol,
                                tempEtMultiBy, tempEtOffset, tempEtSpecial };

                        try {
                            dbHelper.Insert(dBase.TN_CONVS, dBase.CN_CONVS, colData);
                        }
                        catch (SQLException sqlex) {
                            Toast toast = Toast.makeText(myContext, sqlex.getMessage(),
                                    Toast.LENGTH_LONG);
                            toast.show();
                            break;
                        }

                        Toast t = Toast.makeText(myContext, "Conversion was added to the database",
                                Toast.LENGTH_SHORT);
                        t.show();

                        clearEts();

                    }
                }

                // this.dismiss();
                break;

            case R.id.bEditConvsCancel:
                this.dismiss();
                break;
                
            case R.id.bEditConvsDelete:
                clearEts();
                break;
        }

    }

    private void clearEts() {
        // Clear edittexts for next entry
        etEditConvsFrom.setText("");
        etEditConvsTo.setText("");
        etEditConvsMultiBy.setText("");
        etEditConvsOffset.setText("");
        etEditConvsSpecial.setText("");
    }
}
