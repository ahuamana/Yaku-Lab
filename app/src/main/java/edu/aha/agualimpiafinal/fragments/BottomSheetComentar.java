package edu.aha.agualimpiafinal.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import edu.aha.agualimpiafinal.R;
import edu.aha.agualimpiafinal.adapters.CommentariosAdapter;
import edu.aha.agualimpiafinal.databinding.BottomSheetComentarBinding;
import edu.aha.agualimpiafinal.models.Action;
import edu.aha.agualimpiafinal.models.Comment;
import edu.aha.agualimpiafinal.providers.ActionProvider;
import edu.aha.agualimpiafinal.providers.CommentProvider;


public class BottomSheetComentar extends BottomSheetDialogFragment {

    private BottomSheetComentarBinding binding;

    private CommentProvider mCommentProvider;
    private Comment mComment;

    private String id_photo, token;

    CommentariosAdapter mAdapter;
    LinearLayoutManager mLinearLayoutManager;

    ListenerRegistration mListener;
    ListenerRegistration mListenerLikes;

    ActionProvider mActionProvider;
    Action mAction;

    public BottomSheetComentar() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupRatio(bottomSheetDialog);
            }
        });
        return  dialog;
    }


    private void setupRatio(BottomSheetDialog bottomSheetDialog) {
        //id = com.google.android.material.R.id.design_bottom_sheet for Material Components
        //id = android.support.design.R.id.design_bottom_sheet for support librares
        FrameLayout bottomSheet = (FrameLayout)
                bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
        layoutParams.height = getBottomSheetDialogDefaultHeight();
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    private int getBottomSheetDialogDefaultHeight() {
        return getWindowHeight() * 85 / 100;
    }
    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static BottomSheetComentar newInstance(String id_photo, String token) {
        BottomSheetComentar fragment = new BottomSheetComentar();
        Bundle args = new Bundle();
        args.putString("id", id_photo);
        args.putString("token", token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            id_photo = getArguments().getString("id");
            token = getArguments().getString("token");

            Log.e("Token", ""+ token);
            Log.e("id", ""+ id_photo);

        }

        putKeywordOverLayout();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    binding = BottomSheetComentarBinding.inflate(getLayoutInflater());
    View view = binding.getRoot();

        mCommentProvider = new CommentProvider();
        mActionProvider = new ActionProvider();

        validateComment();

        getCommentariosFromPost();

        //get like From user
        getLikeFromUser();

        setLikePhoto();

    return view;
    }

    private void setLikePhoto() {

        binding.imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mActionProvider.getUserLike(token, id_photo).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if(queryDocumentSnapshots.size() == 0)
                        {
                            //Create like for first time
                            createLike();

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

    private void createLike() {

        mAction = new Action();
        mAction.setId_token(id_photo+"_"+token);
        mAction.setToken(token);
        mAction.setStatus(true);
        mAction.setId(id_photo);
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

    private void getLikeFromUser() {



        mListenerLikes = mActionProvider.getUserLike(token,id_photo).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.size() == 0)
                {
                    //Create like for first time

                }else
                {
                    //if already exist set color like
                    boolean status = Boolean.parseBoolean(value.getDocuments().get(0).get("status").toString());

                    if(status)
                    {
                        binding.imageViewLike.setImageResource(R.drawable.facebook_good_like_icon512);
                    }else
                    {
                        Log.e("STATUS","STATUS FALSE");
                        binding.imageViewLike.setImageResource(R.drawable.like);
                    }
                }

            }
        });

    }

    private void getCommentariosFromPost() {

        mLinearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        binding.recyclerViewComentarios.setLayoutManager( mLinearLayoutManager);

        ArrayList<Comment> statusList = new ArrayList<>();

        mListener = mCommentProvider.getCommentsByIdPhoto(id_photo).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                Log.e("SIZE BOTTOM","SIZE: "+ value.size());

                if(value.size() > 0)
                {
                    binding.linearLayoutSinComentarios.setVisibility(View.GONE);

                    for(DocumentSnapshot d : value.getDocuments())
                    {
                        Comment c = d.toObject(Comment.class);
                        statusList.add(c);
                    }

                }else
                {
                    Log.e("TAMAÑO","SIZE: "+ value.size());
                }

                //enviar los datos al adapter
                mAdapter=new CommentariosAdapter(statusList, getContext());
                //asignar datos al recyclerView
                binding.recyclerViewComentarios.setAdapter(mAdapter);

                //Show recycler view
                binding.recyclerViewComentarios.setVisibility(View.VISIBLE);


            }
        });






    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private void validateComment() {

       binding.fabSend.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               String texto = binding.editTextMessage.getText().toString();
               Log.e("TEXTO",""+texto);

               if(!texto.equals(""))
               {
                   createComment();
               }


           }
       });

    }

    private void createComment() {

        mComment = new Comment();
        mComment.setId_photo(id_photo);
        mComment.setStatus(true);
        mComment.setToken(token);
        mComment.setType("comment");
        mComment.setTimestamp(new Date().getTime());
        mComment.setMessage(binding.editTextMessage.getText().toString());

        mCommentProvider.create(mComment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    Log.e("Comment","Message Added");
                    binding.editTextMessage.setText("");
                }else
                {
                    Log.e("Comment","ERROR CREANDO COMENTARIO");
                }

            }
        });


    }


    private void putKeywordOverLayout() {
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
       getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mListener != null)
        {
            mListener.remove();
        }

        if(mListenerLikes !=null)
        {
            mListenerLikes.remove();
        }

    }
}