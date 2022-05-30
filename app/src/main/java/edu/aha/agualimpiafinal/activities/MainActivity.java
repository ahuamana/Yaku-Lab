package edu.aha.agualimpiafinal.activities;

import static java.sql.DriverManager.println;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.installations.Utils;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.prefs.Preferences;

import edu.aha.agualimpiafinal.R;
import edu.aha.agualimpiafinal.databinding.ActivityMainBinding;
import edu.aha.agualimpiafinal.helper.TextUtilsText;
import edu.aha.agualimpiafinal.modulos.login.model.User;
import edu.aha.agualimpiafinal.modulos.login.model.User2;
import edu.aha.agualimpiafinal.modulos.puntaje.views.PointsActivity;
import edu.aha.agualimpiafinal.providers.UserProvider;

public class

MainActivity extends AppCompatActivity {

    UserProvider mUserProvider;
    User mUser;

    String firstname, middlename, lastname, email;
    int unicodeHand = 0x1F44B;

    String emailExtra = "";
    String TAG = "MainActivity";

    private AppBarConfiguration mAppBarConfiguration;

    private ActivityMainBinding binding;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getDataIntent();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cargarPreferencias();


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio,
                R.id.nav_lista,
                //R.id.nav_comomedir,
                //R.id.nav_nosotros,
                //R.id.nav_registrar,
                //R.id.nav_sugerencias,
                //R.id.nav_compartir,
                //R.id.nav_dashboard,
                R.id.nav_laboratorio)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //Inicia metodos importantes
        showNavHeaderData();

        goToPoints();

    }

    //////////////      //////////////      //////////////////      ////////////////////        ////////////////////ON CREATE FINISHED
    //////////////      //////////////      //////////////////      ////////////////////        ////////////////////ON CREATE FINISHED

    private void goToPoints() {
        binding.appBarMain.fabPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, PointsActivity.class);
                i.putExtra("email",emailExtra);
                startActivity(i);

            }
        });
    }

    private void getDataIntent() {

        if(getIntent().getExtras() != null)
        {
            emailExtra =  getIntent().getExtras().getString("email");
            Log.e(TAG,"EMAIL RECIBIDO: "+emailExtra );
        }

    }

    private void showNavHeaderData(){

        View headerView = binding.navView.getHeaderView(0);

        MaterialTextView greetingsTxt = headerView.findViewById(R.id.greetings);
        MaterialTextView greetingsName = headerView.findViewById(R.id.greetings_name);

        greetingsTxt.setText("Buen día "+ getEmojiByUnicode(unicodeHand));

        if(TextUtilsText.getUserLoggedAnonymous())
        {
            Log.e("","Usuario Anonimo");
            greetingsName.setText("Invitad@");
        }else{
            getUserInfo(greetingsName);
            //greetingsName.setText("Invitad@");
        }
    }


    private void getUserInfo(MaterialTextView greetingsName) {

        mUserProvider = new UserProvider();
        mUser = new User();
        Log.e("","Email Recibido:"+ emailExtra);

        mUserProvider.searchUserByEmail(emailExtra).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {
                    //Log.e(TAG,""+task.getResult().toObject(User2.class));
                    User2 user = task.getResult().toObject(User2.class);

                    //Asignar Nombres y Primera letra del apellido del usuario
                    String names= TextUtilsText.instance.replaceFirstCharInSequenceToUppercase(user.getAuthor_nombres());
                    String apellidos= user.getAuthor_apellidos().substring(0,1);

                    greetingsName.setText(names+" "+apellidos.toUpperCase()+".");
                    //savePreferences(user);

                }else
                {
                    Log.e(TAG,""+ task.getException().getMessage());
                }

            }
        });

        /*
        mUserProvider.createToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {

                if(task.isSuccessful())
                {
                    mUser.setToken(task.getResult());

                    guardarTokenLocalmente(mUser.getToken());

                    Log.d("TAG", "TOKENCREADO: "+task.getResult());

                    mUserProvider.searchUser(mUser.getToken()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if(task.getResult().exists())
                            {
                                Log.e("TAG",""+task.getResult());
                            }else
                            {

                                Log.e("TAG",""+task.getResult());
                                goToCreateData(mUser);
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("ERROR","NO INTERNET CONECCTION");
                        }
                    });




                }else
                {
                    Log.d("TAG", "No se pudo crear el token");

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.e("ERROR","ERROR: "+e.getMessage());

            }
        });
         */

    }

    private void guardarTokenLocalmente(String tokenReciever) {

            SharedPreferences preferences = getSharedPreferences("token", Context.MODE_PRIVATE);
            //editor permite editar y almacenar las variables
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("token",tokenReciever);
            editor.commit();
    }

    private void goToCreateData(User mUser) {

        mUser.setAuthor_firstname(firstname);
        mUser.setAuthor_email(email);
        mUser.setAuthor_alias(middlename);
        mUser.setAuthor_lastname(lastname);
        mUser.setPoints(0);
        mUserProvider.create(mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    Log.e("USERPROFILE","User Points creado");

                }else
                {
                   Log.e("USERPROFILE","Error al crear la Base de datos");
                }

            }
        });

    }


    private void cargarPreferencias() {

        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        firstname= preferences.getString("spfirstname","");
        middlename= preferences.getString("spmiddlename","");
        lastname= preferences.getString("splastname","");
        email= preferences.getString("spEmail","");
        //asignar datos guardados a los respectivos campos


    }

    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getFragmentManager().popBackStack();
    }
}