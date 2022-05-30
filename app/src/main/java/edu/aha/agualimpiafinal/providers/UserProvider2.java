package edu.aha.agualimpiafinal.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import edu.aha.agualimpiafinal.modulos.login.model.User;
import edu.aha.agualimpiafinal.modulos.login.model.User2;

public class UserProvider2 {

    private CollectionReference mCollection;

    public UserProvider2 ()
    {
        mCollection = FirebaseFirestore.getInstance().collection("Users");

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();

        FirebaseFirestore.getInstance().setFirestoreSettings(settings);
    }

    public Task<Void> create(User2 user2)
    {
        return mCollection.document(user2.getAuthor_email()).set(user2);
    }

    public Task<DocumentSnapshot> searchUserByEmail(String email)
    {
        return mCollection.document(email).get();
    }
}
