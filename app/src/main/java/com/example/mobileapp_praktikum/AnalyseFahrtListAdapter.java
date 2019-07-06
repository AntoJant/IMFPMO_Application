package com.example.mobileapp_praktikum;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

public class AnalyseFahrtListAdapter extends BaseAdapter {
    private AnalyseergebnisWeg weg;
    private boolean[] ausgeklappteItems;
    public AnalyseFahrtListAdapter(AnalyseergebnisWeg weg){
        this.weg = weg;
        ausgeklappteItems = new boolean[weg.getFahrten().size()];
        for (int i = 0; i < ausgeklappteItems.length; i++){
            ausgeklappteItems[i] = false;
        }
    }


    @Override
    public int getCount() {
        return weg.getFahrten().size();
    }

    @Override
    public Object getItem(int i) {
        return weg.getFahrten().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (ausgeklappteItems[i]) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.analyse_fahrt_item_erweitert_list, viewGroup,false);
        }else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.analyse_fahrt_item_list, viewGroup,false);
        }
        AnalyseergebnisFahrt ergebnis = (AnalyseergebnisFahrt) getItem(i);

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
            case WALK:imageView.setImageResource(R.drawable.ic_directions_walk_black_24dp);
        }
        if(ausgeklappteItems[i]) {
            TextView distanzTextView = (TextView) view.findViewById(R.id.distanzTextView);
            TextView co2TextView = (TextView) view.findViewById(R.id.cO2textView);
            TextView dauerTextView = (TextView) view.findViewById(R.id.dauerTextView);
            TextView alternativZeit = (TextView) view.findViewById(R.id.altZeitdauertextView);
            ImageView altImageView = (ImageView) view.findViewById(R.id.altImageView);
            alternativZeit.setText(" " + ergebnis.getAlternativerZeitaufwand());
            distanzTextView.setText(" " + ergebnis.getDistanz());
            co2TextView.setText(" " + ergebnis.getcO2Austoss());
            dauerTextView.setText(" " + ergebnis.getDauer());
            switch (ergebnis.getAlternativModi()){
                case AUTO: altImageView.setImageResource(R.drawable.ic_directions_car_black_24dp);break;
                case FAHRRAD: altImageView.setImageResource(R.drawable.ic_directions_bike_black_24dp);break;
                case OPNV: altImageView.setImageResource(R.drawable.ic_directions_bus_black_24dp);break;
                case WALK: altImageView.setImageResource(R.drawable.ic_directions_walk_black_24dp);break;
            }
        }
        zeitTextView.setText("Startzeit:" +ergebnis.getStartzeit().get(Calendar.HOUR)+":"+ergebnis.getStartzeit().get(Calendar.MINUTE));
        startAdresse.setText("Startadrasse: " +ergebnis.getStartadresse());


        return view;
    }
    public void setAusgeklappteItems(int i){
        if(ausgeklappteItems[i] == false)
            ausgeklappteItems[i] = true;
        else
            ausgeklappteItems[i] = false;
    }
}
