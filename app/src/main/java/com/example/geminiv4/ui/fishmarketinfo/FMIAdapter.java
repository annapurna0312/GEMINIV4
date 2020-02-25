package com.example.geminiv4.ui.fishmarketinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geminiv4.R;
import com.example.geminiv4.ui.osf.WarningInfo;

import java.util.List;

public class FMIAdapter extends RecyclerView.Adapter<FMIAdapter.RecyclerViewHolder> {

    private List<FishMarketInfo> warningInfos;
    private Context context;

    public FMIAdapter(List<FishMarketInfo> warningInfos , Context context){
        this.warningInfos = warningInfos;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fishprice_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        FishMarketInfo warningInfo= warningInfos.get(position);
    }

    @Override
    public int getItemCount() {
        return warningInfos.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textStateName, textCurrent, textWave, textAdvice;

        public RecyclerViewHolder(View view){
            super(view);
            imageView=view.findViewById(R.id.state_image);
            textStateName=view.findViewById(R.id.state_name);
            textCurrent=view.findViewById(R.id.current_speed);
            textWave=view.findViewById(R.id.wave_height);
            textAdvice=view.findViewById(R.id.advice);
        }
    }
}
