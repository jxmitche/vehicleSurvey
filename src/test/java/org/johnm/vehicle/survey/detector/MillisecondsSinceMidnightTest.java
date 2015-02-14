package org.johnm.vehicle.survey.detector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class MillisecondsSinceMidnightTest {
	private MillisecondsSinceMidnight millisSinceMidnight;
	
	@Before
	public void setup() {
		final Integer millis = new Integer(123);
		millisSinceMidnight = new MillisecondsSinceMidnight(millis);
	}
	
	
	@Test
	public void checkValidMillis() {
		assertEquals(123, millisSinceMidnight.getMillisecondsSinceMidnight());
	}
	
	@Test
	public void checkMillisecondsNull() {
		try {
			new MillisecondsSinceMidnight(null);
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("Milliseconds must not be null", ex.getMessage());
		}
	}
	
	@Test
	public void checkMillisecondsNegative() {
		final Integer millis = new Integer(-123);
		
		millisSinceMidnight = new MillisecondsSinceMidnight(millis);
		assertFalse(millisSinceMidnight.isValid());
	}
	
	@Test
	public void checkMaxMillisecondsMinusOneIsValid() {
		final Integer millis = new Integer(MillisecondsSinceMidnight.MILLISSECONDS_IN_A_DAY - 1);
		
		millisSinceMidnight = new MillisecondsSinceMidnight(millis);
		assertTrue(millisSinceMidnight.isValid());
		
		assertTrue(millisSinceMidnight.validateMillisecondsLessThanMax(millis));
	}
	
	@Test
	public void checkMaxMillisecondsIsInvalid() {
		final Integer millis = new Integer(MillisecondsSinceMidnight.MILLISSECONDS_IN_A_DAY);
		
		millisSinceMidnight = new MillisecondsSinceMidnight(millis);
		assertFalse(millisSinceMidnight.isValid());
		
		assertFalse(millisSinceMidnight.validateMillisecondsLessThanMax(millis));
	}
	
	@Test
	public void checkMaxMillisecondsPlusOneInvalid() {
		final Integer millis = new Integer(MillisecondsSinceMidnight.MILLISSECONDS_IN_A_DAY + 1);
		
		millisSinceMidnight = new MillisecondsSinceMidnight(millis);
		assertFalse(millisSinceMidnight.isValid());
		
		assertFalse(millisSinceMidnight.validateMillisecondsLessThanMax(millis));
	}
}
