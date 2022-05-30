package edu.aha.agualimpiafinal.fragments;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;

import edu.aha.agualimpiafinal.R;
import edu.aha.agualimpiafinal.activities.Challenges.AnimalsChallengerActivity;
import edu.aha.agualimpiafinal.activities.Challenges.PlantsChallengerActivity2;
import edu.aha.agualimpiafinal.activities.Challenges.WaterChallengerActivity;
import edu.aha.agualimpiafinal.adapters.LaboratorioAdapter;
import edu.aha.agualimpiafinal.databinding.LaboratorioDigitalFragmentBinding;
import edu.aha.agualimpiafinal.models.MoldeSustantivo;
import edu.aha.agualimpiafinal.providers.InsectosProvider;
import edu.aha.agualimpiafinal.viewModels.LaboratorioDigitalViewModel;

public class LaboratorioDigital extends Fragment {

    private LaboratorioDigitalViewModel mViewModel;
    private LaboratorioDigitalFragmentBinding binding;

    LaboratorioAdapter mAdapter;
    InsectosProvider mInsectosProvider;
    LinearLayoutManager mLinearLayoutManager;

    private Toolbar myToolbar;

    public static LaboratorioDigital newInstance() {
        return new LaboratorioDigital();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = LaboratorioDigitalFragmentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        toolbarPrincipal();

        openAnimalsChallenge();

        openFrutasChallenge();

        openPlantasChallenge();

        openSustanciasChallenge();

        openObjetosChallenge();

        openOtrosChallenge();

        getDataLaboratorio();


        return view;

    }

    private void toolbarPrincipal() {

        myToolbar = getActivity().findViewById(R.id.toolbar);
        myToolbar.setTitle("Desafios");
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        myToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu));
    }

    private void openOtrosChallenge() {

        binding.linearLayoutOtros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), WaterChallengerActivity.class);
                startActivity(i);

            }
        });

    }

    private void openObjetosChallenge() {

        binding.linearLayoutObjetos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDefaultMessage("Pronto Implementaremos esta seccion :)");
            }
        });

    }

    private void openSustanciasChallenge() {

        binding.linearLayoutSustancias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDefaultMessage("Pronto Implementaremos esta seccion :)");

            }
        });

    }

    private void openPlantasChallenge() {

        binding.linearLayoutPlantas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), PlantsChallengerActivity2.class);
                startActivity(i);

            }
        });

    }

    private void openFrutasChallenge() {

        binding.linearLayoutFrutas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDefaultMessage("Pronto Implementaremos esta seccion :)");
            }
        });

    }

    private void showDefaultMessage(String message)
    {
        Snackbar.make(getActivity().findViewById(android.R.id.content),""+message, Snackbar.LENGTH_SHORT).show();

        Base64.decode("micontraseña",0);
    }

    private void getDataLaboratorio() {

        mLinearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerAnimales.setLayoutManager( mLinearLayoutManager);

        mInsectosProvider = new InsectosProvider();

        FirestoreRecyclerOptions<MoldeSustantivo> options = new FirestoreRecyclerOptions.Builder<MoldeSustantivo>()
                .setQuery(mInsectosProvider.getMuestrasListOrderByTimeStamp(),MoldeSustantivo.class)
                .build();

        //enviar los datos al adapter
        //Log.e("TEST",""+ options.getSnapshots().get(0).toString());

        mAdapter=new LaboratorioAdapter(options, getContext());
        //asignar datos al recyclerView
        binding.recyclerAnimales.setAdapter(mAdapter);

    }

    private void openAnimalsChallenge() {


        binding.linearLayoutAnimales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), AnimalsChallengerActivity.class);
                startActivity(i);

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LaboratorioDigitalViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        mAdapter.stopListening();
    }


}