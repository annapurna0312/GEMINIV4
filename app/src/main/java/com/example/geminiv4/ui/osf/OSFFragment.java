package com.example.geminiv4.ui.osf;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.example.geminiv4.R;
import com.example.geminiv4.devicedata.IncoisMessage;
import com.example.geminiv4.sqlite.FireBaseUtils;
import com.example.geminiv4.sqlite.MessagesHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OSFFragment extends Fragment {

    private View root;
    private OsfViewModel mViewModel;
    List<WarningInfo> warningInfoList;
    RecyclerView recyclerView;
    DatabaseReference database;
    MessagesHandler messagesHandler;

    public static OSFFragment newInstance() {
        return new OSFFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.osf_fragment, container, false);
        database = FireBaseUtils.getDatabaseReference();
        messagesHandler = new MessagesHandler(this.root.getContext());
        recyclerView = (RecyclerView) this.root.findViewById(R.id.recyclerView);
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
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OsfViewModel.class);
    }





    @Override
    public void onResume() {
        super.onResume();
        database.child("hwadata").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                warningInfoList = new ArrayList<>();
                List<IncoisMessage> osfmessages = messagesHandler.getMessages("OSF");
                for(IncoisMessage im : osfmessages){
                        warningInfoList.add(new WarningInfo(
                                messagesHandler.getNavicStateID(im.getZone())+"",
                                im.getZone(),
                                im.getMessage().split("\\,")[4],
                                im.getMessage().split("\\,")[5],
                                im.getMessage().split("\\,")[6],
                                im.getMessage().split("\\,")[7],
                                im.getMessage().split("\\,")[9],
                                im.getMessage().split("\\,")[10],
                                im.getMessage().split("\\,")[11]
                        ));
                }
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    WarningInfo hwa = noteDataSnapshot.getValue(WarningInfo.class);
                    warningInfoList.add(hwa);
                }
                InfoAdapter adapter = new InfoAdapter(warningInfoList,getContext());
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}
