package com.example.geminiv4.ui.osf;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geminiv4.R;

import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.RecyclerViewHolder> {

    private List<WarningInfo> warningInfos;
    private Context context;

    public InfoAdapter(List<WarningInfo> warningInfos , Context context){

        this.warningInfos = warningInfos;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.osf_page_layout, parent, false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        WarningInfo warningInfo= warningInfos.get(position);
        Log.e("====>",warningInfo.getState());
        holder.textStateName.setText(warningInfo.getState());
        holder.textCurrent.setText(String.valueOf(warningInfo.getCs1()+" - "+warningInfo.getCs2()));
        holder.textWave.setText(String.valueOf(warningInfo.getWh1()+" - "+warningInfo.getWh2()));
        holder.textAdvice.setText(warningInfo.getAdvice());
        holder.imageView.setImageDrawable(context.getResources().getDrawable(warningInfo.getImage()));

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
