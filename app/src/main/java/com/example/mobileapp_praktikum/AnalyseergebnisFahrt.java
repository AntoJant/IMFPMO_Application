package com.example.mobileapp_praktikum;

import java.util.Calendar;

public class AnalyseergebnisFahrt {
    private int fahrtID, wegID;
    private FahrtModi modi, alternativModi;
    private int okoBewertung, cO2Austoss;
    private Calendar startzeit, endzeit;
    private String startadresse, zieladresse;
    private int distanz, dauer;
    private int alternativerZeitaufwand;

    public AnalyseergebnisFahrt(int fahrtID, int wegID, FahrtModi modi, FahrtModi alternativModi, int okoBewertung, int cO2Austoss,int dauer,Calendar startzeit, Calendar endzeit, String startadresse, String zieladresse, int distanz, int alternativerZeitaufwand) {
        this.fahrtID = fahrtID;
        this.wegID = wegID;
        this.modi = modi;
        this.alternativModi = alternativModi;
        this.okoBewertung = okoBewertung;
        this.cO2Austoss = cO2Austoss;
        this.startzeit = startzeit;
        this.endzeit = endzeit;
        this.startadresse = startadresse;
        this.zieladresse = zieladresse;
        this.distanz = distanz;
        this.alternativerZeitaufwand = alternativerZeitaufwand;
        this.dauer = dauer;
    }

    public int getFahrtID() {
        return fahrtID;
    }

    public int getWegID() {
        return wegID;
    }

    public FahrtModi getModi() {
        return modi;
    }

    public FahrtModi getAlternativModi() {
        return alternativModi;
    }

    public int getOkoBewertung() {
        return okoBewertung;
    }

    public int getcO2Austoss() {
        return cO2Austoss;
    }

    public Calendar getStartzeit() {
        return startzeit;
    }

    public Calendar getEndzeit() {
        return endzeit;
    }

    public String getStartadresse() {
        return startadresse;
    }

    public String getZieladresse() {
        return zieladresse;
    }

    public int getDistanz() {
        return distanz;
    }

    public int getAlternativerZeitaufwand() {
        return alternativerZeitaufwand;
    }

    public int getDauer(){
        return  dauer;
    }
}
