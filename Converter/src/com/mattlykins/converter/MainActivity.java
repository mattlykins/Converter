package com.mattlykins.converter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

    String chosenTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        dbIntegrity db = new dbIntegrity(this);
        db.createInverses();
        db.checkIntegrity();
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
                final Intent intent = new Intent(this, ViewDB.class);
                startActivity(intent);
                break;

            case R.id.menu_add_units:
                AddUnitsDialog aud = new AddUnitsDialog(this, R.layout.edit_units, "Add Units");
                aud.show();
                break;

            case R.id.menu_add_convs:
                AddConvsDialog acd = new AddConvsDialog(this, R.layout.edit_convs,
                        "Add Conversions");
                acd.show();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
