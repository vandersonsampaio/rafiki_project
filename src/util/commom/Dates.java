package util.commom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;

public class Dates {
	public static DateTime dateTime(String date) {

		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date d = f.parse(date);

			return new DateTime(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static String formatDateTime(Long date) {
		
		return (new DateTime(date)).toString("dd/MM/yyyy");
	}
	
	public static String formatDateTime(String date) throws ParseException {
		String[] parts = date.split("\\.");
		
		if(parts.length > 1)
			//date = parts[0] + "/" + convertMonth(parts[1]) + "/" + parts[2];
			date = parts[0] + "/" + (parts[1].length() == 1 ? "0".concat(parts[1]) : parts[1]) + "/" + parts[2];
		
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
		//try {
			Date d = f.parse(date);

			return f.format(d);
		//} catch (ParseException e) {
		//	e.printStackTrace();
		//}

		//return "";
	}
	
	@SuppressWarnings("unused")
	private static String convertMonth(String month) {
		switch(month) {
			case "January" : return "01";
			case "February" : return "02";
			case "March" : return "03";
			case "April" : return "04";
			case "May" : return "05";
			case "June" : return "06";
			case "July" : return "07";
			case "August" : return "08";
			case "September" : return "09";
			case "October" : return "10";
			case "November" : return "11";
			case "December" : return "12";
		}
		
		return "";
	}
	
	public static Date getZeroTimeDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
}
