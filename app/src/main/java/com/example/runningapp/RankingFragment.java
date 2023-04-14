package com.example.runningapp;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.runningapp.clases.corredor;
import com.example.runningapp.clases.corredorAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RankingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RankingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference corredoresRef;
    private List<corredor> corredores;



    public RankingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RankingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RankingFragment newInstance(String param1, String param2) {
        RankingFragment fragment = new RankingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        corredoresRef = database.getReference("users");

        // Ordenar corredores por distancia y calorias y limitar los resultados a los primeros 3
        Query query = corredoresRef.orderByChild("calorias").limitToLast(3);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                corredores = new ArrayList<>();
                for (DataSnapshot corredorSnapshot : dataSnapshot.getChildren()) {
                    corredor corredor = corredorSnapshot.getValue(corredor.class);
                    corredores.add(corredor);
                }

                // Ordenar la lista de corredores (para obtener los 3 mejores)
                Collections.sort(corredores);

                // Establecer la posición de cada corredor en la lista (para mostrarla en el RecyclerView)
                for (int i = 0; i < corredores.size(); i++) {
                    corredor corredor = corredores.get(i);
                    if (i == 0) {
                        corredor.posicionTexto = "PRIMER LUGAR";
                    } else if (i == 1) {
                        corredor.posicionTexto = "SEGUNDO LUGAR";
                    } else if (i == 2) {
                        corredor.posicionTexto = "TERCER LUGAR";
                    } else {
                        corredor.posicionTexto = String.format(Locale.getDefault(), "%d", i + 1) + "to lugar";
                    }
                }

                // Inicializar y configurar el adaptador para el RecyclerView
                RecyclerView recyclerView = getView().findViewById(R.id.recycler_view);
                corredorAdapter adapter = new corredorAdapter(getActivity(), corredores);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error al obtener los corredores", databaseError.toException());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);
        ImageView imageView = view.findViewById(R.id.confeti);
        Glide.with(this).asGif().load(R.drawable.confetti).into(imageView);

        // Definir un manejador para programar la eliminación del GIF después de 30 segundos
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                imageView.setVisibility(View.GONE);
            }
        }, 5000); // 5 segundos en milisegundos

        return view;
    }


}