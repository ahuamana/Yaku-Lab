package edu.aha.agualimpiafinal.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import edu.aha.agualimpiafinal.adapters.PostAdapter;
import edu.aha.agualimpiafinal.databinding.ActivityComentariosBinding;
import edu.aha.agualimpiafinal.models.MoldeComentarios;
import edu.aha.agualimpiafinal.providers.SugerenciasProvider;

public class ComentariosActivity extends AppCompatActivity {


    private ActivityComentariosBinding binding;

    PostAdapter adapter;
    LinearLayoutManager mLinearLayoutManager;
    SugerenciasProvider mSugerenciasProvider;
    MoldeComentarios mMoldeComentarios;

    //datos de Shared preferences
    String firstname,middlename,lastname, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComentariosBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // all code here
        mSugerenciasProvider = new SugerenciasProvider();
        mMoldeComentarios = new MoldeComentarios();
        mLinearLayoutManager = new LinearLayoutManager(ComentariosActivity.this);


        cargarPreferencias();
        createComment();
        getComentarios();

        goToMainActivity();

    }

    private void goToMainActivity() {

        binding.imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ComentariosActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //eliminar activities anteriores
                startActivity(i);

            }
        });

    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(ComentariosActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //eliminar activities anteriores
        startActivity(i);

    }

    private void getComentarios() {

        //Inicializar Arraylist y asignar contenedor
        binding.SUreclyclerComentarios.setLayoutManager(mLinearLayoutManager);
        mLinearLayoutManager.setStackFromEnd(true);//messages on recycler put over keyboard
        binding.SUreclyclerComentarios.setHasFixedSize(true);

        //traer los datos de la coleccion de firebase

        FirestoreRecyclerOptions<MoldeComentarios> options = new FirestoreRecyclerOptions.Builder<MoldeComentarios>()
                .setQuery(mSugerenciasProvider.getCommentsListOrderByTimeStamp(), MoldeComentarios.class)
                .build();


        //asignar todos lo datos obtenidos al adaptador
        adapter = new PostAdapter(options, ComentariosActivity.this);


        //asignar datos al recyclerView
        binding.SUreclyclerComentarios.setAdapter(adapter);

        //Levar a la ultima posicion del adapter
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                mLinearLayoutManager.setStackFromEnd(true);
                Log.e("TAG-positionStart",String.valueOf(positionStart));
                Log.e("TAG-itemCount",String.valueOf(itemCount));

                //updateStatusMessage();
                //
                int numberItems = adapter.getItemCount();
                Log.e("TAG-NUMBER-OF-ITEMS",String.valueOf(numberItems));
                int lastItemPosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                Log.e("TAG-LASTITEM",String.valueOf(lastItemPosition));

                if(lastItemPosition == -1 || (positionStart >= (numberItems - 1) && lastItemPosition == (positionStart - 1)))
                {
                    Log.e("TAG","ENTRASTE");
                    binding.SUreclyclerComentarios.scrollToPosition(positionStart-1);
                    mLinearLayoutManager.setStackFromEnd(false);
                }

            }
        });

    }

    private void createComment() {

        binding.imageViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(binding.editTextMessage.getText().toString())) {

                    mMoldeComentarios.setAuthorAlias(middlename.toLowerCase());
                    mMoldeComentarios.setAuthorEmail(email.toLowerCase());
                    mMoldeComentarios.setAuthorFirstname(firstname.toLowerCase());
                    mMoldeComentarios.setAuthorLastname(lastname.toLowerCase());
                    mMoldeComentarios.setSugerenciaMensaje(binding.editTextMessage.getText().toString().toLowerCase());
                    mMoldeComentarios.setSugerenciaFechaUnixtime(System.currentTimeMillis()/1000);

                    mSugerenciasProvider.createSuggestion(mMoldeComentarios).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(ComentariosActivity.this, "Comentario registrado, correctamente!", Toast.LENGTH_SHORT).show();

                                //Limpiamos los campos
                                binding.editTextMessage.setText("");


                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(ComentariosActivity.this, "Error al RegistrarFragment un comentario.", Toast.LENGTH_LONG).show();

                        }
                    });

                }else {
                    Toast.makeText(ComentariosActivity.this, "Escribe un comentario!", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


    private void cargarPreferencias() {

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        firstname= preferences.getString("spfirstname","");
        middlename= preferences.getString("spmiddlename","");
        lastname= preferences.getString("splastname","");
        email= preferences.getString("spEmail","");

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}