package org.johnm.vehicle.survey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class VehicleSurveyEventTest {
	private VehicleSurveyEvent event;
	
	@Before
	public void setup() {
		event = new VehicleSurveyEvent("A", "B", Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT, Direction.TRAFFIC_PASSES_FROM_RIGHT_TO_LEFT,
				Calendar.getInstance());
	}
	
	@Test
	public void checkValid() {
		assertEquals(event.getFirstSensorId(), "A");
		assertEquals(event.getSecondSensorId(), "B");
		assertEquals(event.getSensorIdLength(), 1);
		assertEquals(event.getFirstSensorDirection(), Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT);
		assertEquals(event.getOtherDirection(), Direction.TRAFFIC_PASSES_FROM_RIGHT_TO_LEFT);
		
		final List<String>ids = event.getValidSensorIds();
		assertEquals(2, ids.size());
		assertEquals("A", ids.get(0));
		assertEquals("B", ids.get(1));
	}
	
	@Test
	public void checkFirstSensorIdNull() {
		try {
			new VehicleSurveyEvent(null, "B", Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT, Direction.TRAFFIC_PASSES_FROM_RIGHT_TO_LEFT,
					Calendar.getInstance());
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("FirstSensorId must not be null", ex.getMessage());
		}
	}
	
	@Test
	public void checkSecondSensorIdNull() {
		try {
			new VehicleSurveyEvent("A", null, Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT, Direction.TRAFFIC_PASSES_FROM_RIGHT_TO_LEFT,
					Calendar.getInstance());
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("SecondSensorId must not be null", ex.getMessage());
		}
	}
	
	@Test
	public void checkBothSensorIdsNull() {
		try {
			new VehicleSurveyEvent(null, null, Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT, Direction.TRAFFIC_PASSES_FROM_RIGHT_TO_LEFT,
					Calendar.getInstance());
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("FirstSensorId must not be null", ex.getMessage());
		}
	}
	
	@Test
	public void checkFirstSensorDirectionNull() {
		try {
			new VehicleSurveyEvent("A", "B", null, Direction.TRAFFIC_PASSES_FROM_RIGHT_TO_LEFT, Calendar.getInstance());
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("FirstSensorDirection must not be null", ex.getMessage());
		}
	}
	
	@Test
	public void checkOtherDirectionNull() {
		try {
			new VehicleSurveyEvent("A", "B", Direction.TRAFFIC_PASSES_FROM_RIGHT_TO_LEFT, null, Calendar.getInstance());
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("OtherDirection must not be null", ex.getMessage());
		}
	}
	
	@Test
	public void checkStartDateNull() {
		try {
			new VehicleSurveyEvent("A", "B", Direction.TRAFFIC_PASSES_FROM_RIGHT_TO_LEFT, Direction.TRAFFIC_PASSES_FROM_RIGHT_TO_LEFT, null);
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("StartDateOfSurvey must not be null", ex.getMessage());
		}
	}
	
	@Test
	public void checkSensorIdsNotSameLength() {
		try {
			new VehicleSurveyEvent("ab", "abc", Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT, Direction.TRAFFIC_PASSES_FROM_RIGHT_TO_LEFT,
					Calendar.getInstance());
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("Sensor Ids must be of the same length", ex.getMessage());
		}
	}
	
	@Test
	public void checkFirstSensorIdsEmpty() {
		try {
			new VehicleSurveyEvent("", "b", Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT, Direction.TRAFFIC_PASSES_FROM_RIGHT_TO_LEFT,
					Calendar.getInstance());
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("Sensor Ids must be of the same length", ex.getMessage());
		}
	}
	
	@Test
	public void checkSecondSensorIdsEmpty() {
		try {
			new VehicleSurveyEvent("a", "", Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT, Direction.TRAFFIC_PASSES_FROM_RIGHT_TO_LEFT,
					Calendar.getInstance());
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("Sensor Ids must be of the same length", ex.getMessage());
		}
	}
	
	@Test
	public void checkBothSensorIdsEmpty() {
		try {
			new VehicleSurveyEvent("", "", Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT, Direction.TRAFFIC_PASSES_FROM_RIGHT_TO_LEFT,
					Calendar.getInstance());
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("FirstSensorId must not be empty", ex.getMessage());
		}
	}
}
