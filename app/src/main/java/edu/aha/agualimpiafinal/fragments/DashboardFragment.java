package edu.aha.agualimpiafinal.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.aha.agualimpiafinal.R;
import edu.aha.agualimpiafinal.models.MoldeMuestra;
import edu.aha.agualimpiafinal.providers.MuestrasProvider;
import edu.aha.agualimpiafinal.helper.Converters;


public class DashboardFragment extends Fragment {

    MuestrasProvider mMuestrasProvider;
    View mView;

    int amountNegative = 0;
    int amountPositive = 0;

    int muestras_del_dos_mil_veintiuno_positivo;
    int muestras_del_dos_mil_veintiuno_negativo;

    DonutProgress donutProgressNegative;
    DonutProgress donutProgressPositive;

    BarChart mBarChart;

    ProgressBar progressBarChart;

    ProgressDialog mDialog;

    public DashboardFragment() {
    }

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        donutProgressNegative = mView.findViewById(R.id.donut_progressNegative);
        donutProgressPositive = mView.findViewById(R.id.donut_progressPositive);
        progressBarChart = mView.findViewById(R.id.progress_bar_chart);
        mBarChart = mView.findViewById(R.id.barchart);

        mMuestrasProvider = new MuestrasProvider();

        getPreferences();

        getDataForGroupedBarChart();

