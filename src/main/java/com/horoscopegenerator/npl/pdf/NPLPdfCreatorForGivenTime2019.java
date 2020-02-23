package com.horoscopegenerator.npl.pdf;

import com.horoscopegenerator.BhinnaPadaStars;
import com.horoscopegenerator.BirthChart;
import com.horoscopegenerator.Chakra;
import com.horoscopegenerator.HoraStrings;
import com.horoscopegenerator.House;
import com.horoscopegenerator.Planet;
import com.horoscopegenerator.PlanetInfo;
import com.horoscopegenerator.Raasi;
import com.horoscopegenerator.Star;
import com.horoscopegenerator.Utils;
 
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NPLPdfCreatorForGivenTime2019  {
 
    Map<String, byte[]> images;
 
    public static SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("hh:mm a");

    public static String getDisplayDate(Calendar calendar) {

        return HOUR_FORMAT.format(calendar.getTime());
    }
 
    public byte[] generateLocalPdf(BirthChart birthChart, String filePath, Map<String, byte[]> imageData) {

        Planet.setExalDeli();

        this.images = imageData;
  
        try { 
           
        	addNPLTable(birthChart);

            addMiscTable(birthChart);
  
            addRahuKetuRepTable(birthChart);
          
            //fileStream should be used to send the file out to client.
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

        return null;
    }
   
    public String getInitCap(String value) {

        if (value != null && value.length() > 0) {

            value = value.trim().toLowerCase();
            value = value.substring(0, 1).toUpperCase() + value.substring(1);
        }
        return value;

    }
 

    private String getDMS(double value) {
        String retVal = "";
        int deg = 0;
        int min = 0, sec = 0;

        deg = (int) Math.floor(value);
        min = (int) Math.floor((value - deg) * 60);
        sec = (int) Math.floor(((value - deg - min / 60) * 3600) - (min * 60));

        retVal = deg + ":" + min + ":" + sec;

        return retVal;
    }

    private String addMiscTable(BirthChart birthChart) throws Exception {

 
        String[] headers = {"YK Planets", "Planets In YK Star", "Digbala Planets", "UD Planets", "VUD Planets", "Bhinna Pada Planets"};
 
        Planet[] ykPlanets = birthChart.getRaasiChakra().getLagna().getRaasi().getYogaKarakas();
        
        List<String> ykPlanetsInfo = new ArrayList<>();
        
        for(Planet planet: ykPlanets) {
        	ykPlanetsInfo.add(planet.name());
        }
        
        String[] ykPlanetsArray = ykPlanetsInfo.toArray(new String[ykPlanetsInfo.size()]);
        birthChart.setYogakarakas(ykPlanetsArray);
 
        StringBuilder sb = new StringBuilder();
        for (PlanetInfo planetInfo : birthChart.getRaasiChakra().getPlanetInfo()) {
            if (planetInfo.isInYkStar()) {
                sb.append(getInitCap(planetInfo.getPlanet().getShortName())).append(",");
            }
        }
        String[] planetsInYKStar = sb.toString().split(",");
        
        birthChart.setPlanetsInYKStar(planetsInYKStar);
        
        String dbPlanetsStr = getDigbalaPlanets(birthChart);
        
        String[] dbPlanets = dbPlanetsStr.split(",");
        
        birthChart.setDigbalaPlanets(dbPlanets);
        
        
//		    table.addCell( chartRulingCell );

        sb = new StringBuilder();
        for (PlanetInfo planetInfo : birthChart.getRaasiChakra().getPlanetInfo()) {
            if ("UD".equalsIgnoreCase(planetInfo.getUdStatus()) && planetInfo.getPlanet() != Planet.LAGNA) {
                sb.append(getInitCap(planetInfo.getPlanet().getShortName())).append(",");
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
 
        birthChart.setUdPlanets(sb.toString().split(","));
        sb = new StringBuilder();
        for (PlanetInfo planetInfo : birthChart.getRaasiChakra().getPlanetInfo()) {
            if ("VUD".equalsIgnoreCase(planetInfo.getUdStatus()) && planetInfo.getPlanet() != Planet.LAGNA) {
                sb.append(getInitCap(planetInfo.getPlanet().getShortName())).append(",");
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        birthChart.setVudPlanets(sb.toString().split(","));
 
        sb = new StringBuilder();
        for (PlanetInfo planetInfo : birthChart.getRaasiChakra().getPlanetInfo()) {
            int houseNo = planetInfo.getHouse().getAssignedHouseNo();
            //if(houseNo == 6 || houseNo == 8 || houseNo == 12 ){
            if (isInBhinnaPada(planetInfo.getPlanetStar())) {
                sb.append(getInitCap(planetInfo.getPlanet().getShortName())).append(",");
            }
            //}
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        
        String[] bpPlanets = sb.toString().split(",");
        birthChart.setBpPlanets(bpPlanets);
        return null;
    }

    private Set<Planet> getRepresentations(Planet planet, BirthChart birthChart) {

        Set<Planet> planets = new LinkedHashSet<Planet>();

        PlanetInfo currentPlanet = null;
        for (PlanetInfo planetInfo : birthChart.getRaasiChakra().getPlanetInfo()) {
            if (planetInfo.getPlanet() == planet) {
                currentPlanet = planetInfo;
                break;
            }
        }

        //Rahu/Ketu posited house lord
        planets.add(currentPlanet.getHouse().getRaasi().getRaasiLord());

        //Rahu posited star lord
        planets.add(currentPlanet.getPlanetStar().getStarLord());

        //Rahu conjunction
        for (PlanetInfo planetInfo : currentPlanet.getHouse().getPlanets()) {
            planets.add(planetInfo.getPlanet());
        }

        ///Rahu aspected by
        for (PlanetInfo planetInfo : currentPlanet.getHouse().getAspectedPlanets()) {
            planets.add(planetInfo.getPlanet());
        }

        return planets;
    }


    private String addHelp() throws Exception, ParseException {
     
        String[] headers = {"DRM - Day Raising Moon, HRM - Horary Raising Moon, NPL - Nakshatra Prasna," +
                "YK - Yoga Karaka, UD - Uttama Drekkana, VUD - Vargotthama Uttama Drekkana"};

        
        return null;
    }


    private String addRahuKetuRepTable(BirthChart birthChart) throws Exception {
 
       
        Set<Planet> rahuReps = getRepresentations(Planet.RAHU, birthChart);
        List<Planet> finalList = new ArrayList<>();

          for (Planet planet : rahuReps) {
            if (planet == Planet.RAHU || planet == Planet.KETU || planet == Planet.LAGNA)
                continue;
            finalList.add(planet);
          }

        birthChart.setRahuRepresentations(finalList);
        
        Set<Planet> ketuReps = getRepresentations(Planet.KETU, birthChart);

        finalList = new ArrayList<>();
       
        for (Planet planet : ketuReps) {
            if (planet == Planet.RAHU || planet == Planet.KETU || planet == Planet.LAGNA)
                continue;
            finalList.add(planet);
        }

        birthChart.setKetuRepresentations(finalList);
 
        return null;
    }

    private String getWrapped(String input) {

        if (input != null) {
            return input.replace(",", ", ");
        }
        return input;

    }

    private boolean isInBhinnaPada(Star planetStar) {


        for (Star star : BhinnaPadaStars.bhinnaPaadaStars) {
            if (planetStar == star) {
                return true;
            }
        }

        return false;

    }




    private String getDigbalaPlanets(BirthChart birthChart) {

        StringBuffer sb = new StringBuffer();

        for (Planet planet : Planet.values()) {

            PlanetInfo currentPlanet = null;
            for (PlanetInfo planetInfo : birthChart.getRaasiChakra().getPlanetInfo()) {
                if (planetInfo.getPlanet() == planet) {
                    currentPlanet = planetInfo;
                    break;
                }
            }

            if (currentPlanet.isHasDikbala()) {
                sb.append(getInitCap(currentPlanet.getPlanet().getShortName())).append(",");
            }
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }
 

    private String addNPLTable(BirthChart birthChart) throws Exception {

 

//        String[] headers = {"DRM", "HRM", "HRM Lord", "HRML Jeeva", "HRML Sareera", "Kalamsa Lord","NPL" };
// 
//        table.addCell(getNPLCell(birthChart.getNplDetails().getDrm()));
//        table.addCell(getNPLCell(birthChart.getNplDetails().getHrm().toString() + "(" + birthChart.getNplDetails().getHrmPada() + ")"));
//        table.addCell(getNPLCell(birthChart.getNplDetails().getHrmLord().toString()));
//        table.addCell(getNPLCell(birthChart.getNplDetails().getHrmJeeva().toString()));
//        table.addCell(getNPLCell(birthChart.getNplDetails().getHrmSareera().toString()));
//        table.addCell(getNPLCell(birthChart.getNplDetails().getKalamsaLord().toString()));
//        table.addCell(getNPLCell( Utils.getInitCap( birthChart.getNplDetails().getNpl().getEnglishName())));


        return null;

    }

    private int getOtherHouse(List<House> houses, Raasi currentRaasi) {

        Raasi searchFor = null;

        switch (currentRaasi) {
            case MESHAM:
                searchFor = Raasi.VRUSCHIKA;
                break;
            case VRISHABHAM:
                searchFor = Raasi.THULA;
                break;
            case MIDHUNAM:
                searchFor = Raasi.KANYA;
                break;
            case KARKATAKAM:
                searchFor = null;
                break;
            case SIMHAM:
                searchFor = null;
                break;
            case KANYA:
                searchFor = Raasi.MIDHUNAM;
                break;
            case THULA:
                searchFor = Raasi.VRISHABHAM;
                break;
            case VRUSCHIKA:
                searchFor = Raasi.MESHAM;
                break;
            case DHANUS:
                searchFor = Raasi.MEENAM;
                break;
            case MAKARAM:
                searchFor = Raasi.KUMBHAM;
                break;
            case KUMBHAM:
                searchFor = Raasi.MAKARAM;
                break;
            case MEENAM:
                searchFor = Raasi.DHANUS;
                break;
        }
        if (searchFor == null)
            return -1;

        for (House house : houses) {
            if (house.getRaasi() == searchFor) {
                return house.getAssignedHouseNo();
            }
        }

        return -1;

    }
 
 

}
