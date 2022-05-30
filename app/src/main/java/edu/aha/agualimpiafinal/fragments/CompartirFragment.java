package edu.aha.agualimpiafinal.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import edu.aha.agualimpiafinal.R;
import edu.aha.agualimpiafinal.viewModels.CompartirViewModel;

public class CompartirFragment extends Fragment {

    private CompartirViewModel mViewModel;

    ImageButton imgLink;
    TextView text;

    public static CompartirFragment newInstance() {
        return new CompartirFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.compartir_fragment, container, false);

        text= vista.findViewById(R.id.COMPtvcompartir);
        imgLink = vista.findViewById(R.id.COMPbtnlink);

        imgLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                copyDataPortapeles();

            }
        });


        return vista;
    }

    private void copyDataPortapeles() {

        //Codigo para copiar al portapeles el link
        String texto = text.getText().toString();
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("",texto);
        clipboard.setPrimaryClip(clip);

        //Mostrar mensaje para mostrar al usuario
        Toast.makeText(getActivity(), "Copiado! en portapapeles", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CompartirViewModel.class);
        // TODO: Use the ViewModel
    }

}