package edu.aha.agualimpiafinal.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import edu.aha.agualimpiafinal.R;
import edu.aha.agualimpiafinal.databinding.CardviewFullImageBinding;
import edu.aha.agualimpiafinal.databinding.CardviewInsectosBinding;
import edu.aha.agualimpiafinal.fragments.BottomSheetComentar;
import edu.aha.agualimpiafinal.models.Action;
import edu.aha.agualimpiafinal.models.MoldeSustantivo;
import edu.aha.agualimpiafinal.providers.ActionProvider;
import edu.aha.agualimpiafinal.helper.TextUtilsText;

public class LaboratorioAdapter extends FirestoreRecyclerAdapter<MoldeSustantivo, LaboratorioAdapter.ViewHolder> {

    Context context;
    Action mAction;
    String token;

    ActionProvider mActionProvider;

    BottomSheetComentar mBottomSheetComentar;

    public LaboratorioAdapter(@NonNull FirestoreRecyclerOptions<MoldeSustantivo> options, Context context) {
        super(options);

        this.context = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull MoldeSustantivo model) {

        //asignar variables con firebase

        mActionProvider = new ActionProvider();

        cargarTokenLocalmente();

        setColorLikes(model, holder);

        setUserDetails(model, holder);
        
        getInfoPhoto(model, holder);

        openComentarios(model, holder);


        showImageFullScreen(model, holder);

    }
    private void showImageFullScreen(MoldeSustantivo model, ViewHolder holder)
    {
        holder.binding.roundedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CardviewFullImageBinding customBinding = CardviewFullImageBinding.inflate(LayoutInflater.from(context));

                Dialog dialog = new Dialog(context);
                dialog.setCancelable(true);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(customBinding.getRoot());


                Glide.with(context)
                        .load(model.getUrl())
                        .placeholder(R.drawable.loading_icon)
                        .into(customBinding.roundedImageView);

                dialog.show();


            }
        });

    }

    private void openComentarios(MoldeSustantivo model, ViewHolder holder) {

        holder.binding.linearLayoutComentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openBottomSheetComentar(model, holder);

            }
        });

    }

    private void openBottomSheetComentar(MoldeSustantivo model, ViewHolder holder) {

            if(token != null)
            {
                mBottomSheetComentar = BottomSheetComentar.newInstance(model.getId(),token);
                mBottomSheetComentar.show(((FragmentActivity) context).getSupportFragmentManager(), mBottomSheetComentar.getTag());

            }else {
                Toast.makeText(context, "La informacion no se pudo cargar", Toast.LENGTH_SHORT).show();
            }

    }

    private void getInfoPhoto(MoldeSustantivo model, ViewHolder holder) {

        if(token!= null)
        {
            if(!token.equals(""))
            {
                mActionProvider.getUserLike(token,model.getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(value.size() == 0)
                        {
                            Log.e("SIZE","TAMAÑO 0");

                        }else
                        {
                            Log.e("STATUS",""+value.getDocuments().get(0).get("status"));
                            boolean status = Boolean.parseBoolean(value.getDocuments().get(0).get("status").toString());
                            //Code for each like from user
                                if(status)
                                {
                                    Log.e("STATUS","STATUS TRUE");
                                    holder.binding.imageViewLike.setImageResource(R.drawable.facebook_good_like_icon512);
                                    holder.binding.textViewLike.setTextColor(context.getResources().getColor(R.color.facebook_color_like));
                                }else
                                {
                                    Log.e("STATUS","STATUS FALSE");
                                    holder.binding.imageViewLike.setImageResource(R.drawable.like);
                                    holder.binding.textViewLike.setTextColor(context.getResources().getColor(R.color.colorBlack));
                                }

                        }



                    }
                });

            }
        }

    }

    private void cargarTokenLocalmente() {

        SharedPreferences preferences = context.getSharedPreferences("token", Context.MODE_PRIVATE);
        token= preferences.getString("token","");


    }

    private void setColorLikes(MoldeSustantivo model, ViewHolder holder) {

        holder.binding.linearLayoutLike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mActionProvider.getUserLike(token, model.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if(queryDocumentSnapshots.size() == 0)
                        {
                            //Create like for first time
                            createLike(model, holder);

                        }else
                        {
                            //if already exist
                            boolean status = Boolean.parseBoolean(queryDocumentSnapshots.getDocuments().get(0).get("status").toString());
                            String idToken = queryDocumentSnapshots.getDocuments().get(0).get("id_token").toString();

                            updateLike(idToken, status);


                        }

                    }
                });




            }
        });

    }

    private void updateLike(String idToken, boolean status) {

        mActionProvider.updateStatus(idToken,!status).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    Log.e("STATUS","ACTUALIZADO");
                }

            }
        });
    }

    private void createLike(MoldeSustantivo model, ViewHolder holder) {

        Log.e("COLOR","Cambio de Color Aplicado");

        holder.binding.imageViewLike.setImageResource(R.drawable.facebook_good_like_icon512);
        holder.binding.textViewLike.setTextColor(context.getResources().getColor(R.color.facebook_color_like));

        //Log.e("TOKEN", ""+ token);
        //Log.e("ID", ""+ model.getId());
        String idLike = model.getId()+"_"+token;

        Log.e("idLike", ""+ idLike);

        mAction = new Action();
        mAction.setId_token(idLike);
        mAction.setToken(token);
        mAction.setStatus(true);
        mAction.setId(model.getId());
        mAction.setType("Like");

        if(token != null)
        {
            if(!token.equals(""))
            {


                mActionProvider.create(mAction).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            Log.e("LIKE","CREADO LIKE CORRECTAMENTE");
                        }

                    }
                });
            }
        }

    }


    private void setUserDetails(MoldeSustantivo model, ViewHolder holder) {

        String ape=TextUtilsText.instance.replaceFirstCharInSequenceToUppercase(model.getAuthor_lastname());
        String nam=TextUtilsText.instance.replaceFirstCharInSequenceToUppercase(model.getAuthor_name());

        holder.binding.authorName.setText(nam+" "+ ape);

        holder.binding.name.setText(model.getName());

        Glide.with(context)
                .load(model.getUrl())
                .placeholder(R.drawable.loading_icon)
                .into(holder.binding.roundedImageView);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardviewInsectosBinding view =CardviewInsectosBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);


        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardviewInsectosBinding binding;

        public ViewHolder(@NonNull CardviewInsectosBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }


    }
}
