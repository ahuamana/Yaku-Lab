package edu.aha.agualimpiafinal.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.messaging.FirebaseMessaging;

import edu.aha.agualimpiafinal.modulos.login.model.User;

public class UserProvider {

    private CollectionReference mCollection;

    public UserProvider()
    {
        mCollection = FirebaseFirestore.getInstance().collection("Users");

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();

        FirebaseFirestore.getInstance().setFirestoreSettings(settings);
    }

    public Task<String> createToken ()
    {
        return FirebaseMessaging.getInstance().getToken();
    }

    public Task<Void> create(User user)
    {
        return mCollection.document(user.getToken()).set(user);
    }

    public Task<DocumentSnapshot> searchUser(String token)
    {
        return mCollection.document(token).get();
    }

    public Task<DocumentSnapshot> searchUserByEmail(String email)
    {
        return mCollection.document(email).get();
    }

    public Task<Void> updatePoints(String token, int points)
    {
        return mCollection.document(token).update("points",points);
    }

}
