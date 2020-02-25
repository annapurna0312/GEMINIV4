package com.example.geminiv4.ui.languageselection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geminiv4.MainActivity;
import com.example.geminiv4.R;
import com.example.geminiv4.RegistrationActivity;
import com.example.geminiv4.ui.home.HomeViewModel;

public class LanguageSelectionActivity extends AppCompatActivity  {

    public static String TAG = "LanguageSelectionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref = getSharedPreferences("language", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("lid", 1);
                editor.commit();

                Intent refresh = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(refresh);
                finish();
            }
        });
    }

}








