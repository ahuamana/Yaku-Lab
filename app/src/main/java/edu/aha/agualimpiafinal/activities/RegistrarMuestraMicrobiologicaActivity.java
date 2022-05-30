package edu.aha.agualimpiafinal.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

import edu.aha.agualimpiafinal.R;
import edu.aha.agualimpiafinal.databinding.ActivityRegistrarMuestraMicrobiologicaBinding;
import edu.aha.agualimpiafinal.fragments.ListaFragment;
import edu.aha.agualimpiafinal.models.MoldeMuestra;
import edu.aha.agualimpiafinal.providers.ImageProvider;
import edu.aha.agualimpiafinal.providers.MuestrasProvider;
import edu.aha.agualimpiafinal.helper.arrays;
import edu.aha.agualimpiafinal.helper.validaciones;

public class RegistrarMuestraMicrobiologicaActivity extends AppCompatActivity implements LocationListener {

    private ActivityRegistrarMuestraMicrobiologicaBinding binding;

    String ValorURL;
    LocationManager locationManager;

    Context mContext;
    Options mOptions;
    File mImageFile;
    ArrayList<String> mReturnValues = new ArrayList<>();


    MuestrasProvider mMuestrasProvider;
    ImageProvider mImageProvider;

    ProgressDialog mDialog;
    ProgressDialog mDialogLocation;

    //validaciones
    validaciones rules = new validaciones();
    boolean departamento=false,provincia=false,latitud=false,lontitud=false,cantidad_muestra=false,resultado=false, ResultadoMuestra =false;

    private FusedLocationProviderClient ubicacion;

    String firstname,middlename,lastname, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrarMuestraMicrobiologicaBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //all code here

        mMuestrasProvider = new MuestrasProvider();
        mImageProvider = new ImageProvider();


        //Cargar SharePreferences
        cargarPreferencias();

        showBackActivity();

        openCamera();

        showLocation();

        cleanFields();

        clickOnRegistrar();

