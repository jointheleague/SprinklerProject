package org.jointheleague.sprinkler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScheduleReader {

	private static final TimeZone TZ = TimeZone.getTimeZone("PST");

	private final Calendar now = GregorianCalendar.getInstance();

	private static final Logger logger = Logger
			.getLogger(SprinklerController.class.getName());

	private int cachedVersion = -1;
	private List<GpioAction> cachedActionList;

	public static void main(String[] args) {

		ScheduleReader reader = new ScheduleReader();
		// URL url = reader.getClass().getResource("Schedules.xml");
		URL url = null;
		try {
			url = new URL("http://localhost:8888/schedules/Schedules.xml");
		} catch (MalformedURLException e) {
			logger.log(Level.SEVERE, e.getMessage());
			return;
		}
		reader.parseSchedule(url);
		List<GpioAction> list = reader.getActionList();
		for (GpioAction action : list) {
			System.out.println(action);
		}
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public JSONObject read(URL url) throws IOException, JSONException {
		InputStream is = url.openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	public void parseSchedule(URL url) {

		JSONObject document = read(url);
		String versionString = document.getString("version");
		if (versionString == null) {
			return;
		}
		int version = 0;
		try {
			version = Integer.parseInt(versionString);
		} catch (NumberFormatException ex) {
		}
		if (version > cachedVersion) {
			cachedVersion = version;
			cachedActionList = new ArrayList<GpioAction>();
			now.setTimeInMillis(TestTime.currentTimeMillis());
			JSONArray schedules = document.getJSONArray("schedules");
			for (int i = 0; i < schedules.length(); i++ ) {
				for (int day : getDaysOfWeek((JSONObject) schedules.get(i))) {
					if (day == 0) { // value 0 used for erroneous day
						continue;
					}
					for (@SuppressWarnings("unchecked")
					Iterator<Element> j = schedule.elementIterator("action"); j.hasNext();) {
						Element action = j.next();
						try {
							cachedActionList.add(createGpioAction(day, action));
						} catch (ParseException e) {
							continue;
						} catch (NumberFormatException ex) {
							continue;
						}
					}
				}
			}
			Collections.sort(cachedActionList);
		}
	}

	private GpioAction createGpioAction(int day, Element action)
			throws ParseException, NumberFormatException {
		// the ON heads
		String onValue = action.attributeValue("on");
		int[] on = null;
		if (onValue != null) {
			String[] onStrings = onValue.trim().split(", *");
			on = new int[onStrings.length];
			for (int k = 0; k < on.length; k++) {
				on[k] = Integer.parseInt(onStrings[k]);
			}
		} else {
			on = new int[0];
		}
		// the OFF heads
		String offValue = action.attributeValue("off");
		int off[] = null;
		if (offValue != null) {
			String[] offStrings = action.attributeValue("off").trim()
					.split(", *");
			off = new int[offStrings.length];
			for (int k = 0; k < off.length; k++) {
				off[k] = Integer.parseInt(offStrings[k]);
			}
		} else {
			off = new int[0];
		}
		// the time of the action
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		dateFormat.setTimeZone(TZ);
		Date d = dateFormat.parse(action.attributeValue("time"));
		Calendar timeOfDay = GregorianCalendar.getInstance();
		timeOfDay.setTime(d);
		Calendar time = (Calendar) now.clone();
		time.set(Calendar.HOUR_OF_DAY, timeOfDay.get(Calendar.HOUR_OF_DAY));
		time.set(Calendar.MINUTE, timeOfDay.get(Calendar.MINUTE));
		time.set(Calendar.SECOND, timeOfDay.get(Calendar.SECOND));
		time.set(Calendar.DAY_OF_WEEK, day);
		if (time.before(now)) {
			time.add(Calendar.DAY_OF_WEEK, 7);
		}
		// return a new GpioAction
		return new GpioAction(on, off, time);
	}

	private int[] getDaysOfWeek(JSONObject schedule) throws JSONException {
		JSONObject attr = (JSONObject) schedule.get("day_of_week");
		if (attr == null) {
			return null;
		}
		String[] days = attr.toString().trim().split(", *");
		int[] result = new int[days.length];
		for (int i = 0; i < days.length; i++) {
			try {
				result[i] = Integer.parseInt(days[i]);
			} catch (NumberFormatException ex) {
				result[i] = 0;
			}
		}
		return result;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return cachedVersion;
	}

	/**
	 * @return the ActionList
	 */
	public List<GpioAction> getActionList() {
		return cachedActionList;
	}

}
