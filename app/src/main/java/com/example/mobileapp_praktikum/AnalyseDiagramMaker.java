package com.example.mobileapp_praktikum;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AnalyseDiagramMaker {
    public static PieChart makeGesamtCO2Diagramm(int autoCO2, int opnvCO2, PieChart pieChart){
        List<PieEntry> entries = new ArrayList<>();
        if(autoCO2 != 0)
            entries.add(new PieEntry(autoCO2, "Auto"));
        if (opnvCO2 != 0)
            entries.add(new PieEntry(opnvCO2, "ÖPNV"));

        PieDataSet set = new PieDataSet(entries, "CO2-Ausstoss");
        set.setColors(new int[]{Color.rgb(200,0,0), Color.rgb(0,200,0),Color.rgb(0,0,200)}, 70);
        PieData data = new PieData(set);

        pieChart.setData(data);
        pieChart.setClickable(false);
        pieChart.setTouchEnabled(false);
        Description desc = new Description();
        int gesamt = autoCO2 + opnvCO2;
        pieChart.setCenterText("Insgesamt: \n  " + gesamt +"Tonnen CO2");
        desc.setText("");
        pieChart.setDescription(desc);
        pieChart.setRotationEnabled(false);
        return pieChart;
    }

    public static PieChart makeGesamtDistanzDiagramm(int autoDistanz, int opnvDistanz, int fahrradDistanz, PieChart pieChart){
        List<PieEntry> entries = new ArrayList<>();
        if(autoDistanz != 0)
            entries.add(new PieEntry(autoDistanz, "Auto"));
        if(opnvDistanz != 0)
            entries.add(new PieEntry(opnvDistanz, "ÖPNV"));
        if(fahrradDistanz != 0)
            entries.add(new PieEntry(fahrradDistanz, "Fahrrad"));


        PieDataSet set = new PieDataSet(entries, "Distanz");
        set.setColors(new int[]{Color.rgb(200,0,0), Color.rgb(0,200,0),Color.rgb(0,0,200)}, 70);
        PieData data = new PieData(set);

        pieChart.setData(data);
        pieChart.setClickable(false);
        pieChart.setRotationEnabled(false);
        pieChart.setTouchEnabled(false);
        int gesamt = autoDistanz + opnvDistanz + fahrradDistanz;
        pieChart.setCenterText("Insgesamt: \n  " + gesamt +" Kilometer");
        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);
        return pieChart;
    }

    public static PieChart makeGesamtZeitDiagramm(int autoZeit, int opnvZeit, int fahrradZeit, PieChart pieChart){
        List<PieEntry> entries = new ArrayList<>();
        if(autoZeit != 0)
            entries.add(new PieEntry(autoZeit, "Auto"));

        if(opnvZeit != 0)
            entries.add(new PieEntry(opnvZeit, "ÖPNV"));

        if(fahrradZeit != 0)
            entries.add(new PieEntry(fahrradZeit, "Fahrrad"));


        PieDataSet set = new PieDataSet(entries, "Zeit");
        set.setColors(new int[]{Color.rgb(200,0,0), Color.rgb(0,200,0),Color.rgb(0,0,200)}, 70);
        PieData data = new PieData(set);

        pieChart.setData(data);
        pieChart.setTouchEnabled(false);
        pieChart.setRotationEnabled(false);
        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);
        int gesamt = autoZeit + opnvZeit + fahrradZeit;
        pieChart.setCenterText("Insgesamt: \n  " + gesamt +" Stunden");
        return pieChart;
    }

    public static BarChart makeMonatGesamtCO2BarChart(final ArrayList<AnalyseergebnisMonat> monate, BarChart barChart) {
        List<BarEntry> entries = new ArrayList<>();
        int lastIndex = monate.size()-1;

        for (int i = 0; i < monate.size(); i++) {
            entries.add(new BarEntry(i, new float[]{monate.get(lastIndex - i).getCO2Auto(), monate.get(lastIndex - i).getCO2Opnv()}));
        }
        BarDataSet set = new BarDataSet(entries, "Gesamt CO2 Emissionen");
        set.setStackLabels(new String[]{"Auto","ÖPNV"});
        set.setDrawIcons(false);
        set.setColors(new int[]{Color.rgb(200,0,0), Color.rgb(0,200,0)}, 70);
        set.setDrawValues(false);
        BarData barData = new BarData(set);

        barChart.setData(barData);
        barChart.setScaleEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setTouchEnabled(false);
        Description des = new Description();
        des.setText("");
        barChart.setDescription(des);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int lastIndex = monate.size()-1;
                return  monate.get((lastIndex - (int) value)).getDate().get(Calendar.MONTH) +  " " +monate.get(lastIndex - ((int) value)).getDate().get(Calendar.YEAR);
            }
        });

        YAxis yAxis1 = barChart.getAxisLeft();
        YAxis yAxis2 = barChart.getAxisRight();
        yAxis1.setDrawGridLines(false);
        yAxis1.setAxisMinimum(0);
        yAxis2.setEnabled(false);
        return barChart;
    }

    public static BarChart makeMonatGesamtDistanzBarChart(final ArrayList<AnalyseergebnisMonat> monate, BarChart barChart) {
        List<BarEntry> entries = new ArrayList<>();
        int lastIndex = monate.size()-1;

        for (int i = 0; i < monate.size(); i++) {
            entries.add(new BarEntry(i, new float[]{monate.get(lastIndex - i).getAutoDistanz(), monate.get(lastIndex - i).getOpnvDistanz(), monate.get(lastIndex - i).getFahrradDistanz()}));
        }
        BarDataSet set = new BarDataSet(entries, "Gesamt Distanz");
        set.setStackLabels(new String[]{"Auto","ÖPNV","Fahrrad"});
        set.setDrawIcons(false);
        set.setColors(new int[]{Color.rgb(200,0,0), Color.rgb(0,200,0), Color.rgb(0,0,200)}, 70);
        set.setDrawValues(false);
        BarData barData = new BarData(set);

        barChart.setData(barData);
        barChart.setScaleEnabled(false);
        barChart.setDrawGridBackground(false);
        Description des = new Description();
        des.setText("");
        barChart.setDescription(des);
        barChart.setTouchEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int lastIndex = monate.size()-1;
                return  monate.get(lastIndex - ((int) value)).getDate().get(Calendar.MONTH) +  " " +monate.get(lastIndex - ((int) value)).getDate().get(Calendar.YEAR);
            }
        });

        YAxis yAxis1 = barChart.getAxisLeft();
        YAxis yAxis2 = barChart.getAxisRight();
        yAxis1.setDrawGridLines(false);
        yAxis1.setAxisMinimum(0);
        yAxis2.setEnabled(false);
        return barChart;
    }

    public static BarChart makeMonatGesamtZeitBarChart(final ArrayList<AnalyseergebnisMonat> monate, BarChart barChart) {
        List<BarEntry> entries = new ArrayList<>();
        int lastIndex = monate.size()-1;

        for (int i = 0; i < monate.size(); i++) {
            entries.add(new BarEntry(i, new float[]{monate.get(lastIndex - i).getZeitAuto(), monate.get(lastIndex - i).getZeitOpnv(), monate.get(lastIndex - i).getZeitFahrrad()}));
        }
        BarDataSet set = new BarDataSet(entries, "Gesamt Zeit");
        set.setStackLabels(new String[]{"Auto","ÖPNV","Fahrrad"});
        set.setDrawIcons(false);
        set.setColors(new int[]{Color.rgb(200,0,0), Color.rgb(0,200,0), Color.rgb(0,0,200)}, 70);
        set.setDrawValues(false);
        BarData barData = new BarData(set);

        barChart.setData(barData);
        barChart.setScaleEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setTouchEnabled(false);
        Description des = new Description();
        des.setText("");
        barChart.setDescription(des);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int lastIndex = monate.size()-1;
                return  monate.get(lastIndex - ((int) value)).getDate().get(Calendar.MONTH) +  " " +monate.get(lastIndex - ((int) value)).getDate().get(Calendar.YEAR);
            }
        });

        YAxis yAxis1 = barChart.getAxisLeft();
        YAxis yAxis2 = barChart.getAxisRight();
        yAxis1.setDrawGridLines(false);
        yAxis1.setAxisMinimum(0);
        yAxis2.setEnabled(false);
        return barChart;
    }

    public static PieChart makeTagGesamtZeitPieChart(final AnalyseergebnisTag tag, PieChart pieChart){
        List<PieEntry> entries = new ArrayList<>();
        if(tag.getAutoDauer() != 0)
            entries.add(new PieEntry(tag.getAutoDauer(), "Auto"));

        if(tag.getOpnvDauer() != 0)
            entries.add(new PieEntry(tag.getOpnvDauer(), "ÖPNV"));

        if(tag.getFahrradDauer() != 0)
            entries.add(new PieEntry(tag.getFahrradDauer(), "Fahrrad"));


        PieDataSet set = new PieDataSet(entries, "Zeit");
        set.setColors(new int[]{Color.rgb(200,0,0), Color.rgb(0,200,0),Color.rgb(0,0,200)}, 70);
        PieData data = new PieData(set);

        pieChart.setData(data);
        pieChart.setTouchEnabled(false);
        pieChart.setRotationEnabled(false);
        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);
        int gesamt = tag.getAutoDauer() + tag.getFahrradDauer() + tag.getOpnvDauer();
        pieChart.setCenterText("Insgesamt: \n  " + gesamt +" Stunden");
        return pieChart;
    }

    public static PieChart makeTagGesamtCO2PieChart(final AnalyseergebnisTag tag, PieChart pieChart){
        List<PieEntry> entries = new ArrayList<>();

        if(tag.getAutoCO2Austoss() != 0)
            entries.add(new PieEntry(tag.getAutoCO2Austoss(), "Auto"));

        if(tag.getOpnvCO2Austoss() != 0)
            entries.add(new PieEntry(tag.getOpnvCO2Austoss(), "ÖPNV"));

        if(tag.getFahrradCO2Austoss() != 0)
            entries.add(new PieEntry(tag.getFahrradCO2Austoss(), "Fahrrad"));


        PieDataSet set = new PieDataSet(entries, "CO2");
        set.setColors(new int[]{Color.rgb(200,0,0), Color.rgb(0,200,0),Color.rgb(0,0,200)}, 70);
        PieData data = new PieData(set);

        pieChart.setData(data);
        pieChart.setTouchEnabled(false);
        pieChart.setRotationEnabled(false);
        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);
        int gesamt = tag.getCO2Austoss();
        pieChart.setCenterText("Insgesamt: \n  " + gesamt +" Stunden");
        return pieChart;
    }

    public static PieChart makeTagGesamtDistanzPieChart(final AnalyseergebnisTag tag, PieChart pieChart){
        List<PieEntry> entries = new ArrayList<>();

        if(tag.getAutoDistanz() != 0)
            entries.add(new PieEntry(tag.getAutoDistanz(), "Auto"));

        if(tag.getOpnvDistanz() != 0)
            entries.add(new PieEntry(tag.getOpnvDistanz(), "ÖPNV"));

        if(tag.getFahrradDistanz() != 0)
            entries.add(new PieEntry(tag.getFahrradDistanz(), "Fahrrad"));


        PieDataSet set = new PieDataSet(entries, "Distanz");
        set.setColors(new int[]{Color.rgb(200,0,0), Color.rgb(0,200,0),Color.rgb(0,0,200)}, 70);
        PieData data = new PieData(set);

        pieChart.setData(data);
        pieChart.setTouchEnabled(false);
        pieChart.setRotationEnabled(false);
        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);
        int gesamt = tag.getCO2Austoss();
        pieChart.setCenterText("Insgesamt: \n  " + gesamt +" Stunden");
        return pieChart;
    }

    public static PieChart makeWegGesamtZeitPieChart(final AnalyseergebnisWeg weg, PieChart pieChart){
        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(weg.getAutoDauer(), "Auto"));
        entries.add(new PieEntry(weg.getOpnvDauer(), "ÖPNV"));
        entries.add(new PieEntry(weg.getFahrradDauer(), "Fahrrad"));


        PieDataSet set = new PieDataSet(entries, "Zeit");
        set.setColors(new int[]{Color.rgb(200,0,0), Color.rgb(0,200,0),Color.rgb(0,0,200)}, 70);
        PieData data = new PieData(set);

        pieChart.setData(data);
        pieChart.setTouchEnabled(false);
        pieChart.setRotationEnabled(false);
        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);
        int gesamt = weg.getDauer();
        pieChart.setCenterText("Insgesamt: \n  " + gesamt +" Stunden");
        return pieChart;
    }

    public static PieChart makeWegGesamtCO2PieChart(final AnalyseergebnisWeg weg, PieChart pieChart){
        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(weg.getAutoCO2Austoss(), "Auto"));
        entries.add(new PieEntry(weg.getOpnvCO2Austoss(), "ÖPNV"));
        entries.add(new PieEntry(weg.getFahrradCO2Austoss(), "Fahrrad"));


        PieDataSet set = new PieDataSet(entries, "CO2");
        set.setColors(new int[]{Color.rgb(200,0,0), Color.rgb(0,200,0),Color.rgb(0,0,200)}, 70);
        PieData data = new PieData(set);

        pieChart.setData(data);
        pieChart.setTouchEnabled(false);
        pieChart.setRotationEnabled(false);
        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);
        int gesamt = weg.getCO2Austoss();
        pieChart.setCenterText("Insgesamt: \n  " + gesamt +" Stunden");
        return pieChart;
    }

    public static PieChart makeWegGesamtDistanzPieChart(final AnalyseergebnisWeg weg, PieChart pieChart){
        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(weg.getAutoDistanz(), "Auto"));
        entries.add(new PieEntry(weg.getOpnvDistanz(), "ÖPNV"));
        entries.add(new PieEntry(weg.getFahrradDistanz(), "Fahrrad"));


        PieDataSet set = new PieDataSet(entries, "Distanz");
        set.setColors(new int[]{Color.rgb(200,0,0), Color.rgb(0,200,0),Color.rgb(0,0,200)}, 70);
        PieData data = new PieData(set);

        pieChart.setData(data);
        pieChart.setTouchEnabled(false);
        pieChart.setRotationEnabled(false);
        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);
        int gesamt = weg.getCO2Austoss();
        pieChart.setCenterText("Insgesamt: \n  " + gesamt +" Stunden");
        return pieChart;
    }

    public static BarChart makeTagGesamtCO2BarChart(final ArrayList<AnalyseergebnisTag> tage, BarChart barChart) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < tage.size(); i++) {
            entries.add(new BarEntry(i, new float[]{tage.get(i).getAutoCO2Austoss(), tage.get(i).getOpnvCO2Austoss()}));
        }
        BarDataSet set = new BarDataSet(entries, "Gesamt CO2 Emissionen");
        set.setStackLabels(new String[]{"Auto","ÖPNV"});
        set.setDrawIcons(false);
        set.setColors(new int[]{Color.rgb(200,0,0), Color.rgb(0,200,0)}, 70);
        BarData barData = new BarData(set);

        barChart.setData(barData);
        barChart.setScaleEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setTouchEnabled(false);
        Description des = new Description();
        des.setText("");
        barChart.setDescription(des);
        set.setDrawValues(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return  tage.get((int) value).getTag().get(Calendar.MONTH) +  "." +tage.get((int) value).getTag().get(Calendar.YEAR) + "." + tage.get((int) value).getTag().get(Calendar.DAY_OF_MONTH);

            }
        });

        YAxis yAxis1 = barChart.getAxisLeft();
        YAxis yAxis2 = barChart.getAxisRight();
        yAxis1.setDrawGridLines(false);
        yAxis1.setAxisMinimum(0);
        yAxis2.setEnabled(false);
        return barChart;
    }

    public static BarChart makeTagGesamtDistanzBarChart(final ArrayList<AnalyseergebnisTag> tage, BarChart barChart) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < tage.size(); i++) {
            entries.add(new BarEntry(i, new float[]{tage.get(i).getAutoDistanz(), tage.get(i).getOpnvDistanz(),tage.get(i).getFahrradDistanz()}));
        }
        BarDataSet set = new BarDataSet(entries, "Gesamt Distanz");
        set.setStackLabels(new String[]{"Auto","ÖPNV","Fahrrad"});
        set.setDrawIcons(false);
        set.setColors(new int[]{Color.rgb(200,0,0), Color.rgb(0,200,0), Color.rgb(0,0,200)}, 70);

        BarData barData = new BarData(set);

        barChart.setData(barData);
        barChart.setScaleEnabled(false);
        barChart.setDrawGridBackground(false);
        Description des = new Description();
        des.setText("");
        barChart.setDescription(des);
        barChart.setTouchEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        set.setDrawValues(false);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return  tage.get((int) value).getTag().get(Calendar.MONTH) +  "." +tage.get((int) value).getTag().get(Calendar.YEAR) + "." + tage.get((int) value).getTag().get(Calendar.DAY_OF_MONTH);

            }
        });

        YAxis yAxis1 = barChart.getAxisLeft();
        YAxis yAxis2 = barChart.getAxisRight();
        yAxis1.setDrawGridLines(false);
        yAxis1.setAxisMinimum(0);
        yAxis2.setEnabled(false);
        return barChart;
    }

    public static BarChart makeTagGesamtZeitBarChart(final ArrayList<AnalyseergebnisTag> tage, BarChart barChart) {
        List<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < tage.size(); i++) {
            entries.add(new BarEntry(i, new float[]{tage.get(i).getAutoDauer(), tage.get(i).getOpnvDauer(), tage.get(i).getFahrradDauer()}));
        }
        BarDataSet set = new BarDataSet(entries, "Gesamt Zeit");
        set.setStackLabels(new String[]{"Auto","ÖPNV","Fahrrad"});
        set.setDrawIcons(false);
        set.setColors(new int[]{Color.rgb(200,0,0), Color.rgb(0,200,0), Color.rgb(0,0,200)}, 70);
        BarData barData = new BarData(set);
        set.setDrawValues(false);
        barChart.setData(barData);
        barChart.setScaleEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setTouchEnabled(false);
        Description des = new Description();
        des.setText("");
        barChart.setDescription(des);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return  tage.get((int) value).getTag().get(Calendar.MONTH) +  "." +tage.get((int) value).getTag().get(Calendar.YEAR) + "." + tage.get((int) value).getTag().get(Calendar.DAY_OF_MONTH);
            }
        });

        YAxis yAxis1 = barChart.getAxisLeft();
        YAxis yAxis2 = barChart.getAxisRight();
        yAxis1.setDrawGridLines(false);
        yAxis1.setAxisMinimum(0);
        yAxis2.setEnabled(false);
        return barChart;
    }
}
