package com.example.runningapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.runningapp.clases.DataClass;
import com.example.runningapp.clases.MyAdapterp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilActivity extends AppCompatActivity {

    private Button editarbtn;
    private String mName, mEmail, mUsername, mFoto;
    private String mTiempo;
    double mDistancia, mCalorias;
    private TextView username;
    private GridView gridView;
    private ArrayList<DataClass> dataList;
    private MyAdapterp adapter;

    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Post");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        username = findViewById(R.id.perfilusuario);
        TextView Distancia = findViewById(R.id.perfildistancia);
        TextView Tiempo = findViewById(R.id.perfiltiempo);
        TextView Calorias = findViewById(R.id.perfilcalorias);
        CircleImageView Foto = findViewById(R.id.updatefoto);
        ImageView btnsetting = findViewById(R.id.btnsetting);

        datos myApp = (datos) getApplicationContext();
        mUsername = myApp.getUsername();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(mUsername);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mUsername = snapshot.child("username").getValue().toString();
                    mCalorias = Double.parseDouble(snapshot.child("calorias").getValue().toString());
                    mDistancia = Double.parseDouble(snapshot.child("distancia").getValue().toString());
                    mTiempo = snapshot.child("tiempo").getValue().toString();
                    mFoto = snapshot.child("fotoperfil").getValue().toString();
                    // actualizar los campos de TextViews
                    username.setText(mUsername);
                    Distancia.setText(Double.toString(mDistancia));
                    Tiempo.setText(mTiempo + " hrs ");
                    Calorias.setText(Double.toString(mCalorias));
                    Glide.with(getApplicationContext()).load(mFoto).into(Foto);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });


       // Glide.with(this).load(myApp.getFoto()).into(Foto);
        gridView = findViewById(R.id.gridView);
        dataList = new ArrayList<>();
        adapter = new MyAdapterp(this, dataList);
        gridView.setAdapter(adapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                    if (dataClass.getUserName().equals(mUsername)) {
                        // Aquí se verifica el nombre de usuario
                        dataList.add(dataClass);
                    }

                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        btnsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(PerfilActivity.this, SettingActivity.class);
               // startActivity(intent);
                passUserData();

            }
        });


    }
    public void passUserData() {
        String userUsername = username.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                    String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                    String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);
                    String distanciaFromDB = snapshot.child(userUsername).child("distancia").getValue(String.class);
                    Intent intent = new Intent(PerfilActivity.this, EditarActivity.class);
                    intent.putExtra("password", passwordFromDB);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}