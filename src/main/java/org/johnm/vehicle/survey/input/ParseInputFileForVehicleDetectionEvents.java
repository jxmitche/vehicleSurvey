package org.johnm.vehicle.survey.input;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.johnm.vehicle.survey.Direction;
import org.johnm.vehicle.survey.VehicleSurveyEvent;
import org.johnm.vehicle.survey.detector.DoubleHoseSensorVehicleDetectionEvent;
import org.johnm.vehicle.survey.detector.DoubleHoseSensorVehicleDetectionEventInvalid;
import org.johnm.vehicle.survey.detector.SensorEvent;
import org.johnm.vehicle.survey.detector.SingleHoseSensorVehicleDetectionEvent;
import org.johnm.vehicle.survey.detector.SingleHoseSensorVehicleDetectionEventInvalid;
import org.johnm.vehicle.survey.detector.VehicleDetectionEvent;
import org.johnm.vehicle.survey.validation.NullParamValidator;

public class ParseInputFileForVehicleDetectionEvents {
	private final NullParamValidator nullValidator = new NullParamValidator();
	private final List<String> inputLines;
	private final VehicleSurveyEvent vehicleSurveyEvent;
	private final List<VehicleDetectionEvent> detectionEvents = new ArrayList<VehicleDetectionEvent>();
	private final List<SensorEvent> discardedDetectionEvents = new ArrayList<SensorEvent>();
	
	
	public ParseInputFileForVehicleDetectionEvents(final List<String> lines, final VehicleSurveyEvent vehicleSurveyEvent) {
		nullValidator.checkNotNull(lines, "Lines");
		nullValidator.checkNotNull(vehicleSurveyEvent, "VehicleSurveyEvent");
		
		inputLines = lines;
		this.vehicleSurveyEvent = vehicleSurveyEvent;
	}
	
	public void parseInput() {
		final List<String>twoInputLines = new ArrayList<String>();
		final List<String>validSensorIds = vehicleSurveyEvent.getValidSensorIds();
		final int sensorIdLength = vehicleSurveyEvent.getSensorIdLength();
		boolean onePairOnly = true;
		SensorEvent firstSensorEvent = null;
		SensorEvent secondSensorEvent = null;
		
		for (String inputLine : inputLines) {
			if (twoInputLines.size() < 2) {
				twoInputLines.add(inputLine);
			} else {
				if (onePairOnly) {
					firstSensorEvent = new SensorEvent(twoInputLines.get(0), sensorIdLength, validSensorIds);
					secondSensorEvent = new SensorEvent(twoInputLines.get(1), sensorIdLength, validSensorIds);
					onePairOnly = processTwoSensorEventsSuccessfully(firstSensorEvent, secondSensorEvent);
				} else {
					final SensorEvent thirdSensorEvent = new SensorEvent(twoInputLines.get(0), sensorIdLength, validSensorIds);
					final SensorEvent fourthSensorEvent = new SensorEvent(twoInputLines.get(1), sensorIdLength, validSensorIds);
				
					boolean secondPairLeft = 
							processFourSensorEvents(firstSensorEvent, secondSensorEvent, thirdSensorEvent, fourthSensorEvent);
					
					if (secondPairLeft) {
						firstSensorEvent = thirdSensorEvent;
						secondSensorEvent = fourthSensorEvent;
						onePairOnly = false;
					} else {
						onePairOnly = true;
					}
				}
				
				twoInputLines.clear();
				twoInputLines.add(inputLine);
			}
		}
		
		processLeftOverLines(twoInputLines);
		
		addDateToSensorEvents(vehicleSurveyEvent.getStartDateOfSurvey(), detectionEvents);
	}

	public List<VehicleDetectionEvent> getDetectionEvents() {
		return detectionEvents;
	}

	public List<SensorEvent> getDiscardedDetectionEvents() {
		return discardedDetectionEvents;
	}
	
