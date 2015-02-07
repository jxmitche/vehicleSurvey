package org.johnm.vehicle.survey;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.johnm.vehicle.survey.validation.NullParamValidator;

public class VehicleSurveyEvent {
	private NullParamValidator nullValidator = new NullParamValidator();
	private final String firstSensorId;
	private final String secondSensorId;
	private final String singleHoseSensorValidSensorId;
	private int sensorIdLength;
	private Direction firstSensorDirection;
	private Direction otherDirection;
	private Calendar startDateOfSurvey;
	
	public VehicleSurveyEvent(final String firstSensorId, final String secondSensorId, final Direction firstSensorDirection,
			final Direction otherDirection, final Calendar startDateOfSurvey) {
		
		nullValidator.checkNotNull(firstSensorId, "FirstSensorId");
		nullValidator.checkNotNull(secondSensorId, "SecondSensorId");
		nullValidator.checkNotNull(firstSensorDirection, "FirstSensorDirection");
		nullValidator.checkNotNull(otherDirection, "OtherDirection");
		nullValidator.checkNotNull(startDateOfSurvey, "StartDateOfSurvey");
		
		validateSensorIds(firstSensorId, secondSensorId);
		
		this.firstSensorId = firstSensorId;
		this.secondSensorId = secondSensorId;
		this.singleHoseSensorValidSensorId = firstSensorId;
		this.sensorIdLength = firstSensorId.length();
		this.firstSensorDirection = firstSensorDirection;
		this.otherDirection = otherDirection;
		this.startDateOfSurvey = startDateOfSurvey;
	}

	public String getFirstSensorId() {
		return firstSensorId;
	}

	public String getSecondSensorId() {
		return secondSensorId;
	}
	
	public int getSensorIdLength() {
		return sensorIdLength;
	}

	public Direction getFirstSensorDirection() {
		return firstSensorDirection;
	}

	public Direction getOtherDirection() {
		return otherDirection;
	}

	public Calendar getStartDateOfSurvey() {
		return startDateOfSurvey;
	}

	public List<String> getValidSensorIds() {
		List<String>sensorIds = new ArrayList<String>();
		
		sensorIds.add(firstSensorId);
		sensorIds.add(secondSensorId);
		
		return sensorIds;
	}
	
	public String getSingleHoseSensorValidSensorId() {
		return singleHoseSensorValidSensorId;
	}

	void validateSensorIds(final String firstSensorId1, final String secondSensorId1) {
		if (firstSensorId1.length() != secondSensorId1.length()) {
			throw new IllegalArgumentException("Sensor Ids must be of the same length");
		}
		
		if (firstSensorId1.isEmpty()) {
			throw new IllegalArgumentException("FirstSensorId must not be empty");
		}
		
		if (secondSensorId1.isEmpty()) {
			throw new IllegalArgumentException("SecondSensorId must not be empty");
		}
	}
	
}
