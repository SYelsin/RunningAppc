package com.example.runningapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PerfilActivity extends AppCompatActivity {

    private Button editarbtn;
    private String mName, mEmail, mUsername;
    private int mTiempo;
    double mDistancia, mCalorias;
    private TextView  username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        editarbtn = findViewById(R.id.editar_button);
        username = findViewById(R.id.perfilusuario);
        TextView Name = findViewById(R.id.perfilnombre);
        TextView Mail = findViewById(R.id.perfilcorreo);
        TextView Distancia = findViewById(R.id.perfildistancia);
        TextView Tiempo = findViewById(R.id.perfiltiempo);
        TextView Calorias = findViewById(R.id.perfilcalorias);
        datos myApp = (datos) getApplicationContext();
        mName = myApp.getName();
        mEmail = myApp.getEmail();
        mUsername = myApp.getUsername();
        mCalorias = myApp.getCalorias();
        mDistancia = myApp.getDistancia();
        mTiempo = myApp.getTiempo();
        Name.setText(mName);
        username.setText(mUsername);
        Mail.setText(mEmail);
        Distancia.setText(Double.toString(mDistancia));
        Tiempo.setText(Integer.toString(mTiempo) + " hrs ");
        Calorias.setText(Double.toString(mCalorias));

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
                        Intent intent = new Intent(PerfilActivity.this, EditarActivity.class);
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("username", usernameFromDB);
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
