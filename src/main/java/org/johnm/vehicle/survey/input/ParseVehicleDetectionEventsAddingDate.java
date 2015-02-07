package org.johnm.vehicle.survey.input;

import java.util.Calendar;
import java.util.List;

import org.johnm.vehicle.survey.detector.SensorEvent;
import org.johnm.vehicle.survey.detector.VehicleDetectionEvent;
import org.johnm.vehicle.survey.validation.NullParamValidator;

public class ParseVehicleDetectionEventsAddingDate {
	private final NullParamValidator nullValidator = new NullParamValidator();
	private final List<VehicleDetectionEvent> detectionEvents;
	private final Calendar startDate;
	
	public ParseVehicleDetectionEventsAddingDate(final List<VehicleDetectionEvent> detectionEvents, final Calendar startDate) {
		nullValidator.checkNotNull(detectionEvents, "DetectionEvents");
		nullValidator.checkNotNull(startDate, "StartDate");
		
		this.detectionEvents = detectionEvents;
		
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(startDate.getTimeInMillis());
		this.startDate = cal;
	}
	
	public void parseDetectionEventsAddingDate() {
		addDateToSensorEvents(startDate, detectionEvents);
	}
	
	void addDateToSensorEvents(final Calendar startDatePassed, final List<VehicleDetectionEvent> vehicleDetectionEvents) {
		final Calendar currentDate = Calendar.getInstance(); 
		currentDate.setTime(startDatePassed.getTime());
		int currentMilliseconds = 0;
		
		for (VehicleDetectionEvent vehicleDetectionEvent : vehicleDetectionEvents) {
			final List<SensorEvent> sensorEvents = vehicleDetectionEvent.getSensorEventsInTimeOrder();
			
			currentMilliseconds = processUpToFourSensorEventsAddingDate(currentDate, currentMilliseconds, sensorEvents);
		}
	}

	int processUpToFourSensorEventsAddingDate(final Calendar currentDate, final int currentMillisecondsPassed,
			final List<SensorEvent> sensorEvents) {
		
		int currentMilliseconds = currentMillisecondsPassed;
		
		for (SensorEvent sensorEvent : sensorEvents) {
			currentMilliseconds = addDateToOneSensorEvent(currentDate, currentMilliseconds, sensorEvent);
		}
		
		return currentMilliseconds;
	}

	int addDateToOneSensorEvent(final Calendar currentDate, final int currentMilliseconds, final SensorEvent sensorEvent) {
		int nextMilliseconds = sensorEvent.getMilliseconds();
		
		if (nextMilliseconds > currentMilliseconds) {
			sensorEvent.setMidnight(currentDate);
		} else {
			currentDate.add(Calendar.DATE, 1);
			sensorEvent.setMidnight(currentDate);
		}
		
		return nextMilliseconds;
	}
}
