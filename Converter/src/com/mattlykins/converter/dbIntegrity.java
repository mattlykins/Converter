package com.mattlykins.converter;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;

import com.mattlykins.converter.dbContract.dBase;
import com.mattlykins.dblibrary.DatabaseHelper;

public class dbIntegrity {
	
	DatabaseHelper dbHelper;
    Context myContext;

	public dbIntegrity(Context context){
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
		
	}
	
	public void checkIntegrity(){
		Cursor cFrom = dbHelper.Query(true, dBase.TN_CONVS, new String[]{dBase.CN_CONVS_FROM},null,null);
		LogCursor lc = new LogCursor();
		lc.LogConvs(cFrom);
		
		Cursor cTo = dbHelper.Query(true, dBase.TN_CONVS, new String[]{dBase.CN_CONVS_TO},null,null);
		lc.LogConvs(cTo);
	}

}
