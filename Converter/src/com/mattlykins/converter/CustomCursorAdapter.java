package com.mattlykins.converter;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class CustomCursorAdapter extends ResourceCursorAdapter{

    public CustomCursorAdapter(Context context, int layout,Cursor cursor) {
        // TODO Auto-generated constructor stub
        super(context, layout, cursor, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        
        NumberFormat formatter = new DecimalFormat("0.0##E0");
        
        TextView tvListFrom = (TextView)view.findViewById(R.id.tvListFrom);
        TextView tvListTo = (TextView)view.findViewById(R.id.tvListTo);
        TextView tvListMultiplyBy= (TextView)view.findViewById(R.id.tvListMultiplyBy);
        
        tvListFrom.setText(cursor.getString(1));
        tvListTo.setText(cursor.getString(2));
        tvListMultiplyBy.setText(String.valueOf(formatter.format(cursor.getDouble(3))));        
    }

}
