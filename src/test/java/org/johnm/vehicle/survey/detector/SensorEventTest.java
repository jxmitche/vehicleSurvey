package org.johnm.vehicle.survey.detector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SensorEventTest {
	private String input;
	private int sensorIdLength;
	private List<String>validIds;
	private SensorEvent sensorEvent;
	private Calendar startDate;
	
	@Before
	public void setup() {
		input = "A98186";
		sensorIdLength = 1;
		validIds = new ArrayList<String>();
		validIds.add("A");
		startDate = Calendar.getInstance();
		startDate.set(2015, Calendar.JANUARY, 2);
		
		sensorEvent = new SensorEvent(input, sensorIdLength, validIds);
		sensorEvent.setMidnight(startDate);
	}
	
	@Test
	public void check_nullInput() {
		try {
			new SensorEvent(null, sensorIdLength, validIds);
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("Input must not be null", ex.getMessage());
		}
	}
	
	@Test
	public void check_nullValidIds() {
		try {
			new SensorEvent(input, sensorIdLength, null);
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("ValidIds must not be null", ex.getMessage());
		}
	}
	
	@Test
	public void check_validateSensorIdLengthZero() {
		try {
			new SensorEvent(input, 0, validIds);
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("SensorIdLength must not be less than one", ex.getMessage());
		}
	}
	
	@Test
	public void check_validateSensorIdLengthNegOne() {
		try {
			new SensorEvent(input, -1, validIds);
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("SensorIdLength must not be less than one", ex.getMessage());
		}
	}
	
	@Test
	public void checkInvalidId() {
		sensorEvent = new SensorEvent("B12345", sensorIdLength, validIds);
		
		assertFalse(sensorEvent.isValid());
	}
	
	@Test
	public void checkInvalidIdViaMethod() {
		sensorEvent = new SensorEvent("B1235", sensorIdLength, validIds);
		assertFalse(sensorEvent.validateSensorId(validIds));
	}
	
	@Test
	public void checkValidateInputLengthFalse() {
		assertFalse(sensorEvent.validateInputLength(""));
		assertFalse(sensorEvent.validateInputLength("A"));
		assertTrue(sensorEvent.validateInputLength("A1"));
	}
	
	@Test
	public void check_validateInputOnlyDigits_true() {
		assertTrue(sensorEvent.validateInputOnlyDigits("123"));
	}
	
	@Test
	public void check_validateInputOnlyDigits_false() {
		assertFalse(sensorEvent.validateInputOnlyDigits("123a"));
	}
	
	@Test
	public void checkSetMidnight() {
		final Calendar cal = sensorEvent.getMidnight();
		
		assertEquals(2015, cal.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, cal.get(Calendar.MONTH));
		assertEquals(2, cal.get(Calendar.DATE));
		assertEquals(0, cal.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, cal.get(Calendar.MINUTE));
		assertEquals(0, cal.get(Calendar.MILLISECOND));
	}
	
	@Test
	public void checkGetSensorEventDate1m() {
		sensorEvent = new SensorEvent("A1", sensorIdLength, validIds);
		sensorEvent.setMidnight(startDate);
		
		final Calendar cal = sensorEvent.getSensorEventDate();
		
		assertEquals(2015, startDate.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, startDate.get(Calendar.MONTH));
		assertEquals(2, startDate.get(Calendar.DATE));
		
		
		assertEquals(2015, cal.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, cal.get(Calendar.MONTH));
		assertEquals(2, cal.get(Calendar.DATE));
		assertEquals(0, cal.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, cal.get(Calendar.MINUTE));
		assertEquals(1, cal.get(Calendar.MILLISECOND));
	}
	
	@Test
	public void checkGetSensorEventDate1min() {
		sensorEvent = new SensorEvent("A60000", sensorIdLength, validIds);
		sensorEvent.setMidnight(startDate);
		
		final Calendar cal = sensorEvent.getSensorEventDate();
		
		assertEquals(2015, startDate.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, startDate.get(Calendar.MONTH));
		assertEquals(2, startDate.get(Calendar.DATE));
		
		
		assertEquals(2015, cal.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, cal.get(Calendar.MONTH));
		assertEquals(2, cal.get(Calendar.DATE));
		assertEquals(0, cal.get(Calendar.HOUR_OF_DAY));
		assertEquals(1, cal.get(Calendar.MINUTE));
		assertEquals(0, cal.get(Calendar.MILLISECOND));
	}
	
	@Test
	public void checkGetSensorEventDate1hour() {
		sensorEvent = new SensorEvent("A3600000", sensorIdLength, validIds);
		sensorEvent.setMidnight(startDate);
		
		final Calendar cal = sensorEvent.getSensorEventDate();
		
		assertEquals(2015, startDate.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, startDate.get(Calendar.MONTH));
		assertEquals(2, startDate.get(Calendar.DATE));
		
		
		assertEquals(2015, cal.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, cal.get(Calendar.MONTH));
		assertEquals(2, cal.get(Calendar.DATE));
		assertEquals(1, cal.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, cal.get(Calendar.MINUTE));
		assertEquals(0, cal.get(Calendar.MILLISECOND));
	}
}
