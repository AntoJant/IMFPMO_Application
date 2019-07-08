package com.example.mobileapp_praktikum;

import java.nio.charset.Charset;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class AnalyseRandomErgebnisMaker {
    public static AnalyseergebnisFahrt makeFahrt(Calendar c){
        Random random =new Random();
        FahrtModi modi = FahrtModi.OPNV;
        switch (random.nextInt(4)) {
            case 0:
                modi = FahrtModi.AUTO;
                break;
            case 1:
                modi = FahrtModi.OPNV;
                break;
            case 2:
                modi = FahrtModi.FAHRRAD;
                break;
            case 3:
                modi = FahrtModi.WALK;
        }

        FahrtModi alternativModi = FahrtModi.OPNV;
        switch (random.nextInt(4)) {
            case 0:
                alternativModi = FahrtModi.AUTO;
                break;
            case 1:
                alternativModi = FahrtModi.OPNV;
                break;
            case 2:
                alternativModi = FahrtModi.FAHRRAD;
                break;
            case 3:alternativModi = FahrtModi.WALK;
                break;
        }
        int fahrtID = random.nextInt(1001);
        int wegID = random.nextInt(1001);
        int okoBewertung = random.nextInt(3)+1;
        int cO2 = random.nextInt(100000);
        Calendar startZeit = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        startZeit.set(Calendar.HOUR, random.nextInt(23) + 1);
        int dauer = random.nextInt(120) + 1;
        startZeit.set(Calendar.MINUTE, random.nextInt(58) + 1);

        Calendar  endZeit = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        endZeit.set(Calendar.HOUR, startZeit.get(Calendar.HOUR));
        endZeit.set(Calendar.MINUTE,startZeit.get(Calendar.MINUTE));
        endZeit.add(Calendar.MINUTE, dauer);

        String startadresse = randomString();
        String endadresse = randomString();

        int distanz = random.nextInt(10000);

        int alternativerZeitAufwand = dauer - 1;

        AnalyseergebnisFahrt fahrt = new AnalyseergebnisFahrt(wegID,modi,alternativModi, okoBewertung, cO2, dauer ,startZeit, endZeit, startadresse,endadresse, distanz ,alternativerZeitAufwand);

        return fahrt;
    }

    public static  AnalyseergebnisWeg makeWeg(Calendar c){
        ArrayList<AnalyseergebnisFahrt> fahrten = new ArrayList<>();
        int anzahlFahrten = new Random().nextInt(9);
        for(int i = 0; i <= anzahlFahrten; i++){
            fahrten.add(makeFahrt(c));
        }
        Calendar startZeit = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));

        Calendar endZeit = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
        endZeit.add(Calendar.HOUR, 1);
        return new AnalyseergebnisWeg(fahrten,new Random().nextInt(1000),startZeit,endZeit, randomString() ,randomString());
    }

    public static AnalyseergebnisTag makeTag(Calendar date){
        Random random =new Random();
        int i =  random.nextInt(6);
        ArrayList<AnalyseergebnisWeg> wege =new ArrayList<>();
        for(int j=0; j <= i; j++){
            wege.add(makeWeg(new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH))));
        }
        return new AnalyseergebnisTag(wege, date);
    }


    public static AnalyseergebnisMonat makeMonat(Calendar a){
        Random random =new Random();
        ArrayList<AnalyseergebnisTag> tage  = new ArrayList<>();
        for(int i = 1; i <= a.getMaximum(Calendar.DAY_OF_MONTH); i++){
            tage.add(makeTag(new GregorianCalendar(a.get(Calendar.YEAR), a.get(Calendar.MONTH),i)));
        }
        return  new AnalyseergebnisMonat(random.nextInt(1000), random.nextInt(1000), a, random.nextInt(1000),  random.nextInt(1000), random.nextInt(1000), random.nextInt(1000), random.nextInt(1000),random.nextInt(2)+1,tage);
    }

    public static ArrayList<AnalyseergebnisMonat> getYear(){
        Random random =new Random();
        ArrayList<AnalyseergebnisMonat> year =new ArrayList<>();
        for (int i = 11; i >= 0; i--){
            year.add(makeMonat(new GregorianCalendar(2017, i, 1)));
        }
        return year;
    }
    public static  String randomString(){
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }

}
