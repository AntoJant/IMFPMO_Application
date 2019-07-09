package com.imfpmo.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.github.mikephil.charting.charts.PieChart;

public class AnalyseTagDiagramPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private AnalyseergebnisTag tag;


    public AnalyseTagDiagramPagerAdapter(Context context, AnalyseergebnisTag tag){
        this.context = context;
        this.layoutInflater = (LayoutInflater)this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        this.tag = tag;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        View view = this.layoutInflater.inflate(R.layout.analyse_diagram_pie_chart, container, false);
        PieChart pieChart = (PieChart) view.findViewById(R.id.diagramm);
        switch (position){
            case 0: AnalyseDiagramMaker.makeTagGesamtCO2PieChart(tag , pieChart); break;
            case 1: AnalyseDiagramMaker.makeTagGesamtDistanzPieChart(tag ,pieChart); break;
            case 2: AnalyseDiagramMaker.makeTagGesamtZeitPieChart(tag,pieChart);break;
        }
        container.addView(view);
        return view;
    }


    public int getCount() {
        return 3;
    }

    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
