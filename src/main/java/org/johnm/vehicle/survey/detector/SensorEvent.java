package org.johnm.vehicle.survey.detector;

import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import org.johnm.vehicle.survey.validation.NullParamValidator;

public class SensorEvent {
	private final NullParamValidator nullValidator = new NullParamValidator();
	private String sensorId;
	private MillisecondsSinceMidnight millisecondsSinceMidnight;
	private Calendar midnight;
	private boolean isValid = true;

	public SensorEvent(final String input, final int sensorIdLength, final List<String>validIds) {
		nullValidator.checkNotNull(input, "Input");
		nullValidator.checkNotNull(validIds, "ValidIds");
		
		validateSensorIdLength(sensorIdLength);
		validateValidIds(validIds);
		
		isValid = validateInputLength(input);
		
		if (isValid) {
			sensorId = input.substring(0, sensorIdLength);
			isValid = validateSensorId(validIds);
			
			if (isValid) {
				final String milliseconds = input.substring(1, input.length());
				isValid = validateInputOnlyDigits(milliseconds);
				
				if (isValid) {
					createMillisecondsSinceMidnight(milliseconds);
					isValid = millisecondsSinceMidnight.isValid();
				}
			}
		}
	}
	
	public Calendar getSensorEventDate() {
		long millis = midnight.getTimeInMillis();
		millis = millis + millisecondsSinceMidnight.getMillisecondsSinceMidnight();
		final Calendar sensorEventDate = Calendar.getInstance();
		sensorEventDate.setTimeInMillis(millis);
		
		return sensorEventDate;
	}

	public boolean isValid() {
		return isValid;
	}

	public String getSensorId() {
		return sensorId;
	}
	
	public int getMilliseconds() {
		return millisecondsSinceMidnight.getMillisecondsSinceMidnight();
	}
	
	public void setMidnight(Calendar midnight) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(midnight.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		this.midnight = cal;
	}

	public Calendar getMidnight() {
		return midnight;
	}

	void validateSensorIdLength(final int sensorIdLength) {
		if (sensorIdLength < 1) {
			throw new IllegalArgumentException("SensorIdLength must not be less than one");
		}
	}

	void validateValidIds(final List<String>validIds) {
		if (validIds.isEmpty()) {
			throw new IllegalArgumentException("ValidIds must not be empty");
		}
	}
	
	boolean validateInputLength(final String input) {
		if (input.length() < 2) {
			return false;
		} 

		return true;
	}

	boolean validateSensorId(final List<String>validIds) {
		boolean valid = false;
		
		for (String validSensorId : validIds) {
			if (validSensorId.equals(sensorId)) {
				valid = true;
				break;
			}
		}
		
		return valid;
	}
	
	boolean validateInputOnlyDigits(final String milliseconds) {
		return !Pattern.matches(".*[^0-9].*", milliseconds);
	}
	
	void createMillisecondsSinceMidnight(final String milliseconds) {
		final Integer millis = Integer.valueOf(milliseconds);
		millisecondsSinceMidnight = new MillisecondsSinceMidnight(millis);			
	}

}
