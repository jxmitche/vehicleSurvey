package org.johnm.vehicle.survey.detector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.johnm.vehicle.survey.Direction;
import org.junit.Before;
import org.junit.Test;


public class DoubleHoseSensorVehicleDetectionEventTest {
	private DoubleHoseSensorVehicleDetectionEvent event;
	private SensorEvent firstSensorEvent;
	private SensorEvent secondSensorEvent;
	private SensorEvent thirdSensorEvent;
	private SensorEvent fourthSensorEvent;
	
	private List<String>validIds;
	
	@Before
	public void setup() {
		validIds = new ArrayList<String>();
		validIds.add("A");
		validIds.add("B");
		firstSensorEvent = new SensorEvent("A123456", 1, validIds);
		secondSensorEvent = new SensorEvent("B1234567", 1, validIds);
		thirdSensorEvent = new SensorEvent("A1234", 1, validIds);
		fourthSensorEvent = new SensorEvent("B12345", 1, validIds);
		
		event = new DoubleHoseSensorVehicleDetectionEvent(firstSensorEvent, secondSensorEvent, thirdSensorEvent, fourthSensorEvent,
				Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT);
	}
	
	@Test
	public void checkWhereSensorEventsEndUp() {
		event = new DoubleHoseSensorVehicleDetectionEvent(firstSensorEvent, secondSensorEvent, thirdSensorEvent, fourthSensorEvent,
				Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT);
		
		assertEquals(firstSensorEvent, event.getFirstSingleHoseSensorEvent().getFirstSensorEvent());
		assertEquals(secondSensorEvent, event.getSecondSingleHoseSensorEvent().getFirstSensorEvent());
		assertEquals(thirdSensorEvent, event.getFirstSingleHoseSensorEvent().getSecondSensorEvent());
		assertEquals(fourthSensorEvent, event.getSecondSingleHoseSensorEvent().getSecondSensorEvent());
	}
	
	@Test
	public void checkGetSensorEvents() {
		final List<SensorEvent> sensorEvents = event.getSensorEventsInTimeOrder();
		
		assertEquals(4, sensorEvents.size());
		
		final SensorEvent first = sensorEvents.get(0);
		assertEquals(123456, first.getMilliseconds());
		
		final SensorEvent second = sensorEvents.get(1);
		assertEquals(1234567, second.getMilliseconds());
		
		final SensorEvent third = sensorEvents.get(2);
		assertEquals(1234, third.getMilliseconds());
		
		final SensorEvent fourth = sensorEvents.get(3);
		assertEquals(12345, fourth.getMilliseconds());
	}
	
	@Test
	public void checkFirstSensorEventNull() {
		try {
			new DoubleHoseSensorVehicleDetectionEvent(null, secondSensorEvent, thirdSensorEvent, fourthSensorEvent,
					Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT);
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("FirstSensorEvent must not be null", ex.getMessage());
		}
	}
	
	@Test
	public void checkSecondSensorEventNull() {
		try {
			new DoubleHoseSensorVehicleDetectionEvent(firstSensorEvent, null, thirdSensorEvent, fourthSensorEvent,
					Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT);
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("FirstSensorEvent must not be null", ex.getMessage());
		}
	}
	
	@Test
	public void checkThirdSensorEventNull() {
		try {
			new DoubleHoseSensorVehicleDetectionEvent(firstSensorEvent, secondSensorEvent, null, fourthSensorEvent, 
					Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT);
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("SecondSensorEvent must not be null", ex.getMessage());
		}
	}
	
	@Test
	public void checkFourthSensorEventNull() {
		try {
			new DoubleHoseSensorVehicleDetectionEvent(firstSensorEvent, secondSensorEvent, thirdSensorEvent, null,
					Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT);
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("SecondSensorEvent must not be null", ex.getMessage());
		}
	}
	
	@Test
	public void checkDirectionNull() {
		try {
			new DoubleHoseSensorVehicleDetectionEvent(firstSensorEvent, secondSensorEvent, thirdSensorEvent, fourthSensorEvent, null);
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("Direction must not be null", ex.getMessage());
		}
	}
	
	@Test
	public void checkSensorEventsValid() {
		assertTrue(event.checkSensorEventsValid(true, true, true, true));
		assertFalse(event.checkSensorEventsValid(true, true, false, true));
		assertFalse(event.checkSensorEventsValid(true, true, false, false));
		assertFalse(event.checkSensorEventsValid(true, true, true, false));
		
		assertFalse(event.checkSensorEventsValid(false, false, true, true));
		assertFalse(event.checkSensorEventsValid(false, false, false, true));
		assertFalse(event.checkSensorEventsValid(false, false, false, false));
		assertFalse(event.checkSensorEventsValid(false, false, true, false));
		
		assertFalse(event.checkSensorEventsValid(false, true, true, true));
		assertFalse(event.checkSensorEventsValid(false, true, false, true));
		assertFalse(event.checkSensorEventsValid(false, true, false, false));
		assertFalse(event.checkSensorEventsValid(false, true, true, false));

		assertFalse(event.checkSensorEventsValid(true, false, true, true));
		assertFalse(event.checkSensorEventsValid(true, false, false, true));
		assertFalse(event.checkSensorEventsValid(true, false, true, false));
		assertFalse(event.checkSensorEventsValid(true, false, false, false));
	}
	
	@Test
	public void checkFirstAndThirdSensorEventsHaveSameId() {
		assertTrue(event.checkFirstAndThirdSensorEventsHaveSameId("A", "A"));
		assertFalse(event.checkFirstAndThirdSensorEventsHaveSameId("A", "B"));
	}
	
	@Test
	public void checkSecondAndFourthSensorEventsHaveSameId() {
		assertTrue(event.checkSecondAndFourthSensorEventsHaveSameId("A", "A"));
		assertFalse(event.checkSecondAndFourthSensorEventsHaveSameId("A", "B"));
	}
}
