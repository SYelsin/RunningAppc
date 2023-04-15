package com.example.runningapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.runningapp.clases.DataClass;
import com.example.runningapp.clases.MyAdapterp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailActivity extends AppCompatActivity {
    private Button seguirbtn;
    private String uName, uUsername, uFoto;
    private String uTiempo;
    private String uDistancia, uCalorias;
    private int seguidores, seguidos;
    String userActual;

    private GridView gridView;
    private ArrayList<DataClass> dataList;
    private MyAdapterp adapter;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Post");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        TextView name = findViewById(R.id.nombreusuario);
        TextView Distancia = findViewById(R.id.distanciausuario);
        TextView Tiempo = findViewById(R.id.tiempousario);
        TextView Calorias = findViewById(R.id.caloriasusuario);
        TextView Seguidores = findViewById(R.id.seguidoresusuario);
        TextView Seguidos = findViewById(R.id.seguidosusuario);
        CircleImageView Foto = findViewById(R.id.fotousuario);
        seguirbtn = findViewById(R.id.btnseguir);


        Intent intent = getIntent();
        uUsername = intent.getStringExtra("username");
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uUsername);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    uName = snapshot.child("username").getValue().toString();
                    uCalorias = snapshot.child("calorias").getValue().toString();
                    uDistancia = snapshot.child("distancia").getValue().toString();
                    uTiempo = snapshot.child("tiempo").getValue().toString();
                    uFoto = snapshot.child("fotoperfil").getValue().toString();
                    seguidores = Integer.parseInt(snapshot.child("seguidores").getValue().toString());
                    seguidos = Integer.parseInt(snapshot.child("seguidos").getValue().toString());
                    // actualizar los campos de TextViews
                    name.setText(uName);
                    Distancia.setText(uDistancia);
                    Tiempo.setText(uTiempo + " hrs ");
                    Calorias.setText(uCalorias);
                    Seguidores.setText(String.valueOf(seguidores));
                    Seguidos.setText(String.valueOf(seguidos));
                    Glide.with(getApplicationContext()).load(uFoto).into(Foto);

                    Foto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(UserDetailActivity.this);

                            // inflar el diseño personalizado para el diálogo
                            View view = getLayoutInflater().inflate(R.layout.dialog_custom_image, null);
                            ImageView imageView = view.findViewById(R.id.dialog_image_view);
                            ImageView closeButton = view.findViewById(R.id.dialog_close_button);

                            // cargar la imagen con Glide en la vista de ImageView
                            Glide.with(getApplicationContext())
                                    .load(uFoto)
                                    .into(imageView);

                            // establecer el ancho y la altura de la vista de ImageView
                            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));



                            // establecer la vista personalizada en el diálogo
                            builder.setView(view);

                            // crear el diálogo y asignarlo a la variable 'dialog'
                            final AlertDialog dialog = builder.create();

                            // mostrar el diálogo
                            dialog.show();

                            // agregar un OnClickListener al botón de cerrar
                            closeButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    });





                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });
        datos myApp = (datos) getApplicationContext();
        userActual = myApp.getUsername();

        DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference("users").child(userActual);
        DatabaseReference targetUserRef = FirebaseDatabase.getInstance().getReference("users").child(uUsername);

        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.child("siguiendo").hasChild(uUsername)) {
                        // El usuario actual ya sigue al usuario objetivo
                        seguirbtn.setText("Dejar de seguir");
                        seguirbtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.detener)));
                    } else {
                        // El usuario actual no sigue al usuario objetivo
                        seguirbtn.setText("Seguir");
                        seguirbtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.iniciar)));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });


        seguirbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference("users").child(userActual);
                DatabaseReference targetUserRef = FirebaseDatabase.getInstance().getReference("users").child(uUsername);

                // Revisar si el usuario actual ya sigue al usuario objetivo
                currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            if (snapshot.child("siguiendo").hasChild(uUsername)) {
                                // El usuario actual ya sigue al usuario objetivo
                                seguirbtn.setText("Seguir");
                                seguirbtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.iniciar)));
                                targetUserRef.child("seguidores").setValue(seguidores-1);
                                currentUserRef.child("seguidos").setValue(seguidos-1);
                                currentUserRef.child("siguiendo").child(uUsername).removeValue();

                                // Eliminar la referencia a la subtabla "seguidoresname"
                                targetUserRef.child("seguidoresname").child(myApp.getUsername()).removeValue();

                            } else {
                                // El usuario actual no sigue al usuario objetivo
                                seguirbtn.setText("Dejar de seguir");
                                seguirbtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.detener)));
                                targetUserRef.child("seguidores").setValue(seguidores+1);
                                currentUserRef.child("seguidos").setValue(seguidos+1);
                                currentUserRef.child("siguiendo").child(uUsername).setValue("siguiendo");

                                // Crear la subtabla con el nombre del usuario al que se sigue
                                targetUserRef.child("seguidoresname").child(myApp.getUsername()).setValue(true);

                                // Guardar la información del usuario seguido en la subtabla "siguiendo"
                                currentUserRef.child("siguiendo").child(uUsername).setValue("siguiendo");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w(TAG, "loadPost:onCancelled", error.toException());
                    }
                });
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
                    if (dataClass.getUserName().equals(uUsername)) {
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



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition transition = getWindow().getSharedElementEnterTransition();
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {}

                @Override
                public void onTransitionEnd(Transition transition) {
                    // Realizar acciones una vez finalizada la transición
                }

                @Override
                public void onTransitionCancel(Transition transition) {}

                @Override
                public void onTransitionPause(Transition transition) {}

                @Override
                public void onTransitionResume(Transition transition) {}
            });
        }

    }
}