package com.imfpmo.app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Calendar;

public class AnalysisMonthListAdapter extends BaseAdapter {
    private Activity context;
    private FragmentManager fragmentManager;
    private ArrayList<Month> results;
    private FragmentManager support;
    public AnalysisMonthListAdapter(Activity context, ArrayList<Month> results, FragmentManager fragmentManager){
        this.context = context;
        this.results = results;
        this.fragmentManager = fragmentManager;
    }


    @Override
    public int getCount() {
        return results.size()+1;
    }

    @Override
    public Object getItem(int i) {
        if(i == 0){
            return null;
        }
        return results.get(i-1);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {
        if(i == 0){
                view = LayoutInflater.from(context).inflate(R.layout.analyse_monat_viewpage_item_layout, viewGroup,false);

            TextView textView = view.findViewById(R.id.ergebnisType);
            textView.setText("Gesamtergebnisse");
            ViewPager vp = view.findViewById(R.id.viewPage);
            AnalysisMonthOverviewPagerAdapter adapter = new AnalysisMonthOverviewPagerAdapter(context, results);
            vp.setAdapter(adapter);
            return view;
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.analyse_monat_item_layout, viewGroup,false);
            final Month result = (Month) getItem(i);
            TextView month = view.findViewById(R.id.monatLabel);
            switch(result.getDate().get(Calendar.MONTH)){
                case 0:month.setText("Januar" + " " + result.getDate().get(Calendar.YEAR));break;
                case 1:month.setText("Februar" + " " + result.getDate().get(Calendar.YEAR));break;
                case 2:month.setText("März" + " " + result.getDate().get(Calendar.YEAR));break;
                case 3:month.setText("April" + " " + result.getDate().get(Calendar.YEAR));break;
                case 4:month.setText("Mai" + " " + result.getDate().get(Calendar.YEAR));break;
                case 5:month.setText("Juni" + " " + result.getDate().get(Calendar.YEAR));break;
                case 6:month.setText("Juli" + " " + result.getDate().get(Calendar.YEAR));break;
                case 7:month.setText("August" + " " + result.getDate().get(Calendar.YEAR));break;
                case 8:month.setText("September" + " " + result.getDate().get(Calendar.YEAR));break;
                case 9:month.setText("Oktober" + " " + result.getDate().get(Calendar.YEAR));break;
                case 10:month.setText("November" + " " + result.getDate().get(Calendar.YEAR));break;
                case 11:month.setText("Dezember" + " " + result.getDate().get(Calendar.YEAR));break;
            }

            ImageView okoGrade = view.findViewById(R.id.okoImageView);
            switch (result.getOkoGrade()){
                case 1: okoGrade.setImageResource(R.drawable.red_dot_24dp);break;
                case 2: okoGrade.setImageResource(R.drawable.yellow_dot_24dp);break;
                case 3: okoGrade.setImageResource(R.drawable.ic_lens_black_24dp);break;
            }

            ImageView alternativeMode = view.findViewById(R.id.bestAlternativeImageView);
            switch (result.getAlternativeMode()){
                case CAR: alternativeMode.setImageResource(R.drawable.ic_directions_car_black_24dp);break;
                case BIKE: alternativeMode.setImageResource(R.drawable.ic_directions_bike_black_24dp);break;
                case OPNV: alternativeMode.setImageResource(R.drawable.ic_directions_bus_black_24dp);break;
                case WALK: alternativeMode.setImageResource(R.drawable.ic_directions_walk_black_24dp);break;

            }


            Button button = view.findViewById(R.id.button);
            ViewPager vp = view.findViewById(R.id.viewPager);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity activity =(MainActivity) viewGroup.getContext();
                    activity.changeToAnalyseMonatFragment(result.getDate());
                }
            });

            AnalysisMonthDiagramPagerAdapter mp = new AnalysisMonthDiagramPagerAdapter(context, result);
            vp.setAdapter(mp);
            return  view;
        }
    }


}
