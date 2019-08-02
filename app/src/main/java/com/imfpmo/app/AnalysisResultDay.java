package com.imfpmo.app;

import java.util.ArrayList;
import java.util.Calendar;

public class AnalysisResultDay {
    private ArrayList<AnalysisResultPath> wege;
    private Calendar tag;
    private int gesamtCO2, autoCO2, walkCO2, fahrradCO2, opnvCO2;
    private int gesamtDistanz, autoDistanz, walkDistanz, fahrradDistanz, opnvDistanz;
    private int gesamtDauer, autoDauer, walkDauer, fahrradDauer, opnvDauer;
    private int totalRideCount, carRideCount, bikeRideCount, opnvRideCount, walkRideCount;
    private int okoBewertung;

    public AnalysisResultDay(Calendar tag) {
        wege = new ArrayList<>();
        this.tag = tag;
    }

    public void  generateItems(){
        for (AnalysisResultPath path : wege){
            path.generateAtributes();
        }

        gesamtCO2 = 0;
        gesamtDauer = 0;
        gesamtDistanz = 0;
        totalRideCount = 0;

        walkCO2 = 0;
        walkDauer = 0;
        walkDistanz = 0;
        walkRideCount = 0;

        autoCO2 = 0;
        autoDauer = 0;
        autoDistanz = 0;
        carRideCount = 0;

        fahrradCO2 = 0;
        fahrradDauer = 0;
        fahrradDistanz = 0;
        bikeRideCount = 0;

        opnvCO2 = 0;
        opnvDauer = 0;
        opnvDistanz = 0;
        opnvRideCount = 0;

        okoBewertung = 0;
        int bewertetFahrten = 0;
        for(AnalysisResultPath weg : wege) {
            weg.generateAtributes();
            autoCO2 += weg.autoCO2Austoss;
            walkCO2 += weg.walkCO2Austoss;
            fahrradCO2 += weg.fahrradCO2Austoss;
            opnvCO2 += weg.opnvCO2Austoss;
            autoDistanz += weg.autoDistanz;
            fahrradDistanz += weg.getFahrradDistanz();
            walkDistanz += weg.getFussDistanz();
            fahrradDistanz += weg.getFahrradDistanz();
            autoDauer += weg.getAutoDauer();
            fahrradDauer += weg.getFahrradDauer();
            opnvDauer += weg.getOpnvDauer();
            walkDauer += weg.getFussDauer();
            carRideCount += weg.carRideCount;
            bikeRideCount += weg.bikeRideCount;
            walkRideCount += weg.walkRideCount;
            opnvRideCount += weg.opnvRideCount;
            if(weg.getOkobewertung() != 0){
                bewertetFahrten ++;
                okoBewertung += weg.getOkobewertung();
            }
        }
        gesamtDistanz = autoDistanz + fahrradDistanz+ opnvDistanz + walkDistanz;
        gesamtDauer = autoDauer + fahrradDauer +opnvDauer +walkDauer;
        gesamtCO2 = autoCO2 + fahrradCO2 + opnvDauer + walkCO2;
        totalRideCount = carRideCount + walkRideCount + opnvRideCount+ bikeRideCount;

        if(bewertetFahrten != 0){
            okoBewertung = okoBewertung / bewertetFahrten;
        }

    }

    public ArrayList<AnalysisResultPath> getWege() {
        return wege;
    }

    public Calendar getTag() {
        return tag;
    }

    public void addWeg(AnalysisResultPath weg){
        wege.add(weg);
    }

    public int getCO2Austoss(){
        return gesamtCO2;
    }

    public int getAutoCO2Austoss(){
        return autoCO2;
    }

    public int getFahrradCO2Austoss(){
        return fahrradCO2;
    }

    public int getOpnvCO2Austoss(){
        return opnvCO2;
    }

    public int getFussCO2Austoss(){
        return walkCO2;
    }

    public int getDistanz(){
        return gesamtDistanz;
    }

    public int getAutoDistanz(){
        return autoDistanz;
    }

    public int getFahrradDistanz(){
        return fahrradDistanz;
    }

    public int getOpnvDistanz(){
        return opnvDistanz;
    }

    public int getFussDistanz(){
        return walkDistanz;
    }

    public int getDauer(){
        return gesamtDauer;
    }

    public float getAutoDauer(){
        return autoDauer;
    }


    public float getFahrradDauer(){
        return fahrradDauer;
    }

    public float getOpnvDauer(){
        return opnvDauer;
    }

    public float getFussDauer(){
        return walkDauer;
    }

    public double getOkobewertung(){
        return okoBewertung;
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
