
package com.horoscopegenerator.npl.pdf;

import com.horoscopegenerator.BirthChart;
import com.horoscopegenerator.CalculatedRasi;
import com.horoscopegenerator.Chakra;
import com.horoscopegenerator.Constants;
import com.horoscopegenerator.HoraStrings;
import com.horoscopegenerator.House;
import com.horoscopegenerator.KalamsaLords;
import com.horoscopegenerator.NPLInfo;
import com.horoscopegenerator.NativeDetails;
import com.horoscopegenerator.Planet;
import com.horoscopegenerator.PlanetInfo;
import com.horoscopegenerator.Raasi;
import com.horoscopegenerator.Star;
import com.horoscopegenerator.Utils;
 

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import swisseph.DblObj;
import swisseph.SweConst;
import swisseph.SweDate;
import swisseph.SwissEph;


/*
This is an improved, refactored, thread safe and optimized horoscope generator tool.
Earlier version was not easily understood.
*/

public class NPLGeneratorForGivenTime2019 {

    private static final int SID_METHOD = SweConst.SE_SIDM_LAHIRI;

    public NPLGeneratorForGivenTime2019() {}

   

    public BirthChart getCurrentHRM(NativeDetails nativeDetails) throws Exception {

        //initialize necessary variables
        CalculatedRasi[] rasis = new CalculatedRasi[12];
        CalculatedRasi[] navamsaRasis = new CalculatedRasi[12];
        long[] dasaLengthsInMillis = new long[9];

        for (int i = 0; i < rasis.length; i++) {
            rasis[i] = new CalculatedRasi();
            rasis[i].setIndex(  i);
            rasis[i].setName(HoraStrings.RASHIS[i]);
        }

        for (int i = 0; i < HoraStrings.NAKSHATRA_LORDS.length; i++) {
            GregorianCalendar calendar = new GregorianCalendar();

            long begin = calendar.getTimeInMillis();
            calendar.add(Calendar.YEAR , Constants.DASA_LENGTHS[i]);
            long end = calendar.getTimeInMillis();
            dasaLengthsInMillis[i] = (end - begin);
        }


        double longitude = nativeDetails.getLongitude();
        double latitude = nativeDetails.getLatitude();


        BirthChart birthChart = new BirthChart(true);
        birthChart.setBirthDetails(nativeDetails.dateOfBirth + " " + nativeDetails.timeOfBirth);
        birthChart.setNativeName(nativeDetails.getNativeName());
        birthChart.setPlaceOfBirth(nativeDetails.getPlaceOfBirth() + "(La:" + Utils.toDMS(Math.abs(latitude)) + (latitude > 0 ? "N" : "S") + ",Lo:" + Utils.toDMS(Math.abs(longitude)) + (longitude > 0 ? "E" : "W") + ")");
        String[] weekDays = new DateFormatSymbols().getWeekdays();


        DateFormat dfmt = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date dateOfBirth = dfmt.parse(nativeDetails.getDateOfBirth() + " " + nativeDetails.getTimeOfBirth());

        Calendar localDateOfBirth = Calendar.getInstance();
        localDateOfBirth.setTime(dateOfBirth);

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateOfBirth);

        System.out.println("Calendar before adding offset: " + cal.getTime());
        System.out.println("Calendar offset: " + nativeDetails.getOffset());

        cal.add(Calendar.MILLISECOND, nativeDetails.getOffset());

        System.out.println("Calendar after adding offset: " + cal.getTime());

        birthChart.setDay(weekDays[localDateOfBirth.get(Calendar.DAY_OF_WEEK)]);

        birthChart.setLocalDateOfBirth(localDateOfBirth);

        birthChart.setDasaLengthsInMillis(dasaLengthsInMillis);

        // Input data:
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);


        double hour = cal.get(Calendar.HOUR_OF_DAY) +  cal.get(Calendar.MINUTE)/60.;

        SwissEph sw = new SwissEph();
        SweDate sd = new SweDate(year,month,day,hour);

        // Set sidereal mode:
        sw.swe_set_sid_mode(SID_METHOD, 0, 0);

        // Some required variables:
        double[] cusps = new double[13];
        double[] acsc = new double[10];
        double[] xp= new double[6];
        StringBuffer serr = new StringBuffer();

        // Print input details:
        System.out.println("Date (YYYY/MM/DD): " + sd.getYear() + "/" + sd.getMonth() + "/" + sd.getDay() + ", " + Utils.toHMS(sd.getHour()));
        System.out.println("Jul. day:  " + sd.getJulDay());
        System.out.println("DeltaT:    " + sd.getDeltaT()*24*3600 + " sec.");
        System.out.println("Location:  " + Utils.toDMS(Math.abs(longitude)) + (longitude > 0 ? "E" : "W") + " / " + Utils.toDMS(Math.abs(latitude)) + (latitude > 0 ? "N" : "S"));

        // Get and print ayanamsa value for info:
        double ayanamsa = sw.swe_get_ayanamsa_ut(sd.getJulDay());
        System.out.println("Ayanamsa:  " + Utils.toDMS(ayanamsa) + " (" + sw.swe_get_ayanamsa_name(SID_METHOD) + ")");

        // Get and print lagna:
        int flags = SweConst.SEFLG_SIDEREAL;
        int result = sw.swe_houses(sd.getJulDay(),
                flags,
                latitude,
                longitude,
                'P',
                cusps,
                acsc);
        System.out.println("Ascendant: " + Utils.toDMS(acsc[0]) + "\n");

        int ascSign = (int)(acsc[0] / 30) + 1;

        PlanetInfo planetInfo = new PlanetInfo();
        planetInfo.setIndex(-1);
        planetInfo.setFullLongitude(acsc[0]);
        planetInfo.setLongitude(  (acsc[0] - (int) Math.floor(acsc[0] / 30.0D) * 30) ) ;
        planetInfo.setRetrograde(  false );

        rasis[((int) Math.floor( acsc[0] / 30.0D))].getPlanets().add(planetInfo);

        // Calculate all planets:
        int[] planets = { SweConst.SE_SUN,
                SweConst.SE_MOON,
                SweConst.SE_MARS,
                SweConst.SE_MERCURY,
                SweConst.SE_JUPITER,
                SweConst.SE_VENUS,
                SweConst.SE_SATURN,
                SweConst.SE_MEAN_NODE };	// Some systems prefer SE_MEAN_NODE

        flags = SweConst.SEFLG_SWIEPH |		// fastest method, requires data files
                SweConst.SEFLG_SIDEREAL |	// sidereal zodiac
                SweConst.SEFLG_NONUT |		// will be set automatically for sidereal calculations, if not set here
                SweConst.SEFLG_SPEED;		// to determine retrograde vs. direct motion
        int sign;
        int house;
        boolean retrograde = false;

        //Now  get planet positions and copy them to pojo
        for(int p = 0; p < planets.length; p++) {
            int planet = planets[p];
            String planetName = sw.swe_get_planet_name(planet);
            int ret = sw.swe_calc_ut(sd.getJulDay(),
                    planet,
                    flags,
                    xp,
                    serr);

            if (ret != flags) {
                if (serr.length() > 0) {
                    System.err.println("Warning: " + serr);
                } else {
                    System.err.println(
                            String.format("Warning, different flags used (0x%x)", ret));
                }
            }

            sign = (int)(xp[0] / 30) + 1;
            house = (sign + 12 - ascSign) % 12 +1;
            retrograde = (xp[3] < 0);

            System.out.printf("%-12s: %s %c; sign: %2d; %s in house %2d\n",
                    planetName, Utils.toDMS(xp[0]), (retrograde ? 'R' : 'D'), sign, Utils.toDMS(xp[0] % 30), house);


            planetInfo = new PlanetInfo();
            planetInfo.setIndex(p);
            planetInfo.setFullLongitude(xp[0]);
            planetInfo.setLongitude(  (xp[0] - (int) Math.floor(xp[0] / 30.0D) * 30) ) ;
            planetInfo.setRetrograde(  xp[3] < 0.0D );

            rasis[((int) Math.floor( xp[0] / 30.0D))].getPlanets().add(planetInfo);

        }

        double ketuLong = xp[0] + 180.0D >= 360.0D ? xp[0] + 180.0D - 360.0D : xp[0] + 180.0D;

        planetInfo = new PlanetInfo();
        planetInfo.setIndex( planets.length );
        planetInfo.setFullLongitude(ketuLong);
        planetInfo.setLongitude(  ( ketuLong - (int) Math.floor( ketuLong / 30.0D) * 30) ) ;
        planetInfo.setRetrograde(  xp[3] < 0.0D );

        rasis[ ((int) Math.floor( ketuLong / 30.0D))  ].getPlanets().add(planetInfo);

        //Calculate sunrise time as well.
        //Calculate sunrise time as well.
        DblObj returnedTime = new DblObj();

        int sunriseFlags = SweConst.SE_BIT_DISC_CENTER|SweConst.SE_BIT_NO_REFRACTION| 128;

        //Determine sun rise time
        sw.swe_rise_trans( sd.getJulDay(), 0, null, 4 , 1 + sunriseFlags, new double[]{ longitude, latitude, 0}, 0, 0, returnedTime, serr);
        long timeZoneOffset = TimeZone.getDefault().getRawOffset();
        double offsetHrs = timeZoneOffset / (1000 * 60 * 60.0d);
        birthChart.setSunriseTime(toDate(returnedTime.val, offsetHrs));

        String sunRiseTime = Utils.getDisplayDate(birthChart.getSunriseTime());
        System.out.println("Sunrise time: " + sd.getJulDay() + "/" + sunRiseTime +  "/" + longitude + "/" + latitude);

        Planet.setExalDeli();

        calculatePanchanga(rasis, birthChart);

        navamsaRasis = calculateNavamsa(rasis);

        mapToModel(rasis, navamsaRasis, birthChart);

        System.out.println(Arrays.toString( rasis));
        System.out.println(Arrays.toString( navamsaRasis ));

        System.out.println("Birth Chart: " + birthChart);
        String[][] dasaDetails = calculateDasas(birthChart);
        birthChart.setDasaDetails(dasaDetails);

        calculatePlanetStars(birthChart);

        for(PlanetInfo planetInfo1 : birthChart.getRaasiChakra().getPlanetInfo()){
            if(planetInfo1.getPlanet() == Planet.MOON) {
                birthChart.getNplDetails().setDrm( planetInfo1.getPlanetStar() + "(" + planetInfo1.getPada() + ")");
                break;
            }
        }

