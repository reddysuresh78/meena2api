package com.horoscopegenerator;

public enum Star {
	
	ASWINI(Planet.KETU, 1,0,2,3, 28, Raasi.MESHAM,Raasi.MESHAM,Raasi.MESHAM,Raasi.MESHAM),
	BHARANI(Planet.VENUS, 0,0,0,0, 80, Raasi.MESHAM,Raasi.MESHAM,Raasi.MESHAM,Raasi.MESHAM),
	KRITIKA(Planet.SUN , 0, 0,0,0 , 24, Raasi.MESHAM,Raasi.VRISHABHAM ,Raasi.VRISHABHAM,Raasi.VRISHABHAM),
	ROHINI(Planet.MOON, 2, 0,1,3, 40, Raasi.VRISHABHAM ,Raasi.VRISHABHAM,Raasi.VRISHABHAM, Raasi.VRISHABHAM),
	MRIGASIRA(Planet.MARS, 0,0,0,0, 28,  Raasi.VRISHABHAM ,Raasi.VRISHABHAM,Raasi.MIDHUNAM, Raasi.MIDHUNAM),
	ARUDRA(Planet.RAHU, 0,0,0,0, 72, Raasi.MIDHUNAM, Raasi.MIDHUNAM,Raasi.MIDHUNAM, Raasi.MIDHUNAM),
 	PUNARVASU(Planet.JUPITER,3,4,1,2, 64,Raasi.MIDHUNAM, Raasi.MIDHUNAM, Raasi.MIDHUNAM, Raasi.KARKATAKAM),
	PUSHYAMI(Planet.SATURN, 0,0,1,2, 76,Raasi.KARKATAKAM,Raasi.KARKATAKAM,Raasi.KARKATAKAM,Raasi.KARKATAKAM  ),
	ASLESHA(Planet.MERCURY, 0,0,0,0, 68,Raasi.KARKATAKAM,Raasi.KARKATAKAM,Raasi.KARKATAKAM,Raasi.KARKATAKAM  ),
	MAKHA(Planet.KETU, 0,0,0,4, 28, Raasi.SIMHAM,Raasi.SIMHAM,Raasi.SIMHAM,Raasi.SIMHAM),
	PUBBA(Planet.VENUS, 1,0,2,0, 80, Raasi.SIMHAM,Raasi.SIMHAM,Raasi.SIMHAM,Raasi.SIMHAM),
	UTTARA(Planet.SUN, 0,0,0,0, 24, Raasi.SIMHAM, Raasi.KANYA, Raasi.KANYA,Raasi.KANYA),
	HASTHA(Planet.MOON, 0,0,0,4, 40, Raasi.KANYA, Raasi.KANYA,Raasi.KANYA, Raasi.KANYA ),
	CHITTA(Planet.MARS, 2,3,1,4, 28, Raasi.KANYA, Raasi.KANYA, Raasi.THULA, Raasi.THULA),
	SWATHI(Planet.RAHU, 0,0,1,0, 72, Raasi.THULA, Raasi.THULA, Raasi.THULA, Raasi.THULA),
	VISAKHA(Planet.JUPITER, 0,0,0,0, 64, Raasi.THULA, Raasi.THULA, Raasi.THULA, Raasi.VRUSCHIKA),
	ANURADHA(Planet.SATURN, 4,0,3,0, 76,  Raasi.VRUSCHIKA,  Raasi.VRUSCHIKA, Raasi.VRUSCHIKA, Raasi.VRUSCHIKA),
	JYESTA(Planet.MERCURY, 0,0,1,0, 68,  Raasi.VRUSCHIKA, Raasi.VRUSCHIKA, Raasi.VRUSCHIKA, Raasi.VRUSCHIKA),
	MOOLA(Planet.KETU, 0,0,0,0, 28, Raasi.DHANUS,  Raasi.DHANUS, Raasi.DHANUS, Raasi.DHANUS),
	PURVASHADA(Planet.VENUS, 0,0,3,4, 80, Raasi.DHANUS,  Raasi.DHANUS, Raasi.DHANUS, Raasi.DHANUS),
	UTTARASHADA(Planet.SUN, 1,2,3,4, 24, Raasi.DHANUS,  Raasi.MAKARAM, Raasi.MAKARAM, Raasi.MAKARAM),
	SRAVANAM(Planet.MOON, 0,0,0,0, 40,Raasi.MAKARAM, Raasi.MAKARAM, Raasi.MAKARAM, Raasi.MAKARAM ),
	DHANISTA(Planet.MARS, 0,0,0,0, 28, Raasi.MAKARAM, Raasi.MAKARAM, Raasi.KUMBHAM, Raasi.KUMBHAM),
	SATABHISHAM(Planet.RAHU, 3,0,2,4, 72, Raasi.KUMBHAM,Raasi.KUMBHAM,Raasi.KUMBHAM,Raasi.KUMBHAM),
	PURVABHADRA(Planet.JUPITER, 0,0,0,0, 64, Raasi.KUMBHAM,Raasi.KUMBHAM,Raasi.KUMBHAM, Raasi.MEENAM),
	UTTARABHADRA(Planet.SATURN, 0,0,0,0, 76, Raasi.MEENAM,Raasi.MEENAM,Raasi.MEENAM,Raasi.MEENAM),
	REVATHI(Planet.MERCURY, 4,0,2,3, 68,Raasi.MEENAM,Raasi.MEENAM,Raasi.MEENAM,Raasi.MEENAM)
	;
	  
	
	Star(Planet starLord, int vUd1, int vUd2,  int ud1, int ud2, int nplDuration, Raasi pada1Raasi, Raasi pada2Raasi, Raasi pada3Raasi, Raasi pada4Raasi) {
		this.starLord = starLord;
		this.vUd1 = vUd1;
		this.vUd2 = vUd2;		
		this.ud1 = ud1;
		this.ud2 = ud2;
		this.nplDuration = nplDuration;

		this.pada1Raasi = pada1Raasi;
		this.pada2Raasi = pada2Raasi;
		this.pada3Raasi = pada3Raasi;
		this.pada4Raasi = pada4Raasi;
		
  	}
	
	private Planet starLord;
	
	private int vUd1;
	private int vUd2;
	private int ud1;
	private int ud2;

	public int getNplDuration() {
		return nplDuration;
	}

	private Raasi pada1Raasi;
	private Raasi pada2Raasi;
	private Raasi pada3Raasi;
	private Raasi pada4Raasi;

	public void setNplDuration(int nplDuration) {
		this.nplDuration = nplDuration;
	}

	private int nplDuration;

	public Planet getStarLord() {
		return starLord;
  	}

	public Raasi getPada1Raasi() {
		return pada1Raasi;
	}

	public Raasi getPada2Raasi() {
		return pada2Raasi;
	}

	public Raasi getPada3Raasi() {
		return pada3Raasi;
	}

	public Raasi getPada4Raasi() {
		return pada4Raasi;
	}

	public int getvUd1() {
		return vUd1;
	}



	public int getvUd2() {
		return vUd2;
	}



	public int getUd1() {
		return ud1;
	}

	public int getUd2() {
		return ud2;
	}

	@Override
	public String toString() {
		return  name().substring(0, 1).toUpperCase() +  name().substring(1).toLowerCase();
	}


}
