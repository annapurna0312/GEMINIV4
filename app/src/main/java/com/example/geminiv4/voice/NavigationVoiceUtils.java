package com.example.geminiv4.voice;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;

import com.example.geminiv4.R;
import com.example.geminiv4.multilingual.LanguageNavigationHelper;
import com.mapzen.speakerbox.Speakerbox;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class NavigationVoiceUtils {

    Context context;
    Activity activity;
    LanguageNavigationHelper languageNavigationHelper;

    public NavigationVoiceUtils(Context context){
        this.context = context;
        this.activity = (Activity) context;
        this.languageNavigationHelper = new LanguageNavigationHelper(context);
    }


    public void createPlayVoice(Integer audio) {
        playlist.add(audio);
    }

    public void createPlayListForVoice(ArrayList<Integer> playlist) {
        this.playlist = playlist;
    }

    public void createPlayListForVoice() {
        playlist = new ArrayList<>();
        playlist.add(R.raw.en_kilometers);
        playlist.add(R.raw.en_towards);
        playlist.add(R.raw.en_northeast);
    }


    Timer timer;
    MediaPlayer mp;
    ArrayList<Integer> playlist;
    int i=0;



    public void play(){
        mp = MediaPlayer.create(context,playlist.get(0));
        mp.start();
        timer = new Timer();
        if (playlist.size()>1) playNext();
    }
    private void playNext() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mp.reset();
                mp = MediaPlayer.create(context,playlist.get(++i));
                mp.start();
                if (playlist.size() > i+1) {
                    playNext();
                }else{
                    i=0;
                }
            }
        },mp.getDuration()+100);
    }

    public void destroy(){
        if (mp.isPlaying())
            mp.stop();
        timer.cancel();
    }




    public void playFromText(String text){
        Speakerbox speakerbox = new Speakerbox(activity.getApplication());
        speakerbox.remix("min", "minutes");
        speakerbox.play(text);
    }


    public void constructPFZNavigationTextAndPlay(String dist,String dir,String speed){
        Speakerbox speakerbox = new Speakerbox(activity.getApplication());
        speakerbox.remix("min", "minutes");
        int lid = languageNavigationHelper.getSelectedLanguageIndex();
        switch(lid){
            case 0:
                //en
                speakerbox.play("Distance "+dist+" Kilometers, "+dir+" direction, Speed is "+speed+" kilometers per hour");
                break;
            case 1:
                //isocode = ("hi");
                speakerbox.play("Distance "+dist+" Kilometers "+dir+" direction Speed is "+speed+" kilometers per hour");
                break;
            case 2:
                //isocode = ("gu");
                speakerbox.play("Distance "+dist+" Kilometers "+dir+" direction Speed is "+speed+" kilometers per hour");
                break;
            case 3:
                //isocode = ("mr");
                speakerbox.play("Distance "+dist+" Kilometers "+dir+" direction Speed is "+speed+" kilometers per hour");
                break;
            case 4:
                //isocode = ("ka");
                speakerbox.play("Distance "+dist+" Kilometers "+dir+" direction Speed is "+speed+" kilometers per hour");
                break;
            case 5:
                //isocode = ("ml");
                speakerbox.play("Distance "+dist+" Kilometers "+dir+" direction Speed is "+speed+" kilometers per hour");
                break;
            case 6:
                //isocode = ("ta");
                speakerbox.play("Distance "+dist+" Kilometers "+dir+" direction Speed is "+speed+" kilometers per hour");
                break;
            case 7:
                //isocode = ("te");
                speakerbox.play("Distance "+dist+" Kilometers "+dir+" direction Speed is "+speed+" kilometers per hour");
                break;
            case 8:
                //isocode = ("or");
                speakerbox.play("Distance "+dist+" Kilometers "+dir+" direction Speed is "+speed+" kilometers per hour");
                break;
            case 9:
                //isocode = ("bn");
                speakerbox.play("Distance "+dist+" Kilometers "+dir+" direction Speed is "+speed+" kilometers per hour");
                break;
        }

    }



}