//        calculateDikbala(birthChart);

//        calcJSAndGuna(birthChart);

        return birthChart;

    }

    public BirthChart generate(NativeDetails nativeDetails, boolean signsInEnglish, boolean isLocal) throws Exception {

        //initialize necessary variables
        CalculatedRasi[] rasis = new CalculatedRasi[12];
        CalculatedRasi[] navamsaRasis = new CalculatedRasi[12];
        long[] dasaLengthsInMillis = new long[9];

        for (int i = 0; i < rasis.length; i++) {
            rasis[i] = new CalculatedRasi();
            rasis[i].setIndex(  i);
            rasis[i].setName(HoraStrings.RASHIS[i]);
        }

        for (int i = 0; i < HoraStrings.NAKSHATRA_LORDS.length; i++) {
            GregorianCalendar calendar = new GregorianCalendar();

            long begin = calendar.getTimeInMillis();
            calendar.add(Calendar.YEAR , Constants.DASA_LENGTHS[i]);
            long end = calendar.getTimeInMillis();
            dasaLengthsInMillis[i] = (end - begin);
        }


        double longitude = nativeDetails.getLongitude();
        double latitude = nativeDetails.getLatitude();


        BirthChart birthChart = new BirthChart(signsInEnglish);
        birthChart.setBirthDetails(nativeDetails.dateOfBirth + " " + nativeDetails.timeOfBirth);
        birthChart.setNativeName(nativeDetails.getNativeName());
        birthChart.setPlaceOfBirth(nativeDetails.getPlaceOfBirth() + "(La:" + Utils.toDMS(Math.abs(latitude)) + (latitude > 0 ? "N" : "S") + ",Lo:" + Utils.toDMS(Math.abs(longitude)) + (longitude > 0 ? "E" : "W") + ")");
        String[] weekDays = new DateFormatSymbols().getWeekdays();


        DateFormat dfmt = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date dateOfBirth = dfmt.parse(nativeDetails.getDateOfBirth() + " " + nativeDetails.getTimeOfBirth());

        Calendar localDateOfBirth = Calendar.getInstance();
        localDateOfBirth.setTime(dateOfBirth);

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateOfBirth);

        System.out.println("Calendar before adding offset: " + cal.getTime());
        System.out.println("Calendar offset: " + nativeDetails.getOffset());

        cal.add(Calendar.MILLISECOND, nativeDetails.getOffset());

        System.out.println("Calendar after adding offset: " + cal.getTime());

        birthChart.setDay(weekDays[localDateOfBirth.get(Calendar.DAY_OF_WEEK)]);

        birthChart.setLocalDateOfBirth(localDateOfBirth);

        birthChart.setDasaLengthsInMillis(dasaLengthsInMillis);

        // Input data:
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);


        double hour = cal.get(Calendar.HOUR_OF_DAY) +  cal.get(Calendar.MINUTE)/60.;

        SwissEph sw = new SwissEph();
        SweDate sd = new SweDate(year,month,day,hour);

        // Set sidereal mode:
        sw.swe_set_sid_mode(SID_METHOD, 0, 0);

        // Some required variables:
        double[] cusps = new double[13];
        double[] acsc = new double[10];
        double[] xp= new double[6];
        StringBuffer serr = new StringBuffer();

        // Print input details:
        System.out.println("Date (YYYY/MM/DD): " + sd.getYear() + "/" + sd.getMonth() + "/" + sd.getDay() + ", " + Utils.toHMS(sd.getHour()));
        System.out.println("Jul. day:  " + sd.getJulDay());
        System.out.println("DeltaT:    " + sd.getDeltaT()*24*3600 + " sec.");
        System.out.println("Location:  " + Utils.toDMS(Math.abs(longitude)) + (longitude > 0 ? "E" : "W") + " / " + Utils.toDMS(Math.abs(latitude)) + (latitude > 0 ? "N" : "S"));

        // Get and print ayanamsa value for info:
        double ayanamsa = sw.swe_get_ayanamsa_ut(sd.getJulDay());
        System.out.println("Ayanamsa:  " + Utils.toDMS(ayanamsa) + " (" + sw.swe_get_ayanamsa_name(SID_METHOD) + ")");

        // Get and print lagna:
        int flags = SweConst.SEFLG_SIDEREAL;
        int result = sw.swe_houses(sd.getJulDay(),
                flags,
                latitude,
                longitude,
                'P',
                cusps,
                acsc);
        System.out.println("Ascendant: " + Utils.toDMS(acsc[0]) + "\n");

        int ascSign = (int)(acsc[0] / 30) + 1;
//
        PlanetInfo planetInfo = new PlanetInfo();
//        planetInfo.setIndex(-1);
//        planetInfo.setFullLongitude(acsc[0]);
//        planetInfo.setLongitude(  (acsc[0] - (int) Math.floor(acsc[0] / 30.0D) * 30) ) ;
//        planetInfo.setRetrograde(  false );

//        rasis[((int) Math.floor( acsc[0] / 30.0D))].getPlanets().add(planetInfo);

//        //Calculate sunrise time as well.
//        DblObj returnedTime = new DblObj();
//        //Determine sun rise time
//        sw.swe_rise_trans(sd.getJulDay(), 0, null, 4, 1, new double[]{longitude, latitude, 0}, 0, 0, returnedTime, serr);
//        long timeZoneOffset = TimeZone.getDefault().getRawOffset();
//        double offsetHrs = timeZoneOffset/(1000*60*60.0d);
//        birthChart.setSunriseTime(toDate(returnedTime.val, offsetHrs));
//
//        String sunRiseTime = Utils.getDisplayDate(birthChart.getSunriseTime());
//
//        System.out.println("Sunrise time: " + sd.getJulDay() + "/" + sunRiseTime +  "/" + longitude + "/" + latitude);

        // Calculate all planets:
        int[] planets = { SweConst.SE_SUN,
                SweConst.SE_MOON,
                SweConst.SE_MARS,
                SweConst.SE_MERCURY,
                SweConst.SE_JUPITER,
                SweConst.SE_VENUS,
                SweConst.SE_SATURN,
                SweConst.SE_MEAN_NODE };	// Some systems prefer SE_MEAN_NODE

        flags = SweConst.SEFLG_SWIEPH |		// fastest method, requires data files
                SweConst.SEFLG_SIDEREAL |	// sidereal zodiac
                SweConst.SEFLG_NONUT |		// will be set automatically for sidereal calculations, if not set here
                SweConst.SEFLG_SPEED;		// to determine retrograde vs. direct motion
        int sign;
        int house;
        boolean retrograde = false;

        //Now  get planet positions and copy them to pojo
        for(int p = 0; p < planets.length; p++) {
            int planet = planets[p];
            String planetName = sw.swe_get_planet_name(planet);
            int ret = sw.swe_calc_ut(sd.getJulDay(),
                    planet,
                    flags,
                    xp,
                    serr);

            if (ret != flags) {
                if (serr.length() > 0) {
                    System.err.println("Warning: " + serr);
                } else {
                    System.err.println(
                            String.format("Warning, different flags used (0x%x)", ret));
                }
            }

            sign = (int)(xp[0] / 30) + 1;
            house = (sign + 12 - ascSign) % 12 +1;
            retrograde = (xp[3] < 0);

            System.out.printf("%-12s: %s %c; sign: %2d; %s in house %2d\n",
                    planetName, Utils.toDMS(xp[0]), (retrograde ? 'R' : 'D'), sign, Utils.toDMS(xp[0] % 30), house);


            planetInfo = new PlanetInfo();
            planetInfo.setIndex(p);
            planetInfo.setFullLongitude(xp[0]);
            planetInfo.setLongitude(  (xp[0] - (int) Math.floor(xp[0] / 30.0D) * 30) ) ;
            planetInfo.setRetrograde(  xp[3] < 0.0D );

            rasis[((int) Math.floor( xp[0] / 30.0D))].getPlanets().add(planetInfo);

        }

        double ketuLong = xp[0] + 180.0D >= 360.0D ? xp[0] + 180.0D - 360.0D : xp[0] + 180.0D;

        planetInfo = new PlanetInfo();
        planetInfo.setIndex( planets.length );
        planetInfo.setFullLongitude(ketuLong);
        planetInfo.setLongitude(  ( ketuLong - (int) Math.floor( ketuLong / 30.0D) * 30) ) ;
        planetInfo.setRetrograde(  xp[3] < 0.0D );

        rasis[ ((int) Math.floor( ketuLong / 30.0D))  ].getPlanets().add(planetInfo);

        //Calculate sunrise time as well.
        DblObj returnedTime = new DblObj();
        //Determine sun rise time

        int sunriseFlags = SweConst.SE_BIT_DISC_CENTER|SweConst.SE_BIT_NO_REFRACTION| 128;

        //Determine sun rise time
        sw.swe_rise_trans( sd.getJulDay(), 0, null, 4 , 1 + sunriseFlags, new double[]{ longitude, latitude, 0}, 0, 0, returnedTime, serr);


        long timeZoneOffset = TimeZone.getDefault().getRawOffset();
        double offsetHrs = timeZoneOffset / (1000 * 60 * 60.0d);
        birthChart.setSunriseTime(toDate(returnedTime.val, offsetHrs));

        String sunRiseTime = Utils.getDisplayDate(birthChart.getSunriseTime());
        System.out.println("Sunrise time: " + sd.getJulDay() + "/" + sunRiseTime +  "/" + longitude + "/" + latitude);

