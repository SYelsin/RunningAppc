package com.example.runningapp;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mTiempo,mritmo;
    String  mCalorias;
    String mDistancia, mUsername;

   private int mpasos;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView ritmotxt = view.findViewById(R.id.txtritmo);
        TextView duracion = view.findViewById(R.id.txtduracion);
        TextView caloriastxt = view.findViewById(R.id.txtcalorias);
        TextView distanciatxt = view.findViewById(R.id.txtdistancia);
        TextView pasostxt = view.findViewById(R.id.txtpasos);

        datos myApp = (datos) getActivity().getApplicationContext();
        mUsername = myApp.getUsername();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(mUsername);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mUsername = snapshot.child("username").getValue().toString();
                    mCalorias = snapshot.child("calorias").getValue().toString();
                    mDistancia =snapshot.child("distancia").getValue().toString();
                    mTiempo = snapshot.child("tiempo").getValue().toString();
                    mritmo = snapshot.child("ritmo").getValue().toString();
                    mpasos = Integer.parseInt(snapshot.child("pasos").getValue().toString());
                    // actualizar los campos de TextViews
                    duracion.setText(mTiempo +" hrs");
                    ritmotxt.setText(mritmo +" m/min");
                    caloriastxt.setText(mCalorias);
                    distanciatxt.setText(mDistancia);
                    pasostxt.setText(Integer.toString(mpasos));


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });







        return view;
    }
}