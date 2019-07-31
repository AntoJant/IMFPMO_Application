package com.imfpmo.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

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

public class AnalysisDiagramMaker {

    public static PieChart makeGesamtCO2Diagramm(int autoCO2, int opnvCO2, PieChart pieChart, Context context) {
        Drawable auto = context.getDrawable(R.drawable.ic_directions_car_black_24dp);
        Drawable bike = context.getDrawable(R.drawable.ic_directions_bike_black_24dp);
        Drawable opnv = context.getDrawable(R.drawable.ic_directions_bus_black_24dp);
        Drawable walk = context.getDrawable(R.drawable.ic_directions_walk_black_24dp);

        List<PieEntry> entries = new ArrayList<>();
        if (autoCO2 != 0)
            entries.add(new PieEntry(autoCO2, auto));
        if (opnvCO2 != 0)
            entries.add(new PieEntry(opnvCO2, opnv));

        PieDataSet set = new PieDataSet(entries, "CO2-Ausstoss");
        set.setColors(new int[]{Color.rgb(200, 0, 0), Color.rgb(0, 200, 0), Color.rgb(0, 0, 200)}, 70);
        set.setDrawValues(false);
        PieData data = new PieData(set);

        pieChart.setData(data);
        pieChart.setClickable(false);
        pieChart.setTouchEnabled(false);
        Description desc = new Description();
        int gesamt = autoCO2 + opnvCO2;
        pieChart.setCenterText("Insgesamt: \n  " + gesamt + "gramm CO2");
        desc.setText("");
        pieChart.setDescription(desc);
        pieChart.setRotationEnabled(false);

        pieChart.getLegend().setEnabled(false);
        return pieChart;
    }

    public static PieChart makeGesamtDistanzDiagramm(int autoDistanz, int opnvDistanz, int fahrradDistanz, int fussDistanz, PieChart pieChart, Context context) {
        Drawable auto = context.getDrawable(R.drawable.ic_directions_car_black_24dp);
        Drawable bike = context.getDrawable(R.drawable.ic_directions_bike_black_24dp);
        Drawable opnv = context.getDrawable(R.drawable.ic_directions_bus_black_24dp);
        Drawable walk = context.getDrawable(R.drawable.ic_directions_walk_black_24dp);
        List<PieEntry> entries = new ArrayList<>();
        if (autoDistanz != 0)
            entries.add(new PieEntry(autoDistanz, auto));
        if (opnvDistanz != 0)
            entries.add(new PieEntry(opnvDistanz, opnv));
        if (fahrradDistanz != 0)
            entries.add(new PieEntry(fahrradDistanz, bike));
        if (fussDistanz != 0) {
            entries.add(new PieEntry(fussDistanz, walk));
        }


        PieDataSet set = new PieDataSet(entries, "Distanz");
        set.setDrawValues(false);
        set.setColors(new int[]{Color.rgb(200, 0, 0), Color.rgb(0, 200, 0), Color.rgb(0, 0, 200), Color.rgb(0, 200, 200)}, 70);
        PieData data = new PieData(set);

        pieChart.setData(data);
        pieChart.setClickable(false);
        pieChart.setRotationEnabled(false);
        pieChart.setTouchEnabled(false);
        int gesamt = autoDistanz + opnvDistanz + fahrradDistanz;
        pieChart.setCenterText("Insgesamt: \n  " + gesamt + " km");
        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);
        pieChart.getLegend().setEnabled(false);

