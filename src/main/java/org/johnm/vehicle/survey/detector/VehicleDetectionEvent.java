package org.johnm.vehicle.survey.detector;

import java.util.Calendar;
import java.util.List;

import org.johnm.vehicle.survey.Direction;

public interface VehicleDetectionEvent {
	public boolean isValid();
	public List<SensorEvent> getSensorEventsInTimeOrder();
	public Calendar getVehicleDetectionEventDate();
	public int getMillisecondsAfterMidnightOfEvent();
	public Direction getDirection();
}
