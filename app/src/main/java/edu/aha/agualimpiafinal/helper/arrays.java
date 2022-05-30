package edu.aha.agualimpiafinal.helper;

import android.content.Context;
import android.widget.ArrayAdapter;

public class arrays {

    public ArrayAdapter<String> ObtenerArray(Context contexts, int nuevo){

        Context context=  contexts;
        String [] adapter = context.getResources().getStringArray(nuevo);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_dropdown_item, adapter );

        return spinnerArrayAdapter;

    }
}
