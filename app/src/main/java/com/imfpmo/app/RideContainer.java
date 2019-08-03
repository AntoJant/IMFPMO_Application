package com.imfpmo.app;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface RideContainer {

    public void generateAttributes();

    public int getTotalEmissions();

    public int getCarEmissions();

    public int getBikeEmissions();

    public int getOpnvEmissions();

    public int getWalkEmissions();

    public int getTotalDistance();

    public int getCarDistance();

    public int getBikeDistance();

    public int getOpnvDistance();

    public int getWalkDistance();

    public int getTotalTimeEffort();

    public int getCarTimeEffort();

    public int getWalkTimeEffort();

    public int getBikeTimeEffort();

    public int getOpnvTimeEffort();

    public int getOkoGrade();
}
