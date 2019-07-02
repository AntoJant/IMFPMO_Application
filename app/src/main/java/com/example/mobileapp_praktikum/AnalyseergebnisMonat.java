package com.example.mobileapp_praktikum;

import java.util.ArrayList;
import java.util.Calendar;

public class AnalyseergebnisMonat {
    private int userID, analysID;
    private Calendar date;
    private int zeitFahrrad, zeitOpnv, zeitAuto;
    private int gesamtCO2, okoBewertung;
    private ArrayList<AnalyseergebnisTag> tage;

    public AnalyseergebnisMonat(int userID, int analysID, Calendar date, int zeitFahrrad, int zeitOpnv, int zeitAuto, int gesamtCO2, int okoBewertung, ArrayList<AnalyseergebnisTag> tage) {
        this.userID = userID;
        this.analysID = analysID;
        this.date = date;
        this.zeitFahrrad = zeitFahrrad;
        this.zeitOpnv = zeitOpnv;
        this.zeitAuto = zeitAuto;
        this.gesamtCO2 = gesamtCO2;
        this.okoBewertung = okoBewertung;
        this.tage = tage;
    }

    public int getUserID() {
        return userID;
    }

    public int getAnalysID() {
        return analysID;
    }

    public Calendar getDate() {
        return date;
    }

    public int getZeitFahrrad() {
        return zeitFahrrad;
    }

    public int getZeitOpnv() {
        return zeitOpnv;
    }

    public int getZeitAuto() {
        return zeitAuto;
    }

    public int getGesamtCO2() {
        return gesamtCO2;
    }

    public int getOkoBewertung() {
        return okoBewertung;
    }

    public ArrayList<AnalyseergebnisTag> getTage() {
        return tage;
    }

    public int getCO2Auto(){
        int co2 = 0;
        for (AnalyseergebnisTag tag : tage){
            co2 += tag.getAutoCO2Austoss();
        }
        return co2;
    }

    public int getCO2Fahrrad(){
        int co2 = 0;
        for (AnalyseergebnisTag tag : tage){
            co2 += tag.getFahrradCO2Austoss();
        }
        return co2;
    }

    public int getCO2Opnv(){
        int co2 = 0;
        for(AnalyseergebnisTag tag:tage){
            co2 += tag.getOpnvCO2Austoss();
        }
        return co2;
    }

    public int getGesamtDistanz(){
        int distanz = 0;
        for (AnalyseergebnisTag tag:tage){
            distanz += tag.getDistanz();
        }
        return distanz;
    }

    public int getAutoDistanz(){
        int distanz = 0;
        for (AnalyseergebnisTag tag:tage){
            distanz += tag.getAutoDistanz();
        }
        return distanz;
    }

    public int getFahrradDistanz(){
        int distanz = 0;
        for (AnalyseergebnisTag tag:tage){
            distanz += tag.getFahrradDistanz();
        }
        return distanz;
    }

    public int getOpnvDistanz(){
        int distanz = 0;
        for (AnalyseergebnisTag tag:tage){
            distanz += tag.getOpnvDistanz();
        }
        return distanz;
    }



}
