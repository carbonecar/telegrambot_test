package ar.com.espherika.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Time24HoursUtils {

	private Pattern pattern;
	private Matcher matcher;

	private static final String TIME24HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]";

	public Time24HoursUtils() {
		pattern = Pattern.compile(TIME24HOURS_PATTERN);
	}

	/**
	 * Validate time in 24 hours format with regular expression
	 * 
	 * @param time
	 *            time address for validation
	 * @return true valid time fromat, false invalid time format
	 */
	public boolean validate(final String time) {

		matcher = pattern.matcher(time);
		return matcher.matches();

	}

	public static String increase(String horaActualDespierta, int cant) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		Date dateHora=dateFormat.parse(horaActualDespierta);
		Calendar c=Calendar.getInstance();
		c.setTime(dateHora);
		c.add(Calendar.HOUR,cant);
		return dateFormat.format(c.getTime());
	}
}