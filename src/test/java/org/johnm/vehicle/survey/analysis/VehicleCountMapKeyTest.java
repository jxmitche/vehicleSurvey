package org.johnm.vehicle.survey.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.johnm.vehicle.survey.Direction;
import org.johnm.vehicle.survey.analysis.VehicleCountMapKey;
import org.junit.Before;
import org.junit.Test;

public class VehicleCountMapKeyTest {
	private int year;
	private int month;
	private int day;
	private int periodInDay;
	private Direction vehicleDirection;
	private VehicleCountMapKey mapKey1;
	private VehicleCountMapKey mapKey2;
	
	@Before
	public void setup() {
		year = 2015;
		month = 1;
		day = 2;
		periodInDay = 4;
		vehicleDirection = Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT;
		
		mapKey1 = new VehicleCountMapKey(year, month, day, periodInDay, vehicleDirection);
		mapKey2 = new VehicleCountMapKey(year, month, day, periodInDay, vehicleDirection);
	}
	
	@Test
	public void checkEquals() {
		assertEquals(mapKey1, mapKey2);
		assertEquals(year, mapKey1.getYear());
		assertEquals(month, mapKey1.getMonth());
		assertEquals(day, mapKey1.getDay());
		assertEquals(periodInDay, mapKey1.getPeriodInDay());
		assertEquals(vehicleDirection, mapKey1.getVehicleDirection());
		
		assertEquals("year=2015, month=1, day=2, periodInDay=4, vehicleDirection=TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT", mapKey1.toString());
		
		mapKey2 = new VehicleCountMapKey(year, month, day, periodInDay, Direction.TRAFFIC_PASSES_FROM_RIGHT_TO_LEFT);
		assertFalse(mapKey1.equals(mapKey2));
		
		mapKey2 = new VehicleCountMapKey(year, month, day, 5, vehicleDirection);
		assertFalse(mapKey1.equals(mapKey2));
		
		mapKey2 = new VehicleCountMapKey(year, month, 1, periodInDay, vehicleDirection);
		assertFalse(mapKey1.equals(mapKey2));
		
		mapKey2 = new VehicleCountMapKey(year, 5, day, periodInDay, vehicleDirection);
		assertFalse(mapKey1.equals(mapKey2));
		
		mapKey2 = new VehicleCountMapKey(2014, month, day, periodInDay, vehicleDirection);
		assertFalse(mapKey1.equals(mapKey2));
	}
	
	@Test
	public void checkNullParamDirection() {
		try {
			mapKey2 = new VehicleCountMapKey(year, month, day, periodInDay, null);
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("VehicleDirection must not be null", ex.getMessage());
		}
	}

}
