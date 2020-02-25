package com.example.geminiv4.ui.pfz;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.geminiv4.R;
import com.example.geminiv4.bt.BluetoothChatService;
import com.example.geminiv4.devicedata.GPSInfo;
import com.example.geminiv4.devicedata.IncoisMessage;
import com.example.geminiv4.multilingual.LanguageNavigationHelper;
import com.example.geminiv4.sqlite.MessagesHandler;
import com.example.geminiv4.sqlite.NavigationDataHandler;
import com.example.geminiv4.utils.LatLonUtils;
import com.example.geminiv4.voice.NavigationVoiceUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapzen.speakerbox.Speakerbox;

import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay2;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class PFZMapFragment extends Fragment {


    private View root;
    MapView mapView = null;
    Marker currentlocation;

    private boolean fabExpanded;
    private boolean fabOverlaysExpanded;
    private FloatingActionButton fabSettings;
    private FloatingActionButton fabOverlays;
    private LinearLayout layoutFabMarkRockyBottom;
    private LinearLayout layoutFabSavePFZLocation;
    private LinearLayout layoutFabEdit;
    private LinearLayout layoutFabPhoto;
    private LinearLayout layoutFabDangerZone;
    private LinearLayout layoutFabSunkenship;
    private LinearLayout layoutOverPFZ;
    private LinearLayout layoutOverlayRockyBottom;
    private LinearLayout layoutOverlaySunkenShips;


    NavigationDataHandler navigationDataHandler;
    MessagesHandler messagesHandler;
    GeoPoint destination;
    ScheduledExecutorService service;
    private String speed;
    private String direction;
    LanguageNavigationHelper languageNavigationHelper;
    NavigationVoiceUtils voice;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_pfzmap, container, false);
        Bundle arguments = getArguments();
        String nav_lat = arguments.getString("nav_lat");
        String nav_lon = arguments.getString("nav_lon");
        String nav_dep = arguments.getString("nav_dep");
        String nav_maxwh = arguments.getString("nav_maxwh");
        String nav_maxws = arguments.getString("nav_maxws");
        String nav_maxcs = arguments.getString("nav_maxcs");
        init();
        if(LatLonUtils.isLatValid(nav_lat) && LatLonUtils.isLonValid(nav_lon)){
            addDestinationMarker(
                    new GeoPoint(Double.parseDouble(nav_lat),Double.parseDouble(nav_lon)),
                    this.root.getContext().getString(R.string.pfz_depth_header)+" : "+nav_dep+
                            "\nWind Speed (Max): "+nav_maxws+
                            "\nWave Height(Max): "+nav_maxwh+
                            "\nCurrent Speed (Max): "+nav_maxcs
            );
            destination = new GeoPoint(Double.parseDouble(nav_lat),Double.parseDouble(nav_lon));
        }
        startTracking();
        return root;
    }


    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            GPSInfo data = (GPSInfo) intent.getSerializableExtra("gpsinfo");
            if(!data.getLatitude().equalsIgnoreCase("Fetching...") && !data.getLongitude().equalsIgnoreCase("Fetching...")){
                ((LinearLayout) root.findViewById(R.id.maplocationdetails)).setVisibility(View.VISIBLE);
                ((TextView) root.findViewById(R.id.lat)).setText(data.getLatitude());
                ((TextView) root.findViewById(R.id.lon)).setText(data.getLongitude());
                ((TextView) root.findViewById(R.id.distance)).setText(data.getTime());
                if(getContext()!=null){
                    currentLocationMarker(new GeoPoint(Double.parseDouble(data.getLatitude()),Double.parseDouble(data.getLongitude())));
                }
                speed = data.getSpeed();
                direction = data.getHeading();
            }
        }
    };
    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(root.getContext()).registerReceiver(mMessageReceiver, new IntentFilter("receiverdata"));
        super.onResume();
    }






    public void markLocation(final String type) {
        AlertDialog.Builder popuplansele = new AlertDialog.Builder(getContext(),AlertDialog.THEME_HOLO_LIGHT);
        popuplansele.setTitle("Save "+type+" Location");
        popuplansele.setCancelable(false);
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getContext());
        final View promptsView = li.inflate(R.layout.prompt_latlon, null);
        final EditText latdd = ((EditText) promptsView.findViewById(R.id.latDD));
        final EditText londd = ((EditText) promptsView.findViewById(R.id.lonDD));
        if(BluetoothChatService.isDeviceConnected){
            ((LinearLayout) promptsView.findViewById(R.id.usepresentlatlon_ll)).setVisibility(View.VISIBLE);
        }else{
            ((LinearLayout) promptsView.findViewById(R.id.usepresentlatlon_ll)).setVisibility(View.GONE);
        }
        ((Button) promptsView.findViewById(R.id.usepresentlatlon)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lat = ((TextView) root.findViewById(R.id.lat)).getText().toString();
                String lon = ((TextView) root.findViewById(R.id.lon)).getText().toString();
                latdd.setText(lat);
                londd.setText(lon);
                //TODO if lat lon format converstion is need then change here
                /*int d = Integer.valueOf(lat);
                String	m = new DecimalFormat("#.####").format((double) ((Double.valueOf(lat) - d) * 60));*/
            }
        });
        popuplansele.setView(promptsView);
        popuplansele.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
        Toast.makeText(root.getContext(),
                        ((TextView) root.findViewById(R.id.lat)).getText()+" : "+((TextView) root.findViewById(R.id.lon)).getText()+" Location Marked as "+type,
                        Toast.LENGTH_SHORT).show();
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        service.shutdown();
    }

    public void init(){
        languageNavigationHelper = new LanguageNavigationHelper(this.root.getContext());
        voice = new NavigationVoiceUtils(this.root.getContext());
        fabExpanded = false;
        fabOverlaysExpanded = false;
        messagesHandler = new MessagesHandler(this.root.getContext());
        navigationDataHandler = new NavigationDataHandler(this.root.getContext());
        Toolbar toolbar = (Toolbar) this.root.findViewById(R.id.app_bar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        if(!BluetoothChatService.isDeviceConnected){((LinearLayout) this.root.findViewById(R.id.maplocationdetails)).setVisibility(View.GONE);}
        fabSettings = (FloatingActionButton) this.root.findViewById(R.id.fabSetting);
        fabOverlays = (FloatingActionButton) this.root.findViewById(R.id.fabOverlays);




        layoutFabSavePFZLocation = (LinearLayout) this.root.findViewById(R.id.layoutFabSavePFZLocation);
        layoutFabEdit = (LinearLayout) this.root.findViewById(R.id.layoutFabEdit);
        layoutFabPhoto = (LinearLayout) this.root.findViewById(R.id.layoutFabPhoto);
        layoutFabMarkRockyBottom = (LinearLayout) this.root.findViewById(R.id.layoutFabMarkRockyBottom);
        layoutFabDangerZone = (LinearLayout) this.root.findViewById(R.id.layoutFabDangerZone);
        layoutFabSunkenship = (LinearLayout) this.root.findViewById(R.id.layoutFabSunkenShip);
        layoutOverPFZ = (LinearLayout) this.root.findViewById(R.id.layoutOverPFZ);
        layoutOverlayRockyBottom = (LinearLayout) this.root.findViewById(R.id.layoutOverlayRockyBottom);
        layoutOverlaySunkenShips = (LinearLayout) this.root.findViewById(R.id.layoutOverlaySunkenShips);

        fabSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded == true){closeSubMenusFab();}
                else {openSubMenusFab();}
            }
        });
        fabOverlays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabOverlaysExpanded == true){closeOverlaysSubMenusFab();}
                else {openOverlaysSubMenusFab();}
            }
        });
        //Only main FAB is visible in the beginning
        closeSubMenusFab();
        closeOverlaysSubMenusFab();


        //map controls
        mapView = this.root.findViewById(R.id.pfzmap);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        String[] urlArray = {"http://osm.incois.gov.in/osm_tiles/"};
        XYTileSource xYTileSource = new XYTileSource("ArcGisOnline", 0, 25, 256, ".png", urlArray);
        mapView.setTileSource(xYTileSource);
        LatLonGridlineOverlay2 overlay = new LatLonGridlineOverlay2();
        mapView.getOverlays().add(overlay);
        currentlocation = new Marker(mapView);
        setCenter(new GeoPoint(12.901744, 77.769098),5);


        loadGaganPFZLocations();
        setOnclickListners();
    }

    private void startTracking() {
        Log.d("Traking" , "started tracking");
        /*Intent intent = new Intent(getActivity(), com.example.geminiv4.notification.Notification.class);
        PendingIntent p1 = PendingIntent.getBroadcast(this.root.getContext(), 0, intent, 0);
        AlarmManager a = (AlarmManager) this.root.getContext().getSystemService(Context.ALARM_SERVICE);
        a.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,0,2000, p1 );*/
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 0, 5, TimeUnit.MINUTES);
    }

    Runnable runnable = new Runnable() {
        public void run() {
                try{
                    if(LatLonUtils.isLatValid(((TextView) root.findViewById(R.id.lat)).getText().toString()) &&LatLonUtils.isLonValid(((TextView) root.findViewById(R.id.lon)).getText().toString())){
                        Log.d("traking" ,
                                "SRC: "+((TextView) root.findViewById(R.id.lat)).getText().toString()+","+((TextView) root.findViewById(R.id.lon)).getText().toString()+
                                        "\n DEST: "+destination.getLatitude()+","+destination.getLongitude());
                        GeoPoint src = new GeoPoint(
                                Double.parseDouble(((TextView) root.findViewById(R.id.lat)).getText().toString()),
                                Double.parseDouble(((TextView) root.findViewById(R.id.lon)).getText().toString())
                        );
                        String dist = new DecimalFormat("#.##").format(src.distanceToAsDouble(destination)/1000)+" kms";
                        String bearing = new DecimalFormat("#.##").format(src.bearingTo(destination));
                        Log.d("traking" , "Dist: "+dist+"\n DEST: "+bearing);
                        new com.example.geminiv4.notification.Notification().showNotification(
                                getContext(),
                                "Distance : "+dist+"  Bearing : "+bearing,
                                "Speed : "+speed+" Direction : "
                        );
                        /*voice.createPlayListForVoice();
                        voice.play();*/
                        voice.constructPFZNavigationTextAndPlay(((int)(src.distanceToAsDouble(destination)/1000))+"",((int)src.bearingTo(destination))+"",speed);
                        messagesHandler.storeGPSInfo(
                                ((TextView) root.findViewById(R.id.lat)).getText().toString(),
                                ((TextView) root.findViewById(R.id.lon)).getText().toString(),
                                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                        );
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
        }
    };




    private void loadGaganPFZLocations() {
        List<IncoisMessage> pfzlocs = messagesHandler.getMessages("PFZ");
        for(IncoisMessage pfzloc : pfzlocs){
            for(String loc : pfzloc.getMessage().split("\\;")){
                final String elements[] = loc.split("\\,");
                Log.d("PFZLocation",pfzloc.getZone()+" --> "+loc);
                if(LatLonUtils.isLatValid(elements[0]) && LatLonUtils.isLonValid(elements[1])){
                    List<GeoPoint> circle = Polygon.pointsAsCircle(new GeoPoint(Double.parseDouble(elements[0]),Double.parseDouble(elements[1])),1000 * 5);
                    Polygon p = new Polygon(mapView);
                    String title = "Depth  : "+elements[2]
                                   +"\nWave Height (Max) : "+elements[4]
                                   +"\nWind Speed (Max) : "+elements[3]
                                   +"\nCurrent Speed(Max) : "+elements[5];
                    p.setStrokeWidth(0);
                    p.setPoints(circle);
                    p.setTitle(title);
                    p.setFillColor(Color.argb(50,66,133,244));
                    mapView.getOverlayManager().add(p);
                }
            }
        }
        mapView.invalidate();
    }















    public void setCenter(final GeoPoint geoPoint, int zoom) {
        if(geoPoint!=null){
            mapView.getController().setCenter(geoPoint);
            mapView.getController().setZoom(zoom);
        }
    }



    /**
     * Add new {@link Marker} to {@link MapView}
     * @param position Location of the marker
     */
    public void addDestinationMarker(GeoPoint position, String title) {
        Marker marker = new Marker(mapView);
        marker.setPosition(position);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle(title);
        marker.setPanToView(true);
        marker.setDraggable(false);
        marker.showInfoWindow();
        mapView.getOverlays().add(marker);
        mapView.invalidate();
    }

    /**
     * Add new {@link Marker} to {@link MapView}
     * @param position Location of the marker
     */
    public void currentLocationMarker(GeoPoint position) {
        currentlocation.setPosition(position);
        currentlocation.setIcon(getContext().getResources().getDrawable(R.drawable.ic_navigation_black_24dp));
        currentlocation.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        currentlocation.setPanToView(true);
        currentlocation.setDraggable(false);
        mapView.getOverlays().add(currentlocation);
        mapView.invalidate();
    }


    //closes FAB submenus
    private void closeSubMenusFab(){
        layoutFabSunkenship.setVisibility(View.INVISIBLE);
        layoutFabDangerZone.setVisibility(View.INVISIBLE);
        layoutFabMarkRockyBottom.setVisibility(View.INVISIBLE);
        layoutFabSavePFZLocation.setVisibility(View.INVISIBLE);
        layoutFabEdit.setVisibility(View.INVISIBLE);
        layoutFabPhoto.setVisibility(View.INVISIBLE);
        fabSettings.setImageResource(R.drawable.ic_settings_black_24dp);
        fabExpanded = false;
    }

    //Opens FAB submenus
    private void openSubMenusFab(){
        layoutFabSunkenship.setVisibility(View.VISIBLE);
        layoutFabDangerZone.setVisibility(View.VISIBLE);
        layoutFabMarkRockyBottom.setVisibility(View.VISIBLE);
        layoutFabSavePFZLocation.setVisibility(View.VISIBLE);
        layoutFabEdit.setVisibility(View.VISIBLE);
        layoutFabPhoto.setVisibility(View.VISIBLE);
        //Change settings icon to 'X' icon
        fabSettings.setImageResource(R.drawable.ic_clear_red_24dp);
        fabExpanded = true;
    }

    //closes FAB submenus
    private void closeOverlaysSubMenusFab(){
        layoutOverPFZ.setVisibility(View.INVISIBLE);
        layoutOverlayRockyBottom.setVisibility(View.INVISIBLE);
        layoutOverlaySunkenShips.setVisibility(View.INVISIBLE);
        fabOverlays.setImageResource(R.drawable.ic_layers);
        fabOverlaysExpanded = false;
    }

    //Opens FAB submenus
    private void openOverlaysSubMenusFab(){
        layoutOverPFZ.setVisibility(View.VISIBLE);
        layoutOverlayRockyBottom.setVisibility(View.VISIBLE);
        layoutOverlaySunkenShips.setVisibility(View.VISIBLE);
        fabOverlays.setImageResource(R.drawable.ic_layers);
        fabOverlaysExpanded = true;
    }


    private void setOnclickListners(){
        layoutFabSunkenship.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {markLocation("Sunken Ship");}});
        layoutFabDangerZone.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {markLocation("Danger Zone");}});
        layoutFabMarkRockyBottom.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {markLocation("Rocky Bottom");}});
        layoutFabSavePFZLocation.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {markLocation("PFZ");} });

        layoutOverPFZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<GeoPoint> locs =  navigationDataHandler.getPFZLocations();
                for(GeoPoint point : locs){
                    List<GeoPoint> circle = Polygon.pointsAsCircle(point, 100);
                    Polygon p = new Polygon(mapView);
                    p.setPoints(circle);
                    p.setFillColor(Color.GREEN);
                    p.setStrokeColor(Color.GREEN);
                    mapView.getOverlayManager().add(p);
                }
                mapView.invalidate();
            }
        });
        layoutOverlayRockyBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<GeoPoint> locs =  navigationDataHandler.getRockyBottomLocations();
                for(GeoPoint point : locs){
                    List<GeoPoint> circle = Polygon.pointsAsCircle(point, 100);
                    Polygon p = new Polygon(mapView);
                    p.setPoints(circle);
                    p.setFillColor(Color.rgb(90,77,65));
                    p.setStrokeColor(Color.RED);
                    mapView.getOverlayManager().add(p);
                }
                mapView.invalidate();
            }
        });
        layoutOverlaySunkenShips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<GeoPoint> locs =  navigationDataHandler.getSunkenShipLocations();
                for(GeoPoint point : locs){
                    List<GeoPoint> circle = Polygon.pointsAsCircle(point, 100);
                    Polygon p = new Polygon(mapView);
                    p.setPoints(circle);
                    p.setFillColor(Color.RED);
                    p.setStrokeColor(Color.RED);
                    mapView.getOverlayManager().add(p);
                }
                mapView.invalidate();
            }
        });

    }

}

