package com.imfpmo.app;

import com.google.android.gms.common.server.response.FastJsonResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AnalysisResultMonth {
    public int id;
    public String timestamp;
    public int car, bike, foot, opnv;
    public String bestAlternative;
    public int emissions;
    public String ampel;
    private int totalCO2,autoCO2,bikeCO2, footCO2, opnvCO2;
    private int totalDistance, carDistance, bikeDistance, walkDistance, opnvDistance;
    private int totalRideCount, carRideCount, bikeRideCount, walkRideCount, opnvRideCount;
    private Calendar date;
    private FahrtModi altModi;
    private int okoBewertung;
    private ArrayList<AnalysisResultDay> tage;

    public Calendar getDate() {
        return date;
    }

    public void generateItems(){
        for(AnalysisResultDay day : tage){
            day.generateItems();
        }
        autoCO2 = 0;
        bikeCO2 = 0;
        footCO2 = 0;
        opnvCO2 = 0;
        totalCO2 = 0;
        totalDistance = 0;
        carDistance =0;
        bikeDistance = 0;
        walkDistance = 0;
        opnvDistance = 0;
        totalRideCount = 0;
        carRideCount = 0;
        bikeRideCount = 0;
        walkRideCount = 0;
        opnvRideCount = 0;

        for(AnalysisResultDay tag :tage){
            autoCO2 += tag.getAutoCO2Austoss();
            carDistance += tag.getAutoDistanz();
            carRideCount += tag.getCarRideCount();

            bikeCO2 += tag.getFahrradCO2Austoss();
            bikeDistance +=tag.getFahrradDistanz();
            bikeRideCount += tag.getBikeRideCount();

            footCO2 += tag.getFussCO2Austoss();
            walkDistance += tag.getFussDistanz();
            walkRideCount += tag.getWalkRideCount();

            opnvCO2 += tag.getOpnvCO2Austoss();
            opnvDistance += tag.getOpnvDistanz();
            opnvRideCount += tag.getOpnvRideCount();
        }
        totalCO2 = autoCO2 + bikeCO2 + footCO2+ opnvCO2;
        totalRideCount = carRideCount + bikeRideCount +walkRideCount +opnvCO2;
        totalDistance = carDistance + bikeDistance +walkDistance + opnvDistance;
        switch(bestAlternative){
            case "walk": altModi = FahrtModi.WALK;break;
            case "bike": altModi = FahrtModi.FAHRRAD;break;
            case "car" : altModi = FahrtModi.AUTO;break;
            default: altModi = FahrtModi.OPNV;
        }

        switch(ampel){
            case "green": okoBewertung = 3; break;
            case "red" : okoBewertung = 1; break;
            case "yellow" : okoBewertung = 2;break;
        }
        date =new GregorianCalendar(Integer.parseInt(timestamp.substring(0, 4)), Integer.parseInt(timestamp.substring(5, 7)) - 1, Integer.parseInt(timestamp.substring(8, 10)), Integer.parseInt(timestamp.substring(11, 13)), Integer.parseInt(timestamp.substring(14, 16)));
    }

    public int getZeitFahrrad() {
        return bike;
    }

    public int getZeitOpnv() {
        return opnv;
    }

    public int getZeitAuto() {
        return car;
    }

    public int getZeitFuss(){return foot;}

    public int getGesamtCO2() {
        return emissions;
    }

    public int getOkoBewertung() {
        return okoBewertung;
    }

    public ArrayList<AnalysisResultDay> getTage() {
        return tage;
    }

    public int getCO2Auto(){
        return autoCO2;
    }

    public int getCO2Fahrrad(){
        return bikeCO2;
    }

    public int getCO2Opnv(){
        return  opnvCO2;
    }

    public int getGesamtDistanz(){
        return totalDistance;
    }

    public int getAutoDistanz(){
        return carDistance;
    }

    public int getFahrradDistanz(){
        return bikeDistance;
    }

    public int getOpnvDistanz(){
        return opnvDistance;
    }
    public int getFussDistanz(){
        return walkDistance;
    }

    public int getTotalRideCount(){
        return totalRideCount;
    }

    public int getCarRideCount(){
        return carRideCount;
    }

    public int getBikeRideCount(){
        return bikeRideCount;
    }

    public int getOpnvRideCount(){
        return  opnvRideCount;
    }

    public int getWalkRideCount(){
        return walkRideCount;
    }

    public void setTage(ArrayList<AnalysisResultDay> temp){
        tage = temp;
    }

    public FahrtModi getAlternativeModi(){
        return altModi;
    }



}
