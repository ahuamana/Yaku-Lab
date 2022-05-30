package edu.aha.agualimpiafinal.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import edu.aha.agualimpiafinal.R;

public class GaleriaImagenesAdapter extends BaseAdapter {
    //variables
    private Context mContext;
    public List<String> imagenesArray = new ArrayList<String>();


    //constructor
    public GaleriaImagenesAdapter(Context mContext) {

        this.mContext = mContext;
        imagenesArray.add("https://firebasestorage.googleapis.com/v0/b/agualimpia-image.appspot.com/o/1706202118%3A47%3A18.jpg?alt=media&token=1706202118:47:18.jpg");
        imagenesArray.add("https://firebasestorage.googleapis.com/v0/b/agualimpia-image.appspot.com/o/1606202122%3A51%3A28.jpg?alt=media&token=1606202122:51:28.jpg");
        imagenesArray.add("https://firebasestorage.googleapis.com/v0/b/agualimpia-image.appspot.com/o/1606202122%3A51%3A23.jpg?alt=media&token=1606202122:51:23.jpg");
        imagenesArray.add("https://firebasestorage.googleapis.com/v0/b/agualimpia-image.appspot.com/o/1606202122%3A51%3A12.jpg?alt=media&token=1606202122:51:12.jpg");
        imagenesArray.add("https://firebasestorage.googleapis.com/v0/b/agualimpia-image.appspot.com/o/1706202118%3A59%3A33.jpg?alt=media&token=1706202118:59:33.jpg");
        imagenesArray.add("https://firebasestorage.googleapis.com/v0/b/agualimpia-image.appspot.com/o/1606202122%3A51%3A12.jpg?alt=media&token=1606202122:51:12.jpg");
        imagenesArray.add("https://firebasestorage.googleapis.com/v0/b/agualimpia-image.appspot.com/o/1606202122%3A51%3A28.jpg?alt=media&token=1606202122:51:28.jpg");
        imagenesArray.add("https://firebasestorage.googleapis.com/v0/b/agualimpia-image.appspot.com/o/1706202118%3A47%3A18.jpg?alt=media&token=1706202118:47:18.jpg");
        imagenesArray.add("https://www.lavanguardia.com/files/article_main_microformat/files/fp/uploads/2020/08/06/5f2bd32938d7b.r_d.593-707-0.jpeg");
        imagenesArray.add("https://www.avina.net/wp-content/uploads/2020/06/webinar-acceso-al-agua-fundacion-avina.jpg");
        imagenesArray.add("https://www.comexperu.org.pe/upload/images/economia-231017-104654.jpg");

    }

    public GaleriaImagenesAdapter(Context mContext, List<String> imagenesArray) {

        this.mContext = mContext;
        this.imagenesArray = imagenesArray;

    }


    //
    @Override
    public int getCount() {
        return imagenesArray.size();
    }

    @Override
    public Object getItem(int position) {
        return imagenesArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //Codigo para mostrar imagenes
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setAdjustViewBounds(true);
//        imageView.setImageResource(imagenesArray[position]);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setLayoutParams( new ViewGroup.LayoutParams(
//                //ancho y altura de imagenes
//                340,
//                350));
//
//        ///
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Imagen URL: " + imagenesArray.get(position), Toast.LENGTH_SHORT).show();
            }
        });


        Glide.with(mContext)
                .load(imagenesArray.get(position))
                .centerCrop()
                .apply(new RequestOptions().override(340,350))  //set new size from each image
                .placeholder(R.drawable.photography_background)
                .into(imageView);



        return imageView;
    }
}
