package com.example.geminiv4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geminiv4.multilingual.LanguageNavigationHelper;
import com.example.geminiv4.sqlite.MessagesHandler;

import java.util.Locale;


public class SplashActivity extends AppCompatActivity{

    @Override
    protected void
        onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

       /* SharedPreferences lidpref = getSharedPreferences("preferences", 0);
        SharedPreferences.Editor lideditor = lidpref.edit();
        lideditor.remove("selected_language_id");
        lideditor.commit();


        SharedPreferences pref = getSharedPreferences("preferences", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("userid");
        editor.commit();
*/


        String lang = new LanguageNavigationHelper(this).getSelectedLanguageISOCode();
        if(lang==null){
            lang = "en";
        }
        new MessagesHandler(this).removeExpiredMessages();
        Locale myLocale = new Locale(lang);
        Resources res = this.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);


        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        this.finish();
    }

}