        return mView;
    }

    private void getDataForGroupedBarChart() {

        mDialog = new ProgressDialog(getContext());
        mDialog.setTitle("Espere un momento");
        mDialog.setMessage("Cargando Información!");
        mDialog.show();

        final String currentYear= String.valueOf(Converters.instance.epochTimeToDate(Converters.instance.currentUnixTime())).substring(6,10); // get Current Time
        mMuestrasProvider.getCollectionDatosMuestra().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {

                List<MoldeMuestra> moldeMuestra   = querySnapshot.toObjects(MoldeMuestra.class);

                int contadorPositive=0;
                int contadorNegative=0;

                for( MoldeMuestra molde : moldeMuestra)
                {
                    String yearMuestra;
                    yearMuestra = Converters.instance.epochTimeToDate(molde.getMuestraTimeStamp()).substring(6,10);

                    Log.e("MUESTRA","MUESTRA: "+ yearMuestra);

                    if(yearMuestra.equals(currentYear))
                    {
                        if(molde.getMuestraResultado().equals("Positivo"))
                        {
                            contadorPositive++;
                        }

                        if(molde.getMuestraResultado().equals("Negativo"))
                        {
                            contadorNegative++;
                        }

                    }

                }

                android.util.Log.e("MUESTRA:INSIDE","MUESTRA Positivas: " + contadorPositive);
                android.util.Log.e("MUESTRA:INSIDE","MUESTRA Negativas: " + contadorNegative);

                muestras_del_dos_mil_veintiuno_positivo = contadorPositive;
                muestras_del_dos_mil_veintiuno_negativo = contadorNegative;


                //Create asyntask for craete barchar group
                new createGruperBarchartTask().execute();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                android.util.Log.e("ERROR","No se pudo cargar la información: " + e.getMessage());
                mDialog.dismiss();
            }
        });



    }

    private class createGruperBarchartTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            createGroupedBarChat();
            progressBarChart.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }



        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressBarChart.setVisibility(View.GONE);
        }
    }


    private void createGroupedBarChat() {

        //android.util.Log.e("MUESTRA RECIBIDAS","MUESTRA Recibidas Positivas: "+ muestras_del_dos_mil_veintiuno_positivo);
        //android.util.Log.e("MUESTRA RECIBIDAS","MUESTRA Recibidas Negativas: "+ muestras_del_dos_mil_veintiuno_negativo);

        BarDataSet barDataSet1 = new BarDataSet(barEntriesPositives(muestras_del_dos_mil_veintiuno_positivo),"Muestras Positivas");
        barDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);
        barDataSet1.setColor(getResources().getColor(R.color.red));

        BarDataSet barDataSet2 = new BarDataSet(barEntriesNegatives(muestras_del_dos_mil_veintiuno_negativo),"Muestras Negativas");
        barDataSet2.setAxisDependency(YAxis.AxisDependency.LEFT);
        barDataSet2.setColor(getResources().getColor(R.color.lightblue));

        BarData data = new BarData(barDataSet1,barDataSet2);

        mBarChart.setData(data);

        String[] days = new String[]{"2017","2018","2019","2020","2021"};

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        mBarChart.setDragEnabled(true); //move the drag with your hand
        mBarChart.setVisibleXRangeMaximum(5);
        mBarChart.getDescription().setEnabled(false);//Remove description from barchart

        xAxis.setDrawGridLines(false); // disappear vertically axis
        mBarChart.getAxisRight().setDrawGridLines(false); // disappear horizontal axis
        mBarChart.getAxisLeft().setDrawGridLines(false); // disappear horizontal axis

        float groupSpace = 0.09f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        data.setBarWidth(barWidth);

        //mBarChart.getXAxis().setAxisMinimum(0);
        //mBarChart.getXAxis().setAxisMaximum(0+mBarChart.getBarData().getGroupWidth(groupSpace,barSpace));
        //mBarChart.getAxisLeft().setAxisMinimum(0);

        mBarChart.groupBars(0,groupSpace,barSpace);// start at x = 0

        mBarChart.notifyDataSetChanged();// Refresh MVPChart with the new Data first
        mBarChart.invalidate(); // Refresh MVPChart with the new Data second

        //Cerrar dialogo
        mDialog.dismiss();

    }

    private ArrayList<BarEntry> barEntriesPositives(int lastyearData)
    {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1,0));
        barEntries.add(new BarEntry(2,0));
        barEntries.add(new BarEntry(3,0));
        barEntries.add(new BarEntry(4,lastyearData));
        barEntries.add(new BarEntry(5,0));

        return barEntries;
    }

    private ArrayList<BarEntry> barEntriesNegatives(int lastyearData)
    {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1,0));
        barEntries.add(new BarEntry(2,0));
        barEntries.add(new BarEntry(3,0));
        barEntries.add(new BarEntry(4,lastyearData));
        barEntries.add(new BarEntry(5,0));

        return barEntries;
    }


    private void getPreferences( )
    {

        SharedPreferences preferences = getContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String email =  preferences.getString("spEmail","");

        getDataFirebase(email);

    }

    private void getDataFirebase(final String email) {

        //Reset amount negative and positive from donus progress bar
        amountNegative=0;
        amountPositive=0;

        mMuestrasProvider.getCollectionDatosMuestra().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {

                List<MoldeMuestra> moldeMuestra   = querySnapshot.toObjects(MoldeMuestra.class);

                for( MoldeMuestra molde : moldeMuestra)
                {
                    //android.util.Log.e("DATA", "EMAIL: "+ email);
                    //android.util.Log.e("DATA", "EMAILFIRESTORE: "+ molde.getAuthorEmail());

                    if(molde.getAuthorEmail().equals(email))
                    {
                        if(molde.getMuestraResultado().equals("Negativo"))
                        {
                            amountNegative++;
                        }

                        if(molde.getMuestraResultado().equals("Positivo"))
                        {
                            amountPositive++;
                        }
                    }
                    //android.util.Log.e("DATA", "CANTIDAD: "+ cantidad);

                }

                //asignar la cantidad de tus muestras a los donust progress



                setDonutProgressData(amountNegative, donutProgressNegative);
                setDonutProgressData(amountPositive, donutProgressPositive);




            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


            }
        });



    }


    private void setDonutProgressData(int amount, DonutProgress donutProgress) {

        //donutProgress.setTextSize(100);

        switch (amount)
        {
            case 0: {
                donutProgress.setProgress(0f);
                donutProgress.setText("0");
                break;
            }

            case 1: {
                donutProgress.setProgress(10f);
                donutProgress.setText("1");
                break;
            }
            case 2: {
                donutProgress.setProgress(20f);
                donutProgress.setText("2");
                break;
            }
            case 3:{
                donutProgress.setProgress(30f);
                donutProgress.setText("3");
                break;
            }
            case 4:{
                donutProgress.setProgress(40);
                donutProgress.setText("4");
                break;
            }
            case 5:{
                donutProgress.setProgress(50);
                donutProgress.setText("5");
                break;
            }
            case 6:{
                donutProgress.setProgress(60);
                donutProgress.setText("6");
                break;
            }
            case 7:{
                donutProgress.setProgress(70);
                donutProgress.setText("7");
                break;
            }
            case 8:{
                donutProgress.setProgress(80);
                donutProgress.setText("8");
                break;
            }
            case 9:{
                donutProgress.setProgress(90);
                donutProgress.setText("9");
                break;
            }
            case 10:{
                donutProgress.setProgress(100);
                donutProgress.setText("10");
                break;
            }

            default:
                donutProgress.setProgress(0);
                donutProgress.setText("0");
                break;
        }

    }


}