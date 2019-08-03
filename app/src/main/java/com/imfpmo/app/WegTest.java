package com.imfpmo.app;

import java.util.ArrayList;
import java.util.Calendar;

public class WegTest {
    public ArrayList<AnalysisResultRide> rides;
    public String id;
    public AnalysisTrack start,end;
    public Calendar startzeit, endzeit;
    public String startAdresse, endAdresse;

    public ArrayList<AnalysisResultRide> getFahrten() {
        return rides;
    }


    public Calendar getStartzeit() {
        return startzeit;
    }

    public Calendar getEndzeit() {
        return endzeit;
    }

    public int getCO2Austoss(){
        int cO2 = 0;
        for (AnalysisResultRide fahrt : rides)
            cO2 += fahrt.getcO2Austoss();
        return cO2;
    }

    public int getAutoCO2Austoss(){
        int cO2 = 0;
        for (AnalysisResultRide fahrt : rides){
            if(fahrt.getModi() == FahrtModi.AUTO){
                cO2 += fahrt.getcO2Austoss();
            }
        }
        return cO2;
    }

    public int getFahrradCO2Austoss(){
        int cO2 = 0;
        for (AnalysisResultRide fahrt : rides){
            if(fahrt.getModi() == FahrtModi.FAHRRAD){
                cO2 += fahrt.getcO2Austoss();
            }
        }
        return cO2;
    }

    public int getOpnvCO2Austoss(){
        int cO2 = 0;
        for (AnalysisResultRide fahrt : rides){
            if(fahrt.getModi() == FahrtModi.OPNV){
                cO2 += fahrt.getcO2Austoss();
            }
        }
        return cO2;
    }

    public int getFussCO2Austoss(){
        int cO2 = 0;
        for (AnalysisResultRide fahrt : rides){
            if(fahrt.getModi() == FahrtModi.WALK){
                cO2 += fahrt.getcO2Austoss();
            }
        }
        return cO2;
    }

    public int getDistanz(){
        int distanz = 0;
        for(AnalysisResultRide fahrt : rides){
            distanz += fahrt.getDistanz();
        }
        return distanz;
    }

    public int getAutoDistanz(){
        int distanz = 0;
        for (AnalysisResultRide fahrt : rides){
            if(fahrt.getModi() == FahrtModi.AUTO){
                distanz += fahrt.getDistanz();
            }
        }
        return distanz;
    }

    public int getFahrradDistanz(){
        int distanz = 0;
        for (AnalysisResultRide fahrt : rides){
            if(fahrt.getModi() == FahrtModi.FAHRRAD){
                distanz += fahrt.getDistanz();
            }
        }
        return distanz;
    }

    public int getOpnvDistanz(){
        int distanz = 0;
        for (AnalysisResultRide fahrt : rides){
            if(fahrt.getModi() == FahrtModi.OPNV){
                distanz += fahrt.getDistanz();
            }
        }
        return distanz;
    }

    public int getFussDistanz(){
        int distanz = 0;
        for (AnalysisResultRide fahrt : rides){
            if(fahrt.getModi() == FahrtModi.WALK){
                distanz += fahrt.getDistanz();
            }
        }
        return distanz;
    }

    public float getDauer(){
        float dauer = 0;
        for (AnalysisResultRide fahrt: rides)
            dauer += ((float)fahrt.getDauer()) / 60;
        return dauer;
    }

    public float getAutoDauer(){
        float dauer = 0;
        for (AnalysisResultRide fahrt : rides){
            if(fahrt.getModi() == FahrtModi.AUTO){
                dauer += ((float)fahrt.getDauer()) / 60;
            }
        }
        return dauer;
    }

    public float getFussDauer(){
        float dauer = 0;
        for (AnalysisResultRide fahrt : rides){
            if(fahrt.getModi() == FahrtModi.WALK){
                dauer += ((float)fahrt.getDauer()) / 60;
            }
        }
        return dauer;
    }

    public float getFahrradDauer(){
        float dauer = 0;
        for (AnalysisResultRide fahrt : rides){
            if(fahrt.getModi() == FahrtModi.FAHRRAD){
                dauer += ((float)fahrt.getDauer()) / 60;
            }
        }
        return dauer;
    }

    public float getOpnvDauer(){
        float dauer = 0;
        for (AnalysisResultRide fahrt : rides){
            if(fahrt.getModi() == FahrtModi.OPNV){
                dauer += ((float)fahrt.getDauer()) / 60;
            }
        }
        return dauer;
    }

    public double getOkobewertung(){
        double bewertung = 0;
        for(AnalysisResultRide fahrt : rides){
            bewertung += fahrt.getOkoBewertung();
        }
        return bewertung / rides.size();
    }

    public String getStartAdresse(){
        return startAdresse;
    }

    public String getEndAdresse(){
        return endAdresse;
    }
}
