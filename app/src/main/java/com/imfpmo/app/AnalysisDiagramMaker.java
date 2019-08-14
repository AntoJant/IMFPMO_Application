package com.imfpmo.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

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
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AnalysisDiagramMaker {
    private static final int maxMinutes = 60;
    private static final int maxMeters = 1000;
    private static final int maxGrammCO2 = 1000;
    private static final int nachkommerstelleMinutes = 100;
    private static final int nachkommerstelleMeters = 10;
    private static final int nachkommerstelleCO2 = 100;


    public static BarChart makeMonthEmissionBarChart(final ArrayList<Month> months, BarChart barChart, Context context) {
        List<BarEntry> entries = new ArrayList<>();
        int lastIndex = months.size() - 1;

        for (int i = 0; i < months.size(); i++) {
            entries.add(new BarEntry(i, new float[]{months.get(lastIndex - i).getCarEmissions(), months.get(lastIndex - i).getOpnvEmissions()}));
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
                int lastIndex = months.size() - 1;
                int realMonat = months.get((lastIndex - (int) value)).getDate().get(Calendar.MONTH) + 1;
                return realMonat + "." + months.get(lastIndex - ((int) value)).getDate().get(Calendar.YEAR);
            }
        });

        YAxis yAxis1 = barChart.getAxisLeft();
        YAxis yAxis2 = barChart.getAxisRight();
        yAxis1.setDrawGridLines(false);
        yAxis1.setAxisMinimum(0);
        yAxis2.setEnabled(false);
        return barChart;
    }

    public static BarChart makeMonthDistanceBarChart(final ArrayList<Month> months, BarChart barChart, Context context) {
        List<BarEntry> entries = new ArrayList<>();
        int lastIndex = months.size() - 1;

        for (int i = 0; i < months.size(); i++) {
            entries.add(new BarEntry(i, new float[]{months.get(lastIndex - i).getCarDistance(), months.get(lastIndex - i).getOpnvDistance(), months.get(lastIndex - i).getBikeDistance(), months.get(lastIndex - i).getWalkDistance()}));
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
                int lastIndex = months.size() - 1;
                return months.get(lastIndex - ((int) value)).getDate().get(Calendar.MONTH) + "." + months.get(lastIndex - ((int) value)).getDate().get(Calendar.YEAR);
            }
        });

        YAxis yAxis1 = barChart.getAxisLeft();
        YAxis yAxis2 = barChart.getAxisRight();
        yAxis1.setDrawGridLines(false);
        yAxis1.setAxisMinimum(0);
        yAxis2.setEnabled(false);
        return barChart;
    }

    public static BarChart makeMonthTimeEffortBarChart(final ArrayList<Month> months, BarChart barChart, Context context) {
        List<BarEntry> entries = new ArrayList<>();
        int lastIndex = months.size() - 1;

        for (int i = 0; i < months.size(); i++) {
            entries.add(new BarEntry(i, new float[]{months.get(lastIndex - i).getCarTimeEffort(), months.get(lastIndex - i).getOpnvTimeEffort(), months.get(lastIndex - i).getBikeTimeEffort(), months.get(lastIndex - i).getWalkTimeEffort()}));
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
                int lastIndex = months.size() - 1;
                return months.get(lastIndex - ((int) value)).getDate().get(Calendar.MONTH) + "." + months.get(lastIndex - ((int) value)).getDate().get(Calendar.YEAR);
            }
        });

        YAxis yAxis1 = barChart.getAxisLeft();
        YAxis yAxis2 = barChart.getAxisRight();
        yAxis1.setDrawGridLines(false);
        yAxis1.setAxisMinimum(0);
        yAxis2.setEnabled(false);
        return barChart;
    }

    public static BarChart makeMonthTotalRideCountBarChart(final ArrayList<Month> months, BarChart barChart, Context context) {
        List<BarEntry> entries = new ArrayList<>();
        int lastIndex = months.size() - 1;

        for (int i = 0; i < months.size(); i++) {
            entries.add(new BarEntry(i, new float[]{months.get(lastIndex - i).getCarRideCount(), months.get(lastIndex - i).getOpnvRideCount(), months.get(lastIndex - i).getBikeRideCount(), months.get(lastIndex - i).getWalkRideCount()}));
        }
        BarDataSet set = new BarDataSet(entries, "Gesamtzahl Fahrten");
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
                int lastIndex = months.size() - 1;
                return months.get(lastIndex - ((int) value)).getDate().get(Calendar.MONTH) + "." + months.get(lastIndex - ((int) value)).getDate().get(Calendar.YEAR);
            }
        });

        YAxis yAxis1 = barChart.getAxisLeft();
        YAxis yAxis2 = barChart.getAxisRight();
        yAxis1.setDrawGridLines(false);
        yAxis1.setAxisMinimum(0);
        yAxis2.setEnabled(false);
        return barChart;
    }

    public static BarChart makeDayEmissionBarChart(final ArrayList<Day> days, BarChart barChart, Context context) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < days.size(); i++) {
            entries.add(new BarEntry(i, new float[]{days.get(i).getCarEmissions(), days.get(i).getOpnvEmissions()}));
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
                return Integer.toString((int) value + 1);
            }
        });

        YAxis yAxis1 = barChart.getAxisLeft();
        YAxis yAxis2 = barChart.getAxisRight();
        yAxis1.setDrawGridLines(false);
        yAxis1.setAxisMinimum(0);
        yAxis2.setEnabled(false);
        return barChart;
    }

    public static BarChart makeDayDistanceBarChart(final ArrayList<Day> days, BarChart barChart, Context context) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < days.size(); i++) {
            entries.add(new BarEntry(i, new float[]{days.get(i).getCarDistance(), days.get(i).getOpnvDistance(), days.get(i).getBikeDistance(), days.get(i).getWalkDistance()}));
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
                return Integer.toString((int) value + 1);
            }
        });

        YAxis yAxis1 = barChart.getAxisLeft();
        YAxis yAxis2 = barChart.getAxisRight();
        yAxis1.setDrawGridLines(false);
        yAxis1.setAxisMinimum(0);
        yAxis2.setEnabled(false);
        return barChart;
    }

    public static BarChart makeDayTimeEffortBarChart(final ArrayList<Day> days, BarChart barChart, Context context) {
        List<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < days.size(); i++) {
            entries.add(new BarEntry(i, new float[]{days.get(i).getCarTimeEffort(), days.get(i).getOpnvTimeEffort(), days.get(i).getBikeTimeEffort(), days.get(i).getWalkTimeEffort()}));
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
                return Integer.toString((int) value + 1);
            }
        });

        YAxis yAxis1 = barChart.getAxisLeft();
        YAxis yAxis2 = barChart.getAxisRight();
        yAxis1.setDrawGridLines(false);
        yAxis1.setAxisMinimum(0);
        yAxis2.setEnabled(false);
        return barChart;
    }

    public static PieChart makeTimeEffortPieChart(RideContainer container, PieChart pieChart, Context context){
        Drawable auto = context.getDrawable(R.drawable.ic_directions_car_black_24dp);
        Drawable bike = context.getDrawable(R.drawable.ic_directions_bike_black_24dp);
        Drawable opnv = context.getDrawable(R.drawable.ic_directions_bus_black_24dp);
        Drawable walk = context.getDrawable(R.drawable.ic_directions_walk_black_24dp);
        List<PieEntry> entries = new ArrayList<>();

        if (container.getCarTimeEffort() != 0)
            entries.add(new PieEntry(container.getCarTimeEffort(), auto));

        if (container.getOpnvTimeEffort() != 0)
            entries.add(new PieEntry(container.getOpnvTimeEffort(), opnv));

        if (container.getBikeTimeEffort() != 0)
            entries.add(new PieEntry(container.getBikeTimeEffort(), bike));

        if (container.getWalkTimeEffort() != 0) {
            entries.add(new PieEntry(container.getWalkTimeEffort(), walk));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Zeit");
        dataSet.setColors(new int[]{Color.rgb(200, 0, 0), Color.rgb(0, 200, 0), Color.rgb(0, 0, 200), Color.rgb(0, 200, 200)}, 70);
        dataSet.setSliceSpace(7f);
        dataSet.setDrawValues(false);

        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return getTotalDurationText((int) value);
            }
        });

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.setTouchEnabled(false);
        pieChart.setRotationEnabled(false);
        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);
        int gesamt = container.getTotalTimeEffort();
        pieChart.setCenterText(getCenterText("Zeit",getTotalDurationText(gesamt)));
        pieChart.getLegend().setEnabled(false);

        return pieChart;
    }

    public static PieChart makeDistancePieChart(RideContainer container, PieChart pieChart, Context context){
        Drawable auto = context.getDrawable(R.drawable.ic_directions_car_black_24dp);
        Drawable bike = context.getDrawable(R.drawable.ic_directions_bike_black_24dp);
        Drawable opnv = context.getDrawable(R.drawable.ic_directions_bus_black_24dp);
        Drawable walk = context.getDrawable(R.drawable.ic_directions_walk_black_24dp);
        List<PieEntry> entries = new ArrayList<>();
        if (container.getCarDistance() != 0)
            entries.add(new PieEntry(container.getCarDistance(), auto));
        if (container.getOpnvDistance() != 0)
            entries.add(new PieEntry(container.getOpnvDistance(), opnv));
        if (container.getBikeDistance() != 0)
            entries.add(new PieEntry(container.getBikeDistance(), bike));
        if (container.getWalkDistance() != 0) {
            entries.add(new PieEntry(container.getWalkDistance(), walk));
        }


        PieDataSet dataSet = new PieDataSet(entries, "Distanz");
        dataSet.setColors(new int[]{Color.rgb(200, 0, 0), Color.rgb(0, 200, 0), Color.rgb(0, 0, 200), Color.rgb(0, 200, 200)}, 70);
        dataSet.setSliceSpace(7f);
        dataSet.setDrawValues(false);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return getTotalDistanceText((int) value);
            }
        });

        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.setClickable(false);
        pieChart.setRotationEnabled(false);
        pieChart.setTouchEnabled(false);
        int gesamt = container.getTotalDistance();
        pieChart.setCenterText(getCenterText( "Distanz",getTotalDistanceText(gesamt)));
        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);
        pieChart.getLegend().setEnabled(false);

        return pieChart;
    }

    public static PieChart makeEmissionPieChart(RideContainer container, PieChart pieChart, Context context){
        Drawable auto = context.getDrawable(R.drawable.ic_directions_car_black_24dp);
        Drawable bike = context.getDrawable(R.drawable.ic_directions_bike_black_24dp);
        Drawable opnv = context.getDrawable(R.drawable.ic_directions_bus_black_24dp);
        Drawable walk = context.getDrawable(R.drawable.ic_directions_walk_black_24dp);
        List<PieEntry> entries = new ArrayList<>();
        if (container.getCarEmissions() != 0)
            entries.add(new PieEntry(container.getCarEmissions(), auto));

        if (container.getOpnvEmissions() != 0)
            entries.add(new PieEntry(container.getOpnvEmissions(), opnv));

        if (container.getBikeEmissions() != 0)
            entries.add(new PieEntry(container.getBikeEmissions(), bike));

        if (container.getWalkEmissions() != 0) {
            entries.add(new PieEntry(container.getWalkEmissions(), walk));
        }
        PieDataSet dataSet = new PieDataSet(entries, "CO2");
        dataSet.setDrawValues(false);
        dataSet.setColors(new int[]{Color.rgb(200, 0, 0), Color.rgb(0, 200, 0), Color.rgb(0, 0, 200), Color.rgb(0, 200, 200)}, 70);
        dataSet.setSliceSpace(7f);
              dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return getTotalCO2Text((int) value);
            }
        });

        PieData data = new PieData(dataSet);
        data.setValueTextSize(11f);

        pieChart.setData(data);
        pieChart.setTouchEnabled(false);
        pieChart.setRotationEnabled(false);
        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);
        int gesamt = container.getTotalEmissions();
        pieChart.setCenterText(getCenterText("Emissionen", getTotalCO2Text(gesamt)));
        pieChart.getLegend().setEnabled(false);

        return pieChart;
    }

    public static String getTotalCO2Text(int gesamtCO2){
        if(gesamtCO2 >= maxGrammCO2){
            double kilogramm =(double) Math.round((double) gesamtCO2 / 1000 * nachkommerstelleCO2) /nachkommerstelleCO2;
            return   kilogramm + " kg CO2";
        }else{
            return gesamtCO2 + " gramm CO2";
        }
    }

    public static String getTotalDistanceText(int gesamtDistance){
        if(gesamtDistance >= maxMeters){
            double kilometer = (double) Math.round((double) gesamtDistance / 1000 * nachkommerstelleMeters) /nachkommerstelleMeters;
            return  kilometer + " km";
        }else{
            return  gesamtDistance + " m";
        }
    }

    public static String getTotalDurationText(int totalDuration){
        if(totalDuration >= maxMinutes){
            double hour =(double) Math.round((double) totalDuration / 60 * 100)/ 100;
            return  hour + " Stunden";
        }else{
            return  totalDuration + " Minuten";
        }
    }

    public static SpannableString getCenterText(String title, String value){
        SpannableString s = new SpannableString(title + "\n \n" + "Insgesamt" + "\n" + value);
        s.setSpan(new RelativeSizeSpan(1.7f), 0, title.length()+1, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), title.length()+1, s.length() - value.length()-1, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), title.length()+1, s.length() - value.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.f), title.length()+1, s.length() - value.length(), 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - value.length(), s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - value.length(), s.length(), 0);
        return s;
    }



}