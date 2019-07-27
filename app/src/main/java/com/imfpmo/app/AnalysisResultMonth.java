package com.imfpmo.app;

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
    private int autoCO2,bikeCO2, footCO2, opnvCO2;
    private Calendar date;
    private int okoBewertung;
    private ArrayList<AnalysisResultDay> tage;

    public Calendar getDate() {
        return date;
    }

    public void generateItems(){
        for(AnalysisResultDay day : tage){
            day.generateItems();
        }
        for(AnalysisResultDay tag :tage){
            autoCO2 += tag.getAutoCO2Austoss();
            bikeCO2 += tag.getFahrradCO2Austoss();
            footCO2 += tag.getFussCO2Austoss();
            opnvCO2 += tag.getOpnvCO2Austoss();
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
        int distanz = 0;
        for (AnalysisResultDay tag:tage){
            distanz += tag.getDistanz();
        }
        return distanz;
    }

    public int getAutoDistanz(){
        int distanz = 0;
        for (AnalysisResultDay tag:tage){
            distanz += tag.getAutoDistanz();
        }
        return distanz;
    }

    public int getFahrradDistanz(){
        int distanz = 0;
        for (AnalysisResultDay tag:tage){
            distanz += tag.getFahrradDistanz();
        }
        return distanz;
    }

    public int getOpnvDistanz(){
        int distanz = 0;
        for (AnalysisResultDay tag:tage){
            distanz += tag.getOpnvDistanz();
        }
        return distanz;
    }
    public int getFussDistanz(){
        int distanz = 0;
        for (AnalysisResultDay tag:tage){
            distanz += tag.getFussDistanz();
        }
        return distanz;
    }

    public void setTage(ArrayList<AnalysisResultDay> temp){
        tage = temp;
    }



}
