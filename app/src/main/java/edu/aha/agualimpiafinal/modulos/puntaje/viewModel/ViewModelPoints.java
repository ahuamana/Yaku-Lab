package edu.aha.agualimpiafinal.modulos.puntaje.viewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;

import edu.aha.agualimpiafinal.helper.TextUtilsText;
import edu.aha.agualimpiafinal.modulos.login.model.User;
import edu.aha.agualimpiafinal.modulos.login.model.User2;
import edu.aha.agualimpiafinal.providers.UserProvider2;

public class ViewModelPoints extends ViewModel {

    UserProvider2 mUserProvider = new UserProvider2();
    String TAG = "ViewModelPoints";


    private MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();

    private MutableLiveData<String> _userRemote  = new MutableLiveData<>();

    public LiveData<String> getUserRemote ()
    {
        return _userRemote;
    }

    public LiveData<Boolean> getIsLoading ()
    {
        return _isLoading;
    }

    public void getPointsByUser(String email)
    {
        _isLoading.setValue(true);

        try{
            Log.e(TAG,"Buscando puntos de "+ email);

            mUserProvider.searchUserByEmail(email).addOnCompleteListener(task -> {

                if(task.isSuccessful())
                {
                    Log.e(TAG,""+task.getResult().toObject(User2.class));
                    User2 user = task.getResult().toObject(User2.class);
                    String userJson = new Gson().toJson(user);
                    //Asignar Nombres y Primera letra del apellido del usuario
                    _userRemote.setValue(userJson);

                }else
                {
                    Log.e(TAG,"EXCEPTION LOOKING FOR:"+ task.getException().getMessage());
                    _userRemote.setValue(null);
                }

                _isLoading.setValue(false);

            });


        }catch (Exception e)
        {
            //_message.setValue(e.getMessage());
            Log.e(TAG,"EXCEPTION: "+ e.getMessage());
        }


    }

}
