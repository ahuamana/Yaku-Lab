package edu.aha.agualimpiafinal.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.aha.agualimpiafinal.models.MoldeMuestra;
import edu.aha.agualimpiafinal.R;
import edu.aha.agualimpiafinal.adapters.GaleriaImagenesAdapter;
import edu.aha.agualimpiafinal.models.MoldeRasberryPhotos;
import edu.aha.agualimpiafinal.providers.MuestrasProvider;
import edu.aha.agualimpiafinal.viewModels.RasberryImagesViewModel;

public class RasberryImagesFragment extends Fragment {

    private RasberryImagesViewModel mViewModel;

    GridView gridViewImagenes;
    List<MoldeMuestra> listatotalDataURLS= new ArrayList<>();
    List<String> listaURLs = new ArrayList<>();

    //Cloud Firestore
    MuestrasProvider mMuestrasProvider;
    MoldeRasberryPhotos mRasberryPhotos;

    ListenerRegistration mListener;

    GaleriaImagenesAdapter mGaleriaImagenesAdapter;

    public static RasberryImagesFragment newInstance() {
        return new RasberryImagesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.rasberry_images_fragment, container, false);

        gridViewImagenes = vista.findViewById(R.id.grid_imagenes_rasberry);

        //Inicializar firestore
        mMuestrasProvider = new MuestrasProvider();
        mRasberryPhotos = new MoldeRasberryPhotos();


        mListener = mMuestrasProvider.getCollectionDatosMuestra().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                listarFotos(value); // listar fotos en tiempo real

            }
        });


        //codigo





        return vista;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mListener != null)
        {
            mListener.remove();
        }

    }

    private void listarFotos(QuerySnapshot value) {

        if (value != null)
        {

            if(!value.equals(""))
            {
                //Asignar Datos de Firestore al molde
                //mRasberryPhotos = value.toObjects(MoldeRasberryPhotos.class);
                List<String> imagenes = new ArrayList<>();


                //Obtener datos del molde y rellenar al array con strings
                for(QueryDocumentSnapshot doc : value)
                {
                    if (doc.get("image") != null) {
                        imagenes.add(doc.getString("name"));
                    }
                }

                mRasberryPhotos.setImage(imagenes); //asignar toda la collecion

                //mGaleriaImagenesAdapter = new GaleriaImagenesAdapter(getContext(), mRasberryPhotos.getImage());
                mGaleriaImagenesAdapter = new GaleriaImagenesAdapter(getContext());//

                gridViewImagenes.setAdapter(mGaleriaImagenesAdapter); // asignar adapatador con el contexto

            }else {Toast.makeText(getContext(), "Los campos de la referencia estan vacias", Toast.LENGTH_SHORT).show();}

        }else {Toast.makeText(getContext(), "Los campos de la referencia son nulos", Toast.LENGTH_SHORT).show();}



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RasberryImagesViewModel.class);
        // TODO: Use the ViewModel
    }

}