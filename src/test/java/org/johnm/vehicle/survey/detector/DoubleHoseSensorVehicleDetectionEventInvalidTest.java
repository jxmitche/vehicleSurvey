package org.johnm.vehicle.survey.detector;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DoubleHoseSensorVehicleDetectionEventInvalidTest {
	private DoubleHoseSensorVehicleDetectionEventInvalid sensor;
	
	@Before
	public void setup() {
		sensor = new DoubleHoseSensorVehicleDetectionEventInvalid();
	}
	
	@Test
	public void checkMethods() {
		assertNull(sensor.getDirection());
		assertEquals(0, sensor.getMillisecondsAfterMidnightOfEvent());
		assertNull(sensor.getSensorEventsInTimeOrder());
		assertNull(sensor.getVehicleDetectionEventDate());
	}
	
	@Test
	public void checkIsValid() {
		assertFalse(sensor.isValid());
	}
}
