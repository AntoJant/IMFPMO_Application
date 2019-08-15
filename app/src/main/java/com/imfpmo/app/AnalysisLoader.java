package com.imfpmo.app;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AnalysisLoader {
    private Context context;
    private Usermanagement usermanagement;
    private static AnalysisLoader instance;
    private static ArrayList<Month> results;
    private static ArrayList<Path> paths;
    private static int skip = 0;
    private static int errorMessage = 0;
    private static boolean allLoaded;
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

    public static void resetInstance(){
        results = new ArrayList<>();
        skip = 0;
        allLoaded = false;

    }

    public static AnalysisLoader getInstance(){
        return instance;
    }

    public ArrayList<Month> getResults(){
        return results;
    }

    public ArrayList<Path> getPaths(){return  paths;}

    public int loadFirst(int i){
        if(results.size() == 0){
            refresh();
            return loadResults(i);

        }
        return 0;
    }

    public int refresh(){
        Calendar aktMonth  = Calendar.getInstance();
        JsonObject temp = usermanagement.getAnalyseWegeMonat(context,aktMonth.get(Calendar.MONTH)+1,aktMonth.get(Calendar.YEAR),0);
        if(temp == null){
            return 1;
        }
        JsonArray aktPaths =  temp.get("paths").getAsJsonArray();
        Path[] path= new Gson().fromJson(aktPaths, Path[].class);
        this.paths = new ArrayList<Path>(Arrays.asList(path));

        return 0;
    }

    public int loadResults(int i){
        JsonObject analysisResult = usermanagement.getAnalyseErgebnisse(context,skip,i);
        if(analysisResult == null){return 2;}
        Month[] newMonths = new Gson().fromJson(analysisResult.get("results").getAsJsonArray(), Month[].class);
        skip += i;
        if(newMonths.length != i){
            allLoaded = true;
        }

        for(int j = 0; j < newMonths.length; j++ ){
            Calendar lastLoadedMonth = getCalendarDate(newMonths[j].timestamp);
            JsonObject temp = usermanagement.getAnalyseWegeMonat(context, lastLoadedMonth.get(Calendar.MONTH)+1,lastLoadedMonth.get(Calendar.YEAR),0);
            JsonArray object;
            if(temp == null){
                skip -=i;
                return  1;
            }
            object = temp.get("paths").getAsJsonArray();
            Path[] paths = new Gson().fromJson(object, Path[].class);
            ArrayList<Day> days = new ArrayList<>();
            for(int k = 0; k < lastLoadedMonth.getActualMaximum(Calendar.DAY_OF_MONTH); k++){
                days.add(new Day(new GregorianCalendar(lastLoadedMonth.get(Calendar.YEAR), lastLoadedMonth.get(Calendar.MONTH), k+1)));
            }
            for(Path path : paths){
                days.get(Integer.parseInt(path.start.timestamp.substring(8,10))-1).addPath(path);
            }
            newMonths[j].setDays(days);
            newMonths[j].generateAttributes();
            results.add(newMonths[j]);

        }
        if(newMonths.length == 0){
            return 1;
        }else{
            return 0;
        }
    }

    public Calendar getCalendarDate(String date) {
        return new GregorianCalendar(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(5, 7)) - 1, Integer.parseInt(date.substring(8, 10)), Integer.parseInt(date.substring(11, 13)), Integer.parseInt(date.substring(14, 16)));
    }


}
