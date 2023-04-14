package com.example.runningapp.clases;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.runningapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<DataClass> dataList;
    private Context context;
    private long lastClickTime = 0;

    public MyAdapter(Context context, ArrayList<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataClass data = dataList.get(position);
        holder.recyclerCaption.setText(data.getCaption());


        // Aquí se realiza la búsqueda del username y la foto de perfil en la tabla de usuarios utilizando el userId
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(data.getUserName());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("username").getValue(String.class);
                    holder.username.setText(username);

                    String photoUrl = snapshot.child("fotoperfil").getValue(String.class);
                    Glide.with(context).load(photoUrl).into(holder.perfil);
                } else {
                    holder.username.setText("error");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejo de errores
            }
        });

        Glide.with(context).load(data.getImageURL()).into(holder.recyclerImage);

        // Agrega un oyente de clics a la imagen para la animación del corazón
        holder.recyclerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastClickTime < 1000) {
                    holder.corazon.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.corazon.setVisibility(View.GONE);
                        }
                    }, 500);
                    holder.like.setImageResource(R.drawable.corazon);


                }
                lastClickTime = currentTime;
            }
        });
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.like.setImageResource(R.drawable.corazon);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView recyclerImage;

        ImageView corazon, like;
        TextView recyclerCaption;
        TextView username;
        CircleImageView perfil;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerImage = itemView.findViewById(R.id.recyclerImage);
            recyclerCaption = itemView.findViewById(R.id.recyclerCaption);
            username = itemView.findViewById(R.id.usernamen);
            perfil = itemView.findViewById(R.id.perfilpost);
            corazon = itemView.findViewById(R.id.recyclerLike);
            corazon.setVisibility(View.GONE);
            like = itemView.findViewById(R.id.like);
        }
    }
}
