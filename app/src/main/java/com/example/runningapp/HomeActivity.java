package com.example.runningapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.runningapp.databinding.ActivityHomeBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton floatingActionButton;


    GoogleSignInClient gClient;
    GoogleSignInOptions gOptions;
    private String mName, mEmail, mUsername;
    private double mdistancia, mcalorias;

    private int mpasos,mtiempo, mritmo;
    private  String namep, emailp,usernamep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceFragment(new HomeFragment());
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NavigationView navigationView = binding.navigationView;
       // DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView1 = findViewById(R.id.navigation_view);

        View headerView = navigationView1.getHeaderView(0);

        ImageView themeImageView = headerView.findViewById(R.id.tema);
        FloatingActionButton btnCarrera = findViewById(R.id.btncarrera);



        TextView username = headerView.findViewById(R.id.username);
        TextView Name = headerView.findViewById(R.id.usuario);
        CircleImageView photouser = headerView.findViewById(R.id.profilepic);
        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gClient = GoogleSignIn.getClient(this, gOptions);
        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (gAccount != null){
            String Mail = gAccount.getEmail();
            String gName = gAccount.getDisplayName();
            Uri photoUri = gAccount.getPhotoUrl();
            Name.setText(gName);
            username.setText(Mail);
            Glide.with(this).load(photoUri).into(photouser);
        }else{
            // Obtener los datos del intent
            Intent intent = getIntent();
            mUsername = intent.getStringExtra("username");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            Query query = databaseReference.child("users").orderByChild("username").equalTo(mUsername);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Obtén los datos de la base de datos
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        mUsername = userSnapshot.child("username").getValue(String.class);
                        mName = userSnapshot.child("name").getValue(String.class);
                        mEmail = userSnapshot.child("email").getValue(String.class);
                        mdistancia = userSnapshot.child("distancia").getValue(Double.class);
                        mtiempo =  userSnapshot.child("tiempo").getValue(Integer.class);
                        mcalorias = userSnapshot.child("calorias").getValue(Double.class);
                        mpasos = userSnapshot.child("pasos").getValue(Integer.class);
                        mritmo = userSnapshot.child("ritmo").getValue(Integer.class);
                        datos myApp = (datos) getApplicationContext();
                        myApp.setName(mName);
                        myApp.setEmail(mEmail);
                        myApp.setUsername(mUsername);
                        myApp.setCalorias(mcalorias);
                        myApp.setDistancia(mdistancia);
                        myApp.setTiempo(mtiempo);
                        myApp.setPasos(mpasos);
                        myApp.setRitmo(mritmo);
                        namep = myApp.getName();
                        emailp = myApp.getEmail();
                        usernamep = myApp.getUsername();
                        Name.setText(usernamep);
                        username.setText(emailp);




                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle error
                }
            });

        }

        themeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                } else {

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                }
            }
        });
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            themeImageView.setImageResource(R.drawable.luna);
        } else {
            themeImageView.setImageResource(R.drawable.soleado);
        }

        MaterialToolbar toolbar = binding.topAppBar;
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> binding.drawerLayout.openDrawer(binding.navigationView));

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
            if (currentFragment instanceof AboutFragment) {
                bottomNavigationView.setVisibility(View.GONE);
            } else {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.social:
                    replaceFragment(new SocialFragment());
                    break;
                case R.id.chat:
                    Intent intentc = new Intent(HomeActivity.this, Mainchat.class);
                    startActivity(intentc);
                    break;
                case R.id.perfil:
                    Intent intent = new Intent(HomeActivity.this, PerfilActivity.class);
                    startActivity(intent);
                    break;
            }
            return true;
        });


        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            binding.drawerLayout.closeDrawers();
            switch (id) {
                case R.id.nav_home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.calentamiento:
                    Intent intent = new Intent(HomeActivity.this, CalentamientoActivity1.class);
                    startActivity(intent);
                    break;
                case R.id.ranking:
                    replaceFragment(new RankingFragment());
                    break;
                case R.id.estadisticas:
                    Intent intent1 = new Intent(HomeActivity.this, EstadisticasActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.comunidad:
                    replaceFragment(new SocialFragment());
                    break;
                case R.id.aprendizaje:
                    replaceFragment(new AprendizajeFragment());
                    break;
                case R.id.about:
                    replaceFragment(new AboutFragment());
                    break;
                case R.id.logout:
                    FirebaseAuth.getInstance().signOut();
                    gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finish();
                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        }
                    });
                    break;
                default:
                    return true;
            }
            return true;
        });
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.home:
                    fragment = new HomeFragment();
                    break;
                case R.id.social:
                    fragment = new SocialFragment();
                    break;
                case R.id.chat:
                    Intent intentc = new Intent(HomeActivity.this, Mainchat.class);
                    startActivity(intentc);
                    break;
                case R.id.perfil:

                    Intent intent = new Intent(HomeActivity.this, PerfilActivity.class);
                    startActivity(intent);
                    break;
            }

            if (fragment != null) {
                replaceFragment(fragment);
            }

            return fragment != null;
        });

        btnCarrera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CarreraActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}

