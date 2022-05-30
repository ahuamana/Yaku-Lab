package edu.aha.agualimpiafinal.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import edu.aha.agualimpiafinal.R;
import edu.aha.agualimpiafinal.modulos.login.views.LoginActivity;
import edu.aha.agualimpiafinal.modulos.login.views.RegisterActivity;

public class ViewAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private Integer[] images = {  /* Agregar los recursos aqui full imagenes */};
    private ViewPager viewPager;

    public ViewAdapter(Context context, ViewPager viewPager){
        this.context=context;
        this.viewPager=viewPager;

    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item,container,false);

        //Declarar todos los elementos a utilizar del XML
        TextView txtnumber = view.findViewById(R.id.txtnumber);
        TextView txtdescription = view.findViewById(R.id.txtdescription);
        Button btnempezar = view.findViewById(R.id.btnempezar);
        ImageView logo = view.findViewById(R.id.VPimgLogo);
        TextView txtcircle1 = view.findViewById(R.id.txtslider1);
        TextView txtcircle2 = view.findViewById(R.id.txtslider2);
        TextView txtcircle3 = view.findViewById(R.id.txtslider3);
        TextView txtsaltar = view.findViewById(R.id.txtsaltar);



        //InicioFragment del evento click saltar
        txtsaltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        //fin del evento click


        switch (position)
        {

            case 0:
            {
                Glide.with(context)
                        .load(R.drawable.pick_flaticon)
                        //.circleCrop()
                        .into(logo);

                txtdescription.setText("Elige un reto");
                txtnumber.setText("1");
                txtcircle1.setBackgroundResource(R.drawable.circle_selected);
                txtcircle2.setBackgroundResource(R.drawable.circle_unselected);
                txtcircle3.setBackgroundResource(R.drawable.circle_unselected);
                btnempezar.setText("SIGUIENTE");
                btnempezar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(1);
                    }
                });

                break;

            }
            case 1:
            {
                Glide.with(context)
                        .load(R.drawable.puzzle_freepick)
                        //.circleCrop()
                        .into(logo);

                txtdescription.setText("Completa el reto");
                txtnumber.setText("2");
                txtcircle1.setBackgroundResource(R.drawable.circle_unselected);
                txtcircle2.setBackgroundResource(R.drawable.circle_selected);
                txtcircle3.setBackgroundResource(R.drawable.circle_unselected);
                btnempezar.setText("SIGUIENTE");
                btnempezar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(2);
                    }
                });

                break;

            }

            case 2:
            {
                Glide.with(context)
                        .load(R.drawable.trophy_flaticon)
                        //.circleCrop()
                        .into(logo);

                txtdescription.setText("Gana trofeos y premios");
                txtnumber.setText("3");
                txtcircle1.setBackgroundResource(R.drawable.circle_unselected);
                txtcircle2.setBackgroundResource(R.drawable.circle_unselected);
                txtcircle3.setBackgroundResource(R.drawable.circle_selected);
                //InicioFragment del evento click
                btnempezar.setText("EMPEZAR");
                btnempezar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Al hacer click en el botom
                        Intent i = new Intent(context, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                });
                //fin del evento click
                break;
            }


        }


        //fin
        container.addView(view);
        //ImageView imageView = view.findViewById(R.id.imageView);
        //imageView.setImageResource(images[position]);
        //ViewPager viewPager = (ViewPager) container;
        //viewPager.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View) object);
//        ViewPager viewPager = (ViewPager) container;
//        View view = (View) object;
//        viewPager.removeView(view);
    }
}