        return pieChart;
    }

    public static PieChart makeGesamtZeitDiagramm(int autoZeit, int opnvZeit, int fahrradZeit, int fuss, PieChart pieChart, Context context) {
        Drawable auto = context.getDrawable(R.drawable.ic_directions_car_black_24dp);
        Drawable bike = context.getDrawable(R.drawable.ic_directions_bike_black_24dp);
        Drawable opnv = context.getDrawable(R.drawable.ic_directions_bus_black_24dp);
        Drawable walk = context.getDrawable(R.drawable.ic_directions_walk_black_24dp);
        List<PieEntry> entries = new ArrayList<>();

        if (autoZeit != 0)
            entries.add(new PieEntry(autoZeit, auto));

        if (opnvZeit != 0)
            entries.add(new PieEntry(opnvZeit, bike));

        if (fahrradZeit != 0)
            entries.add(new PieEntry(fahrradZeit, opnv));

        if (fuss != 0) {
            entries.add(new PieEntry(fuss, walk));
        }

        PieDataSet set = new PieDataSet(entries, "Zeit");
        set.setDrawValues(false);
        set.setColors(new int[]{Color.rgb(200, 0, 0), Color.rgb(0, 200, 0), Color.rgb(0, 0, 200), Color.rgb(0, 200, 200)}, 70);
        PieData data = new PieData(set);

        pieChart.setData(data);
        pieChart.setTouchEnabled(false);
        pieChart.setRotationEnabled(false);
        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);
        int gesamt = autoZeit + opnvZeit + fahrradZeit;
        pieChart.setCenterText("Insgesamt: \n  " + gesamt + " Stunden");
        pieChart.getLegend().setEnabled(false);

        return pieChart;
    }

    public static BarChart makeMonatGesamtCO2BarChart(final ArrayList<AnalysisResultMonth> monate, BarChart barChart, Context context) {
        List<BarEntry> entries = new ArrayList<>();
        int lastIndex = monate.size() - 1;

        for (int i = 0; i < monate.size(); i++) {
            entries.add(new BarEntry(i, new float[]{monate.get(lastIndex - i).getCO2Auto(), monate.get(lastIndex - i).getCO2Opnv()}));
        }
        BarDataSet set = new BarDataSet(entries, "Gesamt CO2 Emissionen");
        set.setStackLabels(new String[]{"Auto", "ÖPNV"});
        set.setDrawIcons(false);
        set.setColors(new int[]{Color.rgb(200, 0, 0), Color.rgb(0, 200, 0)}, 70);
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
                int lastIndex = monate.size() - 1;
                int realMonat = monate.get((lastIndex - (int) value)).getDate().get(Calendar.MONTH) + 1;
                return realMonat + " " + monate.get(lastIndex - ((int) value)).getDate().get(Calendar.YEAR);
            }
        });

        YAxis yAxis1 = barChart.getAxisLeft();
        YAxis yAxis2 = barChart.getAxisRight();
        yAxis1.setDrawGridLines(false);
        yAxis1.setAxisMinimum(0);
        yAxis2.setEnabled(false);
        return barChart;
    }

    public static BarChart makeMonatGesamtDistanzBarChart(final ArrayList<AnalysisResultMonth> monate, BarChart barChart, Context context) {
        List<BarEntry> entries = new ArrayList<>();
        int lastIndex = monate.size() - 1;

        for (int i = 0; i < monate.size(); i++) {
            entries.add(new BarEntry(i, new float[]{monate.get(lastIndex - i).getAutoDistanz(), monate.get(lastIndex - i).getOpnvDistanz(), monate.get(lastIndex - i).getFahrradDistanz(), monate.get(lastIndex - i).getFussDistanz()}));
        }
        BarDataSet set = new BarDataSet(entries, "Gesamt Distanz");
        set.setStackLabels(new String[]{"Auto", "ÖPNV", "Fahrrad", "zu Fuß"});
        set.setDrawIcons(false);
        set.setColors(new int[]{Color.rgb(200, 0, 0), Color.rgb(0, 200, 0), Color.rgb(0, 0, 200), Color.rgb(0, 200, 200)}, 70);
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
                int lastIndex = monate.size() - 1;
                return monate.get(lastIndex - ((int) value)).getDate().get(Calendar.MONTH) + " " + monate.get(lastIndex - ((int) value)).getDate().get(Calendar.YEAR);
            }
        });

        YAxis yAxis1 = barChart.getAxisLeft();
        YAxis yAxis2 = barChart.getAxisRight();
        yAxis1.setDrawGridLines(false);
        yAxis1.setAxisMinimum(0);
        yAxis2.setEnabled(false);
        return barChart;
    }

    public static BarChart makeMonatGesamtZeitBarChart(final ArrayList<AnalysisResultMonth> monate, BarChart barChart, Context context) {
        List<BarEntry> entries = new ArrayList<>();
        int lastIndex = monate.size() - 1;

        for (int i = 0; i < monate.size(); i++) {
            entries.add(new BarEntry(i, new float[]{monate.get(lastIndex - i).getZeitAuto(), monate.get(lastIndex - i).getZeitOpnv(), monate.get(lastIndex - i).getZeitFahrrad(), monate.get(lastIndex - i).getZeitFuss()}));
        }
        BarDataSet set = new BarDataSet(entries, "Gesamt Zeit");
        set.setStackLabels(new String[]{"Auto", "ÖPNV", "Fahrrad", "zu Fuß"});
        set.setDrawIcons(false);
        set.setColors(new int[]{Color.rgb(200, 0, 0), Color.rgb(0, 200, 0), Color.rgb(0, 0, 200), Color.rgb(0, 200, 200)}, 70);
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
                int lastIndex = monate.size() - 1;
                return monate.get(lastIndex - ((int) value)).getDate().get(Calendar.MONTH) + " " + monate.get(lastIndex - ((int) value)).getDate().get(Calendar.YEAR);
            }
        });

        YAxis yAxis1 = barChart.getAxisLeft();
        YAxis yAxis2 = barChart.getAxisRight();
        yAxis1.setDrawGridLines(false);
        yAxis1.setAxisMinimum(0);
        yAxis2.setEnabled(false);
        return barChart;
    }

    public static PieChart makeTagGesamtZeitPieChart(final AnalysisResultDay tag, PieChart pieChart, Context context) {
        Drawable auto = context.getDrawable(R.drawable.ic_directions_car_black_24dp);
        Drawable bike = context.getDrawable(R.drawable.ic_directions_bike_black_24dp);
        Drawable opnv = context.getDrawable(R.drawable.ic_directions_bus_black_24dp);
        Drawable walk = context.getDrawable(R.drawable.ic_directions_walk_black_24dp);
        List<PieEntry> entries = new ArrayList<>();
        if (tag.getAutoDauer() != 0)
            entries.add(new PieEntry(tag.getAutoDauer(), auto));

        if (tag.getOpnvDauer() != 0)
            entries.add(new PieEntry(tag.getOpnvDauer(), opnv));

        if (tag.getFahrradDauer() != 0)
            entries.add(new PieEntry(tag.getFahrradDauer(), bike));

        if (tag.getFussDauer() != 0) {
            entries.add(new PieEntry(tag.getFussDauer(), walk));
        }

        PieDataSet set = new PieDataSet(entries, "Zeit");
        set.setDrawValues(false);
        set.setColors(new int[]{Color.rgb(200, 0, 0), Color.rgb(0, 200, 0), Color.rgb(0, 0, 200), Color.rgb(0, 200, 200)}, 70);
        PieData data = new PieData(set);

        pieChart.setData(data);
        pieChart.setTouchEnabled(false);
        pieChart.setRotationEnabled(false);
        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);
        float gesamt = tag.getDauer();
        pieChart.setCenterText("Insgesamt: \n  " + gesamt + " Stunden");
        pieChart.getLegend().setEnabled(false);
        return pieChart;
    }

    public static PieChart makeTagGesamtCO2PieChart(final AnalysisResultDay tag, PieChart pieChart, Context context) {
        Drawable auto = context.getDrawable(R.drawable.ic_directions_car_black_24dp);
        Drawable bike = context.getDrawable(R.drawable.ic_directions_bike_black_24dp);
        Drawable opnv = context.getDrawable(R.drawable.ic_directions_bus_black_24dp);
        Drawable walk = context.getDrawable(R.drawable.ic_directions_walk_black_24dp);
        List<PieEntry> entries = new ArrayList<>();

        if (tag.getAutoCO2Austoss() != 0)
            entries.add(new PieEntry(tag.getAutoCO2Austoss(), auto));

        if (tag.getOpnvCO2Austoss() != 0)
            entries.add(new PieEntry(tag.getOpnvCO2Austoss(), opnv));

        if (tag.getFahrradCO2Austoss() != 0)
            entries.add(new PieEntry(tag.getFahrradCO2Austoss(), bike));

        if (tag.getFussCO2Austoss() != 0) {
            entries.add(new PieEntry(tag.getFussCO2Austoss(), walk));
        }
        PieDataSet set = new PieDataSet(entries, "CO2");
        set.setDrawValues(false);
        set.setColors(new int[]{Color.rgb(200, 0, 0), Color.rgb(0, 200, 0), Color.rgb(0, 0, 200), Color.rgb(0, 200, 200)}, 70);
        PieData data = new PieData(set);

        pieChart.setData(data);
        pieChart.setTouchEnabled(false);
        pieChart.setRotationEnabled(false);
        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);
        int gesamt = tag.getCO2Austoss();
        pieChart.setCenterText("Insgesamt: \n  " + gesamt + " gramm");
        pieChart.getLegend().setEnabled(false);

        return pieChart;
    }

    public static PieChart makeTagGesamtDistanzPieChart(final AnalysisResultDay tag, PieChart pieChart, Context context) {
        Drawable auto = context.getDrawable(R.drawable.ic_directions_car_black_24dp);
        Drawable bike = context.getDrawable(R.drawable.ic_directions_bike_black_24dp);
        Drawable opnv = context.getDrawable(R.drawable.ic_directions_bus_black_24dp);
        Drawable walk = context.getDrawable(R.drawable.ic_directions_walk_black_24dp);
        List<PieEntry> entries = new ArrayList<>();

        if (tag.getAutoDistanz() != 0)
            entries.add(new PieEntry(tag.getAutoDistanz(), auto));

        if (tag.getOpnvDistanz() != 0)
            entries.add(new PieEntry(tag.getOpnvDistanz(), opnv));

        if (tag.getFahrradDistanz() != 0)
            entries.add(new PieEntry(tag.getFahrradDistanz(), bike));

        if (tag.getFussDistanz() != 0)
            entries.add(new PieEntry(tag.getFahrradDistanz(), walk));

        PieDataSet set = new PieDataSet(entries, "Distanz");
        set.setDrawValues(false);
        set.setColors(new int[]{Color.rgb(200, 0, 0), Color.rgb(0, 200, 0), Color.rgb(0, 0, 200), Color.rgb(0, 200, 200)}, 70);
        PieData data = new PieData(set);

        pieChart.setData(data);
        pieChart.setTouchEnabled(false);
        pieChart.setRotationEnabled(false);
        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);
        int gesamt = tag.getDistanz();
        pieChart.setCenterText("Insgesamt: \n  " + gesamt + " km");
        pieChart.getLegend().setEnabled(false);

        return pieChart;
    }

    public static PieChart makeWegGesamtZeitPieChart(final AnalysisResultPath weg, PieChart pieChart, Context context) {
        List<PieEntry> entries = new ArrayList<>();
        Drawable auto = context.getDrawable(R.drawable.ic_directions_car_black_24dp);
        Drawable bike = context.getDrawable(R.drawable.ic_directions_bike_black_24dp);
        Drawable opnv = context.getDrawable(R.drawable.ic_directions_bus_black_24dp);
        Drawable walk = context.getDrawable(R.drawable.ic_directions_walk_black_24dp);
        if (weg.getAutoDauer() != 0) {
            entries.add(new PieEntry(weg.getAutoDauer(), auto));
        }
        if (weg.getOpnvDauer() != 0)
            entries.add(new PieEntry(weg.getOpnvDauer(), opnv));

        if (weg.getFahrradDauer() != 0)
            entries.add(new PieEntry(weg.getFahrradDauer(), bike));

        if (weg.getFussDauer() != 0)
            entries.add(new PieEntry(weg.getFussDauer(), walk));

        PieDataSet set = new PieDataSet(entries, "Zeit");
        set.setDrawValues(false);
        set.setColors(new int[]{Color.rgb(200, 0, 0), Color.rgb(0, 200, 0), Color.rgb(0, 0, 200), Color.rgb(0, 200, 200)}, 70);
        PieData data = new PieData(set);

        pieChart.setData(data);
        pieChart.setTouchEnabled(false);
        pieChart.setRotationEnabled(false);
        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);
        float gesamt = weg.getDauer();
        pieChart.setCenterText("Insgesamt: \n  " + gesamt + " min");
        pieChart.getLegend().setEnabled(false);

        return pieChart;
    }

    public static PieChart makeWegGesamtCO2PieChart(final AnalysisResultPath weg, PieChart pieChart, Context context) {
        Drawable auto = context.getDrawable(R.drawable.ic_directions_car_black_24dp);
        Drawable bike = context.getDrawable(R.drawable.ic_directions_bike_black_24dp);
        Drawable opnv = context.getDrawable(R.drawable.ic_directions_bus_black_24dp);
        Drawable walk = context.getDrawable(R.drawable.ic_directions_walk_black_24dp);
        List<PieEntry> entries = new ArrayList<>();

        if (weg.getAutoCO2Austoss() != 0) {
            entries.add(new PieEntry(weg.getAutoCO2Austoss(), auto));
        }
        if (weg.getOpnvCO2Austoss() != 0) {
            entries.add(new PieEntry(weg.getOpnvCO2Austoss(), opnv));
        }

        if (weg.getFahrradCO2Austoss() != 0) {
            entries.add(new PieEntry(weg.getFahrradCO2Austoss(), bike));
        }

        if (weg.getFussCO2Austoss() != 0) {
            entries.add(new PieEntry(weg.getFussCO2Austoss(), walk));
        }


        PieDataSet set = new PieDataSet(entries, "CO2");
        set.setDrawValues(false);
        set.setColors(new int[]{Color.rgb(200, 0, 0), Color.rgb(0, 200, 0), Color.rgb(0, 0, 200), Color.rgb(0, 200, 200)}, 70);
        PieData data = new PieData(set);

        pieChart.setData(data);
        pieChart.setTouchEnabled(false);
        pieChart.setRotationEnabled(false);
        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);
        int gesamt = weg.getCO2Austoss();
        pieChart.setCenterText("Insgesamt: \n  " + gesamt + " CO2");
        pieChart.getLegend().setEnabled(false);

        return pieChart;
    }

    public static PieChart makeWegGesamtDistanzPieChart(final AnalysisResultPath weg, PieChart pieChart, Context context) {
        List<PieEntry> entries = new ArrayList<>();
        Drawable auto = context.getDrawable(R.drawable.ic_directions_car_black_24dp);
        Drawable bike = context.getDrawable(R.drawable.ic_directions_bike_black_24dp);
        Drawable opnv = context.getDrawable(R.drawable.ic_directions_bus_black_24dp);
        Drawable walk = context.getDrawable(R.drawable.ic_directions_walk_black_24dp);
        if (weg.getAutoDistanz() != 0) {
            entries.add(new PieEntry(weg.getAutoDistanz(), auto));
        }
        if (weg.getOpnvDistanz() != 0) {
            entries.add(new PieEntry(weg.getOpnvDistanz(), opnv));
        }
        if (weg.getFahrradDistanz() != 0) {
            entries.add(new PieEntry(weg.getFahrradDistanz(), bike));
        }

        if (weg.getFussDistanz() != 0) {
            entries.add(new PieEntry(weg.getFussDistanz(), walk));
        }


        PieDataSet set = new PieDataSet(entries, "Distanz");
        set.setDrawValues(false);
        set.setColors(new int[]{Color.rgb(200, 0, 0), Color.rgb(0, 200, 0), Color.rgb(0, 0, 200), Color.rgb(0, 200, 200)}, 70);
        PieData data = new PieData(set);

        pieChart.setData(data);
        pieChart.setTouchEnabled(false);
        pieChart.setRotationEnabled(false);
        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);
        int gesamt = weg.getDistanz();
        pieChart.setCenterText("Insgesamt: \n  " + gesamt + " Kilometer");
        pieChart.getLegend().setEnabled(false);

        return pieChart;
    }

    public static BarChart makeTagGesamtCO2BarChart(final ArrayList<AnalysisResultDay> tage, BarChart barChart, Context context) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < tage.size(); i++) {
            entries.add(new BarEntry(i, new float[]{tage.get(i).getAutoCO2Austoss(), tage.get(i).getOpnvCO2Austoss()}));
        }
        BarDataSet set = new BarDataSet(entries, "Gesamt CO2 Emissionen");
        set.setStackLabels(new String[]{"Auto", "ÖPNV"});

        set.setDrawIcons(false);
        set.setColors(new int[]{Color.rgb(200, 0, 0), Color.rgb(0, 200, 0)}, 70);

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
                return Integer.toString((int) value);
            }
        });

        YAxis yAxis1 = barChart.getAxisLeft();
        YAxis yAxis2 = barChart.getAxisRight();
        yAxis1.setDrawGridLines(false);
        yAxis1.setAxisMinimum(0);
        yAxis2.setEnabled(false);
        return barChart;
    }

    public static BarChart makeTagGesamtDistanzBarChart(final ArrayList<AnalysisResultDay> tage, BarChart barChart, Context context) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < tage.size(); i++) {
            entries.add(new BarEntry(i, new float[]{tage.get(i).getAutoDistanz(), tage.get(i).getOpnvDistanz(), tage.get(i).getFahrradDistanz(), tage.get(i).getFussDistanz()}));
        }
        BarDataSet set = new BarDataSet(entries, "Gesamt Distanz");
        set.setStackLabels(new String[]{"Auto", "ÖPNV", "Fahrrad", "zu Fuß"});
        set.setDrawIcons(false);
        set.setColors(new int[]{Color.rgb(200, 0, 0), Color.rgb(0, 200, 0), Color.rgb(0, 0, 200), Color.rgb(0, 200, 200)}, 70);

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
                return Integer.toString((int) value);
            }
        });

        YAxis yAxis1 = barChart.getAxisLeft();
        YAxis yAxis2 = barChart.getAxisRight();
        yAxis1.setDrawGridLines(false);
        yAxis1.setAxisMinimum(0);
        yAxis2.setEnabled(false);
        return barChart;
    }

    public static BarChart makeTagGesamtZeitBarChart(final ArrayList<AnalysisResultDay> tage, BarChart barChart, Context context) {
        List<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < tage.size(); i++) {
            entries.add(new BarEntry(i, new float[]{tage.get(i).getAutoDauer(), tage.get(i).getOpnvDauer(), tage.get(i).getFahrradDauer(), tage.get(i).getFussDauer()}));
        }
        BarDataSet set = new BarDataSet(entries, "Gesamt Zeit");
        set.setStackLabels(new String[]{"Auto", "ÖPNV", "Fahrrad", "zu Fuß"});
        set.setDrawIcons(false);
        set.setColors(new int[]{Color.rgb(200, 0, 0), Color.rgb(0, 200, 0), Color.rgb(0, 0, 200), Color.rgb(0, 200, 200)}, 70);
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
                return Integer.toString((int) value);
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