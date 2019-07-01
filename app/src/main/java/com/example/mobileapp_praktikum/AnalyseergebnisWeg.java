package com.example.mobileapp_praktikum;

import java.util.ArrayList;
import java.util.Calendar;

public class AnalyseergebnisWeg {
    private ArrayList<AnalyseergebnisFahrt> fahrten;
    private int wegID;
    private Calendar startzeit, endzeit;

    public AnalyseergebnisWeg(ArrayList<AnalyseergebnisFahrt> fahrten, int wegID, Calendar startzeit, Calendar endzeit) {
        this.fahrten = fahrten;
        this.wegID = wegID;
        this.startzeit = startzeit;
        this.endzeit = endzeit;
    }

    public ArrayList<AnalyseergebnisFahrt> getFahrten() {
        return fahrten;
    }

    public int getWegID() {
        return wegID;
    }

    public Calendar getStartzeit() {
        return startzeit;
    }

    public Calendar getEndzeit() {
        return endzeit;
    }

    public int getCO2Austoss(){
        int cO2 = 0;
        for (AnalyseergebnisFahrt fahrt : fahrten)
            cO2 += fahrt.getcO2Austoss();
        return cO2;
    }

    public int getAutoCO2Austoss(){
        int cO2 = 0;
        for (AnalyseergebnisFahrt fahrt : fahrten){
            if(fahrt.getModi() == FahrtModi.AUTO){
                cO2 += fahrt.getcO2Austoss();
            }
        }
        return cO2;
    }

    public int getFahrradCO2Austoss(){
        int cO2 = 0;
        for (AnalyseergebnisFahrt fahrt : fahrten){
            if(fahrt.getModi() == FahrtModi.FAHRRAD){
                cO2 += fahrt.getcO2Austoss();
            }
        }
        return cO2;
    }

    public int getOpnvCO2Austoss(){
        int cO2 = 0;
        for (AnalyseergebnisFahrt fahrt : fahrten){
            if(fahrt.getModi() == FahrtModi.OPNV){
                cO2 += fahrt.getcO2Austoss();
            }
        }
        return cO2;
    }


    public int getDistanz(){
        int distanz = 0;
        for(AnalyseergebnisFahrt fahrt : fahrten){
            distanz += fahrt.getDistanz();
        }
        return distanz;
    }

    public int getAutoDistanz(){
        int distanz = 0;
        for (AnalyseergebnisFahrt fahrt : fahrten){
            if(fahrt.getModi() == FahrtModi.AUTO){
                distanz += fahrt.getDistanz();
            }
        }
        return distanz;
    }

    public int getFahrradDistanz(){
        int distanz = 0;
        for (AnalyseergebnisFahrt fahrt : fahrten){
            if(fahrt.getModi() == FahrtModi.FAHRRAD){
                distanz += fahrt.getDistanz();
            }
        }
        return distanz;
    }

    public int getOpnvDistanz(){
        int distanz = 0;
        for (AnalyseergebnisFahrt fahrt : fahrten){
            if(fahrt.getModi() == FahrtModi.OPNV){
                distanz += fahrt.getDistanz();
            }
        }
        return distanz;
    }

    public int getDauer(){
        int dauer = 0;
        for (AnalyseergebnisFahrt fahrt:fahrten)
            dauer += fahrt.getDauer();
        return dauer;
    }

    public int getAutoDauer(){
        int dauer = 0;
        for (AnalyseergebnisFahrt fahrt : fahrten){
            if(fahrt.getModi() == FahrtModi.AUTO){
                dauer += fahrt.getDauer();
            }
        }
        return dauer;
    }


    public int getFahrradDauer(){
        int dauer = 0;
        for (AnalyseergebnisFahrt fahrt : fahrten){
            if(fahrt.getModi() == FahrtModi.FAHRRAD){
                dauer += fahrt.getDauer();
            }
        }
        return dauer;
    }
    public int getOpnvDauer(){
        int dauer = 0;
        for (AnalyseergebnisFahrt fahrt : fahrten){
            if(fahrt.getModi() == FahrtModi.OPNV){
                dauer += fahrt.getDauer();
            }
        }
        return dauer;
    }

    public double getOkobewertung(){
        double bewertung = 0;
        for(AnalyseergebnisFahrt fahrt : fahrten){
            bewertung += fahrt.getOkoBewertung();
        }
        return bewertung / fahrten.size();
    }
}
