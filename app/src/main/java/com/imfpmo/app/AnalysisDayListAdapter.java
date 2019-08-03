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
import java.util.GregorianCalendar;

public class AnalysisDayListAdapter extends BaseAdapter {
    private AnalysisResultMonth monthAnalysis;
    public AnalysisDayListAdapter(AnalysisResultMonth month){
        this.monthAnalysis = month;
    }


    @Override
    public int getCount() {
        return monthAnalysis.getDays().size()+1;
    }

    @Override
    public Object getItem(int i) {
        if(i == 0){
            return null;
        }
        return monthAnalysis.getDays().get(i-1);
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

            AnalysisDayOverviewPagerAdapter adapter = new AnalysisDayOverviewPagerAdapter(viewGroup.getContext(), monthAnalysis.getDays());
            vp.setAdapter(adapter);
            return view;
        }else{
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.analyse_tag_item_layout, viewGroup,false);
            final AnalysisResultDay results = (AnalysisResultDay) getItem(i);
            TextView date = view.findViewById(R.id.monatLabel);
            int day = i;
            int month =  monthAnalysis.getDate().get(Calendar.MONTH) +1 ;
            date.setText( day +"."+ month + "." + monthAnalysis.getDate().get(Calendar.YEAR));
            ImageView okoGrade = view.findViewById(R.id.okoImageView);
            switch ((int) results.getOkoGrade()){
                case 1: okoGrade.setImageResource(R.drawable.red_dot_24dp);break;
                case 2: okoGrade.setImageResource(R.drawable.yellow_dot_24dp);break;
                case 3: okoGrade.setImageResource(R.drawable.ic_lens_black_24dp);break;
            }
            ViewPager vp = view.findViewById(R.id.viewPager);
            AnalysisDayDiagramPagerAdapter mp = new AnalysisDayDiagramPagerAdapter(viewGroup.getContext(), results);
            vp.setAdapter(mp);
            Button button = view.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity =(MainActivity) viewGroup.getContext();
                activity.changeToAnalyseTagFragment(new GregorianCalendar(results.getDay().get(Calendar.YEAR), results.getDay().get(Calendar.MONTH), results.getDay().get(Calendar.DAY_OF_MONTH)));
            }
        });
        return view;
    }}
}
