package com.example.geminiv4.ui.cyclone;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.fragment.app.Fragment;

import com.example.geminiv4.R;
import com.example.geminiv4.sqlite.FireBaseUtils;
import com.example.geminiv4.ui.osf.WarningInfo;
import com.example.geminiv4.ui.pfz.FlcWisePfz;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mapzen.speakerbox.Speakerbox;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay2;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class CycloneFragment extends Fragment {


    private View root;
    MapView mapView = null;
    IMapController mapController;
    DatabaseReference database;
    BottomSheetBehavior<View> mBottomSheetBehavior;



    @Override
    public void onResume() {
        super.onResume();
        database.child("cyclonedata").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    CycloneDTO cyclone = noteDataSnapshot.getValue(CycloneDTO.class);
                    setCenter(new GeoPoint(Double.parseDouble(cyclone.getLat()),Double.parseDouble(cyclone.getLon())),8);
                    updateUI(cyclone);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void updateUI(CycloneDTO cycloneDTO){
        List<GeoPoint> forecastedtrack = new ArrayList<>();
        forecastedtrack.add(new GeoPoint(Double.parseDouble(cycloneDTO.getLat()),Double.parseDouble(cycloneDTO.getLon())));
        forecastedtrack.add(new GeoPoint(Double.parseDouble(cycloneDTO.getForecastedlat1()),Double.parseDouble(cycloneDTO.getForecastedlon1())));
        forecastedtrack.add(new GeoPoint(Double.parseDouble(cycloneDTO.getForecastedlat2()),Double.parseDouble(cycloneDTO.getForecastedlon2())));
        forecastedtrack.add(new GeoPoint(Double.parseDouble(cycloneDTO.getForecastedlat3()),Double.parseDouble(cycloneDTO.getForecastedlon3())));
        addForecastPolyline(forecastedtrack);
        addForecastPolyline(forecastedtrack);

        List<GeoPoint> observedtrack = new ArrayList<>();
        observedtrack.add(new GeoPoint(Double.parseDouble(cycloneDTO.getObservedlat3()),Double.parseDouble(cycloneDTO.getObservedlon3())));
        observedtrack.add(new GeoPoint(Double.parseDouble(cycloneDTO.getObservedlat2()),Double.parseDouble(cycloneDTO.getObservedlon2())));
        observedtrack.add(new GeoPoint(Double.parseDouble(cycloneDTO.getObservedlat1()),Double.parseDouble(cycloneDTO.getObservedlon1())));
        observedtrack.add(new GeoPoint(Double.parseDouble(cycloneDTO.getLat()),Double.parseDouble(cycloneDTO.getLon())));
        addObservationPolyline(observedtrack);
        addObservationPolyline(observedtrack);

        addCurrentLocationMarker(new GeoPoint(Double.parseDouble(cycloneDTO.getLat()),Double.parseDouble(cycloneDTO.getLon())),cycloneDTO.getMaxws(),cycloneDTO.getMaxcs(),cycloneDTO.getMaxwh());
        ((TextView)this.root.findViewById(R.id.advice)).setText(cycloneDTO.getAdvice());

    }

    float trackwidth = 5.0f;
    int pointradius = 10;
    public void addObservationPolyline(List<GeoPoint> geoPoints){
        Polyline line = new Polyline();   //see note below!
        line.setPoints(geoPoints);
        line.setWidth(trackwidth);
        line.setColor(Color.argb(100,0,0,0));
        for(GeoPoint geoPoint : geoPoints){
            List<GeoPoint> circle = Polygon.pointsAsCircle(geoPoint,1000 * pointradius);
            Polygon p = new Polygon(mapView);
            p.setStrokeWidth(0);
            p.setPoints(circle);
            p.setFillColor(Color.argb(100,0,0,0));
            mapView.getOverlayManager().add(p);

        }
        mapView.getOverlayManager().add(line);
    }

    public void addForecastPolyline(List<GeoPoint> geoPoints){
        Polyline line = new Polyline();   //see note below!
        line.setPoints(geoPoints);
        line.setWidth(trackwidth);
        line.setColor(Color.argb(100,255,0,0));
        for(GeoPoint geoPoint : geoPoints){
            List<GeoPoint> circle = Polygon.pointsAsCircle(geoPoint,1000 * pointradius);
            Polygon p = new Polygon(mapView);
            p.setStrokeWidth(0);
            p.setPoints(circle);
            p.setFillColor(Color.argb(100,255,0,0));
            mapView.getOverlayManager().add(p);

        }
        mapView.getOverlayManager().add(line);
    }

    public void addCurrentLocationMarker(GeoPoint geoPoint, String ws,String cs,String wh){
        Marker currentlocation = new Marker(mapView);
        currentlocation.setPosition(geoPoint);
        currentlocation.setIcon(getContext().getResources().getDrawable(R.drawable.cyclone));
        String title = "Wind Speed : "+ws+"\n"+
                "Current Speed : "+cs+"\n"+
                "Wave Height : "+wh;
        currentlocation.setTitle(title);
        currentlocation.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        currentlocation.setPanToView(true);
        currentlocation.setDraggable(false);
        currentlocation.showInfoWindow();
        mapView.getOverlays().add(currentlocation);
    }















































































    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_cyclone, container, false);
        init();
        /*Speakerbox speakerbox = new Speakerbox(getActivity().getApplication());
        speakerbox.remix("min", "minutes");
        speakerbox.play("");*/

        return root;
    }


    public void init(){
        database = FireBaseUtils.getDatabaseReference();
        Toolbar toolbar = (Toolbar) this.root.findViewById(R.id.app_bar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        //map controls
        mapView = this.root.findViewById(R.id.cyclonemap);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        String[] urlArray = {"http://osm.incois.gov.in/osm_tiles/"};
        XYTileSource xYTileSource = new XYTileSource("ArcGisOnline", 0, 25, 256, ".png", urlArray);
        mapView.setTileSource(xYTileSource);
        LatLonGridlineOverlay2 overlay = new LatLonGridlineOverlay2();
        mapView.getOverlays().add(overlay);
        setCenter(new GeoPoint(12.901744, 77.769098),5);
        View bottomSheet = this.root.findViewById(R.id.cycclone_bottomsheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(200);
        mBottomSheetBehavior.setHideable(false);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_DRAGGING);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {if (newState == BottomSheetBehavior.STATE_COLLAPSED) {mBottomSheetBehavior.setPeekHeight(200);}}
            @Override
            public void onSlide(View bottomSheet, float slideOffset) {}
        });
    }

    public void setCenter(final GeoPoint geoPoint, int zoom) {
        if(geoPoint!=null){
            mapView.getController().setCenter(geoPoint);
            mapView.getController().setZoom(zoom);
        }
    }
}

