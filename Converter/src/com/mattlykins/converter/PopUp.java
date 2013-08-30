package com.mattlykins.converter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class PopUp {

    
    public PopUp(Context context, String title, String message) {
     
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setCancelable(false);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // here you can add functions
                dialog.cancel();
            }
        });

        adb.setTitle(title);       
        adb.setMessage(message);
        AlertDialog ad = adb.create();
        ad.show();
    }

}
