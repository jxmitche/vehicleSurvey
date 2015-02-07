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

public class SingleHoseSensorVehicleDetectionEventTest {
	private SingleHoseSensorVehicleDetectionEvent event;
	private SensorEvent firstSensorEvent;
	private SensorEvent secondSensorEvent;
	private List<String>validIds;
	
	@Before
	public void setup() {
		validIds = new ArrayList<String>();
		validIds.add("A");
		firstSensorEvent = new SensorEvent("A123456", 1, validIds);
		
		secondSensorEvent = new SensorEvent("A780", 1, validIds);
		
		event = new SingleHoseSensorVehicleDetectionEvent(firstSensorEvent, secondSensorEvent, Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT);
	}
	
	@Test
	public void checkWhereSensorEventsEndUp() {
		event = new SingleHoseSensorVehicleDetectionEvent(firstSensorEvent, secondSensorEvent, Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT);
		
		assertEquals(firstSensorEvent, event.getFirstSensorEvent());
		assertEquals(secondSensorEvent, event.getSecondSensorEvent());
		assertEquals(Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT, event.getDirection());
	}
	
	@Test
	public void checkFirstSensorEventNull() {
		try {
			new SingleHoseSensorVehicleDetectionEvent(null, secondSensorEvent, Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT);
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("FirstSensorEvent must not be null", ex.getMessage());
		}
	}
	
	@Test
	public void checkSecondSensorEventNull() {
		try {
			new SingleHoseSensorVehicleDetectionEvent(firstSensorEvent, null, Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT);
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("SecondSensorEvent must not be null", ex.getMessage());
		}
	}
	
	@Test
	public void checkDirectionNull() {
		try {
			new SingleHoseSensorVehicleDetectionEvent(firstSensorEvent, secondSensorEvent, null);
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("Direction must not be null", ex.getMessage());
		}
	}
	
	@Test
	public void checkValidateSensorIdsTheSameFalse() {
		assertFalse(event.sensorIdsTheSame("A", "B"));
	}
	
	@Test
	public void checkValidateSensorIdsTheSameFalseViaConstructor() {
		validIds.add("B");
		
		firstSensorEvent = new SensorEvent("A123456", 1, validIds);
		secondSensorEvent = new SensorEvent("B780", 1, validIds);
		event = new SingleHoseSensorVehicleDetectionEvent(firstSensorEvent, secondSensorEvent,
				Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT);
		
		assertFalse(event.isValid());
	}
	
	@Test
	public void checkValidateSensorIdsTheSameTrue() {
		assertTrue(event.sensorIdsTheSame("A", "A"));
	}
	
	@Test
	public void checkSensorEventsValid() {
		assertTrue(event.sensorEventsValid(true, true));
		assertFalse(event.sensorEventsValid(true, false));
		assertFalse(event.sensorEventsValid(false, false));
		assertFalse(event.sensorEventsValid(false, true));
	}
}
