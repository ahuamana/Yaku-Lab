package edu.aha.agualimpiafinal.activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.aha.agualimpiafinal.R;
import edu.aha.agualimpiafinal.databinding.ActivityMapsBinding;
import edu.aha.agualimpiafinal.models.MoldeMuestra;
import edu.aha.agualimpiafinal.providers.MuestrasProvider;
import edu.aha.agualimpiafinal.helper.RelativeTime;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    MuestrasProvider mMuestrasProvider;
    private Marker myMarker;

    //getMyLocation
    private FusedLocationProviderClient ubicacion;
    double latitudActual, longitudActual;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mMuestrasProvider = new MuestrasProvider();


        // Obtain the SupportMapFragment and get notified when the MapFragment is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        showMyLocation();

        binding.imageViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //eliminar activities anteriores
                startActivity(i);
            }
        });

    }

    private void showMyLocation() {

        binding.imageViewGpsFixed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mMap != null)
                {
                    startLocation();
                }

            }
        });



    }

    private void startLocation() {

        //Obtener Permisos geolzalizacion
        ubicacion = LocationServices.getFusedLocationProviderClient(this);
        //permisos
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            ObtenerUbicationActual();

        } else
        {
            ActivityCompat.requestPermissions(this,new String [] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100 );
        }



        //fin Permisos geolzalizacion

        //asginarle variable para el mejor manejo
    }


    @SuppressLint("MissingPermission")
    private void ObtenerUbicationActual() {

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //when location is enabled
            //get last location

            ubicacion.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @SuppressLint("MissingPermission")
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //Inicialize location
                    final Location location = task.getResult();

                    //check condicion
                    if (location != null) {
                        //set latitude
                        latitudActual= location.getLatitude();
                        //set longitud
                        longitudActual=location.getLongitude();


                        /////LLevarme a mi ubicacion Actual
                        Log.e( "DatosLatitud",String.valueOf(latitudActual)+" : "+String.valueOf(longitudActual));

                        //Longitud inicial
                        LatLng pichanaki = new LatLng(latitudActual,longitudActual);
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        myMarker=mMap.addMarker(new MarkerOptions().position(pichanaki).title("Mi Ubicacion"));

                        //Ingresa a la posicion actual
                        CameraPosition cameraPosition = CameraPosition.builder().target(pichanaki).zoom(15).build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(pichanaki));

                        /////fin de llevarme a mi ubicacion Actual



                    } else {


                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback() {

                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                super.onLocationResult(locationResult);

                                //Iniciar location
                                Location location1 = locationResult.getLastLocation();
                                //set latitude
                                latitudActual=location1.getLatitude();
                                //set longitud
                                longitudActual=location1.getLongitude();

                                /////LLevarme a mi ubicacion Actual
                                Log.e( "DatosLatitud",String.valueOf(latitudActual)+" : "+String.valueOf(longitudActual));

                                //Longitud inicial
                                LatLng pichanaki = new LatLng(latitudActual,longitudActual);
                                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                myMarker=mMap.addMarker(new MarkerOptions().position(pichanaki).title("Mi ubicación"));

                                //Ingresa a la posicion actual
                                CameraPosition cameraPosition = CameraPosition.builder().target(pichanaki).zoom(10).build();
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                //googleMap.moveCamera(CameraUpdateFactory.newLatLng(pichanaki));

                                /////fin de llevarme a mi ubicacion Actual

                            }
                        };

                        //fin else
                        //Solicitar Location updates
                        ubicacion.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());


                    }
                    //fin condition


                }
            });

            //fin get last location

        } else {

            //when location services is not enabled
            //Open Location  setting
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)  );


        }



    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String latReceiver = getIntent().getStringExtra("latitud");
        String lonReceiver = getIntent().getStringExtra("longitud");
        String time = getIntent().getStringExtra("timeago");

        Log.e("LAT&LONG",""+ latReceiver+":"+lonReceiver);

        // Add a marker in Sydney and move the camera
        LatLng positionReceiver = new LatLng(Double.parseDouble(latReceiver), Double.parseDouble(lonReceiver));
        //mMap.addMarker(new MarkerOptions().position(positionReceiver).title("Marker in Sydney"));
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(positionReceiver)
                .zoom(18)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ahorrar_agua128))
                .position(positionReceiver)
                .title(time));


        settingsMaps(googleMap);

        setPointsOnGoogleMaps(googleMap, latReceiver, lonReceiver);

    }

    private void setPointsOnGoogleMaps(GoogleMap googleMap, String latReciver, String lonReceiver) {

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(MapsActivity.this, "Info window clicked",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Log.e("MAP POINTS","METHOD");
        Log.e("MAP POINTS","NO ES NULO");

        List<MoldeMuestra> listaMuestras= new ArrayList<>();


        //crear referencia a Firebase
        mMuestrasProvider.getCollectionDatosMuestra().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                //Codigo si obtiene la ListaFragment

                if (documentSnapshots.isEmpty()) {
                    Log.d("TAG", "onSuccess: LIST EMPTY");
                    return;
                } else
                {
                    //Asignar Datos de Firestore al molde
                    List<MoldeMuestra> types = documentSnapshots.toObjects(MoldeMuestra.class);
                    listaMuestras.addAll(types);
                    Log.d("TAG", "onSuccess: " + listaMuestras);

                    //Obtener datos del molde
                    for (int i=0;i<listaMuestras.size();i++)
                    {
                        Log.e("Latitud "+i, String.valueOf(listaMuestras.get(i).getMuestraLatitud()));
                        Log.e("Longitud" +i,String.valueOf(listaMuestras.get(i).getMuestraLongitud()));
                        Log.e("unixtime" +i,String.valueOf(listaMuestras.get(i).getMuestraTimeStamp()));
                        //Obtengo el Resultado de la muestra
                        String muestraResultado= listaMuestras.get(i).getMuestraResultado();

                        ////Horal Obtenida
                        long time = listaMuestras.get(i).getMuestraTimeStamp()*1000;  //
                        //Date df = new java.util.Date(time);
                        //String vv = new SimpleDateFormat("MM dd, yyyy hh:mma").format(df);
                        //String HoraObtenida = new SimpleDateFormat("MM/dd/yyyy hh:mma").format(df);

                        if( listaMuestras.get(i).getMuestraLatitud() ==  Double.parseDouble(latReciver))
                        {
                            Log.e("UnaMuestra","Es igual a lo recibido");
                        }else
                        {
                            //Validar si la muestra es positivo
                            if(muestraResultado.contains("Negativo"))
                            {
                                //Asignar un punto Azul en google maps con su latitud y longitud
                                LatLng newlat = new LatLng(listaMuestras.get(i).getMuestraLatitud(),listaMuestras.get(i).getMuestraLongitud());
                                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                myMarker=googleMap.addMarker(new MarkerOptions()
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.waterblue64))
                                        .position(newlat)
                                        //.title("Muestra "+ i));
                                        .title(RelativeTime.getTimeAgo(time, getApplicationContext())));

                            }
                            else {
                                if(muestraResultado.contains("Positivo"))
                                {
                                    //Asignar un punto Rojo en google maps con su latitud y longitud
                                    LatLng newlats = new LatLng(listaMuestras.get(i).getMuestraLatitud(), listaMuestras.get(i).getMuestraLongitud());
                                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                    mMap.addMarker(new MarkerOptions()
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.waterred64))
                                            .position(newlats)
                                            //.title("Muestra "+ dateTime));
                                            .title(RelativeTime.getTimeAgo(time, getApplicationContext())));


                                }

                            }
                            ////Fin de Validar si la muestra es positivo

                        }

                    }

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Codigo si falla al obtener la ListaFragment
                Toast.makeText(MapsActivity.this, "Error getting data!!!", Toast.LENGTH_LONG).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //limpiar marker
                //googleMaps.clear();
            }
        });

        //}
    }

    private void settingsMaps(GoogleMap googleMap) {

        //añadir botones de + y - para hacer zoom
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        //habilitar compass o brujula
        //googleMap.setMyLocationEnabled(true);
        //googleMap.getUiSettings().setMyLocationButtonEnabled(false);//ocultar boton de ubicacion
        googleMap.getUiSettings().setCompassEnabled(true);


        googleMap.getUiSettings().setAllGesturesEnabled(true); // habilitar gestos


        //mostrar mi localizacion
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        //añadir la brujula
        googleMap.getUiSettings().setCompassEnabled(true);

        //añadir toolbar
        googleMap.getUiSettings().setMapToolbarEnabled(true);


    }
}