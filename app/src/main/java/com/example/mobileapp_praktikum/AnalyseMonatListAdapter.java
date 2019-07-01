package com.example.mobileapp_praktikum;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Calendar;

public class AnalyseMonatListAdapter extends BaseAdapter {
    private Activity context;
    private FragmentManager fragmentManager;
    private ArrayList<AnalyseergebnisMonat> ergebnisse;
    private FragmentManager support;
    public AnalyseMonatListAdapter(Activity context, ArrayList<AnalyseergebnisMonat> ergebnisse, FragmentManager fragmentManager){
        this.context = context;
        this.ergebnisse = ergebnisse;
        this.fragmentManager = fragmentManager;
    }


    @Override
    public int getCount() {
        return ergebnisse.size();
    }

    @Override
    public Object getItem(int i) {
        return ergebnisse.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.analyse_monat_item_layout, viewGroup,false);
        }
        final AnalyseergebnisMonat ergebnis = (AnalyseergebnisMonat) getItem(i);
        TextView monat = (TextView) view.findViewById(R.id.monatLabel);
        switch(ergebnis.getDate().get(Calendar.MONTH)){
            case 0:monat.setText("Januar" + " " + ergebnis.getDate().get(Calendar.YEAR));break;
            case 1:monat.setText("Februar" + " " + ergebnis.getDate().get(Calendar.YEAR));break;
            case 2:monat.setText("März" + " " + ergebnis.getDate().get(Calendar.YEAR));break;
            case 3:monat.setText("April" + " " + ergebnis.getDate().get(Calendar.YEAR));break;
            case 4:monat.setText("Mai" + " " + ergebnis.getDate().get(Calendar.YEAR));break;
            case 5:monat.setText("Juni" + " " + ergebnis.getDate().get(Calendar.YEAR));break;
            case 6:monat.setText("Juli" + " " + ergebnis.getDate().get(Calendar.YEAR));break;
            case 7:monat.setText("August" + " " + ergebnis.getDate().get(Calendar.YEAR));break;
            case 8:monat.setText("September" + " " + ergebnis.getDate().get(Calendar.YEAR));break;
            case 9:monat.setText("Oktober" + " " + ergebnis.getDate().get(Calendar.YEAR));break;
            case 10:monat.setText("November" + " " + ergebnis.getDate().get(Calendar.YEAR));break;
            case 11:monat.setText("Dezember" + " " + ergebnis.getDate().get(Calendar.YEAR));break;
        }

        final TextView okoBewertung = (TextView) view.findViewById(R.id.okoBewertungTextView);
        okoBewertung.setText("3");

        Button button = view.findViewById(R.id.button);
        ViewPager vp = view.findViewById(R.id.viewPager);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_analyse, new AnalysisMonatFragment()).addToBackStack(null).commit();

            }
        });

        AnalyseMonatDiagramPagerAdapter mp = new AnalyseMonatDiagramPagerAdapter(context, ergebnis);
        vp.setAdapter(mp);
        return  view;
    }


}
