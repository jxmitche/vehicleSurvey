package org.johnm.vehicle.survey.detector;

import java.util.Calendar;
import java.util.List;

import org.johnm.vehicle.survey.Direction;

public interface VehicleDetectionEvent {
	boolean isValid();
	
	List<SensorEvent> getSensorEventsInTimeOrder();
	
	Calendar getVehicleDetectionEventDate();
	
	int getMillisecondsAfterMidnightOfEvent();
	
	Direction getDirection();
}
