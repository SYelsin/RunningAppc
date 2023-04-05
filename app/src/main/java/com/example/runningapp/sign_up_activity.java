package com.example.runningapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

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

        // Validar que los campos no estén vacíos
        if (name.isEmpty()) {
            signupNombre.setError("Por favor ingrese su nombre");
            return;
        }
        if (email.isEmpty()) {
            signupEmail.setError("Por favor ingrese su correo electrónico");
            return;
        }
        if (username.isEmpty()) {
            signupUsuario.setError("Por favor ingrese su usuario");
            return;
        }
        if (password.isEmpty()) {
            signupPassword.setError("Por favor ingrese su contraseña");
            return;
        }

        // Verificar si el usuario ya existe antes de crear un nuevo registro
        auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> signInMethods = task.getResult().getSignInMethods();
                        if (signInMethods == null || signInMethods.isEmpty()) {
                            // El correo electrónico no está registrado en Firebase Authentication
                            // Proceder con el registro

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                            reference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        // El nombre de usuario ya está registrado
                                        signupUsuario.setError("Este nombre de usuario ya está registrado");
                                    } else {
                                        // El nombre de usuario no está registrado, se crea un nuevo registro
                                        HelperClass helperClass = new HelperClass(name, email, username, password, tiempo, ritmo, calorias, distancia, pasos);
                                        reference.child(username).setValue(helperClass)
                                                .addOnCompleteListener(task1 -> {
                                                    if (task1.isSuccessful()) {
                                                        // El registro en la base de datos ha sido exitoso
                                                        Toast.makeText(sign_up_activity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                                                        // Registrar al usuario también en Firebase Authentication
                                                        auth.createUserWithEmailAndPassword(email, password)
                                                                .addOnCompleteListener(task2 -> {
                                                                    if (task2.isSuccessful()) {
                                                                        // El usuario se ha registrado con éxito en Firebase Authentication
                                                                        //Toast.makeText(sign_up_activity.this, "Registro Exitoso en Authentication", Toast.LENGTH_SHORT).show();

                                                                        // Enviar correo de verificación
                                                                        sendVerificationEmail();

                                                                        // Redireccionar al usuario a la pantalla de inicio de sesión
                                                                        Intent intent = new Intent(sign_up_activity.this, LoginActivity.class);
                                                                        startActivity(intent);
                                                                    } else {
                                                                        // Ha ocurrido un error al registrar el usuario en Firebase Authentication
                                                                        Toast.makeText(sign_up_activity.this, "Error al registrar en Authentication", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    } else {
                                                        // Ha ocurrido un error al registrar en la base de datos
                                                        Toast.makeText(sign_up_activity.this, "Error al registrar en la base de datos", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Ha ocurrido un error al consultar la base de datos
                                    Toast.makeText(sign_up_activity.this, "Error al verificar el nombre de usuario", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // El correo electrónico ya está registrado en Firebase Authentication
                            signupEmail.setError("Este correo ya está registrado");
                        }
                    } else {
                        // Ha ocurrido un error al consultar Firebase Authentication
                        Toast.makeText(sign_up_activity.this, "Error al verificar el correo electrónico", Toast.LENGTH_SHORT).show();
                    }
                });
    }
        private void sendVerificationEmail() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // El correo de verificación se ha enviado con éxito
                            Toast.makeText(sign_up_activity.this, "Se ha enviado un correo de verificación a " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        } else {
                            // Ha ocurrido un error al enviar el correo de verificación
                            Toast.makeText(sign_up_activity.this, "Error al enviar correo de verificación", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


}