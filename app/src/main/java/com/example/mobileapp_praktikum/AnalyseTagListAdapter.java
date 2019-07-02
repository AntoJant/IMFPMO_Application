package com.example.mobileapp_praktikum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import java.util.Calendar;

public class AnalyseTagListAdapter extends BaseAdapter {
    private AnalyseergebnisMonat monatAnalyse;
    public AnalyseTagListAdapter(AnalyseergebnisMonat monat){
        this.monatAnalyse = monat;
    }


    @Override
    public int getCount() {
        return monatAnalyse.getTage().size()+1;
    }

    @Override
    public Object getItem(int i) {
        if(i == 0){
            return null;
        }
        return monatAnalyse.getTage().get(i-1);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {
        if(i == 0){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.analyse_monat_viewpage_item_layout, viewGroup,false);
            ViewPager vp = view.findViewById(R.id.viewPage);

            AnalyseTagGesamtErgebnisPagerAdapter adapter = new AnalyseTagGesamtErgebnisPagerAdapter(viewGroup.getContext(), monatAnalyse.getTage());
            vp.setAdapter(adapter);
            return view;
        }else{
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.analyse_tag_item_layout, viewGroup,false);
            final AnalyseergebnisTag ergebnis = (AnalyseergebnisTag) getItem(i);
            TextView datum = view.findViewById(R.id.monatLabel);
            int tag = i;
            int month =  monatAnalyse.getDate().get(Calendar.MONTH) +1 ;
            datum.setText( tag +"."+ month + "." + monatAnalyse.getDate().get(Calendar.YEAR));
            final TextView okoBewertung = (TextView) view.findViewById(R.id.okoBewertungTextView);
            okoBewertung.setText("3");
            ViewPager vp = view.findViewById(R.id.viewPager);
            AnalyseTagDiagramPagerAdapter mp = new AnalyseTagDiagramPagerAdapter(viewGroup.getContext(), ergebnis);
            vp.setAdapter(mp);
            Button button = view.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity =(MainActivity) viewGroup.getContext();
                activity.changeToAnalyseTagFragment(ergebnis.getTag());
            }
        });
        return view;
    }}
}
