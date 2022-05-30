package edu.aha.agualimpiafinal.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

import edu.aha.agualimpiafinal.databinding.CardviewComentarioBinding;
import edu.aha.agualimpiafinal.models.Comment;
import edu.aha.agualimpiafinal.providers.UserProvider;
import edu.aha.agualimpiafinal.helper.TextUtilsText;

public class CommentariosAdapter extends RecyclerView.Adapter<CommentariosAdapter.ViewHolder> {

    Context context;
    UserProvider mUserProvider;
    ArrayList<Comment> commentArrayList;


    public CommentariosAdapter( ArrayList<Comment> commentArrayList, Context context) {

        this.context = context;
        this.commentArrayList = commentArrayList;

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Aqui se infla el contenedor del molde donde se cargaran todos los datos
        ///View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post,parent,false);
        CardviewComentarioBinding view = CardviewComentarioBinding.inflate(LayoutInflater.from(parent.getContext()), parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //All code here
        getUserInfoToken(holder,position);

    }




    private void getUserInfoToken(ViewHolder holder, int position) {

        Log.e("EMPEZAR","BUSCAR USER");
        mUserProvider = new UserProvider();

        mUserProvider.searchUser(commentArrayList.get(position).getToken()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {
                    String first = task.getResult().get("author_firstname").toString();
                    String last = task.getResult().get("author_lastname").toString();

                    String fin =TextUtilsText.instance.replaceFirstCharInSequenceToUppercase(first+" "+last);
                    holder.binding.textViewfullname.setText(fin);
                    holder.binding.message.setText(commentArrayList.get(position).getMessage());

                }else
                {
                    Log.e("ERROR","Error al traer los datos de Firebase");
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardviewComentarioBinding binding;

        public ViewHolder(@NonNull CardviewComentarioBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }
}
