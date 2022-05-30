package edu.aha.agualimpiafinal.activities.animals;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.aha.agualimpiafinal.R;
import edu.aha.agualimpiafinal.activities.ResultadoCapturaImageActivity;
import edu.aha.agualimpiafinal.databinding.ActivityCatChallengeBinding;
import edu.aha.agualimpiafinal.databinding.ActivityLombrizChallengeBinding;
import edu.aha.agualimpiafinal.models.MoldeSustantivo;
import edu.aha.agualimpiafinal.providers.ImageProvider;
import edu.aha.agualimpiafinal.providers.InsectosProvider;
import edu.aha.agualimpiafinal.providers.UserProvider;

public class CatChallengeActivity extends AppCompatActivity {

    ActivityCatChallengeBinding binding;

    Options mOptions;
    ArrayList<String> mReturnValues = new ArrayList<>();
    File mImageFile;

    String challenge_name = "Almohadilla de gato";

    String id_photo_alas, id_photo_cabeza;

    Context mContext;
    ProgressDialog mDialog;

    ImageProvider mImageProvider;
    MoldeSustantivo sustantivo;
    InsectosProvider mInsectosProvider;
    UserProvider mUserProvider;

    String email, firstname, lastname;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCatChallengeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mImageProvider=new ImageProvider();
        mInsectosProvider = new InsectosProvider();

        setOnClickListeners();

        cargarPreferencias();

        getUserInfoAll();

