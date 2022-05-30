package edu.aha.agualimpiafinal.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import edu.aha.agualimpiafinal.models.MoldeMuestra;

public class MuestrasProvider {

    private CollectionReference mCollection;

    public MuestrasProvider() {

    mCollection = FirebaseFirestore.getInstance().collection("DatosMuestra");

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();

        FirebaseFirestore.getInstance().setFirestoreSettings(settings);

    }

    public CollectionReference getCollectionDatosMuestra() {

        return mCollection;
    }

    public Task<Void> create (MoldeMuestra muestra)
    {
        return mCollection.document().set(muestra);
    }

    public Query getMuestrasListOrderByTimeStamp()
    {
        return mCollection.orderBy("muestraTimeStamp", Query.Direction.ASCENDING);

    }

    public Query getMuestrasListOrderByDepartment(String newText)
    {
        return mCollection.orderBy("muestraDepartamento").startAt(newText.toLowerCase()).limit(25).endAt(newText.toLowerCase()+'\uf8ff');

    }

    public Query getMuestrasListOrderByAuthorAlias(String newText)
    {
        return mCollection.orderBy("authorAlias").startAt(newText.toLowerCase()).limit(25).endAt(newText.toLowerCase()+'\uf8ff');

    }

    public Query getMuestrasListOrderByProvince(String newText)
    {
        return mCollection.orderBy("muestraProvincia").startAt(newText.toLowerCase()).limit(25).endAt(newText.toLowerCase()+'\uf8ff');
    }
}
