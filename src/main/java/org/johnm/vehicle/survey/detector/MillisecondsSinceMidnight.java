package org.johnm.vehicle.survey.detector;

import org.johnm.vehicle.survey.validation.NullParamValidator;


public class MillisecondsSinceMidnight {
	public static final int MILLISSECONDS_IN_A_DAY = 1000 * 60 * 60 * 24;
	
	private NullParamValidator nullValidator = new NullParamValidator();
	private int millisecondsSinceMidnight;
	private boolean isValid;
	
	public MillisecondsSinceMidnight(final Integer milliseconds) {
		nullValidator.checkNotNull(milliseconds, "Milliseconds");
		
		isValid = validateMillisecondsPositiveOrZero(milliseconds) && validateMillisecondsLessThanMax(milliseconds);
			
		millisecondsSinceMidnight = milliseconds;
	}
	
	public int getMillisecondsSinceMidnight() {
		return millisecondsSinceMidnight;
	}

	public boolean isValid() {
		return isValid;
	}

	boolean validateMillisecondsPositiveOrZero(final int milliseconds) {
		return milliseconds >= 0;
	}
	
	boolean validateMillisecondsLessThanMax(final int milliseconds) {
		return milliseconds < MILLISSECONDS_IN_A_DAY;
	}
}
