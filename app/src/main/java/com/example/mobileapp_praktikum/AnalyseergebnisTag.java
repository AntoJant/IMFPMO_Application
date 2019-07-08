package com.example.mobileapp_praktikum;

import java.util.ArrayList;
import java.util.Calendar;

public class AnalyseergebnisTag {
    private ArrayList<AnalyseergebnisWeg> wege;
    private Calendar tag;

    public AnalyseergebnisTag(ArrayList<AnalyseergebnisWeg> wege, Calendar tag) {
        this.wege = wege;
        this.tag = tag;
    }

    public ArrayList<AnalyseergebnisWeg> getWege() {
        return wege;
    }

    public Calendar getTag() {
        return tag;
    }


    public int getCO2Austoss(){
        int cO2 = 0;
        for (AnalyseergebnisWeg weg : wege)
            cO2 += weg.getCO2Austoss();
        return cO2;
    }

    public int getAutoCO2Austoss(){
        int cO2 = 0;
        for (AnalyseergebnisWeg weg : wege){
            cO2 += weg.getAutoCO2Austoss();

        }
        return cO2;
    }

    public int getFahrradCO2Austoss(){
        int cO2 = 0;
        for (AnalyseergebnisWeg weg : wege){
                cO2 += weg.getFahrradCO2Austoss();
        }
        return cO2;
    }

    public int getOpnvCO2Austoss(){
        int cO2 = 0;
        for (AnalyseergebnisWeg  weg: wege){
                cO2 += weg.getOpnvCO2Austoss();
        }
        return cO2;
    }

    public int getFussCO2Austoss(){
        int cO2 = 0;
        for (AnalyseergebnisWeg  weg: wege){
            cO2 += weg.getFussCO2Austoss();
        }
        return cO2;
    }

    public int getDistanz(){
        int distanz = 0;
        for(AnalyseergebnisWeg weg : wege){
            distanz += weg.getDistanz();
        }
        return distanz;
    }

    public int getAutoDistanz(){
        int distanz = 0;
        for (AnalyseergebnisWeg weg : wege){
                distanz += weg.getAutoDistanz();
        }
        return distanz;
    }

    public int getFahrradDistanz(){
        int distanz = 0;
        for (AnalyseergebnisWeg weg : wege){
                distanz += weg.getFahrradDistanz();
        }
        return distanz;
    }

    public int getOpnvDistanz(){
        int distanz = 0;
        for (AnalyseergebnisWeg weg : wege){
                distanz += weg.getOpnvDistanz();
        }
        return distanz;
    }

    public int getFussDistanz(){
        int distanz = 0;
        for (AnalyseergebnisWeg weg : wege){
            distanz += weg.getFussDistanz();
        }
        return distanz;
    }

    public float getDauer(){
        float dauer = 0;
        for (AnalyseergebnisWeg weg:wege)
            dauer += weg.getDauer();
        return dauer;
    }

    public float getAutoDauer(){
        float dauer = 0;
        for (AnalyseergebnisWeg weg : wege){
            dauer += weg.getAutoDauer();
        }
        return dauer;
    }


    public float getFahrradDauer(){
        float dauer = 0;
        for (AnalyseergebnisWeg weg : wege){
                dauer += weg.getFahrradDauer();
        }
        return dauer;
    }

    public float getOpnvDauer(){
        float dauer = 0;
        for (AnalyseergebnisWeg weg: wege){
                dauer += weg.getOpnvDauer();
        }
        return dauer;
    }

    public float getFussDauer(){
        float dauer = 0;
        for (AnalyseergebnisWeg weg: wege){
            dauer += weg.getFussDauer();
        }
        return dauer;
    }

    public double getOkobewertung(){
        double bewertung = 0;
        for(AnalyseergebnisWeg weg : wege){
            bewertung += weg.getOkobewertung();
        }
        return bewertung / wege.size();
    }

}
