package com.example.runningapp;

import static android.content.ContentValues.TAG;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.runningapp.clases.User;
import com.example.runningapp.clases.UserListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AmigosActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserListAdapter adapter;
    private List<User> userList;
    private DatabaseReference usersRef;
    private CircleImageView userprofile;
    private FloatingActionButton mensajesbtn;
    private ImageView regresarbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);
        final CircleImageView PerfilPic = findViewById(R.id.userProfilePicp);
        recyclerView = findViewById(R.id.personasRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        datos myApp = (datos) getApplicationContext();
        userprofile = findViewById(R.id.userProfilePicp);
        mensajesbtn = findViewById(R.id.fabmensajes);
        regresarbtn = findViewById(R.id.regresarbtn);
        Glide.with(getApplicationContext()).load(myApp.getFoto()).into(userprofile);

        mensajesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AmigosActivity.this, Mainchat.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up_in, R.anim.no_anim);
            }
        });
        PerfilPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AmigosActivity.this, PerfilActivity.class);
                View sharedView = findViewById(R.id.userProfilePicp); // Aquí se debe obtener la vista de transición compartida
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AmigosActivity.this, sharedView, "foto");
                startActivity(intent, options.toBundle());
            }
        });
        regresarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AmigosActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

            }
        });

        userList = new ArrayList<>();
        adapter = new UserListAdapter(userList);
        recyclerView.setAdapter(adapter);
        // Obtener una referencia a la ubicación "users" en la base de datos en tiempo real
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");

        // Agregar un listener para recibir actualizaciones en tiempo real de la ubicación "users"
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (!user.getName().equals(myApp.getName())) { // Excluye al usuario actualmente logueado
                        userList.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Error al recibir actualizaciones de la base de datos", error.toException());
            }
        });


        adapter.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                User user = userList.get(position);
                Intent intent = new Intent(AmigosActivity.this, UserDetailActivity.class);
                UserListAdapter.UserViewHolder holder = (UserListAdapter.UserViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                View sharedView = holder.urlfotoImageView; // Obtener la vista de transición compartida del ViewHolder
                View sharedView1 = holder.nombreTextView;
                String transitionName = "foto"; // Nombre de la transición compartida
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AmigosActivity.this,
                        Pair.create(sharedView, transitionName),
                        Pair.create(sharedView1, "nombres"));
                intent.putExtra("username", user.getUsername());
                startActivity(intent, options.toBundle());
            }
        });

    }
}
