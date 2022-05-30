package edu.aha.agualimpiafinal.activities.Challenges;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import edu.aha.agualimpiafinal.R;
import edu.aha.agualimpiafinal.activities.macroinvertebrados.AnacroneuriaActivity;
import edu.aha.agualimpiafinal.databinding.ActivityWaterChallengerBinding;
import edu.aha.agualimpiafinal.databinding.CustomDialogMoreinfoBinding;

public class WaterChallengerActivity extends AppCompatActivity {

    private ActivityWaterChallengerBinding binding;

    String challenge_naucoridae = "https://firebasestorage.googleapis.com/v0/b/agualimpiafinal.appspot.com/o/LaboratorioDigital%2FWaterChallenge%2FMacroinvertebrados%2FNaucoridae%2Fmacroinvertebrado_naucoridae.png?alt=media&token=d7074b74-c9ba-4475-a906-cbf88555d5b2";
    String challenge_naucoridae_name="Naucoridae";

    String challenge_anacroneuria = "https://firebasestorage.googleapis.com/v0/b/agualimpiafinal.appspot.com/o/LaboratorioDigital%2FWaterChallenge%2FMacroinvertebrados%2Fanacroneuria%2Fmacroinvertebrado_anacroneuria.png?alt=media&token=6b79efb9-d83a-4432-9ab6-1271d07efae0";
    String challenge_anacroneuria_name="anacroneuria";

    String challenge_lymnaeidae = "https://firebasestorage.googleapis.com/v0/b/agualimpiafinal.appspot.com/o/LaboratorioDigital%2FWaterChallenge%2FMacroinvertebrados%2Flymnaeidae%2Fmacroinvertebrado_caracoldeagua.png?alt=media&token=482f1725-df48-4d85-ad98-fa3e656eee0e";
    String challenge_lymnaeidae_name="Caracol de agua";

    String challenge_tension_superficial="https://firebasestorage.googleapis.com/v0/b/agualimpiafinal.appspot.com/o/LaboratorioDigital%2FWaterChallenge%2FExperimentos%20con%20agua%2FTensionSuperficial%2Ftensionsuperficial.png?alt=media&token=aaee92aa-b01f-4da3-9806-cb87f83854ef";
    String challenge_difucion_agua="https://firebasestorage.googleapis.com/v0/b/agualimpiafinal.appspot.com/o/LaboratorioDigital%2FWaterChallenge%2FExperimentos%20con%20agua%2FDifusionAgua%2Fdifusion_de_agua.jpg?alt=media&token=eb9d41d0-8eb5-4058-8107-c183ea2bb072";
    String challenge_lampara_lava="https://firebasestorage.googleapis.com/v0/b/agualimpiafinal.appspot.com/o/LaboratorioDigital%2FWaterChallenge%2FExperimentos%20con%20agua%2FLamparaLava%2Flampara_lava.jpg?alt=media&token=d7d01dfa-769c-4f87-884c-054b7d4b0eaa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWaterChallengerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setPrimaryData();

        openDialogs();



    }

    private void openDialogs() {

        binding.cardview1.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WaterChallengerActivity.this, AnacroneuriaActivity.class);
                createDialog(R.drawable.macroinvertebrado_anacroneuria,R.string.text_anacroneuria,intent,challenge_anacroneuria_name);
            }
        });

        binding.cardview2.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WaterChallengerActivity.this, AnacroneuriaActivity.class);
                createDialog(R.drawable.macroinvertebrado_naucoridae,R.string.text_anacroneuria,intent,challenge_naucoridae_name);
            }
        });

        binding.cardview3.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WaterChallengerActivity.this, AnacroneuriaActivity.class);
                createDialog(R.drawable.macroinvertebrado_caracoldeagua,R.string.text_anacroneuria,intent,challenge_lymnaeidae_name);
            }
        });
    }

    private void setPrimaryData() {

        binding.imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        experiemtsWithMicroinvertebrados();

        experimentsWithWater();

    }

    private void experiemtsWithMicroinvertebrados() {

        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.fitCenter();

        binding.cardview1.name.setText(challenge_anacroneuria_name);
        Glide.with(this)
                .load(challenge_anacroneuria)
                .apply(options)
                .placeholder(R.drawable.loading_icon)
                .into(binding.cardview1.roundedImageView);

        binding.cardview2.name.setText(challenge_naucoridae_name);
        Glide.with(this)
                .load(challenge_naucoridae)
                .apply(options)
                .placeholder(R.drawable.loading_icon)
                .into(binding.cardview2.roundedImageView);

        binding.cardview3.name.setText(challenge_lymnaeidae_name);
        Glide.with(this)
                .load(challenge_lymnaeidae)
                .apply(options)
                .placeholder(R.drawable.loading_icon)
                .into(binding.cardview3.roundedImageView);

    }

    private void experimentsWithWater() {

        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.fitCenter();

        binding.cardviewExperimento1.name.setText("Tension superficial");
        Glide.with(this)
                .load(challenge_tension_superficial)
                .apply(options)
                .placeholder(R.drawable.loading_icon)
                .into(binding.cardviewExperimento1.roundedImageView);

        binding.cardviewExperimento2.name.setText("Difusion");
        Glide.with(this)
                .load(challenge_difucion_agua)
                .apply(options)
                .placeholder(R.drawable.loading_icon)
                .into(binding.cardviewExperimento2.roundedImageView);

        binding.cardviewExperimento3.name.setText("Lampara lava");
        Glide.with(this)
                .load(challenge_lampara_lava)
                .apply(options)
                .placeholder(R.drawable.loading_icon)
                .into(binding.cardviewExperimento3.roundedImageView);

        binding.cardviewExperimento4.name.setText("Fantasma espumante");
        binding.cardviewExperimento4.roundedImageView.setImageDrawable( getDrawable(R.drawable.fantasmas_espumosos) );

    }

    private void createDialog(int drawable, int textChallenge, Intent intentReceiver, String title)
    {
        CustomDialogMoreinfoBinding customBinding = CustomDialogMoreinfoBinding.inflate(LayoutInflater.from(this));

        Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(customBinding.getRoot());

        customBinding.textViewTittle.setText(title);
        customBinding.imageViewChallenge.setImageResource(drawable);
        customBinding.textViewChallenge.setText(textChallenge);

        CardView.LayoutParams params = new CardView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(30,0,30,0);
        customBinding.cardParent.setLayoutParams(params);

        customBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        customBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WaterChallengerActivity.this, "Bien hecho", Toast.LENGTH_SHORT).show();
                startActivity(intentReceiver);

            }
        });

        dialog.show();

    }
}