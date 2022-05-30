package edu.aha.agualimpiafinal.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.AuthProvider;

import edu.aha.agualimpiafinal.modulos.login.model.User;
import edu.aha.agualimpiafinal.providers.UserProvider;

public class LoginActivityViewModel extends ViewModel {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private MutableLiveData<String> _message = new MutableLiveData<>();

    private MutableLiveData<Boolean> _isLoginEmail = new MutableLiveData<>();

    private MutableLiveData<Boolean> _isLoginAnonymous = new MutableLiveData<>();

    private MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();

    public LiveData<Boolean> getIsLoading ()
    {
        return _isLoading;
    }

    public LiveData<String> showMessage()
    {
        return _message;
    }

    public LiveData<Boolean> getIsLoginEmail()
    {
        return _isLoginEmail;
    }

    public LiveData<Boolean> getIsLoginAnonymous()
    {
        return _isLoginAnonymous;
    }

    public LiveData<String> isAlreadyLogging()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            _message.setValue("Ya tienes un inicio de session");
        } else {
            // No user is signed in
            _message.setValue("No haz ingresado");
        }

        return _message;

    }

    public void loginWithEmail(String email, String pass)
    {
        _isLoading.setValue(true);

        try{
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    _message.setValue("Bienvenido");
                    _isLoginEmail.setValue(true);
                    _isLoading.setValue(false);
                }else
                {
                    _message.setValue("Usuario y/o contraseña incorrectos");
                    _isLoginEmail.setValue(false);
                    _isLoading.setValue(false);
                }

            }
        });

        }catch (Exception e)
        {
            _message.setValue(e.getMessage());
        }


    }

    public void loginAnonymous()
    {
        _isLoading.setValue(true);

        try {

            mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        _message.setValue("Bienvenido anónimo");
                        try{
                            Thread.sleep(2000);
                        }catch (Exception e) {Log.e("TAG","Error esperando");}

                        _isLoginAnonymous.setValue(true);
                        _isLoading.setValue(false);

                    } else {
                        _message.setValue("No es posible ingresar. Porfavor contacta con soporte");
                        _isLoginAnonymous.setValue(false);
                        _isLoading.setValue(false);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    _message.setValue(""+e.getMessage());
                }
            });



        }catch (Exception e)
        {
            Log.e("VM_LOGIN","Error:"+e.getMessage());
        }

    }

}
