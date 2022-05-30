package edu.aha.agualimpiafinal.modulos.login.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import edu.aha.agualimpiafinal.R;
import edu.aha.agualimpiafinal.activities.inicioApp;
import edu.aha.agualimpiafinal.databinding.ActivityWelcomeBinding;

public class WelcomeActivity extends AppCompatActivity {

    private ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);
        setStatusBarColor();
        setOnClickListeners();

    }

    private void setOnClickListeners() {


        binding.btnIniciarFirstSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToInicio();

            }
        });

        binding.txtsaltarFirstSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Guardar Preferencias
                guardarPreferencias();
                //Ir a la siguiente actividad
                goToLogin();

            }
        });
    }

    private void goToLogin() {
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goToInicio()
    {
        Intent intent = new Intent(WelcomeActivity.this, inicioApp.class);
        startActivity(intent);
    }

    private void setStatusBarColor()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            getWindow().setStatusBarColor(getResources().getColor(R.color.greeDark, this.getTheme()));
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                getWindow().setStatusBarColor(getResources().getColor(R.color.greeLight));
            }
        }
    }

    private void guardarPreferencias(){

        SharedPreferences preferences = getSharedPreferences("SaltarBienvenida", Context.MODE_PRIVATE);
        boolean bypass = true;

        //editor permite editar y almacenar las variables
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("bypass",bypass);

        editor.commit();

    }

    private boolean cargarPreferencias() {

        SharedPreferences preferences = getSharedPreferences("SaltarBienvenida", Context.MODE_PRIVATE);

        boolean bypass= preferences.getBoolean("bypass",false);

       return bypass;

    }
}