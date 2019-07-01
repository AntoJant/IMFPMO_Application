package com.example.mobileapp_praktikum;

import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class AnalyseDiagramMaker {
    public static PieChart makeGesamtCO2Diagramm(int autoCO2, int opnvCO2, PieChart pieChart){
        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(autoCO2, "Auto"));
        entries.add(new PieEntry(opnvCO2, "ÖPNV"));

        PieDataSet set = new PieDataSet(entries, "CO2-Ausstoss");
        set.setColors(new int[]{Color.rgb(200,0,0), Color.rgb(0,200,0),Color.rgb(0,0,200)}, 70);
        PieData data = new PieData(set);

        pieChart.setData(data);
        pieChart.setClickable(false);
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

        entries.add(new PieEntry(autoDistanz, "Auto"));
        entries.add(new PieEntry(opnvDistanz, "ÖPNV"));
        entries.add(new PieEntry(fahrradDistanz, "Fahrrad"));


        PieDataSet set = new PieDataSet(entries, "Distanz");
        set.setColors(new int[]{Color.rgb(200,0,0), Color.rgb(0,200,0),Color.rgb(0,0,200)}, 70);
        PieData data = new PieData(set);

        pieChart.setData(data);
        pieChart.setClickable(false);
        pieChart.setRotationEnabled(false);
        int gesamt = autoDistanz + opnvDistanz + fahrradDistanz;
        pieChart.setCenterText("Insgesamt: \n  " + gesamt +" Kilometer");
        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);
        return pieChart;
    }

    public static PieChart makeGesamtZeitDiagramm(int autoZeit, int opnvZeit, int fahrradZeit, PieChart pieChart){
        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(autoZeit, "Auto"));
        entries.add(new PieEntry(opnvZeit, "ÖPNV"));
        entries.add(new PieEntry(fahrradZeit, "Fahrrad"));


        PieDataSet set = new PieDataSet(entries, "Zeit");
        set.setColors(new int[]{Color.rgb(200,0,0), Color.rgb(0,200,0),Color.rgb(0,0,200)}, 70);
        PieData data = new PieData(set);

        pieChart.setData(data);
        pieChart.setClickable(false);
        pieChart.setRotationEnabled(false);
        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);
        int gesamt = autoZeit + opnvZeit + fahrradZeit;
        pieChart.setCenterText("Insgesamt: \n  " + gesamt +" Stunden");
        return pieChart;
    }

}
