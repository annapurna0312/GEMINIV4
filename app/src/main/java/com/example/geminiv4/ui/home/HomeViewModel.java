package com.example.geminiv4.ui.home;

import android.util.Log;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.geminiv4.R;
import com.example.geminiv4.devicedata.GPSInfo;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<GPSInfo> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(new GPSInfo());
    }

    public LiveData<GPSInfo> getGPSDetails() {
        return mText;
    }



}