	public int getNumberOfDaysOfSensorData() {
		if (!detectionEvents.isEmpty()) {
			int nbrOfEvents = detectionEvents.size();
			VehicleDetectionEvent lastVehicleDetectionEvent = detectionEvents.get(nbrOfEvents -1);
			List<SensorEvent> sensorEvents = lastVehicleDetectionEvent.getSensorEventsInTimeOrder();
			
			final SensorEvent lastSensorEvent = sensorEvents.get(sensorEvents.size() -1);
			final Calendar lastDate = lastSensorEvent.getMidnight();
			
			final int daysBetween = calculateDaysBetweenDates(vehicleSurveyEvent.getStartDateOfSurvey(), lastDate);
			final int startDayPlusEndDay = 2;
			final int daysOfSensorData = daysBetween + startDayPlusEndDay;
			
			return daysOfSensorData;
		}
		
		return 0;
	}
	
	boolean processTwoSensorEventsSuccessfully(final SensorEvent firstSensorEvent, final SensorEvent secondSensorEvent) {
		final VehicleDetectionEvent firstPairSingleHoseSensorEvent = createSingleHoseSensorEvent(firstSensorEvent,
				secondSensorEvent, vehicleSurveyEvent.getFirstSensorDirection(), vehicleSurveyEvent.getSingleHoseSensorValidSensorId());
		
		if (firstPairSingleHoseSensorEvent.isValid()) {
			detectionEvents.add(firstPairSingleHoseSensorEvent);
		}
		
		return firstPairSingleHoseSensorEvent.isValid();
	}

	boolean processFourSensorEvents(final SensorEvent firstSensorEvent, final SensorEvent secondSensorEvent,
			final SensorEvent thirdSensorEvent, final SensorEvent fourthSensorEvent) {
		
		boolean secondPairLeft = false;
		
		final VehicleDetectionEvent firstPairSingleHoseSensorEvent = createSingleHoseSensorEvent(firstSensorEvent,
				secondSensorEvent, vehicleSurveyEvent.getFirstSensorDirection(), vehicleSurveyEvent.getSingleHoseSensorValidSensorId());
		
		if (firstPairSingleHoseSensorEvent.isValid()) {
			detectionEvents.add(firstPairSingleHoseSensorEvent);
		}

		final VehicleDetectionEvent secondPairSingleHoseSensorEvent = createSingleHoseSensorEvent(thirdSensorEvent,
				fourthSensorEvent, vehicleSurveyEvent.getFirstSensorDirection(), vehicleSurveyEvent.getSingleHoseSensorValidSensorId());
		
		if (secondPairSingleHoseSensorEvent.isValid()) {
			detectionEvents.add(secondPairSingleHoseSensorEvent);
		}
		
		final VehicleDetectionEvent doubleHoseSensorEvent = createDoubleHoseSensorEvent(firstSensorEvent, secondSensorEvent,
				thirdSensorEvent, fourthSensorEvent);
		
		if (doubleHoseSensorEvent.isValid()) {
			detectionEvents.add(doubleHoseSensorEvent);
		}
		
		if (discardPairOfSensorEvents(firstPairSingleHoseSensorEvent.isValid(), doubleHoseSensorEvent.isValid())) {
			discardedDetectionEvents.add(firstSensorEvent);
			discardedDetectionEvents.add(secondSensorEvent);
			secondPairLeft = true;
		}
		
		return secondPairLeft;
 	}
	
	void processLeftOverLines(final List<String>twoInputLines) {
		if (!twoInputLines.isEmpty()) {
			if (twoInputLines.size() == 1) {
				final SensorEvent firstSensorEvent = new SensorEvent(twoInputLines.get(0), vehicleSurveyEvent.getSensorIdLength(),
						vehicleSurveyEvent.getValidSensorIds());
				discardedDetectionEvents.add(firstSensorEvent);
			} else {
				final SensorEvent firstSensorEvent = new SensorEvent(twoInputLines.get(0), vehicleSurveyEvent.getSensorIdLength(),
						vehicleSurveyEvent.getValidSensorIds());
				final SensorEvent secondSensorEvent = new SensorEvent(twoInputLines.get(1), vehicleSurveyEvent.getSensorIdLength(),
						vehicleSurveyEvent.getValidSensorIds());
				final boolean isValid = processTwoSensorEventsSuccessfully(firstSensorEvent, secondSensorEvent);
				
				if (!isValid) {
					discardedDetectionEvents.add(firstSensorEvent);
					discardedDetectionEvents.add(secondSensorEvent);
				}
			}
		}
	}
	
