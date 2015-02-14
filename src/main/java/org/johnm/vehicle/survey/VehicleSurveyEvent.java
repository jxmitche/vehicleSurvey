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
	
	public VehicleSurveyEvent(final String firstSensorIdParam, final String secondSensorIdParam, final Direction firstSensorDirectionParam,
			final Direction otherDirectionParam, final Calendar startDateOfSurveyParam) {
		
		nullValidator.checkNotNull(firstSensorIdParam, "FirstSensorId");
		nullValidator.checkNotNull(secondSensorIdParam, "SecondSensorId");
		nullValidator.checkNotNull(firstSensorDirectionParam, "FirstSensorDirection");
		nullValidator.checkNotNull(otherDirectionParam, "OtherDirection");
		nullValidator.checkNotNull(startDateOfSurveyParam, "StartDateOfSurvey");
		
		validateSensorIds(firstSensorIdParam, secondSensorIdParam);
		
		this.firstSensorId = firstSensorIdParam;
		this.secondSensorId = secondSensorIdParam;
		this.singleHoseSensorValidSensorId = firstSensorIdParam;
		this.sensorIdLength = firstSensorIdParam.length();
		this.firstSensorDirection = firstSensorDirectionParam;
		this.otherDirection = otherDirectionParam;
		this.startDateOfSurvey = startDateOfSurveyParam;
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
		final List<String>sensorIds = new ArrayList<String>();
		
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
