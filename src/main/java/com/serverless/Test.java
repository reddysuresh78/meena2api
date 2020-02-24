package com.serverless;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
	
	public static void main(String[] args) {
		String date = "2014-02-23T00:00:00+05:30";
		
		   try {
				Date inputDate=new SimpleDateFormat("yyyy-MM-dd").parse(date.substring(0, 10));
				String outputDate=new SimpleDateFormat("dd-MM-yyyy").format(inputDate);
				System.out.println(outputDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
