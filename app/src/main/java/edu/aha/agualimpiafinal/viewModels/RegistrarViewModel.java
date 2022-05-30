package edu.aha.agualimpiafinal.viewModels;

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

import edu.aha.agualimpiafinal.modulos.login.model.User2;
import edu.aha.agualimpiafinal.providers.UserProvider2;

public class RegistrarViewModel extends ViewModel {


    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    UserProvider2 mUser = new UserProvider2();

    private MutableLiveData<String> _message = new MutableLiveData<>();

    private MutableLiveData<FirebaseUser> _user = new MutableLiveData<>();

    private MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();

    private MutableLiveData<Boolean> _isSavedFirebase = new MutableLiveData<>();

    public LiveData<Boolean> getIsSavedFirebase()
    {
        return _isSavedFirebase;
    }

    public LiveData<Boolean> getIsLoading ()
    {
        return _isLoading;
    }

    public LiveData<String> showMessage()
    {
        return _message;
    }

    public LiveData<FirebaseUser> getUser()
    {
        return _user;
    }

    public void createUser(String email, String pass)
    {
        _isLoading.setValue(true);

        try {

            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                user = mAuth.getCurrentUser();
                                _user.setValue(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                _message.setValue("Ah ocurrido un error al intentar crear un usuario nuevo");
                                _isLoading.setValue(false);
                            }


                        }
                    }
            ).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    _message.setValue(e.getMessage());
                    _isLoading.setValue(false);
                }
            });

        }catch (Exception e)
        {
            _message.setValue(e.getMessage());
        }
    }

    public void saveFirebaseUser(User2 usernew)
    {
        try{
        mUser.create(usernew).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    _isLoading.setValue(false);
                    _isSavedFirebase.setValue(true);
                }else
                {
                    _isLoading.setValue(false);
                    _isSavedFirebase.setValue(false);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                _isLoading.setValue(false);
                _message.setValue(e.getMessage());
                _isSavedFirebase.setValue(false);
            }
        });

        }catch (Exception e)
        {
            _isLoading.setValue(false);
            _message.setValue(e.getMessage());
            _isSavedFirebase.setValue(false);
        }


    }

}