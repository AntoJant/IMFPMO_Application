package com.imfpmo.app;

import java.util.Calendar;

public class AnalysisResultRide {
    public String id, mode,ampel;
    public AnalysisTrack start, end;
    public int distance, emissions, dauer;
    public FahrtModi modi;
    public int okoBewertung;
    Calendar startZeit, endZeit;


    public void generateAtributes(){
        switch(mode){
            case "walk": modi = FahrtModi.WALK;break;
            case "bike": modi = FahrtModi.FAHRRAD;break;
            case "car" : modi = FahrtModi.AUTO;break;
            default: modi = FahrtModi.OPNV;
        }

        switch (ampel){
            case "red": okoBewertung = 0;break;
            case "yellow": okoBewertung = 1;break;
            case "green": okoBewertung = 2;break;
        }

        startZeit = start.getDate();
        endZeit = end.getDate();

        dauer = (int) ((endZeit.getTimeInMillis()-startZeit.getTimeInMillis()) /60000);
    }

    public FahrtModi getModi() {
        return  modi;
    }

    public FahrtModi getAlternativModi() {
        return FahrtModi.WALK;
    }

    public int getOkoBewertung() {
        return okoBewertung;
    }

    public int getcO2Austoss() {
        return emissions;
    }

    public Calendar getStartzeit() {
        return startZeit;
    }

    public Calendar getEndzeit() {
        return endZeit;
    }

    public String getStartadresse() {
        return start.name;
    }

    public String getZieladresse() {
        return end.name;
    }

    public int getDistanz() {
        return distance;
    }

    public int getAlternativerZeitaufwand() {
        return distance -1;
    }

    public int getDauer(){
        return dauer;
    }
}
