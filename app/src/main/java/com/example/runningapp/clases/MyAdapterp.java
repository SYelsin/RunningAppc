package com.example.runningapp.clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.runningapp.R;

import java.util.ArrayList;

public class MyAdapterp extends BaseAdapter {

    private ArrayList<DataClass> dataList;
    private Context context;
    LayoutInflater layoutInflater;

    public MyAdapterp(Context context, ArrayList<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (layoutInflater == null){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null){
            view = layoutInflater.inflate(R.layout.grid_item, null);
        }

        ImageView gridImage = view.findViewById(R.id.gridImage);

        // cargar la imagen con Glide en la vista de ImageView
        Glide.with(context).load(dataList.get(i).getImageURL()).into(gridImage);

        // agregar listener de clic en la vista de ImageView
        gridImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // inflar el diseño personalizado para el diálogo
                View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_custom_image, null);
                ImageView dialogImage = dialogView.findViewById(R.id.dialog_image_view);
                ImageView closeButton = dialogView.findViewById(R.id.dialog_close_button);

                // cargar la imagen con Glide en la vista de ImageView del diálogo
                Glide.with(context).load(dataList.get(i).getImageURL()).into(dialogImage);

                // establecer el ancho y la altura de la vista de ImageView
                dialogImage.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));


                // construir y mostrar el diálogo personalizado
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
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

        return view;
    }
}
