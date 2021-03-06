package org.johnm.vehicle.survey.detector;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.johnm.vehicle.survey.Direction;
import org.johnm.vehicle.survey.validation.NullParamValidator;


public class SingleHoseSensorVehicleDetectionEvent implements VehicleDetectionEvent {
	private final NullParamValidator nullValidator = new NullParamValidator();
	private final SensorEvent firstSensorEvent;
	private final SensorEvent secondSensorEvent;
	private final Direction direction;
	private final boolean isValid;
	
	public SingleHoseSensorVehicleDetectionEvent(final SensorEvent firstSensorEventParam, final SensorEvent secondSensorEventParam,
			final Direction directionParam) {
		
		nullValidator.checkNotNull(firstSensorEventParam, "FirstSensorEvent");
		nullValidator.checkNotNull(secondSensorEventParam, "SecondSensorEvent");
		nullValidator.checkNotNull(directionParam, "Direction");
		
		isValid = sensorEventsValid(firstSensorEventParam.isValid(), secondSensorEventParam.isValid())
				&& sensorIdsTheSame(firstSensorEventParam.getSensorId(), secondSensorEventParam.getSensorId());
		
		this.firstSensorEvent = firstSensorEventParam;
		this.secondSensorEvent = secondSensorEventParam;
		this.direction = directionParam;
	}
	
	public List<SensorEvent> getSensorEventsInTimeOrder() {
		final List<SensorEvent> events = new ArrayList<SensorEvent>();
		
		events.add(firstSensorEvent);
		events.add(secondSensorEvent);
		
		return events;
	}

	public SensorEvent getFirstSensorEvent() {
		return firstSensorEvent;
	}

	public SensorEvent getSecondSensorEvent() {
		return secondSensorEvent;
	}	

	public Direction getDirection() {
		return direction;
	}

	public boolean isValid() {
		return isValid;
	}

	public Calendar getVehicleDetectionEventDate() {
		return firstSensorEvent.getSensorEventDate();
	}
	
	public int getMillisecondsAfterMidnightOfEvent() {
		return firstSensorEvent.getMilliseconds();
	}
	
	boolean sensorEventsValid(final boolean firstSensorEventValid, final boolean secondSensorEventValid) {
		return firstSensorEventValid && secondSensorEventValid;
	}

	boolean sensorIdsTheSame(final String firstSensorId, final String secondSensorId) {
		return firstSensorId.equals(secondSensorId);
	}

}
