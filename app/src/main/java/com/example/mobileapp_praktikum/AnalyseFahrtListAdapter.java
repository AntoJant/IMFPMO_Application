package com.example.mobileapp_praktikum;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import java.util.Calendar;

public class AnalyseFahrtListAdapter extends BaseAdapter {
    private AnalyseergebnisWeg weg;
    public AnalyseFahrtListAdapter(AnalyseergebnisWeg weg){
        this.weg = weg;
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
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.analyse_fahrt_item_list, viewGroup,false);
        }
        AnalyseergebnisFahrt ergebnis = (AnalyseergebnisFahrt) getItem(i);

        TextView zeitTextView = view.findViewById(R.id.zeitTextView);
        TextView startAdresse = view.findViewById(R.id.startPunktTextView);
        ImageView imageView = view.findViewById(R.id.imageView2);
        LinearLayout mainLayout = view.findViewById(R.id.mainLayout);
        switch (ergebnis.getOkoBewertung()){
            case 1: mainLayout.setBackgroundColor(Color.argb(70,255,0,0));break;
            case 2: mainLayout.setBackgroundColor(Color.argb(70,255,255,0));break;
            case 3: mainLayout.setBackgroundColor(Color.argb(70,0,255,0));break;
        }
        switch (ergebnis.getModi()){
            case AUTO: imageView.setImageResource(R.drawable.ic_directions_car_black_24dp);break;
            case FAHRRAD: imageView.setImageResource(R.drawable.ic_directions_bike_black_24dp);break;
            case OPNV: imageView.setImageResource(R.drawable.ic_directions_bus_black_24dp);break;
        }
        zeitTextView.setText(ergebnis.getStartzeit().get(Calendar.HOUR)+":"+ergebnis.getStartzeit().get(Calendar.MINUTE));
        startAdresse.setText(ergebnis.getStartadresse());


        return view;
    }
}
