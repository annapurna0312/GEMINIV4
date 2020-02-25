package com.example.geminiv4.ui.gps;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.geminiv4.R;
import com.example.geminiv4.devicedata.GPSInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class GPSFragment extends Fragment   implements SensorEventListener {

    private GPSViewModel mViewModel;
    ImageView compass_img;
    TextView txt_compass;
    View view;
    int mAzimuth;
    private SensorManager mSensorManager;
    private Sensor mRotationV, mAccelerometer, mMagnetometer;
    boolean haveSensor = false, haveSensor2 = false;
    float[] rMat = new float[9];
    float[] orientation = new float[3];
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private View root;


    public static GPSFragment newInstance() {
        return new GPSFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_gps, container, false);
        Toolbar toolbar = (Toolbar) this.root.findViewById(R.id.app_bar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        mSensorManager = (SensorManager) container.getContext().getSystemService(Context.SENSOR_SERVICE);
        compass_img = ((ImageView) root.findViewById(R.id.compass));
        txt_compass = ((TextView) root.findViewById(R.id.compassdatalayout));
        start();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(GPSViewModel.class);

    }


    public void stop() {
        if (haveSensor) {mSensorManager.unregisterListener(this, mRotationV);}
        else {mSensorManager.unregisterListener(this, mAccelerometer);mSensorManager.unregisterListener(this, mMagnetometer);}
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(root.getContext()).unregisterReceiver(mMessageReceiver);
        super.onPause();
        stop();
    }


    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            GPSInfo data = (GPSInfo) intent.getSerializableExtra("gpsinfo");
            try {

                if(!data.getLatitude().equalsIgnoreCase("Fetching...") && !data.getLongitude().equalsIgnoreCase("Fetching...")){
                    ((TextView) root.findViewById(R.id.lat)).setText(data.getLatitude());
                    ((TextView) root.findViewById(R.id.lon)).setText(data.getLongitude());
                    ((TextView) root.findViewById(R.id.speed)).setText(data.getSpeed()+" kmph");
                    ((TextView) root.findViewById(R.id.datetime)).setText(getDateTime(data.getDate(),data.getTime()));
                    Log.d("datetiemcheck", data.getDate()+"<=>"+data.getTime()+"==>"+getDateTime(data.getDate(),data.getTime()));
                    if(data.getFix()>1){((ImageView) root.findViewById(R.id.fixicon)).setImageResource(R.drawable.icon_fix);}

                    ((TextView) root.findViewById(R.id.nearest_flc)).setText(data.getNearestflc());
                    ((TextView) root.findViewById(R.id.nearest_flc_dist)).setText(data.getNearestflc_dis()+" km");
                    ((TextView) root.findViewById(R.id.nearest_flc_dir)).setText(data.getNearestflc_dir()+" "+getDirectionName(Double.parseDouble(data.getNearestflc_dir())));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(root.getContext()).registerReceiver(mMessageReceiver, new IntentFilter("receiverdata"));
        super.onResume();
        start();
    }






    public String getDirectionName(double mAzimuth){
        String where = root.getContext().getResources().getString(R.string.nw);
        if (mAzimuth >= 350 || mAzimuth <= 10)
            where = root.getContext().getResources().getString(R.string.n);
        if (mAzimuth < 350 && mAzimuth > 280)
            where = root.getContext().getResources().getString(R.string.nw);
        if (mAzimuth <= 280 && mAzimuth > 260)
            where = root.getContext().getResources().getString(R.string.w);
        if (mAzimuth <= 260 && mAzimuth > 190)
            where = root.getContext().getResources().getString(R.string.sw);
        if (mAzimuth <= 190 && mAzimuth > 170)
            where = root.getContext().getResources().getString(R.string.s);
        if (mAzimuth <= 170 && mAzimuth > 100)
            where = root.getContext().getResources().getString(R.string.se);
        if (mAzimuth <= 100 && mAzimuth > 80)
            where = root.getContext().getResources().getString(R.string.e);
        if (mAzimuth <= 80 && mAzimuth > 10)
            where = root.getContext().getResources().getString(R.string.ne);
        return where;
    }






















    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rMat, event.values);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(rMat, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(rMat, orientation);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }
        mAzimuth = Math.round(mAzimuth);
        compass_img.setRotation(-mAzimuth);
        String where = root.getContext().getResources().getString(R.string.nw);
        if (mAzimuth >= 350 || mAzimuth <= 10)
            where = root.getContext().getResources().getString(R.string.n);
        if (mAzimuth < 350 && mAzimuth > 280)
            where = root.getContext().getResources().getString(R.string.nw);
        if (mAzimuth <= 280 && mAzimuth > 260)
            where = root.getContext().getResources().getString(R.string.w);
        if (mAzimuth <= 260 && mAzimuth > 190)
            where = root.getContext().getResources().getString(R.string.sw);
        if (mAzimuth <= 190 && mAzimuth > 170)
            where = root.getContext().getResources().getString(R.string.s);
        if (mAzimuth <= 170 && mAzimuth > 100)
            where = root.getContext().getResources().getString(R.string.se);
        if (mAzimuth <= 100 && mAzimuth > 80)
            where = root.getContext().getResources().getString(R.string.e);
        if (mAzimuth <= 80 && mAzimuth > 10)
            where = root.getContext().getResources().getString(R.string.ne);
        txt_compass.setText(mAzimuth + "° " + where);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    public void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {
            if ((mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) || (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null)) {
                noSensorsAlert();
            }
            else {
                mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                haveSensor = mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
                haveSensor2 = mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
            }
        }
        else{
            mRotationV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            haveSensor = mSensorManager.registerListener(this, mRotationV, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void noSensorsAlert(){
        /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage("Your device doesn't support the Compass.").setCancelable(false).setNegativeButton("Close",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {finish();    }
                });
        alertDialog.show();*/
        //txt_compass.setText("Your device doesn't support the Compass.");
    }


    public String getDateTime(String date,String time) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT"));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat ddf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
        df.parse(date+" "+time);
        c.add(Calendar.DAY_OF_MONTH,2);
        return (ddf.format(c.getTime()));
    }

}
