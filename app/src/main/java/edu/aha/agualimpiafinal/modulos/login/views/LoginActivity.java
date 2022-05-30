package edu.aha.agualimpiafinal.modulos.login.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;

import edu.aha.agualimpiafinal.R;
import edu.aha.agualimpiafinal.activities.MainActivity;
import edu.aha.agualimpiafinal.databinding.ActivityLoginBinding;
import edu.aha.agualimpiafinal.viewModels.LoginActivityViewModel;
import edu.aha.agualimpiafinal.helper.TextUtilsText;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    boolean isValidEmail= false,isValidPass=false;
    LoginActivityViewModel viewmodel;
    Activity thisActivity;

    AppCompatButton btnAnonimo;
    MaterialButton btnLoginEmail;
    MaterialTextView lblRegister;
    String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewmodel = new ViewModelProvider(this).get(LoginActivityViewModel.class);

        //Vincular vistas
        btnAnonimo  = binding.continuarAnonimoButton;
        btnLoginEmail  = binding.ingresarLoginButton;
        lblRegister  = binding.btnRegister;
        thisActivity = LoginActivity.this;


        //Validate Data
        validateFields();

        //Login Firebase
        loginFirebase();
        registerNewAccount(); //Register

        //Observables with MVVM
        showObservables();

    }

    private void registerNewAccount() {

        lblRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(thisActivity,RegisterActivity.class));
            }
        });

    }

    private void showObservables() {

        viewmodel.showMessage().observe(this, message -> {
            if(message!= null)
            {
                _showMessageMainThread(message);
            }
        });

        viewmodel.getIsLoginAnonymous().observe(this, isLoginAnonymous-> {
            if(isLoginAnonymous)
            {
                startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }

        });

        //Login with email
        viewmodel.getIsLoginEmail().observe(this,isLoginEmail ->{
            if(isLoginEmail)
            {
                Log.e(TAG,"EMAIL ENVIADO: "+binding.email.getText().toString().toLowerCase() );

                startActivity(new Intent(this, MainActivity.class)
                        .putExtra("email",binding.email.getText().toString().toLowerCase())
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });

        viewmodel.getIsLoading().observe(this, isLoading -> {

            Log.e("ISLOADING", "ISLOADING:"+isLoading);

            if(isLoading)
            {
                binding.cortinaLayout.setVisibility(View.VISIBLE);
            }else
            {
                binding.cortinaLayout.setVisibility(View.GONE);
            }

        });

    }


    private void loginFirebase() {

        btnLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextUtilsText.hideKeyboard(LoginActivity.this);

                if(TextUtilsText.isConnected(getApplicationContext()))
                {
                    viewmodel.loginWithEmail(binding.email.getText().toString().trim(), binding.pass.getText().toString().trim());
                }
                else _showMessageMainThread("Sin conexion a internet");
            }
        });

        btnAnonimo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtilsText.isConnected(getApplicationContext()))
                {
                    viewmodel.loginAnonymous();
                }
                else _showMessageMainThread("Sin conexion a internet");
            }
        });
    }

    private void validateFields() {

        binding.email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) validEmail(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() > 5)
                {
                    binding.passLayout.setError(null);
                    isValidPass = true;
                }
                else
                    {
                        binding.passLayout.setError("La contraseña debe tener minimo 6 caracteres");
                        isValidPass = false;
                    }

                validEmailPass();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void validEmail(CharSequence s) {

        if(TextUtilsText.isValidEmail(s))
        {
            binding.emailLayout.setError(null);
            isValidEmail = true;
        }
        else
            {
            binding.emailLayout.setError("Correo electrónico invalido");
            isValidEmail=false;
            }

        validEmailPass();
    }

    private void validEmailPass()
    {
        if(isValidEmail&&isValidPass)
        {
            binding.ingresarLoginButton.setEnabled(true);
            binding.ingresarLoginButton.setBackgroundTintMode(PorterDuff.Mode.SCREEN);
            binding.ingresarLoginButton.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.greenPrimary));
            binding.ingresarLoginButton.setTextColor(ContextCompat.getColor(this,R.color.white));

        }else
        {
            binding.ingresarLoginButton.setEnabled(false);
            binding.ingresarLoginButton.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
            binding.ingresarLoginButton.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.color_input_text));
            binding.ingresarLoginButton.setTextColor(ContextCompat.getColor(this,R.color.color_input_text));
        }
    }

    private void _showMessageMainThread(String message)
    {
       Snackbar.make(findViewById(android.R.id.content),""+message, Snackbar.LENGTH_SHORT).show();
    }


    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.

    }
}