package com.danimaniarqsoft.brain.main;

public class DateCalculator {

	private DateCalculator() {

	}

	public static double convertDate(String date) {
		if(date==null) 
		{
			return Double.NaN;
		}
		String[] dateone = date.split(":");
		int hours = Integer.parseInt(dateone[0]);
		double minutes = Double.parseDouble(dateone[1]) / 60;
		return hours + minutes;
	}
}
