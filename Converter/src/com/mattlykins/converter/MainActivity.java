package com.mattlykins.converter;

import com.mattlykins.converter.dbContract.dBase;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

    String chosenTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_view_db:
                //chooseTableDialog(CONSTANT.EDIT);
                final Intent intent = new Intent(this, ViewDB.class);
               // intent.putExtra(CONSTANT.WHICH_TABLE, chosenTable);
                startActivity(intent);
                break;

            case R.id.menu_add_db:
                chooseTableDialog(CONSTANT.ADD);
                AddUnitsDialog aud = new AddUnitsDialog(this, R.layout.edit_units, "Add Units");
                aud.show();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void choseTableUnits() {
        chosenTable = dBase.TN_UNITS;
    }

    private void choseTableConvs() {
        chosenTable = dBase.TN_CONVS;
    }

    private void chooseTableDialog(final String title) {

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(title);
        b.setItems(dBase.TN_TABLES, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        choseTableUnits();
                        break;
                    case 1:
                        choseTableConvs();
                        break;

                }
            }
        });
        b.show();
    }

}
