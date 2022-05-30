package edu.aha.agualimpiafinal.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

import edu.aha.agualimpiafinal.models.MoldeComentarios;

public class SugerenciasProvider {

    private CollectionReference mCollection;

    public SugerenciasProvider() {
        mCollection = FirebaseFirestore.getInstance().collection("DataComentarios");
    }


    public Query getCommentsListOrderByTimeStamp()
    {
        return mCollection.orderBy("sugerenciaFechaUnixtime", Query.Direction.ASCENDING);
    }

    public Task<Void> createSuggestion(MoldeComentarios comentarios)
    {
        //Creamos un MapFragment con objetos strings y que no haya valores duplicados y lo guardamos todos los datos en el MapFragment
        Map<String, Object> sugerenciaData = new HashMap<>();
        sugerenciaData.put("sugerenciaFechaUnixtime",System.currentTimeMillis()/1000);
        sugerenciaData.put("sugerenciaMensaje",comentarios.getSugerenciaMensaje());
        sugerenciaData.put("authorFirstname",comentarios.getAuthorFirstname());
        sugerenciaData.put("authorLastname",comentarios.getAuthorLastname());
        sugerenciaData.put("authorAlias",comentarios.getAuthorAlias());
        sugerenciaData.put("authorEmail",comentarios.getAuthorEmail());

        return mCollection.document().set(sugerenciaData);
    }

}
