package com.imfpmo.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.github.mikephil.charting.charts.PieChart;

public class AnalysisMonthDiagramPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private AnalysisResultMonth monat;


    public AnalysisMonthDiagramPagerAdapter(Context context, AnalysisResultMonth monat){
        this.context = context;
        this.layoutInflater = (LayoutInflater)this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        this.monat = monat;
    }

    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = this.layoutInflater.inflate(R.layout.analyse_diagram_pie_chart, container, false);
        PieChart pieChart = view.findViewById(R.id.diagramm);
        switch (position){
            case 0: AnalysisDiagramMaker.makeGesamtCO2Diagramm(monat.getCO2Auto(), monat.getCO2Opnv(), pieChart,context); break;
            case 1: AnalysisDiagramMaker.makeGesamtDistanzDiagramm(monat.getAutoDistanz(),monat.getOpnvDistanz(),monat.getFahrradDistanz(),monat.getFussDistanz(),pieChart,context); break;
            case 2: AnalysisDiagramMaker.makeGesamtZeitDiagramm(monat.getZeitAuto(),monat.getZeitFahrrad(),monat.getZeitOpnv(), monat.getZeitFuss(),pieChart, context);break;
        }
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

}
