
package com.example.runningapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarActivity extends AppCompatActivity {

    Button updateButton;
    String name, email, username,password,peso,altura,pais,tiempo, ritmo;
    int pasos;
    String calorias;

    String imageUrl,distancia;
    String key, oldImageURL;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    EditText nombretxt,usuariotxt,passwordtxt,pesotxt,alturatxt,emailtxt;
    ImageView actualizarfoto;
    CircleImageView perfil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        updateButton = findViewById(R.id.update_button);
        nombretxt = findViewById(R.id.update_nombre);
        passwordtxt = findViewById(R.id.update_password);
        pesotxt = findViewById(R.id.update_peso);
        alturatxt = findViewById(R.id.update_altura);
        emailtxt = findViewById(R.id.update_email);
        actualizarfoto = findViewById(R.id.update_foto);
        perfil = findViewById(R.id.fotousuario);


        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            perfil.setImageURI(uri);
                        } else {
                            Toast.makeText(EditarActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        datos myApp = (datos) getApplicationContext();
        String name = myApp.getName();
        String email = myApp.getEmail();
        Intent intent = getIntent();
        String password = intent.getStringExtra("password");

        username = myApp.getUsername();
        nombretxt.setText(name);
        passwordtxt.setText(password);
        emailtxt.setText(email);
        pesotxt.setText(myApp.getMpeso());
        alturatxt.setText(myApp.getMaltura());
        pais = myApp.getPais();
        pasos = myApp.getPasos();
        tiempo = myApp.getTiempo();
        distancia = myApp.getDistancia();
        calorias = myApp.getCalorias();
        ritmo = myApp.getRitmo();
        oldImageURL = myApp.getFoto();

        Glide.with(this).load(myApp.getFoto()).into(perfil);
        emailtxt.setEnabled(false);
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(username);

        actualizarfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                finish();
            }
        });
    }
    public void saveData(){
        // Se verifica si se ha seleccionado una imagen nueva
        if (uri == null) {
            imageUrl = oldImageURL;
            updateData();
            return;
        }

        storageReference = FirebaseStorage.getInstance().getReference().child("Usuarios").child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(EditarActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                if(!uriTask.isComplete()) {
                    while (!uriTask.isComplete()) ;
                    Uri urlImage = uriTask.getResult();
                    imageUrl = urlImage.toString();
                    updateData();
                    dialog.dismiss();
                }else{
                    imageUrl = oldImageURL;
                    updateData();
                    dialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }

    public void updateData(){
        name = nombretxt.getText().toString().trim();
        username = username;
        email = emailtxt.getText().toString();
        password = passwordtxt.getText().toString();
        peso = pesotxt.getText().toString();
        altura = alturatxt.getText().toString();
        HelperClass dataClass = new HelperClass(name,email,username,password,imageUrl,pais,peso,altura,tiempo,ritmo,calorias,distancia,pasos);

        databaseReference.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                    reference.delete();
                    Toast.makeText(EditarActivity.this, "Actualizado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditarActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}