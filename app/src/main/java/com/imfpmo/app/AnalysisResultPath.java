package com.imfpmo.app;

import java.util.ArrayList;
import java.util.Calendar;

public class AnalysisResultPath {
    public ArrayList<AnalysisResultRide> rides;
    public String id;
    public AnalysisTrack start,end;
    public int gesamtCO2Austoss, autoCO2Austoss, fahrradCO2Austoss, opnvCO2Austoss, walkCO2Austoss;
    public int gesamtDistanz, autoDistanz, fahrradDistanz, opnvDistanz, walkDistanz;
    public int gesamtDauer, autoDauer, fahrradDauer, opnvDauer, walkDauer;
    public Calendar startZeit,endZeit;
    public int okoBewertung;
    public ArrayList<AnalysisResultRide> getFahrten() {
        return rides;
    }


    public Calendar getStartzeit() {
        return start.getDate();
    }

    public Calendar getEndzeit() {
        return end.getDate();
    }

    public void generateAtributes(){
        walkCO2Austoss = 0;
        walkDauer = 0;
        walkDistanz = 0;
        autoCO2Austoss = 0;
        autoDauer = 0;
        autoDistanz = 0;
        fahrradCO2Austoss = 0;
        fahrradDauer = 0;
        fahrradDistanz = 0;
        for(AnalysisResultRide fahrt:rides){
            fahrt.generateAtributes();
        }
        for (AnalysisResultRide fahrt:rides){
            switch (fahrt.getModi()){
                case WALK: {
                    walkCO2Austoss += fahrt.getcO2Austoss();
                    walkDauer += fahrt.getDauer();
                    walkDistanz += fahrt.getDistanz();
                }break;
                case AUTO: {
                    autoCO2Austoss += fahrt.getcO2Austoss();
                    autoDauer += fahrt.getDauer();
                    autoDistanz += fahrt.getDistanz();
                }break;
                case OPNV:{
                    opnvCO2Austoss += fahrt.getcO2Austoss();
                    opnvDauer += fahrt.getDauer();
                    opnvDistanz += fahrt.getDistanz();
                }break;
                case FAHRRAD:{
                    fahrradCO2Austoss += fahrt.getcO2Austoss();
                    fahrradDauer += fahrt.getDauer();
                    fahrradDistanz += fahrt.getDistanz();
                }break;
            }
            okoBewertung = fahrt.getOkoBewertung();
        }
        gesamtCO2Austoss = walkCO2Austoss + autoCO2Austoss+ opnvCO2Austoss+ fahrradCO2Austoss;
        gesamtDauer = walkDauer+autoDauer+opnvDauer+fahrradDauer;
        gesamtDistanz = walkDistanz + autoDistanz+opnvDistanz+fahrradDistanz;
        okoBewertung = okoBewertung / rides.size();
        startZeit = start.getDate();
        endZeit = end.getDate();
    }

    public int getCO2Austoss(){
        return gesamtCO2Austoss;
    }

    public int getAutoCO2Austoss(){
        return autoCO2Austoss;
    }

    public int getFahrradCO2Austoss(){
        return fahrradCO2Austoss;
    }

    public int getOpnvCO2Austoss(){
        return opnvCO2Austoss;
    }

    public int getFussCO2Austoss(){
        return walkCO2Austoss;
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

    public float getDauer(){
        return gesamtDauer;
    }

    public float getAutoDauer(){
        return autoDauer;
    }

    public float getFussDauer(){
        return walkDauer;
    }

    public float getFahrradDauer(){
        return fahrradDauer;
    }

    public float getOpnvDauer(){
        return opnvDauer;
    }

    public double getOkobewertung(){
        return okoBewertung;
    }

    public String getStartAdresse(){
        return start.name;
    }

    public String getEndAdresse(){
        return end.name;
    }
}
