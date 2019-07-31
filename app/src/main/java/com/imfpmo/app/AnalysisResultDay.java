package com.imfpmo.app;

import java.util.ArrayList;
import java.util.Calendar;

public class AnalysisResultDay {
    private ArrayList<AnalysisResultPath> wege;
    private Calendar tag;
    private int gesamtCO2, autoCO2, walkCO2, fahrradCO2, opnvCO2;
    private int gesamtDistanz, autoDistanz, walkDistanz, fahrradDistanz, opnvDistanz;
    private int gesamtDauer, autoDauer, walkDauer, fahrradDauer, opnvDauer;
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

        walkCO2 = 0;
        walkDauer = 0;
        walkDistanz = 0;

        autoCO2 = 0;
        autoDauer = 0;
        autoDistanz = 0;

        fahrradCO2 = 0;
        fahrradDauer = 0;
        fahrradDistanz = 0;

        opnvCO2 = 0;
        opnvDauer = 0;
        opnvDistanz = 0;

        okoBewertung = 0;

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
            fahrradDauer += weg.getAutoDauer();
            opnvDauer += weg.getOpnvDauer();
            walkDauer += weg.getFussDauer();
            okoBewertung += weg.getOkobewertung();
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
        int cO2 = 0;
        for (AnalysisResultPath weg : wege)
            cO2 += weg.getCO2Austoss();
        return cO2;
    }

    public int getAutoCO2Austoss(){
        int cO2 = 0;
        for (AnalysisResultPath weg : wege){
            cO2 += weg.getAutoCO2Austoss();

        }
        return cO2;
    }

    public int getFahrradCO2Austoss(){
        int cO2 = 0;
        for (AnalysisResultPath weg : wege){
                cO2 += weg.getFahrradCO2Austoss();
        }
        return cO2;
    }

    public int getOpnvCO2Austoss(){
        int cO2 = 0;
        for (AnalysisResultPath weg: wege){
                cO2 += weg.getOpnvCO2Austoss();
        }
        return cO2;
    }

    public int getFussCO2Austoss(){
        int cO2 = 0;
        for (AnalysisResultPath weg: wege){
            cO2 += weg.getFussCO2Austoss();
        }
        return cO2;
    }

    public int getDistanz(){
        int distanz = 0;
        for(AnalysisResultPath weg : wege){
            distanz += weg.getDistanz();
        }
        return distanz;
    }

    public int getAutoDistanz(){
        int distanz = 0;
        for (AnalysisResultPath weg : wege){
                distanz += weg.getAutoDistanz();
        }
        return distanz;
    }

    public int getFahrradDistanz(){
        int distanz = 0;
        for (AnalysisResultPath weg : wege){
                distanz += weg.getFahrradDistanz();
        }
        return distanz;
    }

    public int getOpnvDistanz(){
        int distanz = 0;
        for (AnalysisResultPath weg : wege){
                distanz += weg.getOpnvDistanz();
        }
        return distanz;
    }

    public int getFussDistanz(){
        int distanz = 0;
        for (AnalysisResultPath weg : wege){
            distanz += weg.getFussDistanz();
        }
        return distanz;
    }

    public float getDauer(){
        float dauer = 0;
        for (AnalysisResultPath weg:wege)
            dauer += weg.getDauer();
        return dauer;
    }

    public float getAutoDauer(){
        float dauer = 0;
        for (AnalysisResultPath weg : wege){
            dauer += weg.getAutoDauer();
        }
        return dauer;
    }


    public float getFahrradDauer(){
        float dauer = 0;
        for (AnalysisResultPath weg : wege){
                dauer += weg.getFahrradDauer();
        }
        return dauer;
    }

    public float getOpnvDauer(){
        float dauer = 0;
        for (AnalysisResultPath weg: wege){
                dauer += weg.getOpnvDauer();
        }
        return dauer;
    }

    public float getFussDauer(){
        float dauer = 0;
        for (AnalysisResultPath weg: wege){
            dauer += weg.getFussDauer();
        }
        return dauer;
    }

    public double getOkobewertung(){
        double bewertung = 0;
        for(AnalysisResultPath weg : wege){
            bewertung += weg.getOkobewertung();
        }
        return bewertung / wege.size();
    }

}
