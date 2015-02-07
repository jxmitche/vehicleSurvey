package org.johnm.vehicle.survey.client;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.johnm.vehicle.survey.Direction;
import org.johnm.vehicle.survey.VehicleSurveyEvent;
import org.johnm.vehicle.survey.analysis.VehicleCountAnalysis;
import org.johnm.vehicle.survey.analysis.VehicleCountMapKey;
import org.johnm.vehicle.survey.detector.VehicleDetectionEvent;
import org.johnm.vehicle.survey.input.ParseInputFileForVehicleDetectionEvents;
import org.johnm.vehicle.survey.input.ReadFile;
import org.johnm.vehicle.survey.reporting.Reporter;
import org.johnm.vehicle.survey.validation.NullParamValidator;

public class Setup {
	private static final String firstSensorId = "A";
	private static final String secondSensorId = "B";
	
	private VehicleSurveyEvent vehicleSurveyEvent;
	private ParseInputFileForVehicleDetectionEvents parser;
	private Calendar startDateOfSurvey;
	private List<Map<VehicleCountMapKey, Integer>> countAndTotals;
	private NullParamValidator nullValidator = new NullParamValidator();

	
	public void process(final String fileName, final String startDateOfSurveyInput) {
		nullValidator.checkNotNull(fileName, "FileName");
		nullValidator.checkNotNull(startDateOfSurveyInput, "StartDateOfSurveyInput");
		
		startDateOfSurvey = setStartDateOfSurvey(startDateOfSurveyInput);
		vehicleSurveyEvent = createVehicleSurveyEvent();
		
		final ReadFile readFile = new ReadFile(fileName);
		final List<String> lines = readFile.readFile();
		parser = new ParseInputFileForVehicleDetectionEvents(lines, vehicleSurveyEvent);
		
		parser.parseInput();
		
		final List<VehicleDetectionEvent> detectionEvents = parser.getDetectionEvents();
		final int numberOfDaysInSurveyData = parser.getNumberOfDaysOfSensorData(); 
		final VehicleCountAnalysis vehicleCountAnalysis = new VehicleCountAnalysis(detectionEvents, startDateOfSurvey, 
				numberOfDaysInSurveyData);
		 
		final List<Integer> periodsInOneDayList = new ArrayList<Integer>();
			periodsInOneDayList.add(2);
		 
		countAndTotals = vehicleCountAnalysis.analyseVehicleCounts(periodsInOneDayList);
	}
	
	public void report(final Reporter reporter) {
		reporter.report(countAndTotals);
	}
	
	VehicleSurveyEvent createVehicleSurveyEvent() {
		return new VehicleSurveyEvent(firstSensorId, secondSensorId, Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT,
				Direction.TRAFFIC_PASSES_FROM_RIGHT_TO_LEFT, startDateOfSurvey);
	}
	
	Calendar setStartDateOfSurvey(final String startDateOfSurveyString) {
		final Calendar cal = Calendar.getInstance();
		
		DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		formatter.setLenient(false);
		
		try {
			final Date startDate = formatter.parse(startDateOfSurveyString);
			cal.setTime(startDate);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Unable to parse start date of survey. Format should be yyyyMMdd");
		}
		
		return cal;
	}

}
