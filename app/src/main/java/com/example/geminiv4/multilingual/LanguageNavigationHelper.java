package com.example.geminiv4.multilingual;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geminiv4.MainActivity;
import com.example.geminiv4.R;

import java.util.Locale;


public class LanguageNavigationHelper {

    String[] listItems;
    int checkedItem;
    int mUserItem;
    Context ctx;
    LanguageSelectionSharedPreferencesHelper ldb;
    Locale myLocale;
    String currentLanguage, currentLang;

    public LanguageNavigationHelper(Context ctx) {
        this.listItems = ctx.getResources().getStringArray(R.array.language_array);
        ldb = new LanguageSelectionSharedPreferencesHelper(ctx, this.listItems);
        this.checkedItem = ldb.getSelectedLanguageID();
        this.mUserItem = this.checkedItem;
        this.ctx = ctx;
        currentLanguage = ldb.getSelectedLanguageISOCode();
    }


    public void navLanguage() {
        AlertDialog.Builder popuplansele = new AlertDialog.Builder(ctx);
        popuplansele.setTitle("Please Select language");
        checkedItem = ldb.getSelectedLanguageID();
        popuplansele.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mUserItem = i;
                checkedItem = i;
            }
        });
        popuplansele.setCancelable(false);
        popuplansele.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                ldb.setLanguage(mUserItem);
                setLocale(ldb.getSelectedLanguageISOCode());
            }
        });
        popuplansele.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = popuplansele.create();
        dialog.show();
    }


    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = ctx.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refreshIntent = new Intent(ctx, MainActivity.class);
        refreshIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ctx.startActivity(refreshIntent);
        ((AppCompatActivity) ctx).finish();
    }


    public String getSelectedLanguageISOCode() {
        return ldb.getSelectedLanguageISOCode();
    }

    public int getSelectedLanguageIndex() {
        return ldb.getSelectedLanguageID();
    }

    public LanguageSelectionSharedPreferencesHelper getLdb() {
        return ldb;
    }

    public void setLdb(LanguageSelectionSharedPreferencesHelper ldb) {
        this.ldb = ldb;
    }

    public void setSelectedLanguage(int lid){
        ldb.setLanguage(lid);
    }

    public String getZoneFromStringsFile(String zone_en) {
        int index = -1;
        String[] zones = {"Gujarat", "Maharashtra", "Goa", "Karnataka", "Kerala", "SouthTamilNadu", "NorthTamilNadu", "SouthAndhraPradesh", "NorthAndhraPradesh", "Odisha", "WestBengal", "Andaman", "Nicobar", "Lakshadweep", "EastCoast", "WestCoast", "ALLINDIA"};
        for (int l = 0; l < zones.length; l++) {
            if (zones[l].equalsIgnoreCase(zone_en)) {
                index = l;
                break;
            }
        }
        String[] retstr = ctx.getResources().getStringArray(R.array.region_array);
        if (index == -1) {
            return zone_en;
        } else {
            return retstr[index];
        }
    }

}


class LanguageSelectionSharedPreferencesHelper {

    SharedPreferences pref;
    Context context;
    String[] listItems;

    /**
     * @param context
     * @param listItems
     */
    public LanguageSelectionSharedPreferencesHelper(Context context, String[] listItems) {
        this.context = context;
        this.listItems = listItems;
        pref = context.getSharedPreferences("preferences", 0);
    }

    /**
     * retuns the id of the selected language
     */
    public int getSelectedLanguageID() {
        SharedPreferences.Editor editor = pref.edit();
        int lan_index = pref.getInt("selected_language_id", -1);
        return lan_index;
    }

    /**
     * returns the iso code of the selected language
     */
    public String getSelectedLanguageISOCode() {
        SharedPreferences.Editor editor = pref.edit();
        String isocode = pref.getString("selected_language_iso", null);
        return isocode;
    }


    /**
     * @param lang_index updates the selected language
     */
    public boolean setLanguage(int lang_index) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("selected_language_id", lang_index);
        editor.putString("selected_language_name", listItems[lang_index]);
        editor.putString("selected_language_iso", getLanguageISOCOde(lang_index));
        editor.commit();
        return true;
    }


    private String getLanguageISOCOde(int index) {
        String isocode = null;
        switch (index) {
            case 0:
                isocode = ("en");
                break;
            case 1:
                isocode = ("hi");
                break;
            case 2:
                isocode = ("gu");
                break;
            case 3:
                isocode = ("mr");
                break;
            case 4:
                isocode = ("ka");
                break;
            case 5:
                isocode = ("ml");
                break;
            case 6:
                isocode = ("ta");
                break;
            case 7:
                isocode = ("te");
                break;
            case 8:
                isocode = ("or");
                break;
            case 9:
                isocode = ("bn");
                break;
        }
        return isocode;
    }
}
