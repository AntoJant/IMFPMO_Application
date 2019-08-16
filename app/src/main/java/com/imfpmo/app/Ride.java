package com.imfpmo.app;

import java.util.Calendar;

public class Ride {
    public String id, mode,ampel,altMode;
    public AnalysisTrack start, end;
    public int distance, emissions, timeEffort,altDuration;
    public RideMode rideMode,alternativeMode;
    public int okoGrade;
    Calendar startZeit, endZeit;


    public void generateAtributes(){
        okoGrade =0;
        switch(mode){
            case "walk": rideMode = RideMode.WALK;break;
            case "bike": rideMode = RideMode.BIKE;break;
            case "car" : rideMode = RideMode.CAR;break;
            default: rideMode = RideMode.OPNV;
        }

        switch(altMode){
            case "walk": alternativeMode = RideMode.WALK;break;
            case "bike": alternativeMode = RideMode.BIKE;break;
            case "car" : alternativeMode = RideMode.CAR;break;
            default: alternativeMode = RideMode.OPNV;
        }



        switch (ampel){
            case "red": okoGrade = 1;break;
            case "yellow": okoGrade = 2;break;
            case "green": okoGrade = 3;break;
        }

        startZeit = start.getDate();
        endZeit = end.getDate();

        timeEffort = (int) ((endZeit.getTimeInMillis()-startZeit.getTimeInMillis()) /60000);
    }

    public RideMode getMode() {
        return rideMode;
    }

    public RideMode getAlternativeMode() {
        return alternativeMode;
    }

    public int getOkoGrade() {
        return okoGrade;
    }

    public int getCO2Emissions() {
        return emissions;
    }
    
    public String getStartAddress() {
        return start.name;
    }

    public String getEndAddress() {
        return end.name;
    }

    public int getDistance() {
        return distance;
    }

    public int getAlternativeTimeEffort() {
        return altDuration;
    }

    public int getTimeEffort(){
        return timeEffort;
    }
}
