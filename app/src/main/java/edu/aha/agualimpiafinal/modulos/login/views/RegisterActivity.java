package edu.aha.agualimpiafinal.modulos.login.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import edu.aha.agualimpiafinal.R;
import edu.aha.agualimpiafinal.activities.MainActivity;
import edu.aha.agualimpiafinal.databinding.ActivityRegisterBinding;
import edu.aha.agualimpiafinal.helper.TextUtilsText;
import edu.aha.agualimpiafinal.helper.validaciones;
import edu.aha.agualimpiafinal.modulos.login.model.User2;
import edu.aha.agualimpiafinal.viewModels.LoginActivityViewModel;
import edu.aha.agualimpiafinal.viewModels.RegistrarViewModel;

public class RegisterActivity extends AppCompatActivity {

    validaciones rules= new validaciones();
    ActivityRegisterBinding binding;
    RegistrarViewModel _viewmodel;

    TextInputEditText fullname , lastname, alias, email, pass;
    TextInputLayout layoutFullname, layoutLastname, layoutAlias,layoutEmail, layoutPass;

    Boolean isValidFullname = false, isValidlastname = false, isValidAlias = false, isValidEmail = false, isValidPass = false;
    MaterialButton sign_up_btn;

    User2 userNew = new User2();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        _viewmodel = new ViewModelProvider(this).get(RegistrarViewModel.class);

        //
        fullname = binding.fullname;
        lastname = binding.lastname;
        alias       = binding.alias;
        email       = binding.email;
        pass        = binding.password;

        layoutFullname      = binding.layoutFullname;
        layoutAlias         = binding.layoutAlias;
        layoutLastname      = binding.layoutLastname;
        layoutEmail         = binding.layoutEmail;
        layoutPass          = binding.layoutPass;

        sign_up_btn         = binding.signUp;

        signUp();

        TextUtilsText.putKeywordOverLayout(getWindow());
        TextUtilsText.hideKeyboard(this);

        validateFields();

        observers();

    }

    private void observers() {

        _viewmodel.showMessage().observe(this, message-> {

            if(message!= null)
            {
                _showMessageMainThread(message);
            }

        });

        _viewmodel.getUser().observe(this, user->{


            if(userNew.getAuthor_email().equals(user.getEmail()))
            {
                _saveOnFirebase(userNew);
            }else
            {
                _showMessageMainThread(user.getEmail());
            }

        });

        _viewmodel.getIsLoading().observe(this, isLoading ->{

            Log.e("ISLOADING", "ISLOADING:"+isLoading);
            if(isLoading)
            {
                binding.cortinaLayout.setVisibility(View.VISIBLE);
            }else
            {
                binding.cortinaLayout.setVisibility(View.GONE);
            }
        });

        _viewmodel.getIsSavedFirebase().observe(this, isSavedFirebase ->{
            if(isSavedFirebase)
            {
                goToPrincipal();
            }else
            {
                _showMessageMainThread("isSavedFirebase:"+isSavedFirebase.toString());
            }
        });
    }

    private void _saveOnFirebase(User2 user2)
    {
        _viewmodel.saveFirebaseUser(user2);
    }


    private void _showMessageMainThread(String message)
    {
        Snackbar.make(findViewById(android.R.id.content),""+message, Snackbar.LENGTH_SHORT).show();
    }

    private void isAllValid() {

        if(isValidAlias
                &&isValidPass
                &&isValidEmail
                &&isValidFullname
                &&isValidlastname)
        {
            sign_up_btn.setEnabled(true);
            sign_up_btn.setBackgroundTintMode(PorterDuff.Mode.SCREEN);
            sign_up_btn.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.greenPrimary));
            sign_up_btn.setTextColor(ContextCompat.getColor(this,R.color.white));

        }else
        {
            sign_up_btn.setEnabled(false);
            sign_up_btn.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
            sign_up_btn.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.color_input_text));
            sign_up_btn.setTextColor(ContextCompat.getColor(this,R.color.color_input_text));
        }

    }

    private void validateFields() {

        fullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 2)
                {
                    layoutFullname.setError(null);
                    isValidFullname = true;
                }
                else
                {
                    layoutFullname.setError("El campo debe tener minimo 3 caracteres");
                    isValidPass = false;
                }

                isAllValid();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 2)
                {
                    layoutLastname.setError(null);
                    isValidlastname = true;
                }
                else
                {
                    layoutLastname.setError("El campo debe tener minimo 3 caracteres");
                    isValidlastname = false;
                }
                isAllValid();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        alias.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 2)
                {
                    layoutAlias.setError(null);
                    isValidAlias = true;
                    //LLamar al metodo principal
                }
                else
                {
                    layoutAlias.setError("El campo debe tener minimo 3 caracteres");
                    isValidAlias = false;

                }
                isAllValid();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtilsText.isValidEmail(s))
                {
                    layoutEmail.setError(null);
                    isValidEmail = true;
                }
                else
                {
                    layoutEmail.setError("Correo electrónico invalido");
                    isValidEmail=false;
                }
                isAllValid();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 5)
                {
                    layoutPass.setError(null);
                    isValidPass = true;
                }
                else
                {
                    layoutPass.setError("La contraseña debe tener minimo 6 caracteres");
                    isValidPass = false;

                }
                isAllValid();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void signUp() {

        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userNew.setAuthor_apellidos(lastname.getText().toString().trim());
                userNew.setAuthor_email(email.getText().toString().trim());
                userNew.setAuthor_nombres(fullname.getText().toString().trim());
                userNew.setPoints(0);
                userNew.setAuthor_alias(alias.getText().toString().trim());
                _viewmodel.createUser(email.getText().toString().trim(), pass.getText().toString().trim());

            }
        });

    }

    private void goToPrincipal() {

    //Guardar las preferencias
    //guardarPreferencias();
    Intent i = new Intent(getApplicationContext(), MainActivity.class).putExtra("email",email.getText().toString().trim());
    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //eliminar activities anteriores
    startActivity(i);
        
    }



    private void guardarPreferencias(){

        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String splastname =  binding.lastname.getText().toString();
        String spEmail=  binding.email.getText().toString();

        //editor permite editar y almacenar las variables
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("splastname",splastname);
        editor.putString("spEmail",spEmail);

        //asignar datos de los campos a las variables para almacenarlos
        binding.lastname.setText(splastname);
        binding.email.setText(spEmail);
        editor.commit();

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }







}