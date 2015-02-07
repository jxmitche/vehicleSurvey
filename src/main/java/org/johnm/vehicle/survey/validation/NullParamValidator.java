package org.johnm.vehicle.survey.validation;

public class NullParamValidator {
	final static String MSG = " must not be null";
	
	public void checkNotNull(final Object param, final String fieldName) {
		if (param == null) {
			throw new IllegalArgumentException(fieldName + MSG);
		}
	}
}