	boolean discardPairOfSensorEvents(final boolean pairSingleHoseSensorEventIsValid, final boolean doubleHoseSensorEventIsValid) {
		return !pairSingleHoseSensorEventIsValid && !doubleHoseSensorEventIsValid;
	}

	VehicleDetectionEvent createSingleHoseSensorEvent(final SensorEvent firstSensorEvent, 
			final SensorEvent secondSensorEvent, final Direction firstSensorDirection, final String validSensorId) {
		
		if (firstSensorEvent.getSensorId().equals(validSensorId)) {
			return new SingleHoseSensorVehicleDetectionEvent(firstSensorEvent, secondSensorEvent, firstSensorDirection);
		}

		return new SingleHoseSensorVehicleDetectionEventInvalid();
	}
	
	VehicleDetectionEvent createDoubleHoseSensorEvent(final SensorEvent firstSensorEvent,
			final SensorEvent secondSensorEvent, final SensorEvent thirdSensorEvent, final SensorEvent fourthSensorEvent) {
		
		if (vehicleSurveyEvent.getFirstSensorId().equals(firstSensorEvent.getSensorId()) &&
				vehicleSurveyEvent.getSecondSensorId().equals(secondSensorEvent.getSensorId()) &&
				vehicleSurveyEvent.getFirstSensorId().equals(thirdSensorEvent.getSensorId()) &&
				vehicleSurveyEvent.getSecondSensorId().equals(fourthSensorEvent.getSensorId())) {
			
			return new DoubleHoseSensorVehicleDetectionEvent(firstSensorEvent, secondSensorEvent, thirdSensorEvent, fourthSensorEvent,
					vehicleSurveyEvent.getOtherDirection());
		}
		
		if (vehicleSurveyEvent.getSecondSensorId().equals(firstSensorEvent.getSensorId()) &&
				vehicleSurveyEvent.getFirstSensorId().equals(secondSensorEvent.getSensorId()) &&
				vehicleSurveyEvent.getSecondSensorId().equals(thirdSensorEvent.getSensorId()) &&
				vehicleSurveyEvent.getFirstSensorId().equals(fourthSensorEvent.getSensorId())) {
			
			return new DoubleHoseSensorVehicleDetectionEvent(firstSensorEvent, secondSensorEvent, thirdSensorEvent, fourthSensorEvent,
					vehicleSurveyEvent.getFirstSensorDirection());
		}
		
		return new DoubleHoseSensorVehicleDetectionEventInvalid();
	}

	void addDateToSensorEvents(final Calendar startDate, final List<VehicleDetectionEvent> vehicleDetectionEvents) {
		final ParseVehicleDetectionEventsAddingDate dateParser = new ParseVehicleDetectionEventsAddingDate(vehicleDetectionEvents, 
				startDate);
		
		dateParser.parseDetectionEventsAddingDate();
	}
	
	int calculateDaysBetweenDates(final Calendar startDateOfSurvey, final Calendar lastDate) {
		int days = 0;
		
		final Calendar lastDateAtMidnight = Calendar.getInstance();
		lastDateAtMidnight.setTimeInMillis(lastDate.getTimeInMillis());
		setDateToMidnight(lastDateAtMidnight);
		
		final Calendar currentDate = Calendar.getInstance();
		currentDate.setTimeInMillis(startDateOfSurvey.getTimeInMillis());
		setDateToMidnight(currentDate);
		
		while (currentDate.before(lastDate)) {
			currentDate.add(Calendar.DATE, 1);
			days = days + 1;
		}
		
		return days - 1;
	}
	
	void setDateToMidnight(final Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
	}
}
