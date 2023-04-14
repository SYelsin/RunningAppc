package com.example.runningapp.clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.runningapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class corredorAdapter extends RecyclerView.Adapter<corredorAdapter.CorredorViewHolder> {
    private Context context;
    private List<corredor> corredores;

    // Constructor
    public corredorAdapter(Context context, List<corredor> corredores) {
        this.context = context;
        this.corredores = corredores;
    }

    @NonNull
    @Override
    public CorredorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_corredor, parent, false);
        return new CorredorViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull CorredorViewHolder holder, int position) {
        corredor corredor = corredores.get(position);
        holder.nombreTextView.setText(corredor.name);
        holder.distanciaTextView.setText(corredor.distancia);
        holder.caloriasTextView.setText(corredor.calorias);
//        holder.perfilTextView.setText(corredor.perfil);
        Glide.with(context).load(corredor.fotoperfil).into(holder.perfil);
       // holder.categoriaTextView.setText(corredor.categoria);
        holder.tiempoTextView.setText(corredor.tiempo);
        holder.posicionTextView.setText(corredor.posicionTexto);
    }


    @Override
    public int getItemCount() {
        return corredores.size();
    }

    public static class CorredorViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreTextView;
        public TextView distanciaTextView;
        public TextView caloriasTextView;
        public CircleImageView perfil;
        public TextView categoriaTextView;
        public TextView posicionTextView;
        public TextView tiempoTextView;

        public CorredorViewHolder(View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombretxt);
            distanciaTextView = itemView.findViewById(R.id.distanciatxt);
            caloriasTextView = itemView.findViewById(R.id.caloriastxt);
            perfil = itemView.findViewById(R.id.fototxt);
           // categoriaTextView = itemView.findViewById(R.id.categoriatxt);
            posicionTextView = itemView.findViewById(R.id.categoriatxt);
            tiempoTextView = itemView.findViewById(R.id.tiempotxt);
        }
    }
}
