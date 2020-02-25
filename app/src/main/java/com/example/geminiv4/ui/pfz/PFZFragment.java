package com.example.geminiv4.ui.pfz;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.geminiv4.R;
import com.example.geminiv4.sqlite.FireBaseUtils;
import com.example.geminiv4.sqlite.NavicFishLandingCenters;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PFZFragment extends Fragment {

    private View root;
    DatabaseReference database;
    NavicFishLandingCenters navicFishLandingCenters;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_pfz, container, false);
        init();
        return root;
    }



    public void init(){
        database = FireBaseUtils.getDatabaseReference();
        navicFishLandingCenters = new NavicFishLandingCenters();
        Toolbar toolbar = (Toolbar) this.root.findViewById(R.id.app_bar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        final Spinner spinner = (Spinner) this.root.findViewById(R.id.flcspinner);
        final List<String> categories = new ArrayList<>();
        for(String flc : navicFishLandingCenters.getFishLandingcenters("NorthAndhraPradesh").values()){
            categories.add(flc);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.root.getContext(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setData(navicFishLandingCenters.getFishLandingCenters().indexOf(categories.get(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ((Button) this.root.findViewById(R.id.pfznavigateusinggagan)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Fragment fragment = new PFZMapFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                fragment.setArguments(getArgumentsData());
                transaction.replace(R.id.nav_host_fragment, fragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });

    }
    public Bundle getArgumentsData(){
        Bundle arguments = new Bundle();
        arguments.putString( "nav_dep" ,((TextView) this.root.findViewById(R.id.depth)).getText().toString());
        arguments.putString( "nav_lat" ,((TextView) this.root.findViewById(R.id.lat)).getText().toString());
        arguments.putString( "nav_lon" ,((TextView) this.root.findViewById(R.id.lon)).getText().toString());
        arguments.putString( "nav_maxwh" ,((TextView) this.root.findViewById(R.id.maxwh)).getText().toString());
        arguments.putString( "nav_maxws" ,((TextView) this.root.findViewById(R.id.maxws)).getText().toString());
        arguments.putString( "nav_maxcs" ,((TextView) this.root.findViewById(R.id.maxcs)).getText().toString());
        return arguments;
    }

    @Override
    public void onResume() {
        super.onResume();
        setData(996);
    }

    public void enableNavigateButton(){
        ((Button) this.root.findViewById(R.id.pfznavigateusinggagan)).setVisibility(View.VISIBLE);
    }
    public void disableNavigateButton(){
        ((Button) this.root.findViewById(R.id.pfznavigateusinggagan)).setVisibility(View.GONE);
    }

    private void updatePFZDataonUI(FlcWisePfz flcWisePfz) {
        ((TextView) this.root.findViewById(R.id.distance)).setText(flcWisePfz.getDistance());
        ((TextView) this.root.findViewById(R.id.direction)).setText(flcWisePfz.getDirection());
        ((TextView) this.root.findViewById(R.id.depth)).setText(flcWisePfz.getDepth());
        ((TextView) this.root.findViewById(R.id.lat)).setText(flcWisePfz.getLatitude());
        ((TextView) this.root.findViewById(R.id.lon)).setText(flcWisePfz.getLongitude());
        ((TextView) this.root.findViewById(R.id.flcname)).setText(flcWisePfz.getCoast());
        ((TextView) this.root.findViewById(R.id.maxwh)).setText(flcWisePfz.getMaxwh());
        ((TextView) this.root.findViewById(R.id.maxws)).setText(flcWisePfz.getMaxws());
        ((TextView) this.root.findViewById(R.id.maxcs)).setText(flcWisePfz.getMaxcs());
    }

    private void resetData() {
        ((TextView) this.root.findViewById(R.id.distance)).setText("--");
        ((TextView) this.root.findViewById(R.id.direction)).setText("--");
        ((TextView) this.root.findViewById(R.id.depth)).setText("--");
        ((TextView) this.root.findViewById(R.id.lat)).setText("--");
        ((TextView) this.root.findViewById(R.id.lon)).setText("--");
        ((TextView) this.root.findViewById(R.id.flcname)).setText("--");
        ((TextView) this.root.findViewById(R.id.maxwh)).setText("--");
        ((TextView) this.root.findViewById(R.id.maxws)).setText("--");
        ((TextView) this.root.findViewById(R.id.maxcs)).setText("--");
    }

    private void setData(final int flcid){
        resetData();
        database.child("flcpfzdata").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.d("firebasedata" ,""+postSnapshot.toString());
                    FlcWisePfz flcWisePfz = postSnapshot.getValue(FlcWisePfz.class);
                    if(flcWisePfz!=null && flcWisePfz.getFlcid()==flcid){
                        updatePFZDataonUI(flcWisePfz);
                        break;
                    }
                }
                enableNavigateButton();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                disableNavigateButton();
            }
        });
    }
}
