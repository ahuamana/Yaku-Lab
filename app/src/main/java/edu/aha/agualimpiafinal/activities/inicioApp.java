package edu.aha.agualimpiafinal.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import edu.aha.agualimpiafinal.R;
import edu.aha.agualimpiafinal.adapters.ViewAdapter;

public class inicioApp extends AppCompatActivity {

    //Variables
    ViewPager viewPager;
    SpringDotsIndicator dot1;
    ViewAdapter viewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_app);

        viewPager= findViewById(R.id.viewPagerxml);
        dot1=findViewById(R.id.dot1);

        viewAdapter = new ViewAdapter(this, viewPager);
        viewPager.setAdapter(viewAdapter);
        dot1.setViewPager(viewPager);
        dot1.setVisibility(View.GONE);

    }
}