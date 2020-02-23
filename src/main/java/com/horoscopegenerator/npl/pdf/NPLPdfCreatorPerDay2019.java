package com.horoscopegenerator.npl.pdf;

import com.horoscopegenerator.BirthChart;
import com.horoscopegenerator.Chakra;
import com.horoscopegenerator.HoraStrings;
import com.horoscopegenerator.House;
import com.horoscopegenerator.NPLInfo;
import com.horoscopegenerator.Planet;
import com.horoscopegenerator.PlanetInfo;
import com.horoscopegenerator.Star;
 
import java.util.ArrayList;
import java.util.Map;


public class NPLPdfCreatorPerDay2019   {
 
    Map<String, byte[]> images;
 
    public byte[] generateLocalPdf(BirthChart birthChart, String filePath, Map<String, byte[]> imageData) {

        Planet.setExalDeli();

        this.images = imageData;
           

            calculatePlanetStars(birthChart);

            birthChart.getRaasiChakra().setChakraName("Raasi Chart at Sunrise Time");
 

            addNPLTable(birthChart);

          
         
        return null;
    }


    private String getHeader(BirthChart birthChart) throws Exception {
 

        String[] headers = {"Sunrise",   "Place"};
 
//        dobCell.addElement(new Phrase(birthChart.getBirthDetails(), new Font(this.baseFont, HoraStrings.MEDIUM_FONT_SIZE, 0, BaseColor.BLACK)));
//        table.addCell(getCell(birthChart.getBirthDetails(), 15, HoraStrings.EXTRA_MEDIUM_FONT_SIZE));
//
//        table.addCell(getCell(birthChart.getPlaceOfBirth(), 15, HoraStrings.EXTRA_MEDIUM_FONT_SIZE));

        return headers[0];

    }


    public static void calculatePlanetStars(BirthChart birthChart) {

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
 


    private String addNPLTable(BirthChart birthChart)   {
 

        String[] headers = { "HRM Start", "HRM", "HRM SL"  };
        int i=0;

//       
//        for(NPLInfo nplInfo : birthChart.getNplInfo()) {
//            i++;
//            table1.addCell(getCell(nplInfo.getStartTime(), 20, HoraStrings.SMALL_FONT_SIZE));
////           
//            table1.addCell(getCell(nplInfo.getStar(), 20, HoraStrings.SMALL_FONT_SIZE));
//            table1.addCell(getCell(nplInfo.getStarLord (), 20, HoraStrings.SMALL_FONT_SIZE));
//            if(i>8) {
//                table1.addCell(getCell("", 20, HoraStrings.SMALL_FONT_SIZE));
////          
//                table1.addCell(getCell("", 20, HoraStrings.SMALL_FONT_SIZE));
//                table1.addCell(getCell("", 20, HoraStrings.SMALL_FONT_SIZE));
//                break;
//            }
////           
//        }
//
//        PdfPTable table2 = new PdfPTable(3);
//
// 
// 
//        for(NPLInfo nplInfo : birthChart.getNplInfo()) {
//            i++;
//            if(i<10) continue;
//
//            table2.addCell(getCell(nplInfo.getStartTime(), 20, HoraStrings.SMALL_FONT_SIZE));
////            
//            table2.addCell(getCell(nplInfo.getStar(), 20, HoraStrings.SMALL_FONT_SIZE));
//            table2.addCell(getCell(nplInfo.getStarLord (), 20, HoraStrings.SMALL_FONT_SIZE));
//
//            if(i>17) {
//                table2.addCell(getCell("", 20, HoraStrings.SMALL_FONT_SIZE));
////          
//                table2.addCell(getCell("", 20, HoraStrings.SMALL_FONT_SIZE));
//                table2.addCell(getCell("", 20, HoraStrings.SMALL_FONT_SIZE));
//
//                break;
//            }
//        }
//
//        PdfPTable table3 = new PdfPTable(3);
//
////  
//        i=0;
//        for(NPLInfo nplInfo : birthChart.getNplInfo()) {
//            i++;
//            if(i<19) continue;
//
//            table3.addCell(getCell(nplInfo.getStartTime(), 20, HoraStrings.SMALL_FONT_SIZE));
////           
//            table3.addCell(getCell(nplInfo.getStar(), 20, HoraStrings.SMALL_FONT_SIZE));
//            table3.addCell(getCell(nplInfo.getStarLord (), 20, HoraStrings.SMALL_FONT_SIZE));
//
//        }

   
        return null; 

    }
 
   

}