        goBackActivity();
    }




    private void goBackActivity() {
        binding.imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getUserInfoAll() {

        //Challenge photo - need to change for each challenge
        Glide.with(getApplicationContext())
                .load("https://www.purina-latam.com/sites/g/files/auxxlc391/files/styles/social_share_large/public/01_%C2%BFQu%C3%A9-puedo-hacer-si-mi-gato-est%C3%A1-triste-.png?itok=w67Nhubc")
                .placeholder(R.drawable.loading_icon)
                .into(binding.challengeMainPhoto);

        //SubChallenge photo - need to change for each challenge
        Glide.with(getApplicationContext())
                .load("https://migatodomestico.es/wp-content/uploads/2021/07/las-almohadillas-de-los-gatos.jpg")
                .placeholder(R.drawable.loading_icon)
                .into(binding.challengeRoundedImageViewSubitem);



        getUserInfo(email, challenge_name, binding.circleImageViewPhoto, binding.textViewImagenNosubida, binding.btnregistrar);

    }

    private void getUserInfo(String emailReceiver, String nameSustantivo, final CircleImageView circleImageView, TextView textView, Button btnregistrar) {

        //Log.e("TASK", "email" + email);
        mInsectosProvider.search(emailReceiver,nameSustantivo).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful())
                {

                    if(task.getResult().size() > 0)
                    {
                        String id_photo = task.getResult().getDocuments().get(0).get("id").toString();

                        //Log.e("TASK", "URL DOCUMENTO 0:"+ task.getResult().getDocuments().get(0).get("url"));
                        if(nameSustantivo.equals(challenge_name))
                        {
                            id_photo_cabeza = id_photo;
                        }else
                        {
                            id_photo_alas = id_photo;

                        }

                        String url = task.getResult().getDocuments().get(0).get("url").toString();
                        circleImageView.setBorderColor(0);//eliminar border color del XML para que se vea mas agradable
                        circleImageView.setBorderWidth(0);//eliminar ancho de border del XML para que se vea mas agradable
                        btnregistrar.setText("ACTUALIZAR");
                        textView.setVisibility(View.GONE);

                        //Set image from db
                        Glide.with(getApplicationContext())
                                .load(url)
                                .placeholder(R.drawable.loading_icon)
                                .into(circleImageView);
                    }
                }

            }
        });

    }

    private void cargarPreferencias() {

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        firstname= preferences.getString("spfirstname","");
        //middlename= preferences.getString("spmiddlename","");
        lastname= preferences.getString("splastname","");
        email= preferences.getString("spEmail","");

        SharedPreferences preferencesToken = getSharedPreferences("token", Context.MODE_PRIVATE);
        token= preferencesToken.getString("token","");


    }

    private void setOnClickListeners() {

        binding.fabSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openCamera(100);
            }
        });



        binding.btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(binding.btnregistrar.getText().equals("ACTUALIZAR"))
                {
                    Log.e("ACTUALIZAR","TIENES QUE ACTUALIZAR CABEZA");
                    updatePhoto(mImageFile, binding.textViewImagenNosubida, id_photo_cabeza);

                }else
                {
                    setSustantivoData();
                    sustantivo.setName(challenge_name);

                    registrarData(mImageFile, binding.textViewImagenNosubida);
                }

            }
        });



    }

    private void updatePhoto(final File mImageFileReciever, MaterialTextView textViewImagenNosubida, String idphoto) {

        mDialog = new ProgressDialog(CatChallengeActivity.this);
        mDialog.setTitle("Espere un momento");
        mDialog.setMessage("Guardando Información");

        if(idphoto != null)
        {
            Log.e("idphoto", ""+idphoto);

            if(mImageFileReciever != null)
            {
                if(!mImageFileReciever.equals(""))
                {
                    mDialog.show();

                    mImageProvider.save(CatChallengeActivity.this, mImageFileReciever).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
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

                                        //sustantivo.setUrl(url);

                                        updatePhotoFirebase(url, textViewImagenNosubida, idphoto); //ACtualiza la informacion en firestorage

                                    }
                                });
                            }else {
                                mDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "No se pudo almacenar la imagen", Toast.LENGTH_SHORT).show();
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

        }else
        {
            Log.e("TAG","ERROR IDPHOTO NULL");
        }



    }

    private void updatePhotoFirebase(String url, MaterialTextView textView, String idphoto) {

        Log.e("url","url reciever: "+url);

        mInsectosProvider.update(idphoto, url).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    textView.setVisibility(View.GONE);
                    Toast.makeText(CatChallengeActivity.this, "Foto actualizado Correctamente", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();

                    int pointsganados = 0;

                    senDataIntent(pointsganados);


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

    private void setSustantivoData ()
    {
        String documento =  mInsectosProvider.createDocument().getId();
        sustantivo = new MoldeSustantivo();

        sustantivo.setId(documento);
        sustantivo.setAuthor_email(email);
        sustantivo.setAuthor_name(firstname);
        sustantivo.setAuthor_lastname(lastname);
        sustantivo.setTimestamp(System.currentTimeMillis()/1000);
        sustantivo.setTipo("animal");   //Change in each activity

    }

    private void registrarData(final File mImageFileReciever, TextView textView) {

        mDialog = new ProgressDialog(CatChallengeActivity.this);
        mDialog.setTitle("Espere un momento");
        mDialog.setMessage("Guardando Información");

        if(mImageFileReciever != null)
        {
            if(!mImageFileReciever.equals(""))
            {
                mDialog.show();

                mImageProvider.save(CatChallengeActivity.this, mImageFileReciever).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
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

                                    SaveOnFirebase(url , sustantivo, textView); //ACtualiza la informacion en firestorage

                                }
                            });
                        }else {
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "No se pudo almacenar la imagen", Toast.LENGTH_SHORT).show();
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

    private void SaveOnFirebase(String url, MoldeSustantivo sus, TextView textView) {

        Log.e("url","url reciever: "+url);

        mInsectosProvider.create(sus).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    textView.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Datos registrados correctamente", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();

                    getPointsFirebase();



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

    private void getPointsFirebase() {

        mUserProvider = new UserProvider();

        if(token != null)
        {
            mUserProvider.searchUser(token).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful())
                    {
                        int points = Integer.parseInt(task.getResult().get("points").toString());

                        updatePoints(points);
                    }

                }
            });
        }

    }

    private void updatePoints(int points) {

        mUserProvider.updatePoints(token,points+1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                senDataIntent(1);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.e("POINTS",""+e.getMessage());
            }
        });
    }

    private void senDataIntent(int pointsganados) {
        Log.e("POINTS","PUNTOS ACTUALIZADOS");


        final int min = 1;
        final int max = 3;
        int ramdom = new Random().nextInt((max-min)+1)+min; //Generate numbers between 1 - 3
        String imageurl = null;
        int descripcion = 0;
        String title = "Gato";

        if(ramdom != 0)
        {
            switch (ramdom)
            {
                case 1:
                    imageurl = "https://www.lavanguardia.com/files/image_948_465/uploads/2019/04/02/5fa523c44bc98.jpeg";
                    descripcion = R.string.descripcion_cat1;
                    break;

                case 2:
                    imageurl = "https://www.hogarmania.com/archivos/201510/gato-bengala-1-1280x720x80xX.jpg";
                    descripcion = R.string.descripcion_cat2;
                    break;
            }
        }

        goToNextActivity(pointsganados, descripcion, imageurl,title);
    }

    private void goToNextActivity(int points, int descripcion, String imageurl, String title) {

        Intent i = new Intent(getApplicationContext(), ResultadoCapturaImageActivity.class);
        i.putExtra("points",points);
        i.putExtra("descripcion",descripcion);
        i.putExtra("imageurl",imageurl);
        i.putExtra("title",title);
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


        Pix.start(CatChallengeActivity.this, mOptions);

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //check condition

        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(CatChallengeActivity.this, mOptions);
                } else {
                    Toast.makeText(getApplicationContext(), "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
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
            }

        }
        else
        {
            Toast.makeText(getApplicationContext(), "operacion Cancelado!", Toast.LENGTH_SHORT).show();
        }


    }
}