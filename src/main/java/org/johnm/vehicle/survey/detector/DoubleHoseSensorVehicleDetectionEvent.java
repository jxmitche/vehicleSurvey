package org.johnm.vehicle.survey.detector;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.johnm.vehicle.survey.Direction;



public class DoubleHoseSensorVehicleDetectionEvent implements VehicleDetectionEvent {
	private SingleHoseSensorVehicleDetectionEvent firstSingleHoseSensorEvent;
	private SingleHoseSensorVehicleDetectionEvent secondSingleHoseSensorEvent;
	private boolean isValid;
	
	public DoubleHoseSensorVehicleDetectionEvent(final SensorEvent firstSensorEvent, final SensorEvent secondSensorEvent,
			final SensorEvent thirdSensorEvent, final SensorEvent fourthSensorEvent,
			final Direction direction) {

		firstSingleHoseSensorEvent = new SingleHoseSensorVehicleDetectionEvent(firstSensorEvent, thirdSensorEvent, direction);
		secondSingleHoseSensorEvent = new SingleHoseSensorVehicleDetectionEvent(secondSensorEvent, fourthSensorEvent, direction);
		
		isValid = checkSensorEventsValid(firstSensorEvent.isValid(), secondSensorEvent.isValid(), thirdSensorEvent.isValid(),
				fourthSensorEvent.isValid()
				&& checkFirstAndThirdSensorEventsHaveSameId(firstSensorEvent.getSensorId(), thirdSensorEvent.getSensorId()) 
				&& checkSecondAndFourthSensorEventsHaveSameId(secondSensorEvent.getSensorId(), fourthSensorEvent.getSensorId())
				&& checkFirstAndSecondSensorEventsHaveDifferentId(firstSensorEvent.getSensorId(), secondSensorEvent.getSensorId())); 
	}
	
	public List<SensorEvent> getSensorEventsInTimeOrder() {
		
		final List<SensorEvent> events = new ArrayList<SensorEvent>();
		final List<SensorEvent> firstSensorEvents = firstSingleHoseSensorEvent.getSensorEventsInTimeOrder();
		final List<SensorEvent> secondSensorEvents = secondSingleHoseSensorEvent.getSensorEventsInTimeOrder();
		
		events.add(firstSensorEvents.get(0));
		events.add(secondSensorEvents.get(0));
		events.add(firstSensorEvents.get(1));
		events.add(secondSensorEvents.get(1));
		
		return events;
	}

	public SingleHoseSensorVehicleDetectionEvent getFirstSingleHoseSensorEvent() {
		return firstSingleHoseSensorEvent;
	}

	public SingleHoseSensorVehicleDetectionEvent getSecondSingleHoseSensorEvent() {
		return secondSingleHoseSensorEvent;
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	public Calendar getVehicleDetectionEventDate() {
		return firstSingleHoseSensorEvent.getVehicleDetectionEventDate();
	}
	
	public int getMillisecondsAfterMidnightOfEvent() {
		return firstSingleHoseSensorEvent.getMillisecondsAfterMidnightOfEvent();
	}
	
	public Direction getDirection() {
		return firstSingleHoseSensorEvent.getDirection();
	}

	boolean checkSensorEventsValid(final boolean firstSensorEventIsValid, final boolean secondSensorEventIsValid,
			final boolean thirdSensorEventIsValid, final boolean fourthSensorEventIsValid) {
		
		return firstSensorEventIsValid && secondSensorEventIsValid &&
				thirdSensorEventIsValid && fourthSensorEventIsValid;
	}
	
	boolean checkFirstAndThirdSensorEventsHaveSameId(final String firstSensorId, final String thirdSensorId) {
		return firstSensorId.equals(thirdSensorId);
	}
	
	boolean checkSecondAndFourthSensorEventsHaveSameId(final String secondSensorId, final String fourthSensorId) {
		return secondSensorId.equals(fourthSensorId);
	}
	
	boolean checkFirstAndSecondSensorEventsHaveDifferentId(final String firstSensorId, final String secondSensorId) {
		return !firstSensorId.equals(secondSensorId);
	}


}
