package com.horoscopegenerator.pdf;

//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.horoscopegenerator.HoraGenerator;
import com.horoscopegenerator.Native;
import com.horoscopegenerator.NativeDetails;


public class FileBasedGenerator {

    private static List<Native> getNatives(String fileName) throws  Exception {

        List<Native> natives = new ArrayList<>();

        String line;

        InputStream fis = new FileInputStream(fileName);
        InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
        BufferedReader br = new BufferedReader(isr);

        boolean firstLine = true;
        while ((line = br.readLine()) != null) {
            if(firstLine){
                firstLine = false;
            }else {
                System.out.println(line);
                Native curNative = new Native(line);
                if(curNative.isGenerateChart()) {
                    natives.add(new Native(line));
                }
            }
        }

        return natives;

    }

    public static void main(String[] args) throws Exception {

        List<Native> natives = getNatives("c:\\temp\\Sarada_Phd_Charts.csv");

        File file = new File("c:\\temp\\jands.txt");
        if(file.exists())
            file.delete();

        Map<String, byte[]> images = new HashMap<>();
        images.put("MEENA2.JPG", null);


        for( Native curNative: natives){

            HoraGenerator2019 generator = new HoraGenerator2019();

            NativeDetails nd = new NativeDetails();
            nd.setNativeName( curNative.getNameInChart() );

            nd.setDateOfBirth( curNative.getDate());
            nd.setTimeOfBirth( curNative.getTime() );
            nd.setLatitude( curNative.getLatitude());
            nd.setLongitude( curNative.getLongitude());
            nd.setPlaceOfBirth( curNative.getPlace());
            nd.setTimeZoneId(curNative.getTimeZone()) ;
            nd.setOffset( curNative.getOffset());
            nd.setFilePath("c:\\temp\\" + curNative.getRefNo()+ "_" + curNative.getName() + ".pdf");
            nd.setImageData(images);

            System.out.println("Input: " + nd.toString());
            generator.generate(nd, true, true );
        }
    }

    public static void mainOld(String[] args) throws Exception {

        List<Native> natives = getNatives("c:\\temp\\Sarada_Phd_Charts.csv");

        File file = new File("c:\\temp\\jands.txt");
        if(file.exists())
            file.delete();

        for( Native curNative: natives){

            HoraGenerator generator = new HoraGenerator(
                    curNative.getNameInChart(),
                    curNative.getLongitude(),
                    curNative.getLatitude(),
                    curNative.getDate(),
                    28800000,
                    TimeZone.getTimeZone("America/Los_Angeles"),
                    curNative.getPlace(), true);
            generator.setLocal(true);
            Map<String, byte[]> images = new HashMap<>();
//            images.put("MEENA2.JPG", getImageBytes());
            generator.setImageData(images);
            generator.setFilePath("c:\\temp\\" + curNative.getRefNo()+ "_" + curNative.getName() + ".pdf");
            generator.generate();
        }
    }

    private static byte[] getImageBytes () throws IOException {
        String filepath = "D:\\AndroidStudioProjects\\Meena2\\app\\src\\main\\res\\drawable\\meena2.jpg";
        File imagefile = new File(filepath);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        BitmapFactory.Options factoryOptions = new BitmapFactory.Options();
//        factoryOptions.inPurgeable = true;
//        factoryOptions.inSampleSize = 2;
//
//        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG, 100 , baos);
        return baos.toByteArray();
    }



}
