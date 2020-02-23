package com.horoscopegenerator;

import java.util.ArrayList;
import java.util.List;

public class CalculatedRasi {

    private int index = 0;
    private String name = "";
    private List<PlanetInfo> planets = new ArrayList<PlanetInfo>();

    public int getIndex() {
        return index;
    }

    public List<PlanetInfo> getPlanets() {
        return planets;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlanets(List<PlanetInfo> planets) {
        this.planets = planets;
    }

    @Override
    public String toString() {
        return "Rasi [index=" + index + ", name=" + name + ", planets="
                + planets + "]";
    }

}