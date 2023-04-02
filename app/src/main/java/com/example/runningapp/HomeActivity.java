package com.example.runningapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.runningapp.databinding.ActivityHomeBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private String usuario;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton floatingActionButton;
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
                    replaceFragment(new ChatFragment());
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
                    Toast.makeText(HomeActivity.this, "Cerrar Sesión", Toast.LENGTH_SHORT).show();
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
                    fragment = new ChatFragment();
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

