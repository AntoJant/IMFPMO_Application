package com.imfpmo.app;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AnalysisLoader {
    private Context context;
    private Usermanagement usermanagement;
    private static AnalysisLoader instance;
    private ArrayList<AnalysisResultMonth> results;
    private int skip = 0;
    private AnalysisLoader(Context context){
        this.context = context;
        usermanagement = Usermanagement.getInstance();
        results = new ArrayList<>();
    }

    public static void createInstance(Context context){
        if(instance == null){
            instance = new AnalysisLoader(context);
        }
    }

    public static AnalysisLoader getInstance(){
        return instance;
    }

    public ArrayList<AnalysisResultMonth> getResults(){
        return results;
    }

    public void loadFirst(int i){
        if(results.size() == 0){
            loadResults(i);
        }
    }

    public void loadResults(int i){
        JsonArray analysisResult = usermanagement.getAnalyseErgebnisse(context,skip,i).get("results").getAsJsonArray();
        AnalysisResultMonth[] newMonths = new Gson().fromJson(analysisResult, AnalysisResultMonth[].class);
        skip += i;

        for(int j = 0; j < newMonths.length; j++ ){
            Calendar lastLoadedMonth = getCalendarDate(newMonths[j].timestamp);
            JsonArray object = usermanagement.getAnalyseWegeMonat(context, lastLoadedMonth.get(Calendar.MONTH)+1,lastLoadedMonth.get(Calendar.YEAR),1).get("paths").getAsJsonArray();
            AnalysisResultPath[] paths = new Gson().fromJson(object, AnalysisResultPath[].class);
            ArrayList<AnalysisResultDay> days = new ArrayList<>();
            int test = lastLoadedMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
            for(int k = 0; k < lastLoadedMonth.getActualMaximum(Calendar.DAY_OF_MONTH); k++){
                days.add(new AnalysisResultDay(new GregorianCalendar(lastLoadedMonth.get(Calendar.YEAR), lastLoadedMonth.get(Calendar.MONTH), k+1)));
            }
            for(AnalysisResultPath path : paths){
                days.get(Integer.parseInt(path.start.timestamp.substring(8,10))-1).addWeg(path);
            }
            newMonths[j].setTage(days);
            newMonths[j].generateItems();
            results.add(newMonths[j]);

        }
    }



    public Calendar getCalendarDate(String date) {
        return new GregorianCalendar(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(5, 7)) - 1, Integer.parseInt(date.substring(8, 10)), Integer.parseInt(date.substring(11, 13)), Integer.parseInt(date.substring(14, 16)));
    }
}
