package edu.aha.agualimpiafinal.helper;


import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Converters extends AppCompatActivity {

    public static Converters instance = new Converters();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        instance = new Converters();
    }

    public String epochTimeToDate(Long DateToConvert)
    {
        long dateEpoch = DateToConvert*1000;// its need to be in milisecond
        Date dateFormat = new java.util.Date(dateEpoch);
        String dateFinal = new SimpleDateFormat("dd-MM-yyyy").format(dateFormat);

        return dateFinal;
    }


    public long currentUnixTime ()
    {
        return System.currentTimeMillis() / 1000L;
    }
}
