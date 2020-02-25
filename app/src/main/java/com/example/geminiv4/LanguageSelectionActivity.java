package com.example.geminiv4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geminiv4.multilingual.LanguageNavigationHelper;

public class LanguageSelectionActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_selection_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Button en = (Button) findViewById(R.id.en);
        en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedLanguage(0);refresh();
            }
        });
        Button hi = (Button) findViewById(R.id.hi);
        hi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedLanguage(1);refresh();
            }
        });
        Button gj = (Button) findViewById(R.id.gj);
        gj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedLanguage(2);refresh();
            }
        });
        Button mr = (Button) findViewById(R.id.mr);
        mr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedLanguage(3);refresh();
            }
        });
        Button ka = (Button) findViewById(R.id.ka);
        ka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedLanguage(4);refresh();
            }
        });
        Button ml = (Button) findViewById(R.id.ml);
        ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedLanguage(5);refresh();
            }
        });
        Button ta = (Button) findViewById(R.id.ta);
        ta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedLanguage(6);refresh();
            }
        });
        Button te = (Button) findViewById(R.id.te);
        te.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedLanguage(7);refresh();
            }
        });
        Button or = (Button) findViewById(R.id.or);
        or.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedLanguage(8);refresh();
            }
        });
        Button bn = (Button) findViewById(R.id.bn);
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedLanguage(9);
                refresh();
            }
        });




    }

    private void setSelectedLanguage(int lid){
        new LanguageNavigationHelper(this).setSelectedLanguage(lid);
    }

    private void refresh(){
        Intent refresh = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(refresh);
        finish();
    }


}








