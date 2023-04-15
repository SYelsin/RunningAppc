package com.example.runningapp.clases;

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

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {
    private List<User> userList;
    private static OnItemClickListener listener;

    public UserListAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personas_adapter_layout, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.nombreTextView.setText(user.getUsername());
        holder.distanciaTextView.setText(user.getDistancia() + " km");
        holder.caloriasTextView.setText(user.getCalorias());
        holder.seguidoresTextView.setText(String.valueOf(user.getSeguidores()));
        Glide.with(holder.itemView.getContext()).load(user.getFotoperfil()).into(holder.urlfotoImageView);
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView urlfotoImageView;
        public TextView nombreTextView;
        public TextView distanciaTextView;
        public TextView caloriasTextView;
        public TextView seguidoresTextView;

        public UserViewHolder(View itemView) {
            super(itemView);
            urlfotoImageView = itemView.findViewById(R.id.fotopersona);
            nombreTextView = itemView.findViewById(R.id.nombrepersona);
            distanciaTextView = itemView.findViewById(R.id.distanciapersona);
            caloriasTextView = itemView.findViewById(R.id.caloriaspersona);
            seguidoresTextView = itemView.findViewById(R.id.seguidores);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
