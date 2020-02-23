package com.horoscopegenerator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BirthChart {

	private Chakra raasiChakra;
	
	private Chakra navamsaChakra;
	
	private String nativeName;
	
	private String birthDetails;
	
	private String placeOfBirth;
	
	private String day;
	
	private String tithi;
	
	private String nakshatra;
	
	private String balanceNakshtra;
	
	private String pada;

	private List<NPLInfo> nplInfo = new ArrayList<>();
	 
	private String[][] dasaDetails;
	
	private boolean signsInEnglish;

	private Calendar sunriseTime;

	private Calendar localDateOfBirth;

	private double moonLongitude;

	private int nakshatraNum;

	private double balanceNakshatraNum;

	private long[] dasaLengthsInMillis;
	
	private String[] yogakarakas;
	
	private String[] planetsInYKStar;
	
	private String[] digbalaPlanets;
	
	private String[] bpPlanets;
	
	private String[] udPlanets;
	
	private String[] vudPlanets;
	 
	private NPLForGivenTimeDetails nplDetails = new NPLForGivenTimeDetails();
	
	private List<Planet> rahuRepresentations = new ArrayList<>();
	private List<Planet> ketuRepresentations = new ArrayList<>();
	 
 
	public String[] getUdPlanets() {
		return udPlanets;
	}

	public void setUdPlanets(String[] udPlanets) {
		this.udPlanets = udPlanets;
	}

	public String[] getVudPlanets() {
		return vudPlanets;
	}

	public void setVudPlanets(String[] vudPlanets) {
		this.vudPlanets = vudPlanets;
	}

	public String[] getPlanetsInYKStar() {
		return planetsInYKStar;
	}

	public void setPlanetsInYKStar(String[] planetsInYKStar) {
		this.planetsInYKStar = planetsInYKStar;
	}

	public String[] getDigbalaPlanets() {
		return digbalaPlanets;
	}

	public void setDigbalaPlanets(String[] digbalaPlanets) {
		this.digbalaPlanets = digbalaPlanets;
	}

	public String[] getBpPlanets() {
		return bpPlanets;
	}

	public void setBpPlanets(String[] bpPlanets) {
		this.bpPlanets = bpPlanets;
	}

	public String[] getYogakarakas() {
		return yogakarakas;
	}

	public void setYogakarakas(String[] yogakarakas) {
		this.yogakarakas = yogakarakas;
	}

	public List<Planet> getRahuRepresentations() {
		return rahuRepresentations;
	}

	public void setRahuRepresentations(List<Planet> rahuRepresentations) {
		this.rahuRepresentations = rahuRepresentations;
	}

	public List<Planet> getKetuRepresentations() {
		return ketuRepresentations;
	}

	public void setKetuRepresentations(List<Planet> ketuRepresentations) {
		this.ketuRepresentations = ketuRepresentations;
	}

	public NPLForGivenTimeDetails getNplDetails() {
		return nplDetails;
	}

	public void setNplDetails(NPLForGivenTimeDetails nplDetails) {
		this.nplDetails = nplDetails;
	}

	public long[] getDasaLengthsInMillis() {
		return dasaLengthsInMillis;
	}

	public void setDasaLengthsInMillis(long[] dasaLengthsInMillis) {
		this.dasaLengthsInMillis = dasaLengthsInMillis;
	}

	public double getBalanceNakshatraNum() {
		return balanceNakshatraNum;
	}

	public void setBalanceNakshatraNum(double balanceNakshatraNum) {
		this.balanceNakshatraNum = balanceNakshatraNum;
	}

	public int getNakshatraNum() {
		return nakshatraNum;
	}

	public void setNakshatraNum(int nakshatraNum) {
		this.nakshatraNum = nakshatraNum;
	}

	public Calendar getLocalDateOfBirth() {
		return localDateOfBirth;
	}

	public void setLocalDateOfBirth(Calendar localDateOfBirth) {
		this.localDateOfBirth = localDateOfBirth;
	}

	public List<NPLInfo> getNplInfo() {
		return nplInfo;
	}

	public void setNplInfo(List<NPLInfo> nplInfo) {
		this.nplInfo = nplInfo;
	}

	public double getMoonLongitude() {
		return moonLongitude;
	}

	public void setMoonLongitude(double moonLongitude) {
		this.moonLongitude = moonLongitude;
	}

	public Calendar getSunriseTime() {
		return sunriseTime;
	}


	public void setSunriseTime(Calendar sunriseTime) {
		this.sunriseTime = sunriseTime;
	}
	 
	public BirthChart(boolean signsInEnglish) {
  		this.signsInEnglish = signsInEnglish;
	}

	
	
	public boolean isSignsInEnglish() {
		return signsInEnglish;
	}



	public void setSignsInEnglish(boolean signsInEnglish) {
		this.signsInEnglish = signsInEnglish;
	}



	public String[][] getDasaDetails() {
		return dasaDetails;
	}

	public void setDasaDetails(String[][] dasaDetails) {
		this.dasaDetails = dasaDetails;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getTithi() {
		return tithi;
	}

	public void setTithi(String tithi) {
		this.tithi = tithi;
	}

	public String getNakshatra() {
		return nakshatra;
	}

	public void setNakshatra(String nakshatra) {
		this.nakshatra = nakshatra;
	}

	public String getBalanceNakshtra() {
		return balanceNakshtra;
	}

	public void setBalanceNakshtra(String balanceNakshtra) {
		this.balanceNakshtra = balanceNakshtra;
	}

	public String getPada() {
		return pada;
	}

	public void setPada(String pada) {
		this.pada = pada;
	}

	public Chakra getRaasiChakra() {
		return raasiChakra;
	}

	public void setRaasiChakra(Chakra raasiChakra) {
		this.raasiChakra = raasiChakra;
	}

	public Chakra getNavamsaChakra() {
		return navamsaChakra;
	}

	public void setNavamsaChakra(Chakra navamsaChakra) {
		this.navamsaChakra = navamsaChakra;
	}

	public String getNativeName() {
		return nativeName;
	}

	public void setNativeName(String nativeName) {
		this.nativeName = nativeName;
	}

	public String getBirthDetails() {
		return birthDetails;
	}

	public void setBirthDetails(String birthDetails) {
		this.birthDetails = birthDetails;
	}

	public String getPlaceOfBirth() {
		return placeOfBirth;
	}

	public void setPlaceOfBirth(String placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}

	@Override
	public String toString() {
		return "BirthChart [raasiChakra=" + raasiChakra + ", navamsaChakra="
				+ navamsaChakra + ", nativeName=" + nativeName
				+ ", birthDetails=" + birthDetails + ", placeOfBirth="
				+ placeOfBirth + ", day=" + day + ", tithi=" + tithi
				+ ", nakshatra=" + nakshatra + ", balanceNakshtra="
				+ balanceNakshtra + ", pada=" + pada + "]";
	}
 	
}
