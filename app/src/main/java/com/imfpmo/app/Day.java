package com.imfpmo.app;

import java.util.ArrayList;
import java.util.Calendar;

public class Day implements RideContainer{
    private ArrayList<Path> paths;
    private Calendar day;
    private int totalEmissions, carEmissions, walkEmissions, bikeEmissions, opnvEmissions;
    private int totalDistance, carDistance, walkDistance, bikeDistance, opnvDistance;
    private int totalTimeEffort, carTimeEffort, walkTimeEffort, bikeTimeEffort, opnvTimeEffort;
    private int totalRideCount, carRideCount, bikeRideCount, opnvRideCount, walkRideCount;
    private int okoGrade;

    public Day(Calendar day) {
        paths = new ArrayList<>();
        this.day = day;
    }

    public void generateAttributes(){
        for (Path path : paths){
            path.generateAttributes();
        }

        totalEmissions = 0;
        totalTimeEffort = 0;
        totalDistance = 0;
        totalRideCount = 0;

        walkEmissions = 0;
        walkTimeEffort = 0;
        walkDistance = 0;
        walkRideCount = 0;

        carEmissions = 0;
        carTimeEffort = 0;
        carDistance = 0;
        carRideCount = 0;

        bikeEmissions = 0;
        bikeTimeEffort = 0;
        bikeDistance = 0;
        bikeRideCount = 0;

        opnvEmissions = 0;
        opnvTimeEffort = 0;
        opnvDistance = 0;
        opnvRideCount = 0;

        okoGrade = 0;
        int ratedRides = 0;
        for(Path path : paths) {
            path.generateAttributes();
            carEmissions += path.carEmissions;
            walkEmissions += path.walkEmissions;
            bikeEmissions += path.bikeEmissions;
            opnvEmissions += path.opnvEmissions;
            carDistance += path.carDistance;
            bikeDistance += path.getBikeDistance();
            walkDistance += path.getWalkDistance();
            opnvDistance += path.getOpnvDistance();
            carTimeEffort += path.getCarTimeEffort();
            bikeTimeEffort += path.getBikeTimeEffort();
            opnvTimeEffort += path.getOpnvTimeEffort();
            walkTimeEffort += path.getWalkTimeEffort();
            carRideCount += path.carRideCount;
            bikeRideCount += path.bikeRideCount;
            walkRideCount += path.walkRideCount;
            opnvRideCount += path.opnvRideCount;
            if(path.getOkoGrade() != 0){
                ratedRides ++;
                okoGrade += path.getOkoGrade();
            }
        }
        totalDistance = carDistance + bikeDistance + opnvDistance + walkDistance;
        totalTimeEffort = carTimeEffort + bikeTimeEffort + opnvTimeEffort + walkTimeEffort;
        totalEmissions = carEmissions + bikeEmissions + opnvEmissions + walkEmissions;
        totalRideCount = carRideCount + walkRideCount + opnvRideCount+ bikeRideCount;

        if(ratedRides != 0){
            okoGrade = okoGrade / ratedRides;
        }

    }

    public ArrayList<Path> getRides() {
        return paths;
    }

    public Calendar getDay() {
        return day;
    }

    public void addPath(Path weg){
        paths.add(weg);
    }

    public int getTotalEmissions(){
        return totalEmissions;
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

    public int getWalkEmissions(){
        return walkEmissions;
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

    public int getTotalTimeEffort(){
        return totalTimeEffort;
    }

    public int getCarTimeEffort(){
        return carTimeEffort;
    }


    public int getBikeTimeEffort(){
        return bikeTimeEffort;
    }

    public int getOpnvTimeEffort(){
        return opnvTimeEffort;
    }

    public int getWalkTimeEffort(){
        return walkTimeEffort;
    }

    public int getOkoGrade(){
        return okoGrade;
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

    public int getWalkRideCount(){
        return walkRideCount;
    }

    public int getOpnvRideCount(){
        return opnvRideCount;
    }

}
