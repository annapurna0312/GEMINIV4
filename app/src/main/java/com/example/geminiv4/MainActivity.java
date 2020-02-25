package com.example.geminiv4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.geminiv4.ui.home.HomeViewModel;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity  {

    public static String TAG = "MainActivity";

    private Context context;


    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    HomeViewModel homeViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /*SharedPreferences.Editor editor = pref.edit();
        editor.remove("userid");
        editor.commit();*/
        SharedPreferences pref = getSharedPreferences("preferences", 0);

        if(!pref.contains("selected_language_id")){
            Intent refresh = new Intent(this, LanguageSelectionActivity.class);
            this.startActivity(refresh);
            finish();
        }else if (!pref.contains("userid")) {
            Intent refresh = new Intent(this, RegistrationActivity.class);
            this.startActivity(refresh);
            finish();
        }else{
            setContentView(R.layout.activity_main);
        }
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /*DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_tools, R.id.nav_share, R.id.nav_send).setDrawerLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);*/
    }

}








