package edu.aha.agualimpiafinal.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.aha.agualimpiafinal.activities.MapsActivity;
import edu.aha.agualimpiafinal.models.MoldeMuestra;
import edu.aha.agualimpiafinal.R;
import edu.aha.agualimpiafinal.helper.RelativeTime;

public class MuestrasAdapter extends FirestoreRecyclerAdapter<MoldeMuestra,MuestrasAdapter.MuestrasHolder> {

    Context context;

    public MuestrasAdapter(@NonNull FirestoreRecyclerOptions<MoldeMuestra> options, Context context) {
        super(options);

        this.context = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull MuestrasHolder holder, int position, @NonNull MoldeMuestra model) {

        Log.e("DATAFIRSTNAME",""+ model.getAuthorFirstname());
        //asignar variables con firebase

        String inputnombrecompleto= model.getAuthorAlias();

        if(model.getAuthorAlias() != null)
        {

            if(model.getAuthorAlias().equals(""))
            {
                holder.txtnombrecompleto.setText("Anónimo");
            }else
            {
                String outputnombrecompleto = inputnombrecompleto.substring(0, 1).toUpperCase() + inputnombrecompleto.substring(1);
                holder.txtnombrecompleto.setText(outputnombrecompleto);
            }

        }else
        {
           holder.txtnombrecompleto.setText("Anónimo");
        }


        //cambiar Hora actual
        //Date df = new java.util.Date(model.getMuestraTimeStamp()*1000);
        //String horaNueva = new SimpleDateFormat("dd MMMM yyyy", new Locale("es","ES")).format(df);

        holder.txttiempo.setText(RelativeTime.getTimeAgo(model.getMuestraTimeStamp(), context)); // Asignar la nueva hora en formato humano xd
        holder.txtResultado.setText(String.valueOf(model.getMuestraResultado()));

        //Asignar color de letra dependiendo el resultado

        if(model.getMuestraResultado().equals("Positivo"))
        {
            holder.txtResultado.setTextColor(Color.RED);
        }else {
                if (model.getMuestraResultado().equals("Negativo")) {
                holder.txtResultado.setTextColor(Color.parseColor("#00bcd4"));
                }
            }

        //Fin de Asignar color de letra dependiendo el resultado


        //set first letter to Uppercase
        String input= model.getMuestraDepartamento();
        String output = input.substring(0, 1).toUpperCase() + input.substring(1);
        holder.txtDepartamento.setText(output);

        String input1= model.getMuestraProvincia();
        String output1 = input1.substring(0, 1).toUpperCase() + input1.substring(1);
        holder.txtProvincia.setText(output1);



        Glide.with(holder.foto.getContext())
                .load(model.getMuestraFotoPATH())
                .placeholder(R.drawable.loading_icon)
                .into(holder.foto);

       goToMaps(model, holder);

    }

    private void goToMaps(MoldeMuestra model, MuestrasHolder holder) {

        holder.googlemaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Log.e("latitude","ENVIANDO:"+ model.getMuestraLatitud());
                //Log.e("longitud","ENVIANDO:"+ model.getMuestraLongitud());

                String time = RelativeTime.getTimeAgo(model.getMuestraTimeStamp(), context);

                Intent i = new Intent(context, MapsActivity.class);
                i.putExtra("latitud",String.valueOf(model.getMuestraLatitud()));
                i.putExtra("longitud",String.valueOf(model.getMuestraLongitud()));
                i.putExtra("timeago",time);
                context.startActivity(i);

            }
        });
    }

    @NonNull
    @Override
    public MuestrasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_muestra,parent,false);

        return new MuestrasHolder(vista);
    }


    public class MuestrasHolder extends RecyclerView.ViewHolder {

        //crear variables para hacer la refenrencia al molde xml
        TextView txtnombrecompleto, txtcantidadmuestra, txttiempo, txtResultado, txtDepartamento, txtProvincia;
        String   txtidmuestra;
        CircleImageView foto;
        Button googlemaps;

        public MuestrasHolder(@NonNull View vista) {
            super(vista);

            txtnombrecompleto= vista.findViewById(R.id.MMtxtauthor);
            //txtcantidadmuestra=vista.findViewById(R.id.MMtxtMuestra);
            txttiempo=vista.findViewById(R.id.MMtxtTiempo);
            txtResultado=vista.findViewById(R.id.MMtxtResultado);
            txtDepartamento= vista.findViewById(R.id.MMtxtDepartamento);
            txtProvincia= vista.findViewById(R.id.MMtxtProvincia);
            foto=vista.findViewById(R.id.MMivImagen);
            googlemaps = vista.findViewById(R.id.google_button);


        }


    }
}
