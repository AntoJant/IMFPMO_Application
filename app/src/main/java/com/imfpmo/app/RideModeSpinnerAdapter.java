package com.imfpmo.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RideModeSpinnerAdapter extends ArrayAdapter<RideMode> {

    public RideModeSpinnerAdapter(Context context, ArrayList<RideMode> array){
        super(context, 0, array);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_ride_item, parent, false
            );
        }
        ImageView imageViewFlag = convertView.findViewById(R.id.modiImageView);
        RideMode mode = getItem(position);
        if (mode != null) {
            switch (mode){
                case BIKE: imageViewFlag.setImageResource(R.drawable.ic_directions_bike_black_24dp);break;
                case CAR: imageViewFlag.setImageResource(R.drawable.ic_directions_car_black_24dp);break;
                case OPNV: imageViewFlag.setImageResource(R.drawable.ic_directions_bus_black_24dp);break;
                case WALK: imageViewFlag.setImageResource(R.drawable.ic_directions_walk_black_24dp);break;
            }
        }
        return convertView;
    }


}
