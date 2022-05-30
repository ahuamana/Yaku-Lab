package edu.aha.agualimpiafinal.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import edu.aha.agualimpiafinal.models.Comment;

public class CommentProvider {

    CollectionReference mCollection;

    public CommentProvider ()
    {
        mCollection = FirebaseFirestore.getInstance().collection("Comment");
    }

    public Task<Void> create (Comment comment)
    {
        DocumentReference document = mCollection.document();
        comment.setId(document.getId());
        return document.set(comment);
    }

    public Query getCommentsByIdPhoto(String idphoto)
    {
        return mCollection
                .whereEqualTo("id_photo",idphoto)
                .orderBy("timestamp", Query.Direction.ASCENDING);
    }

}
