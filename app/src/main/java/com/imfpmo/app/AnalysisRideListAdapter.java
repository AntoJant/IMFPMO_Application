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
    private AnalysisResultPath path;
    private boolean[] expandedItems;
    public AnalysisRideListAdapter(AnalysisResultPath path){
        this.path = path;
        expandedItems = new boolean[path.getFahrten().size()];
        for (int i = 0; i < expandedItems.length; i++){
            expandedItems[i] = false;
        }
    }


    @Override
    public int getCount() {
        return path.getFahrten().size();
    }

    @Override
    public Object getItem(int i) {
        return path.getFahrten().get(i);
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
        AnalysisResultRide ergebnis = (AnalysisResultRide) getItem(i);

        TextView zeitTextView = view.findViewById(R.id.zeitTextView);
        TextView startAdresse = view.findViewById(R.id.startPunktTextView);
        ImageView imageView = view.findViewById(R.id.imageView2);
        RelativeLayout relativeLayout = view.findViewById(R.id.relativeLayout);
        LinearLayout mainLayout = view.findViewById(R.id.mainLayout);
        switch (ergebnis.getOkoBewertung()){
            case 1: relativeLayout.setBackgroundColor(Color.argb(40,255,0,0));break;
            case 2: relativeLayout.setBackgroundColor(Color.argb(40,255,255,0));break;
            case 3: relativeLayout.setBackgroundColor(Color.argb(40,0,255,0));break;
        }
        switch (ergebnis.getModi()){
            case AUTO: imageView.setImageResource(R.drawable.ic_directions_car_black_24dp);break;
            case FAHRRAD: imageView.setImageResource(R.drawable.ic_directions_bike_black_24dp);break;
            case OPNV: imageView.setImageResource(R.drawable.ic_directions_bus_black_24dp);break;
            case WALK:imageView.setImageResource(R.drawable.ic_directions_walk_black_24dp); break;
        }
        if(expandedItems[i]) {
            TextView zielAdresseTextView = view.findViewById(R.id.zielTextView);
            TextView distanzTextView = view.findViewById(R.id.distanzTextView);
            TextView co2TextView = view.findViewById(R.id.cO2textView);
            TextView dauerTextView = view.findViewById(R.id.dauerTextView);
            TextView alternativZeit = view.findViewById(R.id.altZeitdauertextView);
            ImageView altImageView = view.findViewById(R.id.altImageView);
            alternativZeit.setText(" " + ergebnis.getAlternativerZeitaufwand()+" min");
            distanzTextView.setText(" " + ergebnis.getDistanz()+ " km");
            co2TextView.setText(" " + ergebnis.getcO2Austoss()+" gramm CO2");
            dauerTextView.setText(" " + ergebnis.getDauer()+" min");
            zielAdresseTextView.setText("" + ergebnis.getZieladresse());
            switch (ergebnis.getAlternativModi()){
                case AUTO: altImageView.setImageResource(R.drawable.ic_directions_car_black_24dp);break;
                case FAHRRAD: altImageView.setImageResource(R.drawable.ic_directions_bike_black_24dp);break;
                case OPNV: altImageView.setImageResource(R.drawable.ic_directions_bus_black_24dp);break;
                case WALK: altImageView.setImageResource(R.drawable.ic_directions_walk_black_24dp);break;
            }
        }
        zeitTextView.setText("" + ergebnis.start.getTimeAsString());
        startAdresse.setText("" +ergebnis.getStartadresse());
        return view;
    }
    public void expandItems(int i){
        if(expandedItems[i] == false)
            expandedItems[i] = true;
        else
            expandedItems[i] = false;
    }
}
