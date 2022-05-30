package edu.aha.agualimpiafinal.modulos.puntaje.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;

import edu.aha.agualimpiafinal.R;
import edu.aha.agualimpiafinal.databinding.ActivityPointsBinding;
import edu.aha.agualimpiafinal.modulos.login.model.User2;
import edu.aha.agualimpiafinal.modulos.puntaje.viewModel.ViewModelPoints;
import edu.aha.agualimpiafinal.providers.UserProvider;
import edu.aha.agualimpiafinal.viewModels.LoginActivityViewModel;

public class PointsActivity extends AppCompatActivity {

    private ActivityPointsBinding binding;
    Toolbar myToolbar;

    //ViewModel
    ViewModelPoints viewmodel;

    String TAG = "PointsActivity";
    String email_extra = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPointsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if(getIntent().getExtras() != null)
        {
           email_extra = getIntent().getStringExtra("email");
           Log.e(TAG,"Email Recibido extra: "+email_extra);
        }

        //Vincular vistas
        myToolbar = binding.toolbarActivity;

        setToolbar();
        observerComponent();


        viewmodel.getPointsByUser(email_extra);
    }

    private void observerComponent() {

        viewmodel = new ViewModelProvider(this).get(ViewModelPoints.class);

        viewmodel.getIsLoading().observe(this, isLoading -> {

            Log.e(TAG, "ISLOADING: "+isLoading);

            if(isLoading)
            {
                Log.e(TAG, "mostrando skeleton");
                binding.contentSkeletom.setVisibility(View.VISIBLE);
                binding.contentMain.setVisibility(View.GONE);
            }else
            {
                Log.e(TAG, "ocultando skeleton");
                binding.contentSkeletom.setVisibility(View.GONE);
                binding.contentMain.setVisibility(View.VISIBLE);
            }

        });

        viewmodel.getUserRemote().observe(this, userJson -> {

            Log.e(TAG, "usuario data: "+userJson);

            if(!userJson.isEmpty() || userJson != null)
            {
                //Asignar Puntaje
               User2 user = new Gson().fromJson(userJson, User2.class);

               binding.textViewPoints.setText(String.valueOf(user.getPoints()));
               setMedalla(user.getPoints());

            }else
            {
                //Asignar Puntaje Error - Usuario Anonimo

            }

        });

    }

    private void setMedalla(int points) {

        //Asignar Medalla
        if(points < 10)
        {
            binding.imageViewMedalPoints.setImageResource(R.drawable.bronze_medal);
        }else
        {
            if(points>10 && points< 50)
            {
                binding.imageViewMedalPoints.setImageResource(R.drawable.silver_medal);
            }else
            {
                if(points>50)
                {
                    binding.imageViewMedalPoints.setImageResource(R.drawable.gold_medal);

                }
            }
        }
    }




    private void setToolbar() {
        setSupportActionBar(myToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.toolbarActivity.setNavigationIcon(getDrawable(R.drawable.ic_arrow_back));
        binding.toolbarActivity.setTitleTextColor(getColor(R.color.white));
        binding.toolbarActivity.setTitle("Puntos");
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG","CLICKED ON BACK");
                onBackPressed();
            }
        });
    }

}