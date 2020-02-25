package com.example.geminiv4.ui.sarat;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toolbar;

import com.example.geminiv4.R;
import com.example.geminiv4.sqlite.FireBaseUtils;
import com.example.geminiv4.ui.osf.WarningInfo;
import com.example.geminiv4.ui.pfz.FlcWisePfz;
import com.example.geminiv4.ui.pfz.PFZMapFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay2;

import java.util.ArrayList;
import java.util.List;


public class SARATFragment extends Fragment {


    private View root;
    DatabaseReference database;
    MapView mapView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_sarat, container, false);
        init();

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
        mapView = this.root.findViewById(R.id.saratmap);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        String[] urlArray = {"http://osm.incois.gov.in/osm_tiles/"};
        XYTileSource xYTileSource = new XYTileSource("ArcGisOnline", 0, 25, 256, ".png", urlArray);
        mapView.setTileSource(xYTileSource);
        LatLonGridlineOverlay2 overlay = new LatLonGridlineOverlay2();
        mapView.getOverlays().add(overlay);
        setCenter(new GeoPoint(12.901744, 77.769098),5);

    }




    public void setCenter(final GeoPoint geoPoint, int zoom) {
        if(geoPoint!=null){
            mapView.getController().setCenter(geoPoint);
            mapView.getController().setZoom(zoom);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        database.child("saratdata").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    SARATDto saratDto = noteDataSnapshot.getValue(SARATDto.class);
                    updateSaratDataonUI(saratDto);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void updateSaratDataonUI(SARATDto saratDto){
        for(Region region : saratDto.getRegions()){
            List<GeoPoint> geoPoints = region.getRegion();
            Polygon polygon = new Polygon();    //see note below
            polygon.setFillColor(Color.argb(75, 255,0,0));
            geoPoints.add(geoPoints.get(0));    //forces the loop to close
            polygon.setPoints(geoPoints);
            polygon.setTitle("Probability : "+region.getProbability()+"%");
            mapView.getOverlayManager().add(polygon);
        }
        mapView.invalidate();
    }

}
