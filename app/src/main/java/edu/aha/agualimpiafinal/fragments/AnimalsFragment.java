package edu.aha.agualimpiafinal.fragments;

import static android.app.Activity.RESULT_CANCELED;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.aha.agualimpiafinal.activities.ResultadoCapturaImageActivity;
import edu.aha.agualimpiafinal.databinding.FragmentAnimalsBinding;
import edu.aha.agualimpiafinal.models.MoldeSustantivo;
import edu.aha.agualimpiafinal.providers.ImageProvider;
import edu.aha.agualimpiafinal.providers.InsectosProvider;


public class AnimalsFragment extends Fragment {

    private FragmentAnimalsBinding binding;

    Options mOptions;
    ArrayList<String> mReturnValues = new ArrayList<>();
    File mImageFile;
    File mImageFile2;
    File mImageFile3;

    Context mContext;
    ProgressDialog mDialog;

    ImageProvider mImageProvider;
    MoldeSustantivo sustantivo;
    InsectosProvider mInsectosProvider;

    String email, firstname, lastname;

    public AnimalsFragment() {

    }


    public static AnimalsFragment newInstance(String param1, String param2) {
        AnimalsFragment fragment = new AnimalsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImageProvider=new ImageProvider();
        mInsectosProvider = new InsectosProvider();

        cargarPreferencias();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAnimalsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setOnClickListeners();


        getUserInfo(email, "cabeza mariposa", binding.circleImageViewPhoto);
        getUserInfo(email, "alas mariposa", binding.circleImageViewPhotoAlas);
        getUserInfo(email, "adbomen mariposa", binding.circleImageViewPhotoAbdomen);


        return view;
    }

