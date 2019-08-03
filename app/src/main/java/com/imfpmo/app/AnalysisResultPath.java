package com.imfpmo.app;

import java.util.ArrayList;
import java.util.Calendar;

public class AnalysisResultPath implements RideContainer {
    public ArrayList<AnalysisResultRide> rides;
    public String id;
    public AnalysisTrack start,end;
    public int totalEmissions, carEmissions, bikeEmissions, opnvEmissions, walkEmissions;
    public int totalDistance, carDistance, bikeDistance, opnvDistance, walkDistance;
    public int totalTimeEffort, carTimeEffort, bikeTimeEffort, opnvTimeEffort, walkTimeEffort;
    public int totalRideCount, carRideCount, bikeRideCount, opnvRideCount, walkRideCount;
    public Calendar startTime, endTime;
    public int okoGrade;
    public ArrayList<AnalysisResultRide> getRides() {
        return rides;
    }


    public Calendar getStartDate() {
        return start.getDate();
    }

    public Calendar getEndDate() {
        return end.getDate();
    }

    public void generateAttributes(){
        walkEmissions = 0;
        walkTimeEffort = 0;
        walkDistance = 0;
        carEmissions = 0;
        carTimeEffort = 0;
        carDistance = 0;
        bikeEmissions = 0;
        bikeTimeEffort = 0;
        bikeDistance = 0;
        opnvTimeEffort = 0;
        opnvDistance = 0;
        opnvEmissions = 0;
        totalEmissions = 0;
        totalTimeEffort = 0;
        totalDistance = 0;
        totalRideCount  =0;
        carRideCount = 0;
        bikeRideCount = 0;
        opnvRideCount = 0;
        walkRideCount = 0;
        int ratedRides = 0;
        for(AnalysisResultRide ride:rides){
            ride.generateAtributes();
        }
        for (AnalysisResultRide ride:rides){
            switch (ride.getMode()){
                case WALK: {
                    walkEmissions += ride.getCO2Emissions();
                    walkTimeEffort += ride.getTimeEffort();
                    walkDistance += ride.getDistance();
                    walkRideCount++;
                }break;
                case CAR: {
                    carEmissions += ride.getCO2Emissions();
                    carTimeEffort += ride.getTimeEffort();
                    carDistance += ride.getDistance();
                    carRideCount++;
                }break;
                case OPNV:{
                    opnvEmissions += ride.getCO2Emissions();
                    opnvTimeEffort += ride.getTimeEffort();
                    opnvDistance += ride.getDistance();
                    opnvRideCount ++;
                }break;
                case BIKE:{
                    bikeEmissions += ride.getCO2Emissions();
                    bikeTimeEffort += ride.getTimeEffort();
                    bikeDistance += ride.getDistance();
                    bikeRideCount ++;
                }break;
            }
            if(ride.getOkoGrade() != 0) {
                okoGrade += ride.getOkoGrade();
                ratedRides++;
            }

        }
        totalRideCount = carRideCount + bikeRideCount+ opnvRideCount+walkRideCount;
        totalEmissions = walkEmissions + carEmissions + opnvEmissions + bikeEmissions;
        totalTimeEffort = walkTimeEffort + carTimeEffort + opnvTimeEffort + bikeTimeEffort;
        totalDistance = walkDistance + carDistance + opnvDistance + bikeDistance;
        if(ratedRides != 0 ){
            okoGrade = okoGrade / ratedRides;
        }

        startTime = start.getDate();
        endTime = end.getDate();
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

    public int getWalkTimeEffort(){
        return walkTimeEffort;
    }

    public int getBikeTimeEffort(){
        return bikeTimeEffort;
    }

    public int getOpnvTimeEffort(){
        return opnvTimeEffort;
    }

    public int getOkoGrade(){
        return okoGrade;
    }

    public String getStartAdress(){
        return start.name;
    }

    public String getEndAdress(){
        return end.name;
    }
}
