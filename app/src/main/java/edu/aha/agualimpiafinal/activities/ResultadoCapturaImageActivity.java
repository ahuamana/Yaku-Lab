package edu.aha.agualimpiafinal.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;

import edu.aha.agualimpiafinal.R;
import edu.aha.agualimpiafinal.databinding.ActivityResultadoCapturaImageBinding;
import edu.aha.agualimpiafinal.modulos.puntaje.views.PointsActivity;

public class ResultadoCapturaImageActivity extends AppCompatActivity {

    private ActivityResultadoCapturaImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultadoCapturaImageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getDataLastIntent();

        SetOnclickListeners();

        goToPoints();


    }

    private void getDataLastIntent() {

        if(getIntent().getExtras() != null)
        {
            int points = getIntent().getIntExtra("points",100);
            int descripcion = getIntent().getIntExtra("descripcion",100);
            String imageurl = getIntent().getStringExtra("imageurl");
            String title = getIntent().getStringExtra("title");

            Log.e("imageurl","imageurl: "+ imageurl);


            binding.points.setText(String.valueOf(points));
            binding.textViewDescriptionSustantivo.setText(descripcion);
            binding.title.setText(title);

            Glide.with(this)
                    .load(imageurl)
                    .placeholder(R.drawable.loading_icon)
                    .into(binding.roundedImageViewSustantivo);

        }else
        {
            Log.e("TAG","Data from intent not received");
        }

    }

    private void goToPoints() {

        binding.fabPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ResultadoCapturaImageActivity.this, PointsActivity.class);
                startActivity(i);

            }
        });

    }

    private void SetOnclickListeners() {

        binding.backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.fabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ResultadoCapturaImageActivity.this, MainActivity.class);
                startActivity(i);

            }
        });

    }
}