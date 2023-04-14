package com.example.runningapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    private Button editarbtn;
    private String mName, mEmail, mUsername, mFoto,mPais,maltura, mpeso;
    private int mTiempo;
    double mDistancia, mCalorias;
    private TextView username;
    private TextView Name;
    private TextView Mail;
    private TextView Distancia;
    private TextView Tiempo;
    private TextView Calorias;
    private TextView Peso;
    private TextView Altura;
    private TextView Pais;
    private CircleImageView Foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        username = findViewById(R.id.perfilusuario);
        Name = findViewById(R.id.perfilnombre);
        Mail = findViewById(R.id.perfilcorreo);
        Distancia = findViewById(R.id.perfildistancia);
        Tiempo = findViewById(R.id.perfiltiempo);
        Calorias = findViewById(R.id.perfilcalorias);
        Peso = findViewById(R.id.perfilpeso);
        Altura = findViewById(R.id.perfilestatura);
        Pais = findViewById(R.id.perfilpais);
        Foto = findViewById(R.id.updatefoto);

        datos myApp = (datos) getApplicationContext();
        mUsername = myApp.getUsername();
        editarbtn = findViewById(R.id.update_button);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(mUsername);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mUsername = snapshot.child("username").getValue(String.class);
                    mName = snapshot.child("name").getValue(String.class);
                    mEmail = snapshot.child("email").getValue(String.class);
                    mPais = snapshot.child("pais").getValue(String.class);
                    mCalorias = snapshot.child("calorias").getValue(Double.class);
                    mDistancia = snapshot.child("distancia").getValue(Double.class);
                    mTiempo = snapshot.child("tiempo").getValue(Integer.class);
                    maltura = snapshot.child("altura").getValue(String.class);
                    mpeso = snapshot.child("peso").getValue(String.class);
                    mFoto = snapshot.child("fotoperfil").getValue(String.class);
                    // actualizar los campos de TextViews

                    username.setText(mUsername);
                    Name.setText(mName);
                    Mail.setText(mEmail);
                    Pais.setText(mPais);
                    Distancia.setText(String.valueOf(mDistancia));
                    Tiempo.setText(String.valueOf(mTiempo) + " hrs ");
                    Altura.setText(String.valueOf(maltura) + " cm ");
                    Peso.setText(String.valueOf(mpeso));
                    Calorias.setText(String.valueOf(mCalorias));
                    Glide.with(getApplicationContext()).load(mFoto).into(Foto);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });

        editarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    Double distanciaFromDB = snapshot.child(userUsername).child("distancia").getValue(Double.class);
                    Intent intent = new Intent(SettingActivity.this, EditarActivity.class);
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