    private void getUserInfo(String emailReceiver, String nameSustantivo, final CircleImageView circleImageView) {

        //Log.e("TASK", "email" + email);
        mInsectosProvider.search(emailReceiver,nameSustantivo).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful())
                {
                    if(task.getResult().size() > 0)
                    {
                        //Log.e("TASK", "URL DOCUMENTO 0:"+ task.getResult().getDocuments().get(0).get("url"));

                        String url = task.getResult().getDocuments().get(0).get("url").toString();
                        circleImageView.setBorderColor(0);//eliminar border color del XML para que se vea mas agradable
                        circleImageView.setBorderWidth(0);//eliminar ancho de border del XML para que se vea mas agradable

                        //Set image from db
                        Glide.with(getActivity())
                                .load(url)
                                .into(circleImageView);
                    }
                }

            }
        });

    }

    private void cargarPreferencias() {

        SharedPreferences preferences = getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        firstname= preferences.getString("spfirstname","");
        //middlename= preferences.getString("spmiddlename","");
        lastname= preferences.getString("splastname","");
        email= preferences.getString("spEmail","");

    }

    private void setOnClickListeners() {

        binding.fabSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openCamera(100);
            }
        });

        binding.fabSelectImageAlas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera(110);
            }
        });

        binding.fabSelectImageAbdomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera(120);
            }
        });

        binding.btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setSustantivoData();
                sustantivo.setName("cabeza mariposa");

                registrarData(mImageFile);

            }
        });

        binding.btnregistrarAlas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setSustantivoData();
                sustantivo.setName("alas mariposa");

                registrarData(mImageFile2);
            }
        });

        binding.btnregistrarAbdomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setSustantivoData();
                sustantivo.setName("adbomen mariposa");


                registrarData(mImageFile3);
            }
        });

    }

    private void setSustantivoData ()
    {
        sustantivo = new MoldeSustantivo();
        sustantivo.setAuthor_email(email);
        sustantivo.setAuthor_name(firstname);
        sustantivo.setAuthor_lastname(lastname);
        sustantivo.setTimestamp(System.currentTimeMillis()/1000);
        sustantivo.setTipo("Insecto");

    }

    private void registrarData(final File mImageFileReciever) {

        mDialog = new ProgressDialog(getContext());
        mDialog.setTitle("Espere un momento");
        mDialog.setMessage("Guardando Información");

        if(mImageFileReciever != null)
        {
            if(!mImageFileReciever.equals(""))
            {
                mDialog.show();

                mImageProvider.save(getContext(), mImageFileReciever).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful())
                        {
                            mImageProvider.getDownloadUri().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri)
                                {

                                    String url = uri.toString();
                                    Log.e("URL","url: "+ url);

                                    sustantivo.setUrl(url);

                                    SaveOnFirebase(url , sustantivo); //ACtualiza la informacion en firestorage

                                }
                            });
                        }else {
                            mDialog.dismiss();
                            Toast.makeText(getContext(), "No se pudo almacenar la imagen", Toast.LENGTH_SHORT).show();
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        mDialog.dismiss();
                        Log.e("TAG","ERROR" + e.getMessage());

                    }
                });

            }
        }

    }

    private void SaveOnFirebase(String url, MoldeSustantivo sus) {

        Log.e("url","url reciever: "+url);

        mInsectosProvider.create(sus).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    Toast.makeText(getActivity(), "Datos registrados correctamente", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();

                    goToNextActivity();

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

    private void goToNextActivity() {

        //Intent i = new Intent(mContext, )
        Intent i = new Intent(getActivity(), ResultadoCapturaImageActivity.class);
        startActivity(i);
    }

    private void openCamera(int requescode) {

        //ImagePicker
        mOptions = Options.init()
                .setRequestCode(requescode)                                           //Request code for activity results
                .setCount(1)                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start                             //Pre selected Image Urls
                .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                .setMode(Options.Mode.Picture)                                     //Option to select only pictures or videos or both
                .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/pix/images");


        Pix.start(AnimalsFragment.this, mOptions);

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //check condition

        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(getActivity(), mOptions);
                } else {
                    Toast.makeText(getActivity(), "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED)
        {
            if (data != null)
            {
                if (resultCode == Activity.RESULT_OK && requestCode == 100)
                {
                    Log.e("DATA INGRESASTE: ", "RequestCode: " + requestCode + " & resultacode: "+resultCode);
                    mReturnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                    mImageFile = new File(mReturnValues.get(0)); // Guardar en File la imagen recibida si el usuario selecciono una imagen
                    binding.circleImageViewPhoto.setBorderColor(0);//eliminar border color del XML para que se vea mas agradable
                    binding.circleImageViewPhoto.setBorderWidth(0);//eliminar ancho de border del XML para que se vea mas agradable
                    binding.circleImageViewPhoto.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath())); //Asignar la imagen al id del xml
                    Log.e("IMAGE PATH",""+ mReturnValues.get(0));
                    Log.e("IMAGE ABS PATH",""+ mImageFile.getAbsolutePath());

                }
                else
                {
                    if(resultCode == Activity.RESULT_OK && requestCode == 110)
                    {
                        //code here
                        Log.e("DATA INGRESASTE: ", "RequestCode: " + requestCode + " & resultacode: "+resultCode);
                        mReturnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                        mImageFile2 = new File(mReturnValues.get(0)); // Guardar en File la imagen recibida si el usuario selecciono una imagen
                        binding.circleImageViewPhotoAlas.setBorderColor(0);//eliminar border color del XML para que se vea mas agradable
                        binding.circleImageViewPhotoAlas.setBorderWidth(0);//eliminar ancho de border del XML para que se vea mas agradable
                        binding.circleImageViewPhotoAlas.setImageBitmap(BitmapFactory.decodeFile(mImageFile2.getAbsolutePath())); //Asignar la imagen al id del xml
                        Log.e("IMAGE PATH",""+ mReturnValues.get(0));
                        Log.e("IMAGE ABS PATH",""+ mImageFile2.getAbsolutePath());


                    }else
                    {
                        if(resultCode == Activity.RESULT_OK && requestCode == 120)
                        {
                            //code here
                            Log.e("DATA INGRESASTE: ", "RequestCode: " + requestCode + " & resultacode: "+resultCode);
                            mReturnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                            mImageFile3 = new File(mReturnValues.get(0)); // Guardar en File la imagen recibida si el usuario selecciono una imagen
                            binding.circleImageViewPhotoAbdomen.setBorderColor(0);//eliminar border color del XML para que se vea mas agradable
                            binding.circleImageViewPhotoAbdomen.setBorderWidth(0);//eliminar ancho de border del XML para que se vea mas agradable
                            binding.circleImageViewPhotoAbdomen.setImageBitmap(BitmapFactory.decodeFile(mImageFile3.getAbsolutePath())); //Asignar la imagen al id del xml
                            Log.e("IMAGE PATH",""+ mReturnValues.get(0));
                            Log.e("IMAGE ABS PATH",""+ mImageFile3.getAbsolutePath());


                        }else
                        {
                            Toast.makeText(getContext(), "error al seleccionar la foto", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }

        }
        else
        {
            Toast.makeText(getContext(), "operacion Cancelado!", Toast.LENGTH_SHORT).show();
        }


    }




}