package edu.aha.agualimpiafinal.fragments;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.aha.agualimpiafinal.R;
import edu.aha.agualimpiafinal.activities.RegistrarMuestraMicrobiologicaActivity;
import edu.aha.agualimpiafinal.databinding.RegistrarFragmentBinding;
import edu.aha.agualimpiafinal.viewModels.RegistrarViewModel;

public class RegistrarFragment extends Fragment {

    private RegistrarFragmentBinding binding;

    private RegistrarViewModel mViewModel;

    public static RegistrarFragment newInstance() {
        return new RegistrarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RegistrarFragmentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();


        showActivityRegistroMicrobilogico();



        return view;
    }

    private void showActivityRegistroMicrobilogico() {

        binding.REbtnmicrobiologico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), RegistrarMuestraMicrobiologicaActivity.class);
                startActivity(i);
            }
        });


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RegistrarViewModel.class);
        // TODO: Use the ViewModel
    }

}