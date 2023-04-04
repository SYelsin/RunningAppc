package com.example.runningapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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
    private int mTiempo,mritmo;
    double mDistancia, mCalorias;

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
        mCalorias = myApp.getCalorias();
        mDistancia = myApp.getDistancia();
        mTiempo = myApp.getTiempo();
        mpasos = myApp.getPasos();
        mritmo = myApp.getRitmo();
        duracion.setText(Integer.toString(mTiempo)+" hrs");
        ritmotxt.setText(Integer.toString(mritmo)+" m/min");
        caloriastxt.setText(Double.toString(mCalorias));
        distanciatxt.setText(Double.toString(mDistancia));
        pasostxt.setText(Integer.toString(mpasos));




        return view;
    }
}