        showDepartamento();



    }

    private void verMiMuestra() {

        binding.buttonVerMuestra.setVisibility(View.VISIBLE);

        binding.buttonVerMuestra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.scrollview.setVisibility(View.GONE);


                Fragment nuevofragmento = ListaFragment.newInstance();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.linearlayoutContenedor, nuevofragmento);
                transaction.commit();

            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void showDepartamento() {

        binding.RIedtDepartamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                showProvincia(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void showProvincia(int position) {

        arrays datos = new arrays();
        ArrayAdapter<String> spinnerArrayAdapter;
        switch (position)
        {
            case 0:{
                Log.e("Seleccionaste",": nada");
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.Seleccione);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }

            case 1:{
                Log.e("Seleccionaste",": Veracruz");
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasVeracruz);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;

            }
            case 2:{
                Log.e("Seleccionaste",": ancash");
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasAncash);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }
            case 3:{
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasApurimac);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }

            case 4:{
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasArequipa);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }

            case 5:{
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasAyacucho);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }
            case 6:{
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasCajamarca);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }
            case 7:{
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasCallao);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }
            case 8:{
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasCusco);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }
            case 9:{
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasHuancavelica);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }
            case 10:{
                Log.e("Seleccionaste",": apurimac");
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasHuanuco);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }
            case 11:{
                Log.e("Seleccionaste",": apurimac");
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasIca);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }
            case 12:{
                Log.e("Seleccionaste",": apurimac");
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasJunín);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }
            case 13:{
                Log.e("Seleccionaste",": apurimac");
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasLibertad);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }
            case 14:{
                Log.e("Seleccionaste",": apurimac");
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasLambayeque);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }
            case 15:{
                Log.e("Seleccionaste",": apurimac");
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasLima);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }
            case 16:{
                Log.e("Seleccionaste",": apurimac");
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasLoreto);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }
            case 17:{
                Log.e("Seleccionaste",": apurimac");
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasMadreDeDios);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }
            case 18:{
                Log.e("Seleccionaste",": apurimac");
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasMoquegua);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }
            case 19:{
                Log.e("Seleccionaste",": apurimac");
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasPasco);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }
            case 20:{
                Log.e("Seleccionaste",": apurimac");
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasPiura);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }
            case 21:{
                Log.e("Seleccionaste",": apurimac");
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasPuno);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }
            case 22:{
                Log.e("Seleccionaste",": apurimac");
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasSanMartin);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }
            case 23:{
                Log.e("Seleccionaste",": apurimac");
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasTacna);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }
            case 24:{
                Log.e("Seleccionaste",": apurimac");
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasTumbes);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }
            case 25:{
                Log.e("Seleccionaste",": apurimac");
                spinnerArrayAdapter=datos.ObtenerArray(getApplicationContext(),R.array.provinciasUcayali);
                binding.RIedtProvincia.setAdapter(spinnerArrayAdapter);
                break;
            }


        }

    }

    private void clickOnRegistrar() {

        binding.RIbtnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registrarMuestraAnalizada();

            }
        });
    }

    private void registrarMuestraAnalizada() {

        //Validar campos vacios
        latitud=rules.checkField(binding.RIedtLatitud);
        lontitud=rules.checkField(binding.RIedtLongitud);
        cantidad_muestra=rules.checkField(binding.RIedtcantidadmuestra);
        departamento=rules.checkSpinner(binding.RIedtDepartamento,"Seleccione");
        provincia=rules.checkSpinner(binding.RIedtProvincia,"Seleccione");
        ResultadoMuestra =rules.checkSpinner(binding.RIedtBQV,"Seleccione Resultado");

        if(latitud)
        {
            if(lontitud)
            {
                if(cantidad_muestra)
                {
                    if(departamento)
                    {
                        if(provincia)
                        {
                            if(ResultadoMuestra)
                            {
                                if(mImageFile != null)
                                {

                                    mDialog.show();
                                    mImageProvider.save(RegistrarMuestraMicrobiologicaActivity.this, mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                                            if(task.isSuccessful())
                                            {
                                                //Inicia otra tarea para descargar la URL que se subira a firestorage
                                                mImageProvider.getDownloadUri().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String url = uri.toString();
                                                        saveDataOnFirestore(url); //ACtualiza la informacion en firestorage
                                                    }
                                                });
                                                //Fin de tarea descargar la URL que se subira a firestorage
                                            } else {
                                                mDialog.dismiss();
                                                Toast.makeText(RegistrarMuestraMicrobiologicaActivity.this, "No se pudo almacenar la imagen", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            mDialog.dismiss();
                                            Toast.makeText(RegistrarMuestraMicrobiologicaActivity.this, "No se cargar la tarea para subir la imagen", Toast.LENGTH_SHORT).show();
                                        }
                                    });





                                }
                                else {
                                    Toast.makeText(RegistrarMuestraMicrobiologicaActivity.this, "Agregar la foto", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
            }
        }

        //fin de validar
    }

    private void saveDataOnFirestore(String url) {

        MoldeMuestra mMoldeMuestra = new MoldeMuestra();
        mMoldeMuestra.setAuthorFirstname(firstname.toLowerCase());
        mMoldeMuestra.setAuthorLastname(lastname.toLowerCase());
        mMoldeMuestra.setAuthorAlias(middlename.toLowerCase());
        mMoldeMuestra.setAuthorEmail(email.toLowerCase());
        mMoldeMuestra.setMuestraCantidad(binding.RIedtcantidadmuestra.getText().toString());
        mMoldeMuestra.setMuestraDepartamento(binding.RIedtDepartamento.getSelectedItem().toString().toLowerCase());
        mMoldeMuestra.setMuestraProvincia(binding.RIedtProvincia.getSelectedItem().toString().toLowerCase());
        mMoldeMuestra.setMuestraLatitud(Double.parseDouble(binding.RIedtLatitud.getText().toString()));
        mMoldeMuestra.setMuestraLongitud(Double.parseDouble(binding.RIedtLongitud.getText().toString()));
        mMoldeMuestra.setMuestraResultado(binding.RIedtBQV.getSelectedItem().toString());
        mMoldeMuestra.setMuestraFotoPATH(url);
        mMoldeMuestra.setMuestraTimeStamp(System.currentTimeMillis()/1000);


        mMuestrasProvider.create(mMoldeMuestra).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(RegistrarMuestraMicrobiologicaActivity.this, "Datos registrados correctamente", Toast.LENGTH_SHORT).show();
                    clearFieldsWithoutButton();

                    verMiMuestra();

                    mDialog.dismiss();
                }else {
                    mDialog.dismiss();
                    Toast.makeText(mContext, "No se pudieron almacenar los datos", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, "Error al al crear la tarea", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });

    }

    private void cleanFields() {

        binding.RIbtnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearFieldsWithoutButton();

            }
        });
    }

    private void showLocation() {

        binding.RIbtngeolocalizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateLocation();
            }
        });

    }

    private void validateLocation() {

        Log.e("Mensaje","Entraste a getLocation");
        ubicacion = LocationServices.getFusedLocationProviderClient(RegistrarMuestraMicrobiologicaActivity.this);

        if(ContextCompat.checkSelfPermission(RegistrarMuestraMicrobiologicaActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(RegistrarMuestraMicrobiologicaActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {

            //Capturar ultima ubicacion
            getCurrentLocation();

        }
        else
        {
            //ActivityCompat.requestPermissions(getActivity(),new String [] {Manifest.permission.ACCESS_FINE_LOCATION},1 );
            ActivityCompat.requestPermissions(RegistrarMuestraMicrobiologicaActivity.this,new String [] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100 );
        }


    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {

        mDialog = new ProgressDialog(RegistrarMuestraMicrobiologicaActivity.this);
        mDialog.setTitle("Espere un momento");
        mDialog.setMessage("Guardando Información");

        mDialogLocation = new ProgressDialog(RegistrarMuestraMicrobiologicaActivity.this);
        mDialogLocation.setTitle("Espere un momento");
        mDialogLocation.setMessage("Obteniendo localización");


        Log.e("Mensaje","Entraste a Current Location");
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            mDialogLocation.show();
            //when location is enabled
            //get last location

            ubicacion.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //Inicialize location
                    final Location location = task.getResult();

                    //check condicion
                    if (location != null) {
                        //set latitude
                        binding.RIedtLatitud.setText(String.valueOf(location.getLatitude()));
                        //set longitud
                        binding.RIedtLongitud.setText(String.valueOf(location.getLongitude()));

                        worksWithLocation(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()) );


                        mDialogLocation.dismiss();

                    } else {

                        //String bestProvider;
                        //Criteria criteria = new Criteria();
                        //bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
                        //locationManager.requestLocationUpdates(bestProvider, 1000, 0, (LocationListener) locationManager);

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
                                String latitud = String.valueOf(location1.getLatitude());
                                //set longitud
                                String longitud = String.valueOf(location1.getLongitude());

                                worksWithLocation(latitud ,longitud);

                                mDialogLocation.dismiss();


                            }
                        };

                        //fin else
                        //Solicitar Location updates
                        ubicacion.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());


                    }
                    //fin condition


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(RegistrarMuestraMicrobiologicaActivity.this, "No se pudo ejecutar la tarea", Toast.LENGTH_SHORT).show();
                    mDialogLocation.dismiss();
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

    private void worksWithLocation(String latitud, String longitud) {

        //setColor
        Log.e("metodo","workswithlocation");
        binding.RIbtnRegistrar.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.greenPrimary));


    }


    private void openCamera() {


        binding.fabSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCamera();
            }
        });
    }

    private void startCamera() {

        //ImagePicker
        mOptions = Options.init()
                .setRequestCode(100)                                           //Request code for activity results
                .setCount(1)                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setPreSelectedUrls(mReturnValues)                               //Pre selected Image Urls
                .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                .setMode(Options.Mode.Picture)                                     //Option to select only pictures or videos or both
                .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/pix/images");                                       //Custom Path For media Storage

        Pix.start(RegistrarMuestraMicrobiologicaActivity.this, mOptions);
    }

    private void clearFieldsWithoutButton() {

        binding.RIedtcantidadmuestra.setText("");
        binding.RIedtDepartamento.setSelection(0,true);
        binding.RIedtProvincia.setSelection(0,true);
        binding.RIedtLatitud.setText("");
        binding.RIedtLongitud.setText("");
        binding.RIedtBQV.setSelection(0,true);
        ValorURL="";


        binding.RIimgFoto.setBorderColor(getColor(R.color.colorGrayImageCameraBackground));//eliminar border color del XML para que se vea mas agradable
        binding.RIimgFoto.setBorderWidth(80);//eliminar ancho de border del XML para que se vea mas agradable
        binding.RIimgFoto.setImageDrawable(getDrawable(R.drawable.ic_image_camera));

    }

    private void showBackActivity() {

        binding.imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void cargarPreferencias() {

        SharedPreferences preferences = getBaseContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        firstname= preferences.getString("spfirstname","");
        middlename= preferences.getString("spmiddlename","");
        lastname= preferences.getString("splastname","");
        email= preferences.getString("spEmail","");

    }

    @Override
    protected void onStart() {
        super.onStart();

        validateLocation();

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //check condition

        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(RegistrarMuestraMicrobiologicaActivity.this, mOptions);
                } else {
                    Toast.makeText(getApplicationContext(), "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        locationManager.removeUpdates((LocationListener) getApplicationContext());

        //open the MapFragment:
        //set latitude
        binding.RIedtLatitud.setText(String.valueOf(location.getLatitude()));
        //set longitud
        binding.RIedtLongitud.setText(String.valueOf(location.getLongitude()));
        Toast.makeText(getApplicationContext(), "latitude:" + String.valueOf(location.getLatitude()) + " longitude:" + String.valueOf(location.getLongitude()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        Log.e("DATA", "camara resultado");

        if (resultCode != RESULT_CANCELED) {
            if (data != null) {
                if (resultCode == Activity.RESULT_OK && requestCode == 100) {
                    Log.e("DATA INGRESASTE: ", "RequestCode: " + requestCode + " & resultacode: " + resultCode);


                    mReturnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                    mImageFile = new File(mReturnValues.get(0)); // Guardar en File la imagen recibida si el usuario selecciono una imagen
                    binding.RIimgFoto.setBorderColor(0);//eliminar border color del XML para que se vea mas agradable
                    binding.RIimgFoto.setBorderWidth(0);//eliminar ancho de border del XML para que se vea mas agradable
                    binding.RIimgFoto.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath())); //Asignar la imagen al id del xml

                    Log.e("IMAGE PATH", "" + mReturnValues.get(0));
                    Log.e("IMAGE ABS PATH", "" + mImageFile.getAbsolutePath());

                } else {
                    Toast.makeText(getApplicationContext(), "error al seleccionar la foto", Toast.LENGTH_SHORT).show();
                }
            }

        } else {
            Toast.makeText(getApplicationContext(), "operacion Cancelado!", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);

    }


}