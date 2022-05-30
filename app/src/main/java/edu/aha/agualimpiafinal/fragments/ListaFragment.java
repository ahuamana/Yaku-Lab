package edu.aha.agualimpiafinal.fragments;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;


import edu.aha.agualimpiafinal.databinding.ListaFragmentBinding;
import edu.aha.agualimpiafinal.models.MoldeMuestra;
import edu.aha.agualimpiafinal.adapters.MuestrasAdapter;
import edu.aha.agualimpiafinal.providers.MuestrasProvider;
import edu.aha.agualimpiafinal.viewModels.ListaViewModel;

public class ListaFragment extends Fragment {


    MuestrasAdapter mAdapter;
    MuestrasProvider mMuestrasProvider;
    LinearLayoutManager mLinearLayoutManager;
    private ListaFragmentBinding binding;
    private ListaViewModel mViewModel;

    public static ListaFragment newInstance() {
        return new ListaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ListaFragmentBinding.inflate(getLayoutInflater());
        View vista = binding.getRoot();

        //Instanciar variables
        mMuestrasProvider = new MuestrasProvider();

        searchOnRecycler();

        showFirstTimeRecyclerView();

        return vista;
    }

    private void showFirstTimeRecyclerView() {

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setStackFromEnd(true);//messages on recycler put over keyboard
        binding.idRecycler.setLayoutManager( mLinearLayoutManager);

        binding.idRecycler.setHasFixedSize(true);


        //crear referencia a Firebase
        //Craer un builder de firebase del children

        FirestoreRecyclerOptions<MoldeMuestra> options = new FirestoreRecyclerOptions.Builder<MoldeMuestra>()
                .setQuery(mMuestrasProvider.getMuestrasListOrderByTimeStamp(),MoldeMuestra.class)
                .build();

        //enviar los datos al adapter
        mAdapter =new MuestrasAdapter(options, getContext());
        //asignar datos al recyclerView
        binding.idRecycler.setAdapter(mAdapter);



        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                mLinearLayoutManager.setStackFromEnd(true);
                Log.e("TAG-positionStart",String.valueOf(positionStart));
                Log.e("TAG-itemCount",String.valueOf(itemCount));

                //updateStatusMessage();
                //
                int numberItems = mAdapter.getItemCount();
                Log.e("TAG-NUMBER-OF-ITEMS",String.valueOf(numberItems));
                int lastItemPosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                Log.e("TAG-LASTITEM",String.valueOf(lastItemPosition));

                if(lastItemPosition == -1 || (positionStart >= (numberItems - 1) && lastItemPosition == (positionStart - 1)))
                {
                    Log.e("TAG","ENTRASTE");
                    binding.idRecycler.scrollToPosition(positionStart-1);
                    mLinearLayoutManager.setStackFromEnd(false);
                }

            }
        });

    }

    private void searchOnRecycler() {

        binding.searchItem.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                updateSearchData(binding.searchItem.getText().toLowerCase());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void updateSearchData(String toLowerCase) {

        mMuestrasProvider.getMuestrasListOrderByDepartment(toLowerCase).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(queryDocumentSnapshots.size() > 0)
                {
                    buscarDepartamentoOnFirestorage(toLowerCase);
                }else
                {
                    buscarProvinciaOnFirestorage(toLowerCase);
                }

            }
        });

        //buscarAuthorAliasOnFirestorage(toLowerCase);

    }

    private void buscarDepartamentoOnFirestorage(String newText) {

        mAdapter =null;
        Log.e("mensajebusqueda: ",newText.toLowerCase());
        FirestoreRecyclerOptions <MoldeMuestra> newoptions = new FirestoreRecyclerOptions.Builder<MoldeMuestra>()
                .setQuery(mMuestrasProvider.getMuestrasListOrderByDepartment(newText), MoldeMuestra.class)
                .build();

        //enviar los datos al adapter
        mAdapter =new MuestrasAdapter(newoptions, getContext());


        //asignar datos al recyclerView
        binding.idRecycler.setAdapter(mAdapter);

        mAdapter.startListening();//que escuche en timepo real los cambios

        mAdapter.notifyDataSetChanged();




    }

    private void buscarAuthorAliasOnFirestorage(String newText) {

        //codigo
        mAdapter =null;
        Log.e("mensajebusqueda: ",newText.toLowerCase());

                FirestoreRecyclerOptions <MoldeMuestra> newoptions = new FirestoreRecyclerOptions.Builder<MoldeMuestra>()
                .setQuery(mMuestrasProvider.getMuestrasListOrderByAuthorAlias(newText),MoldeMuestra.class)
                .build();

        //enviar los datos al adapter
        mAdapter =new MuestrasAdapter(newoptions, getContext());
        mAdapter.startListening();

        //asignar datos al recyclerView
        binding.idRecycler.setAdapter(mAdapter);
        ////
    }

    private void buscarProvinciaOnFirestorage(String newText) {
        //codigo implementar
        //Log.e("data cambiado", "cambiando a provincia");

        mAdapter =null;
        Log.e("mensajebusqueda: ",newText.toLowerCase());

        FirestoreRecyclerOptions <MoldeMuestra> newoptions = new FirestoreRecyclerOptions.Builder<MoldeMuestra>()
                .setQuery(mMuestrasProvider.getMuestrasListOrderByProvince(newText),MoldeMuestra.class)
                .build();

        //enviar los datos al adapter
        mAdapter =new MuestrasAdapter(newoptions, getContext());
        mAdapter.startListening();

        //asignar datos al recyclerView
        binding.idRecycler.setAdapter(mAdapter);
        ////
    }


    @Override
    public void onStart() {
        super.onStart();

        if(mAdapter != null)
        {
            mAdapter.startListening();
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        if(mAdapter != null)
        {
            mAdapter.stopListening();
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ListaViewModel.class);
        // TODO: Use the ViewModel
    }









}