//        //Calculate sunrise time as well.
//        DblObj returnedTime = new DblObj();
//        //Determine sun rise time
//        sw.swe_rise_trans( sd.getJulDay(), 0, null, 4 , 1 + 256, new double[]{ longitude, latitude, 0}, 0, 0, returnedTime, serr);
//        long timeZoneOffset = TimeZone.getDefault().getRawOffset();
//        double offsetHrs = timeZoneOffset / (1000 * 60 * 60.0d);
//        birthChart.setSunriseTime(toDate(returnedTime.val, offsetHrs));

        Planet.setExalDeli();

        calculatePanchanga(rasis, birthChart);

        navamsaRasis = calculateNavamsa(rasis);



        System.out.println(Arrays.toString( rasis));
        System.out.println(Arrays.toString( navamsaRasis ));

        System.out.println("Birth Chart: " + birthChart);

        birthChart.setDasaDetails(null);

        String givenTimeStr = nativeDetails.getTimeOfBirth();


        NPLGeneratorForGivenTime2019 generatorForSunRise = new NPLGeneratorForGivenTime2019();
        nativeDetails.setTimeOfBirth(sunRiseTime);
//        "", longitude, latitude, newTime, TimeZone.getTimeZone(  nativeDetails.getTimeZoneId()), birthChart.getPlaceOfBirth(), true);
        BirthChart sunriseChart = generatorForSunRise.getCurrentHRM(nativeDetails  );
        sunriseChart.setSunriseTime(sunriseChart.getSunriseTime());
        String balNakshatra = sunriseChart.getBalanceNakshtra();
        float balMinutes = Float.parseFloat(balNakshatra.substring(0,balNakshatra.length() - 2)) *8;  // 13.2/100 = 800/100

        int nplDuration  = Star.valueOf(sunriseChart.getNakshatra()).getNplDuration();

        float balMinutesToAdd = ( balMinutes * nplDuration /800 ) ;

        String prevTime = Utils.getDisplayDateHHMM(sunriseChart.getSunriseTime());

        sunriseChart.getSunriseTime().add(Calendar.SECOND, Math.round( balMinutesToAdd  * 60));

        String newTime1 = Utils.getDisplayDateHHMM(sunriseChart.getSunriseTime());


        Planet starLord = Star.valueOf(sunriseChart.getNakshatra()).getStarLord();
        String npl = "";
        for (PlanetInfo planetInfo1 : sunriseChart.getRaasiChakra().getPlanetInfo()) {
            if (planetInfo1.getPlanet() == starLord) {
                npl = planetInfo1.getHouse().getRaasi().getCamelCaseString();
                break;
            }
        }
        sunriseChart.getNplInfo().add(new NPLInfo(prevTime  , newTime1  ,Star.valueOf(sunriseChart.getNakshatra()).toString(), starLord.toString(), npl ));
        System.out.println(prevTime + " - "  + newTime1 +  " - " +  Star.valueOf(sunriseChart.getNakshatra()).toString() + " - " + starLord + " - " + npl); //  + " - " + jeeva  + " - " + sareera ) ;

        sunriseChart.getSunriseTime().add(Calendar.MINUTE, 1);
        prevTime = Utils.getDisplayDateHHMM(sunriseChart.getSunriseTime());

        sunriseChart.getSunriseTime().add(Calendar.MINUTE, -1);

        int curOrdinal = Star.valueOf(sunriseChart.getNakshatra().toUpperCase()).ordinal();

        for (int i = curOrdinal + 1; i < 27 ; i++  ){

            int curSeconds = Star.values()[i].getNplDuration() * 60;
            sunriseChart.getSunriseTime().add(Calendar.SECOND, curSeconds);
            newTime1 = Utils.getDisplayDateHHMM(sunriseChart.getSunriseTime());

            starLord = Star.values()[i].getStarLord();
            npl = "";
            for (PlanetInfo planetInfo1 : sunriseChart.getRaasiChakra().getPlanetInfo()){
                if(planetInfo1.getPlanet() == starLord){
                    npl = planetInfo1.getHouse().getRaasi().getCamelCaseString();
                }
            }

            System.out.println(  prevTime + " - "  + newTime1 + " - " +   Star.values()[i]  +  " - " + starLord + " - " + npl);
            sunriseChart.getNplInfo().add(new NPLInfo(prevTime  , newTime1  ,Star.values()[i].toString(), starLord.toString(), npl ));

            sunriseChart.getSunriseTime().add(Calendar.MINUTE, 1);
            prevTime = Utils.getDisplayDateHHMM(sunriseChart.getSunriseTime());

            sunriseChart.getSunriseTime().add(Calendar.MINUTE, -1);

        }

        for (int i = 0; i <= curOrdinal  ;  i++  ){
            int curSeconds = Star.values()[i].getNplDuration() * 60;
            sunriseChart.getSunriseTime().add(Calendar.SECOND, curSeconds);
            newTime1 = Utils.getDisplayDateHHMM(sunriseChart.getSunriseTime());

            starLord = Star.values()[i].getStarLord();
            npl = "";
            for (PlanetInfo planetInfo1 : sunriseChart.getRaasiChakra().getPlanetInfo()){
                if(planetInfo1.getPlanet() == starLord){
                    npl = planetInfo1.getHouse().getRaasi().getCamelCaseString();
                }
            }

            System.out.println( prevTime + " - "  + newTime1 + " - " +   Star.values()[i]  +  " - " + starLord + " - " + npl);
            sunriseChart.getNplInfo().add(new NPLInfo(prevTime, newTime1,Star.values()[i].toString(), starLord.toString(), npl ));

            sunriseChart.getSunriseTime().add(Calendar.MINUTE, 1);
            prevTime = Utils.getDisplayDateHHMM(sunriseChart.getSunriseTime());

            sunriseChart.getSunriseTime().add(Calendar.MINUTE, -1);
        }
        Star hrm = null;
        int pada = 0;
        Calendar givenTime = Utils.getTimeFromString24(givenTimeStr);
        System.out.println("Given time: " +  Utils.getDisplayDateHHMM(givenTime));
        for(NPLInfo nplInfo : sunriseChart.getNplInfo()){
            Calendar startTime = Utils.getTimeFromString(nplInfo.getStartTime());
            Calendar endTime = Utils.getTimeFromString(nplInfo.getEndTime());
//            System.out.println("Start/End time: " + Utils.getDisplayDateHHMM(startTime) + "/" + Utils.getDisplayDateHHMM(endTime));

            if( (givenTime.equals(startTime)) ||
                    (givenTime.equals(endTime)) ||
                    (givenTime.after(startTime) && givenTime.before(endTime)) ) {
                hrm = Star.valueOf( nplInfo.getStar().toUpperCase());

                if(givenTime.equals(startTime)){
                    pada = 1;
                }else if(givenTime.equals(endTime)){
                    pada = 4;
                }else{
                    int duration =  hrm.getNplDuration();
                    int fraction = duration/4;
                    long diff = givenTime.getTimeInMillis() - startTime.getTimeInMillis();
                    int diffInMins = (int) (diff/(1000*60));
                    pada = diffInMins/fraction + 1;
                }
                break;
            }
        }

        System.out.println("HRM/Lord: " + hrm + "/" + hrm.getStarLord());

        planetInfo = new PlanetInfo();
        planetInfo.setIndex(-1);
        planetInfo.setRetrograde(  false );

        outer:
        for (CalculatedRasi tempRasi : rasis) {
             for (PlanetInfo planetInfo1 : tempRasi.getPlanets()) {
                Planet curPlanet = Planet.getPlanetByIndex(planetInfo1.getIndex());
                if(curPlanet == hrm.getStarLord()) {
                    planetInfo.setFullLongitude( planetInfo1.getFullLongitude() );
                    planetInfo.setLongitude(planetInfo1.getLongitude());
                    tempRasi.getPlanets().add(planetInfo);
                    break outer;
                }
             }
        }

        mapToModel(rasis, navamsaRasis, birthChart);

        calculatePlanetStars(birthChart);

        calculateDikbala(birthChart);

        calcJSAndGuna(birthChart);

        String drm = sunriseChart.getNplDetails().getDrm();
        for(PlanetInfo planetInfo1: birthChart.getRaasiChakra().getPlanetInfo()) {
            if(planetInfo1.getPlanet() == Planet.MOON){
                drm = planetInfo1.getPlanetStar() + "(" + planetInfo1.getPada() + ")";
                break;
            }

        }

        birthChart.getNplDetails().setDrm(drm);
        birthChart.getNplDetails().setHrm(hrm);
        birthChart.getNplDetails().setHrmLord(hrm.getStarLord());
        birthChart.getNplDetails().setHrmPada(pada);


        for(PlanetInfo planetInfo1: birthChart.getRaasiChakra().getPlanetInfo()) {
            if(planetInfo1.getPlanet() == hrm.getStarLord()) {
                birthChart.getNplDetails().setHrmJeeva( planetInfo1.getJeeva() );
                birthChart.getNplDetails().setHrmSareera( planetInfo1.getSareera()  );
                birthChart.getNplDetails().setNpl( planetInfo1.getHouse().getRaasi()  );
                birthChart.getNplDetails().setKalamsaLord( planetInfo1.getKalamsaLord()   );

                break;
            }
        }

        new NPLPdfCreatorForGivenTime2019().generateLocalPdf(birthChart, nativeDetails.getFilePath(), nativeDetails.getImageData());

        return birthChart;
    }


    private static Calendar toDate(double d, double tzHours) {
        SweDate sd = new SweDate(d + tzHours / 24. + 0.5/24./3600. /* round to second */);
        double hour = sd.getHour();

        Calendar c = Calendar.getInstance();
        c.set(sd.getYear(), sd.getMonth() - 1, sd.getDay(), (int) hour, (int) ((hour - (int) hour) * 60), (int) ((hour * 60 - (int) (hour * 60)) * 60));
        return c;
    }

    public static void calcJSAndGuna(BirthChart birthChart) throws Exception {
        System.out.println("**********JEEVA SAREERA START ********" );

        for (Planet planet : Planet.values()) {
            PlanetInfo currentPlanet = null;
            for (PlanetInfo planetInfo : birthChart.getRaasiChakra().getPlanetInfo()) {
                if (planetInfo.getPlanet() == planet) {
                    currentPlanet = planetInfo;
                    break;
                }
            }
            System.out.println("Determining for: " + planet.getShortName() + "/" + currentPlanet.getPlanet().getShortName() );

            Planet[] jsPlanets = getJeevaSareeraPlanets(birthChart, planet, currentPlanet, true);
            String threeFoldGuna = getThreeFoldGuna(birthChart, planet);
            currentPlanet.setGunas(threeFoldGuna);
            currentPlanet.setJeeva(jsPlanets[0]);
            currentPlanet.setSareera(jsPlanets[1]);

            System.out.println("Current planet/j/s: " + currentPlanet.getPlanet().getShortName() + "/" + currentPlanet.getJeeva().getShortName() + "/" + currentPlanet.getSareera().getShortName());

            Planet kalamsaLord = getKalamsaLord(currentPlanet.getHouse().getRaasi().getRaasiNo(), currentPlanet.longitude);
            currentPlanet.setKalamsaLord(kalamsaLord);

            Planet[] kalamsaJSPlanets = getJeevaSareeraPlanets(birthChart, kalamsaLord, currentPlanet, true);

            currentPlanet.setKalamsaJeevaPlanet(kalamsaJSPlanets[0]);
            currentPlanet.setKalamsaSareeraPlanet(kalamsaJSPlanets[1]);

            String  kalamsaThreeFoldGuna = getThreeFoldGuna(birthChart, kalamsaLord);
            currentPlanet.setKalamsaThreeFoldGuna(kalamsaThreeFoldGuna);


        }
        System.out.println("**********JEEVA SAREERA END ********" );
    }
    private static Planet getKalamsaLord(int raasiNo, double longitude) {

        int arrayIndex = raasiNo % 4;
// 		 System.out.println("kalamsa: raasiNo: " + raasiNo + " raasiNo: " + (raasiNo%4) + " longitude: " + getDMS(longitude));
        int length = KalamsaLords.kalamsaRanges[arrayIndex].length;

        for (int i = 0; i < length; i++) {
            String[] kalamsaInfo = KalamsaLords.kalamsaRanges[arrayIndex][i].split(":");
            double value = Integer.parseInt(kalamsaInfo[0]) + Integer.parseInt(kalamsaInfo[1]) / 60d + Integer.parseInt(kalamsaInfo[2]) / 3600d;
            if (value > longitude) {
                return KalamsaLords.kalamsaLords[arrayIndex][i - 1];
            }
// 			  System.out.println("longitude " + longitude + "  range: " + value );
        }
        return KalamsaLords.kalamsaLords[arrayIndex][length - 1];
    }


    private static String getThreeFoldGuna(BirthChart birthChart, Planet planet) {

        PlanetInfo currentPlanet = null;
        for(PlanetInfo planetInfo : birthChart.getRaasiChakra().getPlanetInfo()) {
            if(planetInfo.getPlanet() == planet) {
                currentPlanet = planetInfo;
                break;
            }
        }

        double longitude = (currentPlanet.getHouse().getRaasi().getRaasiNo() * 30 + currentPlanet.longitude) ;

        int nakshatra = ((int)Math.floor(longitude / 13.333333333333334D));
        double balanceOfNakshatra = (1.0D - (longitude  / 13.333333333333334D - nakshatra));
        int pada = ((int)Math.floor((1.0D - balanceOfNakshatra) / 0.25D) + 1);
// 	    String starName = HoraStrings.NAKSHATRAS[nakshatra];

// 	    Star star = Star.valueOf(starName);

//	    String guna=""+ star.getStarLord().getGuna();
        int gunaStarNo = nakshatra % 9; //They repeat for a set of 9 stars
        String[][] threeFoldGunas = planet.getThreeFoldGunas();
        String threeFoldGuna = threeFoldGunas[pada - 1][gunaStarNo];

        if (planet == Planet.LAGNA) {
            threeFoldGuna = "";
        }
        return threeFoldGuna;
    }


    private static Planet getStarLord(BirthChart birthChart, Planet forPlanet){

        PlanetInfo currentPlanet = null;
        for (PlanetInfo planetInfo : birthChart.getRaasiChakra().getPlanetInfo()) {
            if (planetInfo.getPlanet() == forPlanet) {
                currentPlanet = planetInfo;
                break;
            }
        }

        double longitude = (currentPlanet.getHouse().getRaasi().getRaasiNo() * 30 + currentPlanet.longitude);

        int nakshatra = ((int) Math.floor(longitude / 13.333333333333334D));
        String starName = HoraStrings.NAKSHATRAS[nakshatra];
        Star star = Star.valueOf(starName);
        Planet starLord = star.getStarLord(); //This is planet's star lord.

        return starLord;
    }

    private static Star getStar(BirthChart birthChart, Planet forPlanet){

        PlanetInfo currentPlanet = null;
        for (PlanetInfo planetInfo : birthChart.getRaasiChakra().getPlanetInfo()) {
            if (planetInfo.getPlanet() == forPlanet) {
                currentPlanet = planetInfo;
                break;
            }
        }

        double longitude = (currentPlanet.getHouse().getRaasi().getRaasiNo() * 30 + currentPlanet.longitude);

        int nakshatra = ((int) Math.floor(longitude / 13.333333333333334D));
        String starName = HoraStrings.NAKSHATRAS[nakshatra];
        Star star = Star.valueOf(starName);
        return star;
    }


    private static Planet[] getJeevaSareeraPlanets(BirthChart birthChart, Planet forPlanet, PlanetInfo currentPlanet, boolean noExplanation) {

        StringBuilder sb = new StringBuilder();


        Planet[] jsPlanets = {null, null};
        Planet starLord = getStarLord(birthChart, forPlanet);
        Star star = getStar(birthChart, forPlanet);
//        sb.append("\n\n").append(forPlanet.toString()).append(": ");
        sb.append("The planet ").append(forPlanet.toString()).append(" is posited in ").append(starLord.toString()).append("'s star ").append(star.toString()).append(". ");

        jsPlanets[0] = getJeevaOrSareeraPlanet(birthChart, starLord, sb);
        Planet jeevaPlanet = jsPlanets[0];
        sb.append("Hence ").append( jeevaPlanet).append(" becomes Jeeva planet. ");

        if(forPlanet != Planet.LAGNA) {
            if(!noExplanation) {
                currentPlanet.setJeevaExplanation(sb.toString());
            }
        }
        sb = new StringBuilder();

        starLord = getStarLord(birthChart, jeevaPlanet);
        star = getStar(birthChart, jeevaPlanet);
        sb.append("\nThe Jeeva planet ").append(jeevaPlanet.toString()).append(" is posited in ").append(starLord.toString()).append("'s star ")
                .append(star.toString()).append(". ");

        //If Jeeva planet is in its own star, consider Raasi lord to determine Sareera Planet
        for(PlanetInfo planetInfo : birthChart.getRaasiChakra().getPlanetInfo()) {
            if(planetInfo.getPlanet() == jeevaPlanet) {
// 	    	    System.out.println("Jeeva planet " + jeevaPlanet);
//                System.out.println("Planetstar " + planetInfo );
                if( planetInfo.getPlanetStar().getStarLord() == jeevaPlanet){
                    starLord = planetInfo.getHouse().getRaasi().getRaasiLord();
                    sb.append("Due to Jeeva planet is in its own star, we need to consider Raasi lord ").append(starLord.toString()).append(". ");
                    break;
                }
            }
        }
        jsPlanets[1] = getJeevaOrSareeraPlanet(birthChart, starLord, sb);

        Planet houseLord = null;

        if(jsPlanets[0] == jsPlanets[1]) {
            //If Jeeva planet and Sareera planets are same, consider Raasi lord to determine Sareera Planet
            for(PlanetInfo planetInfo : birthChart.getRaasiChakra().getPlanetInfo()) {
                if(planetInfo.getPlanet() == jeevaPlanet) {
                    starLord = planetInfo.getHouse().getRaasi().getRaasiLord();
                    houseLord =starLord;
                    sb.append("Due to Jeeva planet and Sareera planets are same, we need to consider Raasi lord ").append(starLord.toString()).append(" as star lord to determine Sareera planet").append(". ");

                    break;
                }
            }
            jsPlanets[1] = getJeevaOrSareeraPlanet(birthChart, starLord, sb);
        }

        if(jsPlanets[0] == jsPlanets[1] && jsPlanets[0] == Planet.SATURN && houseLord == Planet.MARS) {
            sb.append("Due to Jeeva planet and Sareera planets are same, and Jeeva planet is Saturn and house lord is Mars, we should consider Sareera as Mars. " );

            //If jeeva and sareera is Saturn, then we should consider raasi lord as sareera. This can only happen for Saturn in Anuradha.
            jsPlanets[1] = Planet.MARS;
        }
        sb.append("So, ").append( jsPlanets[1]).append(" has become Sareera planet. ");;
        if(forPlanet != Planet.LAGNA) {
            if(!noExplanation) {
                currentPlanet.setSareeraExplanation(sb.toString());
            }
        }
        return jsPlanets;

    }

    private static Planet getJeevaOrSareeraPlanet(BirthChart birthChart, Planet starLord, StringBuilder sb) {

//		System.out.println("Trying to find out js for planet " + forPlanet.getShortName());

        Planet jOrSPlanet = null;
        PlanetInfo currentPlanet = null;

        for (PlanetInfo planetInfo : birthChart.getRaasiChakra().getPlanetInfo()) {
            if (planetInfo.getPlanet() == starLord) {
                currentPlanet = planetInfo;
                break;
            }
        }

        int planetCount = currentPlanet.getHouse().getPlanets().size();
        if (currentPlanet.getHouse() == birthChart.getRaasiChakra().getLagna()) {
            planetCount--;
        }

        if (planetCount == 1) {
//            sb.append("Also the planet has not conjoined with any other planet. ");
            jOrSPlanet = starLord;
        } else {
            //When there are multiple planets, find strongest among them
            //Only for string
            sb.append("The planet ").append( starLord.toString() ).append(" is posited along with ");

            StringBuilder planets = new StringBuilder();
            for (PlanetInfo planetInfo : currentPlanet.getHouse().getPlanets()) {
                if(planetInfo.getPlanet() != starLord) {
                    planets.append(planetInfo.getPlanet().toString()).append(",");
                }
            }
            if(planets.length()>0) {
                planets.deleteCharAt(planets.length() - 1);

                int lastComma = planets.lastIndexOf(",");
                if(lastComma > 0) {
                    planets.insert(lastComma, " and ");
                    planets.deleteCharAt(lastComma + 5);
                }

            }

            sb.append(planets.toString()).append(". ");
            //Only for string

            //Check if saturn is one of the stars along with SL
            if (currentPlanet.getHouse().getRaasi() == Raasi.VRUSCHIKA) {
                for (PlanetInfo planetInfo : currentPlanet.getHouse().getPlanets()) {
                    if (planetInfo.getPlanet() == Planet.SATURN && planetInfo.getPlanetStar() == Star.ANURADHA) {
                        jOrSPlanet = Planet.SATURN;
                        sb.append("The planet is in Vruchika and Saturn is in Anuradha Star. ");
                        break;
                    }
                }
            }
            //If jeeva planet is still null
            if (jOrSPlanet == null) {

                //Check if any planet is in own house
                for (PlanetInfo planetInfo : currentPlanet.getHouse().getPlanets()) {
                    if (planetInfo.getPlanet() == currentPlanet.getHouse().getRaasi().getRaasiLord()) {
                        jOrSPlanet = planetInfo.getPlanet();
                        sb.append("However, the planet ").append(jOrSPlanet.toString()).append(" is in own house " )
                                .append(currentPlanet.getHouse().getRaasi().toString().toLowerCase()).append(". ");
                        break;
                    }
                }


                if (jOrSPlanet == null) {
                    //Check if Rahu is in kumbha or Ketu is in Mesha. If so, they are the jeeva/sareera planets
                    for (PlanetInfo planetInfo : currentPlanet.getHouse().getPlanets()) {
                        if (planetInfo.getPlanet() == Planet.RAHU && currentPlanet.getHouse().getRaasi() == Raasi.KUMBHAM) {
                            jOrSPlanet = Planet.RAHU;
                            sb.append("However, the planet Rahu is in Kumbha. ");
                            break;
                        } else if (planetInfo.getPlanet() == Planet.KETU && currentPlanet.getHouse().getRaasi() == Raasi.MESHAM) {
                            jOrSPlanet = Planet.KETU;
                            sb.append("However, the planet Ketu is in Mesha. ");
                            break;
                        }
                    }
                }
            }

            if (jOrSPlanet == null) {
                //Check if any exalted planet in this house
                for (PlanetInfo planetInfo : currentPlanet.getHouse().getPlanets()) {
                    if (planetInfo.getPlanet().getExaltation() == currentPlanet.getHouse().getRaasi()) {
                        jOrSPlanet = planetInfo.getPlanet();
                        sb.append("However, the planet ").append(jOrSPlanet.toString()).append(" is exalted in ")
                                .append(currentPlanet.getHouse().getRaasi().toString()).append(". ");
                        break;
                    }
                }
            }


            if (jOrSPlanet == null) {
                //Check if any planet has digbala
                List<PlanetInfo> digbalaPlanets = new ArrayList<PlanetInfo>();
                sb.append("However, ");
                for (PlanetInfo planetInfo : currentPlanet.getHouse().getPlanets()) {
                    if (planetInfo.isHasDikbala()) {
                        digbalaPlanets.add(planetInfo);
                        sb.append(planetInfo.getPlanet().toString()).append(",");
                    }
                }

                if (digbalaPlanets.size() == 1) {
                    sb.deleteCharAt(sb.length()-1).append(" has digbala and became strong. ");
                    jOrSPlanet = digbalaPlanets.get(0).getPlanet();
                } else if (digbalaPlanets.size() > 1) {
                    sb.deleteCharAt(sb.length()-1).append(" have digbala. ");
                    //More than one planet has digbala.
                    jOrSPlanet = getStrongPlanet(digbalaPlanets, sb);
                } else {
                    sb.append("No other rules have satisfied. ");
                    jOrSPlanet = starLord;
                    System.out.println("Using natural planet for " + starLord);
                }
            }

//  	    	if(jOrSPlanet == null) {
//  	    		jOrSPlanet = getStrongPlanet(currentPlanet.getHouse().getPlanets());
//  	    	}
        }

        return jOrSPlanet;

    }

    private static Planet getStrongPlanet(List<PlanetInfo> planets, StringBuilder sb) {

        List<PlanetInfo> goodPlanets = new ArrayList<>();

        //Eliminate planets that are in debilitation. this is only applicable when we have more than one planet in digbala.
        boolean debPlanetExists = false;
        StringBuilder sbDeb = new StringBuilder();
        for (PlanetInfo planetInfo : planets) {
            if (planetInfo.getPlanet().getDelibitation() == planetInfo.getHouse().getRaasi()) {
                debPlanetExists = true;
                //If planet is debilitated, do not consider it

                sbDeb.append(planetInfo.getPlanet().toString()).append(",");
//                System.out.println("Removing digbala planet, which is in debiliation: " + planetInfo.getPlanet().getShortName());
                continue;
            }
            goodPlanets.add(planetInfo);
        }
        if(debPlanetExists) {
            sbDeb.deleteCharAt(sbDeb.length() -1);
            sb.append("Removing debilitated planet: ").append(sbDeb).append(" from digbala list.");
        }

        planets.clear();
        planets.addAll(goodPlanets);

        Planet strongPlanet = null;
        List<PlanetInfo> ownStarPlanets = new ArrayList<PlanetInfo>();

        boolean mercuryDeb = false;
        for (PlanetInfo planetInfo : planets) {

            if (planetInfo.getPlanetStar().getStarLord() == planetInfo.getPlanet()) {
                sb.append(planetInfo.getPlanet().toString()).append(",");
                //A planet is in debilitation and in its own star is only possible for Mercury.
                //If only mercury is in own star, he would anyway win in the above if Condition
                //Otherwise, we can safely exclude debilitation planet from competing planets.
                //Again this is only possible for mercury.

                if (planetInfo.getPlanet().getDelibitation() == planetInfo.getHouse().getRaasi()) {
                    mercuryDeb = true;
                    //If planet is debilitated, do not consider it for own star. This can only happen for mercury.
                    continue;
                }
                ownStarPlanets.add(planetInfo);
            }
        }

        //Check what should be the condition from saradha.

        if (ownStarPlanets.size() == 1) {
            if(mercuryDeb) {
                sb.append(" are in own star. However, Mercury is debilitated. So, Mercury can be ignored. ");
            }else{
                sb.append(" is in own star. ");
            }

            return ownStarPlanets.get(0).getPlanet();
        } else {
            if(ownStarPlanets.size() >0){
                sb.append(" are in own star. So, considering order of strengh to determine the strong planet. ");
            }else{
                sb.append("None of the digbala planets are in own star. So, considering order of strengh to determine the strong planet. ");
            }

            int strength = -1;
            //More than one db planet is in own star or none of the db planets in own star. Choose strongest planet based on order of strength
            for (PlanetInfo planetInfo : planets) {

                switch (planetInfo.getPlanet()) {

                    case RAHU:
                        strength = 1;
                        strongPlanet = planetInfo.getPlanet();
                        break;

                    case KETU:
                        if (strongPlanet == null) {
                            strength = 2;
                            strongPlanet = planetInfo.getPlanet();
                        } else {
                            if (strength > 2) {
                                strongPlanet = planetInfo.getPlanet();
                                strength = 2;
                            }
                        }
                        break;

                    case SUN:
                        if (strongPlanet == null) {
                            strength = 3;
                            strongPlanet = planetInfo.getPlanet();
                        } else {
                            if (strength > 3) {
                                strongPlanet = planetInfo.getPlanet();
                                strength = 3;
                            }
                        }
                        break;

                    case MOON:

                        if (strongPlanet == null) {
                            strength = 4;
                            strongPlanet = planetInfo.getPlanet();
                        } else {
                            if (strength > 4) {
                                strongPlanet = planetInfo.getPlanet();
                                strength = 4;
                            }
                        }

                        break;
                    case VENUS:

                        if (strongPlanet == null) {
                            strength = 5;
                            strongPlanet = planetInfo.getPlanet();
                        } else {
                            if (strength > 5) {
                                strongPlanet = planetInfo.getPlanet();
                                strength = 5;
                            }
                        }

                        break;
                    case JUPITER:

                        if (strongPlanet == null) {
                            strength = 6;
                            strongPlanet = planetInfo.getPlanet();
                        } else {
                            if (strength > 6) {
                                strongPlanet = planetInfo.getPlanet();
                                strength = 6;
                            }
                        }

                        break;
                    case SATURN:

                        if (strongPlanet == null) {
                            strength = 7;
                            strongPlanet = planetInfo.getPlanet();
                        } else {
                            if (strength > 7) {
                                strongPlanet = planetInfo.getPlanet();
                                strength = 7;
                            }
                        }

                        break;

                    case MARS:

                        if (strongPlanet == null) {
                            strength = 8;
                            strongPlanet = planetInfo.getPlanet();
                        } else {
                            if (strength > 8) {
                                strongPlanet = planetInfo.getPlanet();
                                strength = 8;
                            }
                        }

                        break;


                    case MERCURY:

                        if (strongPlanet == null) {
                            strength = 9;
                            strongPlanet = planetInfo.getPlanet();
                        } else {
                            if (strength > 9) {
                                strongPlanet = planetInfo.getPlanet();
                                strength = 9;
                            }
                        }

                        break;

                    default:
                        strongPlanet = null;

                }
            }
        }

        if(strongPlanet != null) {
            sb.append("So, the strongest planet based on order of strength is ").append(strongPlanet.toString()).append(". ");
        }
        return strongPlanet;
    }

    private void calculateDikbala(BirthChart birthChart) {

        for (PlanetInfo planetInfo : birthChart.getRaasiChakra().getPlanetInfo()) {

            int position = 0;
            switch (planetInfo.getPlanet()) {

                case JUPITER:
                case MERCURY:
                    position = 1;
                    break;

                case MOON:
                case VENUS:
                    position = 4;
                    break;

                case SATURN:
                case RAHU:
                    position = 7;
                    break;

                case SUN:
                case MARS:
                case KETU:
                    position = 10;
                    break;
                default:
                    break;

            }
            if (position > 0) {
                boolean hasDikbala = determineDikbala(birthChart, planetInfo, position);
                planetInfo.setHasDikbala(hasDikbala);
            }
        }

    }

    private boolean determineDikbala(BirthChart birthChart, PlanetInfo planetInfo, int position) {

//		System.out.println("Trying to calculate for " + planetInfo.getPlanet() + " " + position);

        //Rule 1: is planet posited in given position
        if (planetInfo.getHouse().getAssignedHouseNo() == position) {
// 			System.out.println("Rule 1 passed");
            return true;
        }

        //Rule 2: is star lord posited in given position
        Planet starLord = planetInfo.getPlanetStar().getStarLord();
        for (PlanetInfo planetInfo2 : birthChart.getRaasiChakra().getOrderedHouses().get(position - 1).getPlanets()) {
            if (planetInfo2.getPlanet() == starLord) {
//  				System.out.println("Rule 2 passed");
                return true;
            }
        }

        //Rule 3: is star lord the lord of given position
        if (birthChart.getRaasiChakra().getOrderedHouses().get(position - 1).getRaasi().getRaasiLord() == starLord) {
//  			System.out.println("Rule 3 passed");
            return true;
        }


        //Rule 4: is planet posited in conjunction with lord of given position
        Planet positionHouseLord = birthChart.getRaasiChakra().getOrderedHouses().get(position - 1).getRaasi().getRaasiLord();

        if (positionHouseLord != planetInfo.getPlanet()) {
            for (PlanetInfo planetInfo2 : planetInfo.getHouse().getPlanets()) {
                if (planetInfo2.getPlanet() == positionHouseLord) {
//	  				System.out.println("Rule 4 passed");
                    return true;
                }
            }
        }

        //Rule 5: Is planet aspected by lord of given position
        if (positionHouseLord != planetInfo.getPlanet()) {
            for (PlanetInfo planetInfo2 : planetInfo.getHouse().getAspectedPlanets()) {
                if (planetInfo2.getPlanet() == positionHouseLord) {
//	  				System.out.println("Rule 5 passed");
                    return true;
                }
            }
        }

        //Rule 6: Is planet aspected by any planet residing in given position
        for (PlanetInfo planetInfo2 : birthChart.getRaasiChakra().getOrderedHouses().get(position - 1).getPlanets()) {
            for (PlanetInfo planetInfo3 : planetInfo.getHouse().getAspectedPlanets()) {
                if (planetInfo3.getPlanet() == planetInfo2.getPlanet()) {
                    //Rahu and Ketu aspecting each other does not yield digbala
                    if ((planetInfo.getPlanet() == Planet.RAHU && planetInfo2.getPlanet() == Planet.KETU) ||
                            (planetInfo.getPlanet() == Planet.KETU && planetInfo2.getPlanet() == Planet.RAHU)) {
                        continue;
                    } else {
                        return true;
                    }
//	  				System.out.println("Rule 6 passed");
                }
            }
        }

        //Rule 7: Is planet star lord is aspected by given position lord
        for (PlanetInfo planetInfo3 : birthChart.getRaasiChakra().getPlanetInfo()) {
            if (planetInfo3.getPlanet() == starLord) {
                for (PlanetInfo planetInfo4 : planetInfo3.getHouse().getAspectedPlanets()) {
                    if (planetInfo4.getPlanet() == positionHouseLord) {
                        return true;
                    }
                }
            }
        }

        //Rule 8: Is planet star lord is associated with given position lord
        for (PlanetInfo planetInfo3 : birthChart.getRaasiChakra().getPlanetInfo()) {
            if (planetInfo3.getPlanet() == starLord) {
                for (PlanetInfo planetInfo4 : planetInfo3.getHouse().getPlanets()) {
                    if (planetInfo4.getPlanet() == positionHouseLord) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    private static void calculatePlanetStars(BirthChart birthChart) {

        for (PlanetInfo planetInfo : birthChart.getRaasiChakra().getPlanetInfo()) {
            double longitude = (planetInfo.getHouse().getRaasi().getRaasiNo() * 30 + planetInfo.longitude);

            int nakshatra = ((int) Math.floor(longitude / 13.333333333333334D));
            double balanceOfNakshatra = (1.0D - (longitude / 13.333333333333334D - nakshatra));
            int pada = ((int) Math.floor((1.0D - balanceOfNakshatra) / 0.25D) + 1);
            String starName = HoraStrings.NAKSHATRAS[nakshatra];

            Star star = Star.valueOf(starName);
            planetInfo.setPlanetStar(star);
            planetInfo.setPada(pada);

            String ud = "";
            if (star.getUd1() == pada || star.getUd2() == pada) {
                ud = "UD";
            } else if (star.getvUd1() == pada || star.getvUd2() == pada) {
                ud = "VUD";
            }

            if (planetInfo.getPlanet() != Planet.LAGNA) {
                planetInfo.setUdStatus(ud);
            }

            Planet[] ykPlanets = birthChart.getRaasiChakra().getLagna().getRaasi().getYogaKarakas();

            for (Planet ykPlanet : ykPlanets) {
                if (ykPlanet == star.getStarLord()) {
                    planetInfo.setInYkStar(true);
                    break;
                }
            }
        }

    }

    private void calculatePanchanga(CalculatedRasi[] rasis, BirthChart birthChart) {

        double moonLong = 0;
        double sunLong = 0;

        for(int i=0; i< rasis.length; i++){
            if(rasis[i].getPlanets().size() >  0) {
                for(PlanetInfo planetInfo: rasis[i].getPlanets()) {
                    if(planetInfo.getIndex() == 0) {
                        sunLong = planetInfo.getFullLongitude();
                    }
                    if(planetInfo.getIndex() == 1) {
                        moonLong = planetInfo.getFullLongitude();
                    }
                }
            }
        }

        double adjustedMoonLong = moonLong >= sunLong ? moonLong : moonLong + 360.0D;
        int tithi = ((int) Math.floor((adjustedMoonLong - sunLong) / 12.0D) + 1);

        if (tithi <= 15) {
            birthChart.setTithi(HoraStrings.PAKSHAS[0] + " " + HoraStrings.TITHIS[tithi]);
        } else if (tithi < 30) {
            birthChart.setTithi(HoraStrings.PAKSHAS[1] + " " + HoraStrings.TITHIS[(tithi - 15)]);
        } else {
            birthChart.setTithi(HoraStrings.PAKSHAS[1] + " " + HoraStrings.TITHIS[0]);
        }

        int nakshatra = ((int) Math.floor(moonLong / 13.333333333333334D));
        double balanceOfNakshatra = (1.0D - (moonLong / 13.333333333333334D - nakshatra));
        int pada = ((int) Math.floor((1.0D - balanceOfNakshatra) / 0.25D) + 1);


        birthChart.setNakshatraNum(nakshatra);
        birthChart.setNakshatra(HoraStrings.NAKSHATRAS[nakshatra]);
        birthChart.setPada(String.valueOf(pada));
        birthChart.setBalanceNakshtra(Double.valueOf(new DecimalFormat("#.##").format(balanceOfNakshatra * 100.0D)) + "% ");
        birthChart.setBalanceNakshatraNum( balanceOfNakshatra);

    }

    private String[][] calculateDasas(BirthChart birthChart) throws Exception {

        String[][] dasaDetails = {{"", "", "", "", "", "", "", "", "", ""}, {"", "", "", "", "", "", "", "", "", ""}, {"", "", "", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", "", "", ""}, {"", "", "", "", "", "", "", "", "", ""}, {"", "", "", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", "", "", ""}, {"", "", "", "", "", "", "", "", "", ""}, {"", "", "", "", "", "", "", "", "", ""}};


        int i = 0;
        int j = 0;
        int dasaIndex = 0;
        long elapsedMillis = 0L;
        Calendar dasaTime = new GregorianCalendar();

        dasaTime.setTimeInMillis(birthChart.getLocalDateOfBirth().getTimeInMillis());

        int nakshatraLord = (birthChart.getNakshatraNum() % 9);

        elapsedMillis = (long) ((1.0D - birthChart.getBalanceNakshatraNum() ) * birthChart.getDasaLengthsInMillis() [nakshatraLord]);
        dasaTime.setTimeInMillis(dasaTime.getTimeInMillis() - elapsedMillis);
        Calendar antarDasaTime = (Calendar) dasaTime.clone();
        dasaDetails[dasaIndex][0] = (HoraStrings.NAKSHATRA_LORD_DISPLAY_NAMES[nakshatraLord] + " " + HoraStrings.MAHA_DASA + ": " + Utils.getDDMMYY(dasaTime) + " to ");
        dasaTime.setTimeInMillis(dasaTime.getTimeInMillis() + birthChart.getDasaLengthsInMillis()[nakshatraLord]);

        int tmp181_180 = 0;

        String[] tmp181_179 = dasaDetails[dasaIndex];
        tmp181_179[tmp181_180] = (tmp181_179[tmp181_180] + Utils.getDDMMYY(dasaTime));
        calculateAntarDasas(nakshatraLord, antarDasaTime, dasaIndex, birthChart, dasaDetails);

        for (i = nakshatraLord + 1; i < HoraStrings.NAKSHATRA_LORD_DISPLAY_NAMES.length; i++) {
            dasaIndex++;
            antarDasaTime = (Calendar) dasaTime.clone();
            dasaDetails[dasaIndex][0] = (HoraStrings.NAKSHATRA_LORD_DISPLAY_NAMES[i] + " " + HoraStrings.MAHA_DASA + ": " + Utils.getDDMMYY(dasaTime) + " to ");
            dasaTime.setTimeInMillis(dasaTime.getTimeInMillis() + birthChart.getDasaLengthsInMillis()[i]);

            int tmp332_331 = 0;

            String[] tmp332_330 = dasaDetails[dasaIndex];

            tmp332_330[tmp332_331] = (tmp332_330[tmp332_331] + Utils.getDDMMYY(dasaTime));

            calculateAntarDasas(i, antarDasaTime, dasaIndex, birthChart, dasaDetails);
        }
        for (i = 0; i < nakshatraLord; i++) {
            dasaIndex++;
            Calendar tmp332_331 = (Calendar) dasaTime.clone();
            dasaDetails[dasaIndex][0] = (HoraStrings.NAKSHATRA_LORD_DISPLAY_NAMES[i] + " " + HoraStrings.MAHA_DASA + ": " + Utils.getDDMMYY(dasaTime) + " to ");
            dasaTime.setTimeInMillis(dasaTime.getTimeInMillis() + birthChart.getDasaLengthsInMillis()[i]);
            int
                    tmp490_489 = 0;
            String[] tmp490_488 = dasaDetails[dasaIndex];
            tmp490_488[tmp490_489] = (tmp490_488[tmp490_489] + Utils.getDDMMYY(dasaTime));
            calculateAntarDasas(i, tmp332_331, dasaIndex, birthChart, dasaDetails);
        }

        return dasaDetails;

    }

    private void calculateAntarDasas(int dasaLord, Calendar antarDasaTime, int dasaIndex, BirthChart birthChart, String[][] dasaDetails) {

        int i = 0;
        int antarDasaIndex = 0;
        long antarDasaLengthInMillis = 0L;

        Calendar pratyantarDasaTime = (Calendar) antarDasaTime.clone();

        antarDasaLengthInMillis = (long) (Constants.DASA_LENGTHS[dasaLord] / 120.0D * birthChart.getDasaLengthsInMillis()[dasaLord]);
        dasaDetails[dasaIndex][(antarDasaIndex + 1)] = (HoraStrings.NAKSHATRA_LORD_DISPLAY_NAMES[dasaLord] + " " + HoraStrings.ANTAR_DASA);
        antarDasaTime.setTimeInMillis(antarDasaTime.getTimeInMillis() + antarDasaLengthInMillis);
        calculatePratyantarDasas(dasaLord, antarDasaLengthInMillis, pratyantarDasaTime, dasaIndex, antarDasaIndex, birthChart, dasaDetails);

        for (i = dasaLord + 1; i < HoraStrings.NAKSHATRA_LORD_DISPLAY_NAMES.length; i++) {
            antarDasaIndex++;
            antarDasaLengthInMillis = (long) (Constants.DASA_LENGTHS[i] / 120.0D * birthChart.getDasaLengthsInMillis()[dasaLord]);
            pratyantarDasaTime = (Calendar) antarDasaTime.clone();
            dasaDetails[dasaIndex][(antarDasaIndex + 1)] = (HoraStrings.NAKSHATRA_LORD_DISPLAY_NAMES[i] + " " + HoraStrings.ANTAR_DASA);
            antarDasaTime.setTimeInMillis(antarDasaTime.getTimeInMillis() + antarDasaLengthInMillis);
            calculatePratyantarDasas(i, antarDasaLengthInMillis, pratyantarDasaTime, dasaIndex, antarDasaIndex, birthChart, dasaDetails);
        }

        for (i = 0; i < dasaLord; i++) {
            antarDasaIndex++;
            antarDasaLengthInMillis = (long) (Constants.DASA_LENGTHS[i] / 120.0D * birthChart.getDasaLengthsInMillis()[dasaLord]);
            pratyantarDasaTime = (Calendar) antarDasaTime.clone();
            dasaDetails[dasaIndex][(antarDasaIndex + 1)] = (HoraStrings.NAKSHATRA_LORD_DISPLAY_NAMES[i] + " " + HoraStrings.ANTAR_DASA);
            antarDasaTime.setTimeInMillis(antarDasaTime.getTimeInMillis() + antarDasaLengthInMillis);
            calculatePratyantarDasas(i, antarDasaLengthInMillis, pratyantarDasaTime, dasaIndex, antarDasaIndex, birthChart, dasaDetails);
        }

    }

    private void calculatePratyantarDasas(int antarDasaLord, long antarDasaLengthInMillis, Calendar pratyantarDasaTime, int dasaIndex, int antarDasaIndex, BirthChart birthChart, String[][] dasaDetails) {
        long pratyantarDasaLengthInMillis = 0L;

        pratyantarDasaLengthInMillis = (long) (Constants.DASA_LENGTHS[antarDasaLord] / 120.0D * antarDasaLengthInMillis);

        int tmp31_30 = (antarDasaIndex + 1);
        String[] tmp31_26 = dasaDetails[dasaIndex];
        tmp31_26[tmp31_30] = (tmp31_26[tmp31_30] + "\n" + HoraStrings.NAKSHATRA_LORD_DISPLAY_NAMES[antarDasaLord] + " " + Utils.getDDMMYY(pratyantarDasaTime));

        pratyantarDasaTime.setTimeInMillis(pratyantarDasaTime.getTimeInMillis() + pratyantarDasaLengthInMillis);

        for (int i = antarDasaLord + 1; i < HoraStrings.NAKSHATRA_LORD_DISPLAY_NAMES.length; i++) {
            pratyantarDasaLengthInMillis = (long) (Constants.DASA_LENGTHS[i] / 120.0D * antarDasaLengthInMillis);
            int xyz = (antarDasaIndex + 1);
            String[] str = dasaDetails[dasaIndex];
            str[xyz] = (str[xyz] + "\n" + HoraStrings.NAKSHATRA_LORD_DISPLAY_NAMES[i] + " " + Utils.getDDMMYY(pratyantarDasaTime));
            pratyantarDasaTime.setTimeInMillis(pratyantarDasaTime.getTimeInMillis() + pratyantarDasaLengthInMillis);
        }

        for (int i = 0; i < antarDasaLord; i++) {
            pratyantarDasaLengthInMillis = (long) (Constants.DASA_LENGTHS[i] / 120.0D * antarDasaLengthInMillis);
            int tmp246_245 = (antarDasaIndex + 1);
            String[] tmp246_241 = dasaDetails[dasaIndex];
            tmp246_241[tmp246_245] = (tmp246_241[tmp246_245] + "\n" + HoraStrings.NAKSHATRA_LORD_DISPLAY_NAMES[i] + " " + Utils.getDDMMYY(pratyantarDasaTime));
            pratyantarDasaTime.setTimeInMillis(pratyantarDasaTime.getTimeInMillis() + pratyantarDasaLengthInMillis);
        }

    }

    private CalculatedRasi[] calculateNavamsa(CalculatedRasi[] rasis) {

        CalculatedRasi[] navamsaRasis = new CalculatedRasi[12];

        for (int i = 0; i < rasis.length; i++) {
            navamsaRasis[i] = new CalculatedRasi();
            navamsaRasis[i].setIndex(i);
            navamsaRasis[i].setName(HoraStrings.RASHIS[i]);
        }

        double divLength = 3.333333333333334D;

        for (int i = 0; i < rasis.length; i++) {
            CalculatedRasi rasi = rasis[i];
            boolean isMovable = false;
            boolean isFixed = false;

            isMovable = (i + 1) % 3 == 1;
            isFixed = (i + 1) % 3 == 2;

            for (int j = 0; j < rasi.getPlanets().size(); j++) {
                PlanetInfo planetInfo = (PlanetInfo) rasi.getPlanets().get(j);
                PlanetInfo navamsaPlanet = new PlanetInfo();
                int division = 0;
                int index = 0;

                division = (int) Math.floor(planetInfo.longitude / divLength);

                if (isMovable)
                    index = (i + division) % 12;
                else if (isFixed)
                    index = (i + division + 8) % 12;
                else {
                    index = (i + division + 4) % 12;
                }
                navamsaPlanet.index = planetInfo.index;
                navamsaPlanet.longitude = ((planetInfo.longitude - division * divLength) / divLength * 30.0D);
                navamsaPlanet.isRetrograde = planetInfo.isRetrograde;
                navamsaRasis[index].getPlanets().add(navamsaPlanet);
            }
        }
        return navamsaRasis;
    }

    private void mapToModel(CalculatedRasi[] rasis, CalculatedRasi[] navamsaRasis, BirthChart birthChart) {

        boolean isLagna = false;

        Chakra raasiChakra = new Chakra();

        raasiChakra.setChakraName("Raasi");

        int lagnaNo = 1;

        for (CalculatedRasi tempRasi : rasis) {

            House house = new House();

            Raasi raasi = Raasi.getRaasi(tempRasi.getIndex());

            house.setRaasi(raasi);

            for (PlanetInfo planetInfo : tempRasi.getPlanets()) {
                planetInfo.setPlanet(Planet.getPlanetByIndex(planetInfo.getIndex()));
                planetInfo.setHouse(house);
                raasiChakra.getPlanetInfo().add(planetInfo);
                if (planetInfo.getIndex() == -1) {
                    isLagna = true;
                }
            }

            house.getPlanets().addAll(tempRasi.getPlanets());

            raasiChakra.getHouses().add(house);

            if (isLagna) {
                lagnaNo = raasiChakra.getHouses().size() - 1;
                house.setAssignedHouseNo(1);
                raasiChakra.setLagna(house);
                isLagna = false;
            }

        }

        int i = 0;
        int houseNo = (12 - lagnaNo + 1) % 12;
        if (houseNo == 0)
            houseNo = 12;

        for (House house : raasiChakra.getHouses()) {
            house.setAssignedHouseNo(houseNo++ % 13);
            raasiChakra.getOrderedHouses().add(house);
            if (houseNo % 13 == 0) {
                houseNo = 1;
            }
        }

        for (House house : raasiChakra.getHouses()) {
            raasiChakra.getOrderedHouses().set(house.getAssignedHouseNo() - 1, house);
        }


        i = 0;
        for (House house : raasiChakra.getOrderedHouses()) {
            for (PlanetInfo planetInfo : house.getPlanets()) {
                switch (planetInfo.getPlanet()) {

                    case JUPITER: //5,7,9
                        raasiChakra.getOrderedHouses().get((i + 4) % 12).getAspectedPlanets().add(planetInfo);
                        raasiChakra.getOrderedHouses().get((i + 6) % 12).getAspectedPlanets().add(planetInfo);
                        raasiChakra.getOrderedHouses().get((i + 8) % 12).getAspectedPlanets().add(planetInfo);
                        break;
                    case MARS: //4,7,8
                        raasiChakra.getOrderedHouses().get((i + 3) % 12).getAspectedPlanets().add(planetInfo);
                        raasiChakra.getOrderedHouses().get((i + 6) % 12).getAspectedPlanets().add(planetInfo);
                        raasiChakra.getOrderedHouses().get((i + 7) % 12).getAspectedPlanets().add(planetInfo);
                        break;

                    case SATURN: //3,7,10
                        raasiChakra.getOrderedHouses().get((i + 2) % 12).getAspectedPlanets().add(planetInfo);
                        raasiChakra.getOrderedHouses().get((i + 6) % 12).getAspectedPlanets().add(planetInfo);
                        raasiChakra.getOrderedHouses().get((i + 9) % 12).getAspectedPlanets().add(planetInfo);
                        break;

                    case LAGNA: //No aspects
                        break;
                    default: //7 only
                        raasiChakra.getOrderedHouses().get((i + 6) % 12).getAspectedPlanets().add(planetInfo);
                        break;
                }
            }
            i++;
        }

        Chakra navamsaChakra = new Chakra();

        navamsaChakra.setChakraName("Navamsa");

        for (CalculatedRasi tempRasi : navamsaRasis) {

            House house = new House();

            Raasi raasi = Raasi.getRaasi(tempRasi.getIndex());

            house.setRaasi(raasi);

            for (PlanetInfo planetInfo : tempRasi.getPlanets()) {
                planetInfo.setPlanet(Planet.getPlanetByIndex(planetInfo.getIndex()));
                planetInfo.setHouse(house);
                navamsaChakra.getPlanetInfo().add(planetInfo);
                if (planetInfo.getIndex() == -1) {
                    isLagna = true;
                }
            }

            house.getPlanets().addAll(tempRasi.getPlanets());

            navamsaChakra.getHouses().add(house);
            if (isLagna) {
                lagnaNo = navamsaChakra.getHouses().size() - 1;
                house.setAssignedHouseNo(1);
                navamsaChakra.setLagna(house);
                isLagna = false;
            }

        }

        houseNo = (12 - lagnaNo + 1) % 12;
        if (houseNo == 0)
            houseNo = 12;

        for (House house : navamsaChakra.getHouses()) {
            house.setAssignedHouseNo(houseNo++ % 13);
            navamsaChakra.getOrderedHouses().add(house);
            if (houseNo % 13 == 0) {
                houseNo = 1;
            }
        }

        for (House house : navamsaChakra.getHouses()) {
            navamsaChakra.getOrderedHouses().set(house.getAssignedHouseNo() - 1, house);
        }

        birthChart.setRaasiChakra(raasiChakra);
        birthChart.setNavamsaChakra(navamsaChakra);

    }

}
