package org.johnm.vehicle.survey.input;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.johnm.vehicle.survey.Direction;
import org.johnm.vehicle.survey.detector.SensorEvent;
import org.johnm.vehicle.survey.detector.SingleHoseSensorVehicleDetectionEvent;
import org.johnm.vehicle.survey.detector.VehicleDetectionEvent;
import org.junit.Before;
import org.junit.Test;

public class ParseVehicleDetectionEventsAddingDateTest {
	private ParseVehicleDetectionEventsAddingDate parser;
	private List<VehicleDetectionEvent> detectionEvents;
	private Calendar startDate;
	private SingleHoseSensorVehicleDetectionEvent event;
	private SingleHoseSensorVehicleDetectionEvent secondEvent;
	private SensorEvent firstSensorEvent;
	private SensorEvent secondSensorEvent;
	private SensorEvent thirdSensorEvent;
	private SensorEvent fourthSensorEvent;
	private List<String> validIds;
	private List<SensorEvent> sensorEvents;
	
	@Before
	public void setup() {
		startDate = Calendar.getInstance();
		startDate.set(2015, Calendar.JANUARY, 2);
		validIds = new ArrayList<String>();
		validIds.add("A");
		
		firstSensorEvent = new SensorEvent("A123456", 1, validIds);
		secondSensorEvent = new SensorEvent("A780", 1, validIds);
		
		thirdSensorEvent = new SensorEvent("A11111", 1, validIds);
		fourthSensorEvent = new SensorEvent("A111112", 1, validIds);
		
		sensorEvents = new ArrayList<SensorEvent>();
		sensorEvents.add(firstSensorEvent);
		
		event = new SingleHoseSensorVehicleDetectionEvent(firstSensorEvent, secondSensorEvent,
				Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT);
		
		secondEvent = new SingleHoseSensorVehicleDetectionEvent(thirdSensorEvent, fourthSensorEvent,
				Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT);

		detectionEvents = new ArrayList<VehicleDetectionEvent>();
		detectionEvents.add(event);
		
		parser = new ParseVehicleDetectionEventsAddingDate(detectionEvents, startDate);
	}
	
	@Test
	public void checkAddDateToSensorEvents() {
		parser.addDateToSensorEvents(startDate, detectionEvents);
		
		assertEquals(2015, startDate.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, startDate.get(Calendar.MONTH));
		assertEquals(2, startDate.get(Calendar.DATE));
		
		final Calendar midnightFirst = firstSensorEvent.getMidnight();
		assertEquals(2015, midnightFirst.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, midnightFirst.get(Calendar.MONTH));
		assertEquals(2, midnightFirst.get(Calendar.DATE));
		
		final Calendar midnightSecond = secondSensorEvent.getMidnight();
		assertEquals(2015, midnightSecond.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, midnightSecond.get(Calendar.MONTH));
		assertEquals(3, midnightSecond.get(Calendar.DATE));
	}
	
	@Test
	public void checkAddDateToSensorEventsTwoDetectionEvents() {
		detectionEvents.add(secondEvent);
		parser.addDateToSensorEvents(startDate, detectionEvents);
		
		assertEquals(2015, startDate.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, startDate.get(Calendar.MONTH));
		assertEquals(2, startDate.get(Calendar.DATE));
		
		final Calendar midnightFirst = firstSensorEvent.getMidnight();
		assertEquals(2015, midnightFirst.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, midnightFirst.get(Calendar.MONTH));
		assertEquals(2, midnightFirst.get(Calendar.DATE));
		
		final Calendar midnightSecond = secondSensorEvent.getMidnight();
		assertEquals(2015, midnightSecond.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, midnightSecond.get(Calendar.MONTH));
		assertEquals(3, midnightSecond.get(Calendar.DATE));
		
		final Calendar midnightThird = thirdSensorEvent.getMidnight();
		assertEquals(2015, midnightThird.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, midnightThird.get(Calendar.MONTH));
		assertEquals(3, midnightThird.get(Calendar.DATE));
		
		final Calendar midnightFourth = fourthSensorEvent.getMidnight();
		assertEquals(2015, midnightFourth.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, midnightFourth.get(Calendar.MONTH));
		assertEquals(3, midnightFourth.get(Calendar.DATE));
	}
	
	@Test
	public void checkProcessUpToFourSensorEventsAddingDateSameDayTwoSensorEvents() {
		sensorEvents.add(secondSensorEvent);
		final int nextMilliseconds = parser.processUpToFourSensorEventsAddingDate(startDate, 1, sensorEvents);
		
		assertEquals(780, nextMilliseconds);
		assertEquals(2015, startDate.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, startDate.get(Calendar.MONTH));
		assertEquals(3, startDate.get(Calendar.DATE));
	}
	
	@Test
	public void checkProcessUpToFourSensorEventsAddingDateSameDay() {
		final int nextMilliseconds = parser.processUpToFourSensorEventsAddingDate(startDate, 1, sensorEvents);
		
		assertEquals(123456, nextMilliseconds);
		assertEquals(2015, startDate.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, startDate.get(Calendar.MONTH));
		assertEquals(2, startDate.get(Calendar.DATE));
	}
	
	@Test
	public void checkProcessUpToFourSensorEventsAddingDateNextDay() {
		final int nextMilliseconds = parser.processUpToFourSensorEventsAddingDate(startDate, 123457, sensorEvents);
		
		assertEquals(123456, nextMilliseconds);
		assertEquals(2015, startDate.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, startDate.get(Calendar.MONTH));
		assertEquals(3, startDate.get(Calendar.DATE));
	}

	@Test
	public void checkAddDateToOneSensorEventSameDay() {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(startDate.getTimeInMillis());
		final int nextMilliseconds = parser.addDateToOneSensorEvent(cal, 1, firstSensorEvent);
		final Calendar midnight = firstSensorEvent.getMidnight();
		
		assertEquals(123456, nextMilliseconds);
		assertEquals(2015, midnight.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, midnight.get(Calendar.MONTH));
		assertEquals(2, midnight.get(Calendar.DATE));
		
		assertEquals(2015, cal.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, cal.get(Calendar.MONTH));
		assertEquals(2, cal.get(Calendar.DATE));
		
		assertEquals(2015, startDate.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, startDate.get(Calendar.MONTH));
		assertEquals(2, startDate.get(Calendar.DATE));
	}
	
	@Test
	public void checkAddDateToOneSensorEventChangedDay() {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(startDate.getTimeInMillis());
		final int nextMilliseconds = parser.addDateToOneSensorEvent(cal, 123457, firstSensorEvent);
		final Calendar midnight = firstSensorEvent.getMidnight();
		
		assertEquals(123456, nextMilliseconds);
		assertEquals(2015, midnight.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, midnight.get(Calendar.MONTH));
		assertEquals(3, midnight.get(Calendar.DATE));
		
		assertEquals(2015, cal.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, cal.get(Calendar.MONTH));
		assertEquals(3, cal.get(Calendar.DATE));
		
		assertEquals(2015, startDate.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, startDate.get(Calendar.MONTH));
		assertEquals(2, startDate.get(Calendar.DATE));
	}
}
