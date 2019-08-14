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

public class AnalysisRideListAdapter extends BaseAdapter {
    private Path path;
    private boolean[] expandedItems;
    public AnalysisRideListAdapter(Path path){
        this.path = path;
        expandedItems = new boolean[path.getRides().size()];
        for (int i = 0; i < expandedItems.length; i++){
            expandedItems[i] = false;
        }
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
        if (expandedItems[i]) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.analyse_fahrt_item_erweitert_list, viewGroup,false);
        }else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.analyse_fahrt_item_list, viewGroup,false);
        }
        Ride results = (Ride) getItem(i);

        TextView timeTextView = view.findViewById(R.id.zeitTextView);
        TextView startAddress = view.findViewById(R.id.startPunktTextView);
        ImageView imageView = view.findViewById(R.id.imageView2);
        RelativeLayout relativeLayout = view.findViewById(R.id.relativeLayout);
        LinearLayout mainLayout = view.findViewById(R.id.mainLayout);
        switch (results.getOkoGrade()){
            case 1: relativeLayout.setBackgroundColor(Color.argb(40,255,0,0));break;
            case 2: relativeLayout.setBackgroundColor(Color.argb(40,255,255,0));break;
            case 3: relativeLayout.setBackgroundColor(Color.argb(40,0,255,0));break;
        }
        switch (results.getMode()){
            case CAR: imageView.setImageResource(R.drawable.ic_directions_car_black_24dp);break;
            case BIKE: imageView.setImageResource(R.drawable.ic_directions_bike_black_24dp);break;
            case OPNV: imageView.setImageResource(R.drawable.ic_directions_bus_black_24dp);break;
            case WALK:imageView.setImageResource(R.drawable.ic_directions_walk_black_24dp); break;
        }
        if(expandedItems[i]) {
            TextView endAddressTextView = view.findViewById(R.id.zielTextView);
            TextView distanceTextView = view.findViewById(R.id.distanzTextView);
            TextView emissionTextView = view.findViewById(R.id.cO2textView);
            TextView timeEffortTextView = view.findViewById(R.id.dauerTextView);
            TextView alternativeTime = view.findViewById(R.id.altZeitdauertextView);
            ImageView altImageView = view.findViewById(R.id.altImageView);
            alternativeTime.setText(" " + results.getAlternativeTimeEffort()+" min");
            distanceTextView.setText(" " + results.getDistance()+ " m");
            emissionTextView.setText(" " + results.getCO2Emissions()+" gramm CO2");
            timeEffortTextView.setText(" " + results.getTimeEffort()+" min");
            endAddressTextView.setText("" + results.getEndAddress());
            switch (results.getAlternativeMode()){
                case CAR: altImageView.setImageResource(R.drawable.ic_directions_car_black_24dp);break;
                case BIKE: altImageView.setImageResource(R.drawable.ic_directions_bike_black_24dp);break;
                case OPNV: altImageView.setImageResource(R.drawable.ic_directions_bus_black_24dp);break;
                case WALK: altImageView.setImageResource(R.drawable.ic_directions_walk_black_24dp);break;
            }
        }
        timeTextView.setText("" + results.start.getTimeAsString());
        startAddress.setText("" +results.getStartAddress());
        return view;
    }
    public void expandItems(int i){
        if(expandedItems[i] == false)
            expandedItems[i] = true;
        else
            expandedItems[i] = false;
    }
}
