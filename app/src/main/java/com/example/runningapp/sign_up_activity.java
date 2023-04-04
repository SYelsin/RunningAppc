package com.example.runningapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class sign_up_activity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword, signupNombre, signupUsuario;
    private Button signupButton;
    private TextView loginRedirectText;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupNombre = findViewById(R.id.signup_nombre);
        signupUsuario = findViewById(R.id.signup_usuario);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                createUser();
            }
        });
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(sign_up_activity.this, LoginActivity.class));
            }
        });
    }

    private void createUser() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");
        String name = signupNombre.getText().toString().trim();
        String email = signupEmail.getText().toString().trim();
        String username = signupUsuario.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();
        double calorias = 0.00;
        double distancia = 0.00;
        int pasos = 0;
        int ritmo = 0;
        int tiempo = 0;
        // Verificar si el usuario ya existe antes de crear un nuevo registro
        reference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // El usuario ya existe
                    signupUsuario.setError("El usuario ya existe");
                } else {
                    // El usuario no existe, se crea un nuevo registro
                    HelperClass helperClass = new HelperClass(name, email, username, password,tiempo,ritmo,calorias,distancia,pasos);
                    reference.child(username).setValue(helperClass);
                    Toast.makeText(sign_up_activity.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();

                    // Registrar al usuario también en Firebase Authentication
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // El usuario se ha registrado con éxito en Firebase Authentication
                                    Toast.makeText(sign_up_activity.this, "Registro Exitoso en Authentication", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Ha ocurrido un error al registrar el usuario en Firebase Authentication
                                    Toast.makeText(sign_up_activity.this, "Error al registrar en Authentication", Toast.LENGTH_SHORT).show();
                                }
                            });

                    // Redireccionar al usuario a la pantalla de inicio de sesión
                    Intent intent = new Intent(sign_up_activity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejo del error de la consulta
                Toast.makeText(sign_up_activity.this, "Error al verificar el usuario", Toast.LENGTH_SHORT).show();
            }
        });

    }
}