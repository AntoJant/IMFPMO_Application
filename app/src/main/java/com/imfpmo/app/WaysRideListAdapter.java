package com.imfpmo.app;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WaysRideListAdapter extends BaseAdapter {
    private Path path;
    public WaysRideListAdapter(Path path){
        this.path = path;this.path = path;

    }


    @Override
    public int getCount() {
        return path.getRides().size();
    }

    @Override
    public Object getItem(int i) {
        return path.getRides().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ways_ride_item_list, viewGroup,false);

        Ride results = (Ride) getItem(i);

        TextView timeTextView = view.findViewById(R.id.zeitTextView);
        TextView startAddress = view.findViewById(R.id.startPunktTextView);
        ImageView imageView = view.findViewById(R.id.imageView2);


        switch (results.mode){
            case "car": imageView.setImageResource(R.drawable.ic_directions_car_black_24dp);break;
            case "bike": imageView.setImageResource(R.drawable.ic_directions_bike_black_24dp);break;
            case "walk":imageView.setImageResource(R.drawable.ic_directions_walk_black_24dp); break;
            default: imageView.setImageResource(R.drawable.ic_directions_bus_black_24dp);break;
        }

        timeTextView.setText("" + results.start.getTimeAsString());
        startAddress.setText("" +results.getStartAddress());
        return view;
    }

}