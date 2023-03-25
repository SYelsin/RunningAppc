package com.example.runningapp;

import static com.example.runningapp.R.id.backbtn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.runningapp.clases.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    ViewPager mSLideViewPager;
    LinearLayout mDotLayout;
    Button backbtn, nextbtn, skipbtn;

    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_KEY_SLIDER_SHOWED = "slider_showed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getSupportActionBar().hide();

        setTheme(R.style.Theme_RunningApp);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean sliderShowed = settings.getBoolean(PREF_KEY_SLIDER_SHOWED, false);

        if (sliderShowed) {
            // El slider ya se ha mostrado, redirigir a la pantalla principal de la aplicación
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            // El slider aún no se ha mostrado, mostrarlo
            setContentView(R.layout.activity_main);

            backbtn = findViewById(R.id.backbtn);
            nextbtn = findViewById(R.id.nextbtn);
            skipbtn = findViewById(R.id.skipButton);

            backbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getitem(0) > 0){
                        mSLideViewPager.setCurrentItem(getitem(-1),true);
                    }
                }
            });

            nextbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getitem(0) < 2)
                        mSLideViewPager.setCurrentItem(getitem(1),true);
                    else {
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            });

            skipbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            });

            mSLideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
            mDotLayout = (LinearLayout) findViewById(R.id.indicator_layout);

            viewPagerAdapter = new ViewPagerAdapter(this);

            mSLideViewPager.setAdapter(viewPagerAdapter);

            setUpindicator(0);
            mSLideViewPager.addOnPageChangeListener(viewListener);

            // Establecer la bandera en verdadera después de que el usuario haya visto el slider
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(PREF_KEY_SLIDER_SHOWED, true);
            editor.apply();
        }
    }

    public void setUpindicator(int position){
        dots = new TextView[3];
        mDotLayout.removeAllViews();

        for (int i = 0 ; i < dots.length ; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.inactive,getApplicationContext().getTheme()));
            mDotLayout.addView(dots[i]);
        }

        dots[position].setTextColor(getResources().getColor(R.color.active,getApplicationContext().getTheme()));
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setUpindicator(position);

            if (position > 0){
                backbtn.setVisibility(View.VISIBLE);
            }else {
                backbtn.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private int getitem(int i){

        return mSLideViewPager.getCurrentItem() + i;
    }
    }
