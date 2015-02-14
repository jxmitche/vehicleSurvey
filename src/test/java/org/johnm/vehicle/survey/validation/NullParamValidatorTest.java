package org.johnm.vehicle.survey.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class NullParamValidatorTest {
	private NullParamValidator validator;
	
	@Before
	public void setup() {
		validator = new NullParamValidator();
	}
	
	@Test
	public void checkNullObject() {
		final Object param = null;
		final String fieldName = "Obj";
		
		try {
			validator.checkNotNull(param, fieldName);
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals(fieldName + NullParamValidator.MSG, ex.getMessage());
		}
	}
}
