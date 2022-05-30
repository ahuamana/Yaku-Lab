package edu.aha.agualimpiafinal.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import edu.aha.agualimpiafinal.R;
import edu.aha.agualimpiafinal.activities.animals.ButterflyChallengeActivity;
import edu.aha.agualimpiafinal.databinding.CustomDialogMoreinfoBinding;

public class InsectosAdapter extends BaseAdapter {

    Context context;
    int[] imageInsectos;
    String[] nameInsectos;
    LayoutInflater inflater;

    public InsectosAdapter(Context context, int[] imageInsectos, String[] nameInsectos)
    {
        this.context = context;
        this.imageInsectos = imageInsectos;
        this.nameInsectos = nameInsectos;

    }

    @Override
    public int getCount() {
        return imageInsectos.length;
    }

    @Override
    public Object getItem(int position) {



        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(inflater == null)
        {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.row_item_insectos, null);
        }

        //Declarar variables del layout
        ImageView imageView = convertView.findViewById(R.id.imageView_row_insectos);
        CardView cardView = convertView.findViewById(R.id.cardView_insectos);
        TextView textViewNameInsectos = convertView.findViewById(R.id.textView_name_insectos);


        ////All code here
        //cardView.setCardBackgroundColor(Color.GREEN);  //Background color for cardview
        imageView.setImageResource(imageInsectos[position]);
        textViewNameInsectos.setText(nameInsectos[position]);

        goToNextActivity(context, position, cardView);

        return convertView;
    }

    private void goToNextActivity(final Context context, int position, CardView cardView) {

        switch (position)
        {
            case 1:
                break;


            case 2:

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Log.e("POSITION","POSITION 3 ");
                        String text = "Al finalizar el reto podras conocer las partes de la mariposa y su importancia, asi que tendrás que buscar una mariposa para poder lograr todos los objetivos";
                        Intent intent = new Intent(context, ButterflyChallengeActivity.class);

                        createDialog(R.drawable.mariposa_icon, text, intent);


                    }
                });

                break;
        }
    }

   private void createDialog(int drawable, String textChallenge, Intent intentReceiver)
   {
       CustomDialogMoreinfoBinding customBinding = CustomDialogMoreinfoBinding.inflate(LayoutInflater.from(context));

       Dialog dialog = new Dialog(context);
       dialog.setCancelable(true);
       dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
       dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
       dialog.setContentView(customBinding.getRoot());

       customBinding.imageViewChallenge.setImageResource(drawable);
       customBinding.textViewChallenge.setText(textChallenge);

       customBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.dismiss();
           }
       });

       customBinding.btnOk.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(context, "Bien hecho", Toast.LENGTH_SHORT).show();

               context.startActivity(intentReceiver);

           }
       });

       dialog.show();

   }


}
