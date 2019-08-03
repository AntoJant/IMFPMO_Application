package com.imfpmo.app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AnalysisResultMonth implements RideContainer{
    public int id;
    public String timestamp;
    public int car, bike, foot, opnv;
    public String bestAlternative;
    public int emissions;
    public String ampel;
    private int totalEmissions;
    private int carEmissions;
    private int bikeEmissions;

    @Override
    public int getWalkEmissions() {
        return walkEmissions;
    }

    private int walkEmissions;
    private int opnvEmissions;
    private int totalTimeEffort, carTimeEffort, bikeTimeEffort, walkTimeEffort, opnvTimeEffort;
    private int totalDistance, carDistance, bikeDistance, walkDistance, opnvDistance;
    private int totalRideCount, carRideCount, bikeRideCount, walkRideCount, opnvRideCount;
    private Calendar date;
    private RideMode alternativeMode;
    private int okoGrade;
    private ArrayList<AnalysisResultDay> days;

    public Calendar getDate() {
        return date;
    }

    public void generateAttributes(){
        for(AnalysisResultDay day : days){
            day.generateAttributes();
        }

        totalTimeEffort = 0;
        carTimeEffort = 0;
        bikeTimeEffort =0;
        walkTimeEffort = 0;
        opnvTimeEffort = 0;

        carEmissions = 0;
        bikeEmissions = 0;
        walkEmissions = 0;
        opnvEmissions = 0;
        totalEmissions = 0;
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

        for(AnalysisResultDay day : days){
            carEmissions += day.getCarEmissions();
            carDistance += day.getCarDistance();
            carRideCount += day.getCarRideCount();
            carTimeEffort += day.getCarTimeEffort();

            bikeEmissions += day.getBikeEmissions();
            bikeDistance +=day.getBikeDistance();
            bikeRideCount += day.getBikeRideCount();
            bikeTimeEffort += day.getBikeTimeEffort();

            walkEmissions += day.getWalkEmissions();
            walkDistance += day.getWalkDistance();
            walkRideCount += day.getWalkRideCount();
            walkTimeEffort += day.getWalkTimeEffort();

            opnvEmissions += day.getOpnvEmissions();
            opnvDistance += day.getOpnvDistance();
            opnvRideCount += day.getOpnvRideCount();
            opnvTimeEffort += day.getOpnvTimeEffort();

        }
        totalTimeEffort = carTimeEffort+ bikeTimeEffort+walkTimeEffort+opnvTimeEffort;
        totalEmissions = carEmissions + bikeEmissions + walkEmissions + opnvEmissions;
        totalRideCount = carRideCount + bikeRideCount +walkRideCount + opnvEmissions;
        totalDistance = carDistance + bikeDistance +walkDistance + opnvDistance;
        switch(bestAlternative){
            case "walk": alternativeMode = RideMode.WALK;break;
            case "bike": alternativeMode = RideMode.BIKE;break;
            case "car" : alternativeMode = RideMode.CAR;break;
            default: alternativeMode = RideMode.OPNV;
        }

        switch(ampel){
            case "green": okoGrade = 3; break;
            case "red" : okoGrade = 1; break;
            case "yellow" : okoGrade = 2;break;
        }
        date =new GregorianCalendar(Integer.parseInt(timestamp.substring(0, 4)), Integer.parseInt(timestamp.substring(5, 7)) - 1, Integer.parseInt(timestamp.substring(8, 10)), Integer.parseInt(timestamp.substring(11, 13)), Integer.parseInt(timestamp.substring(14, 16)));
    }

    public int getBikeTimeEffort() {
        return bikeTimeEffort;
    }

    public int getOpnvTimeEffort() {
        return opnvTimeEffort;
    }

    public int getCarTimeEffort() {
        return carTimeEffort;
    }

    public int getWalkTimeEffort(){return walkTimeEffort;}

    public int getTotalTimeEffort(){
        return totalTimeEffort;
    }

    public int getTotalEmissions() {
        return emissions;
    }

    public int getOkoGrade() {
        return okoGrade;
    }

    public ArrayList<AnalysisResultDay> getDays() {
        return days;
    }

    public int getCarEmissions(){
        return carEmissions;
    }

    public int getBikeEmissions(){
        return bikeEmissions;
    }

    public int getOpnvEmissions(){
        return opnvEmissions;
    }

    public int getTotalDistance(){
        return totalDistance;
    }

    public int getCarDistance(){
        return carDistance;
    }

    public int getBikeDistance(){
        return bikeDistance;
    }

    public int getOpnvDistance(){
        return opnvDistance;
    }
    public int getWalkDistance(){
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

    public void setDays(ArrayList<AnalysisResultDay> temp){
        days = temp;
    }

    public RideMode getAlternativeMode(){
        return alternativeMode;
    }



}
