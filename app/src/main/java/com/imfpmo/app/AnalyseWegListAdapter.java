package com.imfpmo.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import java.util.Calendar;

public class AnalyseWegListAdapter extends BaseAdapter {
    private AnalyseergebnisTag tag;
    private boolean[] istAufgeklappt;
    public AnalyseWegListAdapter(AnalyseergebnisTag tag){
        this.tag = tag;
        istAufgeklappt = new boolean[tag.getWege().size()];
        for (int i = 0; i<istAufgeklappt.length;i++)
            istAufgeklappt[i] = false;
    }


    @Override
    public int getCount() {
        return tag.getWege().size();
    }

    @Override
    public Object getItem(int i) {
        return tag.getWege().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.analyse_weg_item_layout, viewGroup,false);
        }
        final AnalyseergebnisWeg ergebnis = (AnalyseergebnisWeg) getItem(i);
        TextView startZeit = view.findViewById(R.id.startZeitTextView);
        TextView endZeit = view.findViewById(R.id.endZeitTextView);
        TextView startPunkt = view.findViewById(R.id.startAdresseTextView);
        TextView endPunkt = view.findViewById(R.id.endAdresseTextView);
        Button b = view.findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity =(MainActivity) view.getContext();
                activity.changeToAnalyseFahrtFragment(ergebnis);
            }
        });
        startZeit.setText(ergebnis.getStartzeit().get(Calendar.HOUR_OF_DAY) + "." + ergebnis.getStartzeit().get(Calendar.MINUTE) );
        endZeit.setText(ergebnis.getEndzeit().get(Calendar.HOUR_OF_DAY) + "." +ergebnis.getEndzeit().get(Calendar.MINUTE));
        startPunkt.setText(ergebnis.getStartAdresse());
        endPunkt.setText(ergebnis.getEndAdresse());
        ImageView okoBewertung = view.findViewById(R.id.okoImageView);
        switch ((int) ergebnis.getOkobewertung()){
            case 1: okoBewertung.setImageResource(R.drawable.red_dot_24dp);break;
            case 2: okoBewertung.setImageResource(R.drawable.yellow_dot_24dp);break;
            case 3: okoBewertung.setImageResource(R.drawable.ic_lens_black_24dp);break;
        }

        ViewPager vp = view.findViewById(R.id.viewPager);
        AnalyseWegDiagramPagerAdapter mp = new AnalyseWegDiagramPagerAdapter(viewGroup.getContext(), ergebnis);
        vp.setAdapter(mp);


        return view;

    }

}
