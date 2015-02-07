package org.johnm.vehicle.survey.detector;

import java.util.Calendar;
import java.util.List;

import org.johnm.vehicle.survey.Direction;

public class SingleHoseSensorVehicleDetectionEventInvalid implements VehicleDetectionEvent {
	private final boolean isValid = false;
	
	public boolean isValid() {
		return isValid;
	}
	
	public List<SensorEvent> getSensorEventsInTimeOrder() {
		return null;
	}
	
	public Calendar getVehicleDetectionEventDate() {
		return null;
	}
	
	public int getMillisecondsAfterMidnightOfEvent() {
		return 0;
	}
	
	public Direction getDirection() {
		return null;
	}
}
