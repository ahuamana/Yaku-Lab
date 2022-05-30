package edu.aha.agualimpiafinal.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


import edu.aha.agualimpiafinal.models.MoldeMuestra;
import edu.aha.agualimpiafinal.R;
import edu.aha.agualimpiafinal.providers.MuestrasProvider;
import edu.aha.agualimpiafinal.helper.RelativeTime;

public class MapFragment extends Fragment implements GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient ubicacion;
    double latitudActual, longitudActual;
    LocationManager locationManager;
    ImageView imageViewGPS;

    private Marker myMarker;

    //Firebase
    //DatabaseReference ref;
    //Cloud Firestore
    FirebaseFirestore fStore;

    MuestrasProvider mMuestrasProvider;

    //guardar datos en esta ListaFragment los nuevos datos de Firestore
    List<MoldeMuestra> listaMuestras= new ArrayList<>();


    GoogleMap googleMaps;
    //Marker Global de posicion
    Marker currentmarker=null;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(final GoogleMap googleMap) {

            //startLocation(googleMap); //getlocation

            mMap = googleMap;

            //InicioFragment onMapClickListener
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {

                    if(currentmarker != null) {
                        currentmarker.remove();
                    }

                    //when click on MapFragment
                    //Inicializar marker options
                    MarkerOptions markerOptions = new MarkerOptions();
                    //Set position of marker
                    markerOptions.position(latLng);

                    //asignar tipo de mapa
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                    //set tittle of marker
                    markerOptions.title(latLng.latitude+ " : "+latLng.longitude);
                    //remove all marker


                    //animate to zoom the marker
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                    //add marker on MapFragment
                    currentmarker=googleMap.addMarker(markerOptions);



                    //fin para remover marker

                }
            });

            //fin onMapClickListener




            startLocation(googleMap); //getlocation



        }
    };

    private void startLocation(GoogleMap googleMap) {

        //Obtener Permisos geolzalizacion
        ubicacion = LocationServices.getFusedLocationProviderClient(getActivity());
        //permisos
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
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

            ObtenerUbicationActual(googleMap);

        } else
        {
            ActivityCompat.requestPermissions(getActivity(),new String [] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100 );
        }



        //fin Permisos geolzalizacion

        //asginarle variable para el mejor manejo
    }


    @SuppressLint("MissingPermission")
    private void ObtenerUbicationActual(final GoogleMap googleMap) {


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

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
                                      googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        currentmarker=googleMap.addMarker(new MarkerOptions().position(pichanaki).title("Oficina Principal"));

                        //Ingresa a la posicion actual
                        CameraPosition cameraPosition = CameraPosition.builder().target(pichanaki).zoom(15).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
                                              googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                currentmarker=googleMap.addMarker(new MarkerOptions().position(pichanaki).title("Oficina Principal"));

                                        //Ingresa a la posicion actual
                                        CameraPosition cameraPosition = CameraPosition.builder().target(pichanaki).zoom(10).build();
                                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //check condition

        if(requestCode==100 &&(grantResults.length>0) && (grantResults[0] + grantResults[1] ==PackageManager.PERMISSION_GRANTED))
        {
            ObtenerUbicationActual(googleMaps);

        }else {

            //Permisos denegados
            Toast.makeText(getActivity(), "Permisos denied", Toast.LENGTH_SHORT).show();
        }

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_map, container, false);



        //Inicializar firestore
        fStore=FirebaseFirestore.getInstance();

        mMuestrasProvider = new MuestrasProvider();

        imageViewGPS = vista.findViewById(R.id.imageView_gps_fixed);


        imageViewGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get location when clicked

                if(mMap != null)
                {
                    startLocation(mMap);

                }else
                {
                    Toast.makeText(getContext(), "El mapa no se ah inicializado", Toast.LENGTH_SHORT).show();
                }


            }
        });


       setPointsOnGoogleMaps();




        return vista;
    }

    private void setPointsOnGoogleMaps() {

        Log.e("MAP POINTS","METHOD");

       //if(mMap != null)
       //{
           Log.e("MAP POINTS","NO ES NULO");

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



                           //Validar si la muestra es positivo
                           if(muestraResultado.contains("Negativo"))
                           {
                               //Asignar un punto Azul en google maps con su latitud y longitud
                               LatLng newlat = new LatLng(listaMuestras.get(i).getMuestraLatitud(),listaMuestras.get(i).getMuestraLongitud());
                               mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                               myMarker=mMap.addMarker(new MarkerOptions()
                                       .icon(BitmapDescriptorFactory.fromResource(R.drawable.waterblue64))
                                       .position(newlat)
                                       //.title("Muestra "+ i));
                                       .title(RelativeTime.getTimeAgo(time, getContext())));

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
                                           .title(RelativeTime.getTimeAgo(time, getContext())));


                               }

                           }
                           ////Fin de Validar si la muestra es positivo

                       }

                   }

               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   //Codigo si falla al obtener la ListaFragment
                   Toast.makeText(getActivity(), "Error getting data!!!", Toast.LENGTH_LONG).show();
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


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }


    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (marker.equals(myMarker))
        {
            //handle click here
            Toast.makeText(getActivity(), "Hiciste click a myMarker", Toast.LENGTH_SHORT).show();
        }

        return false;
    }




}