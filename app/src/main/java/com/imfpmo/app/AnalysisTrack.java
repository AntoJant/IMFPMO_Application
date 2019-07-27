package com.imfpmo.app;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AnalysisTrack {
   public String timestamp,name;

   public AnalysisTrack(String timestamp, String name){
      this.timestamp = timestamp;
      this.name = name;
   }

   public Calendar getDate() {
      return new GregorianCalendar(Integer.parseInt(timestamp.substring(0, 4)), Integer.parseInt(timestamp.substring(5, 7)) - 1, Integer.parseInt(timestamp.substring(8, 10)), Integer.parseInt(timestamp.substring(11, 13)), Integer.parseInt(timestamp.substring(14, 16)));
   }
}
