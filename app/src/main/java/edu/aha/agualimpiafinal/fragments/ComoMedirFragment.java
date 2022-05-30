package edu.aha.agualimpiafinal.fragments;

import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

import edu.aha.agualimpiafinal.R;
import edu.aha.agualimpiafinal.viewModels.ComomedirViewModel;

public class ComoMedirFragment extends Fragment {

    private ComomedirViewModel mViewModel;

    ImageView imgHealthMetric,imgWaterSafe;
    Button btnHealthMetric, btnWaterSafe, btnHealthMetricText, btnWaterSafeText;
    Dialog dialog;
    String videoId = "";

    public static ComoMedirFragment newInstance() {
        return new ComoMedirFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View vista=inflater.inflate(R.layout.comomedir_fragment, container, false);

        imgHealthMetric = vista.findViewById(R.id.CMivHealtMetric);
        imgWaterSafe = vista.findViewById(R.id.CMivWaterSafe);
        btnHealthMetric= vista.findViewById(R.id.CMbtnHealthMetric_masinfo_video);
        btnWaterSafe=vista.findViewById(R.id.CMbtnWaterSafe_masinfo_video);

        //Dialog Text
        btnHealthMetricText= vista.findViewById(R.id.CMbtnHealthMetric_masinfo_texto);
        btnWaterSafeText = vista.findViewById(R.id.CMbtnWaterSafe_masinfo_texto);

        //Inicializar el dialogo con el contexto
        final Context context= vista.getContext();
        dialog=new Dialog(context);

        //cerra listener de dialog






        //Inicio al hacer click el buttom WaterSafe
        btnWaterSafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoId="rT-l8VnHx_Q";
                //Log.e("HIzoclick",videoId);
                OpenDialog(videoId,"Water Safe");
            }
        });
        //

        //Inicio al hacer click el buttom HeatlMetric
        btnHealthMetric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoId="_4VTtEJMjHc";
                //Log.e("HIzoclick",videoId);
                OpenDialog(videoId,"Health Metric");
            }
        });
        //

        btnHealthMetricText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.setContentView(R.layout.custom_text_dialog_healtmetric);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                dialog.setCanceledOnTouchOutside(true);

            }
        });

        btnWaterSafeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.setContentView(R.layout.custom_text_dialog_watersafe);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                dialog.setCanceledOnTouchOutside(true);

            }
        });



        Glide.with(this)
                .load("https://images-na.ssl-images-amazon.com/images/I/81B8ZtKR%2BXL._AC_SY355_.jpg")
                .placeholder(R.mipmap.ic_launcher)
                .into(imgWaterSafe);

        Glide.with(this)
                .load("https://images-na.ssl-images-amazon.com/images/I/71dvCVFZ71L._AC_SY355_.jpg")
                .placeholder(R.mipmap.ic_launcher)
                .into(imgHealthMetric);

        return vista;
    }

    private void OpenDialog(final String videoID, String titulo) {
        Log.e("DialogoconelID",videoID);
        dialog.setContentView(R.layout.custom_video_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //Declarar variables del dialogo que se utilizan
        TextView DItittle = dialog.findViewById(R.id.DItittle);
        DItittle.setText(titulo);
        final YouTubePlayerView video = dialog.findViewById(R.id.DIvideoYoutube);

        getLifecycle().addObserver(video);

        video.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NotNull YouTubePlayer youTubePlayer) {

                youTubePlayer.loadVideo(videoID,0);
            }
        });

        dialog.show();
        dialog.setCanceledOnTouchOutside(true);

    }





    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ComomedirViewModel.class);
        // TODO: Use the ViewModel
    }

}