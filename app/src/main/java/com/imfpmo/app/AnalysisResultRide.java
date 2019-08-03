package com.imfpmo.app;

import java.util.Calendar;

public class AnalysisResultRide {
    public String id, mode,ampel;
    public AnalysisTrack start, end;
    public int distance, emissions, timeEffort;
    public RideMode rideMode;
    public int okoGrade;
    Calendar startZeit, endZeit;


    public void generateAtributes(){
        switch(mode){
            case "walk": rideMode = RideMode.WALK;break;
            case "bike": rideMode = RideMode.BIKE;break;
            case "car" : rideMode = RideMode.CAR;break;
            default: rideMode = RideMode.OPNV;
        }

        switch (ampel){
            case "red": okoGrade = 0;break;
            case "yellow": okoGrade = 1;break;
            case "green": okoGrade = 2;break;
        }

        startZeit = start.getDate();
        endZeit = end.getDate();

        timeEffort = (int) ((endZeit.getTimeInMillis()-startZeit.getTimeInMillis()) /60000);
    }

    public RideMode getMode() {
        return rideMode;
    }

    public RideMode getAlternativeMode() {
        return RideMode.WALK;
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
        return distance -1;
    }

    public int getTimeEffort(){
        return timeEffort;
    }
}
