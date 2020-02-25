package com.example.geminiv4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geminiv4.sqlite.FishMarketDataBase;
import com.example.geminiv4.sqlite.NavicFishLandingCenters;
import com.example.geminiv4.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

public class RegistrationActivity extends AppCompatActivity  {

    NavicFishLandingCenters navicFishLandingCenters;
    String[] statearray;
    FishMarketDataBase fishMarketDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        navicFishLandingCenters = new NavicFishLandingCenters();
        fishMarketDataBase = new FishMarketDataBase(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final NavicFishLandingCenters navicFishLandingCenters = new NavicFishLandingCenters();

        Spinner spinner = (Spinner) findViewById(R.id.state);
        statearray = getResources().getStringArray(R.array.region_array_registration);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setFlcItems(position);
                setMarketItems(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Button register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref = getSharedPreferences("preferences", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("userid", 1);
                editor.commit();

                Intent refresh = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(refresh);
                finish();
            }
        });
    }



    private void setFlcItems(int position){
        Spinner spinner = (Spinner) findViewById(R.id.FLC_spinner);
        LinkedHashMap<Integer, String> flcs = navicFishLandingCenters.getFishLandingcenters(statearray[position]);
        ArrayList<String> flcnames = new ArrayList<>();
        for(Integer key : flcs.keySet()){
            flcnames.add(flcs.get(key));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, flcnames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    private void setMarketItems(int position){
        Spinner spinner = (Spinner) findViewById(R.id.market);
        Log.d("Markets["+statearray[position]+"]", fishMarketDataBase.getMarketNames(statearray[position]).toString());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, fishMarketDataBase.getMarketNames(statearray[position]));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }
}








