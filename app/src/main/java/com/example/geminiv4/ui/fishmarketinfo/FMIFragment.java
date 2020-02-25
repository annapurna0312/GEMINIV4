package com.example.geminiv4.ui.fishmarketinfo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.example.geminiv4.R;
import com.example.geminiv4.sqlite.FireBaseUtils;
import com.example.geminiv4.ui.osf.InfoAdapter;
import com.example.geminiv4.ui.osf.OsfViewModel;
import com.example.geminiv4.ui.osf.WarningInfo;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;


public class FMIFragment extends Fragment {

    private View root;
    private OsfViewModel mViewModel;
    List<FishMarketInfo> warningInfoList;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_fmi, container, false);

        recyclerView = (RecyclerView) this.root.findViewById(R.id.fmirecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.root.getContext()));

        Toolbar toolbar = (Toolbar) this.root.findViewById(R.id.app_bar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        warningInfoList = new ArrayList<>();
        FishMarketInfo fmi = new FishMarketInfo();

        warningInfoList.add(fmi);

        FMIAdapter adapter = new FMIAdapter(warningInfoList,getContext());
        recyclerView.setAdapter(adapter);
        return root;
    }

}
