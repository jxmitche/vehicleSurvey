package org.johnm.vehicle.survey.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.johnm.vehicle.survey.Direction;
import org.johnm.vehicle.survey.VehicleSurveyEvent;
import org.johnm.vehicle.survey.detector.SensorEvent;
import org.johnm.vehicle.survey.detector.VehicleDetectionEvent;
import org.junit.Before;
import org.junit.Test;

public class ParseInputFileForVehicleDetectionEventsTest {
	private List<String> lines;
	private VehicleSurveyEvent vehicleSurveyEvent;
	private ParseInputFileForVehicleDetectionEvents parser;
	private Calendar startDateOfSurvey;
	
	
	@Before
	public void setup() {
		startDateOfSurvey = Calendar.getInstance();
		startDateOfSurvey.set(2015, Calendar.JANUARY, 5);
		lines = new ArrayList<String>();
		vehicleSurveyEvent = new VehicleSurveyEvent("A", "B", Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT,
				Direction.TRAFFIC_PASSES_FROM_RIGHT_TO_LEFT, startDateOfSurvey);
		parser = new ParseInputFileForVehicleDetectionEvents(lines, vehicleSurveyEvent);
	}

	@Test
	public void checkParsingProvidedTestFile() {
		setupParser("VehicleSurveyCodingChallengeSampleData.txt", "A", "B");
		parser.parseInput();
		
		commonAssertions(67296, 22372, 0);
	}
	
	@Test
	public void checkParsingFileAAAA() {
		setupParser("AAAA.txt", "A", "B");
		parser.parseInput();
		
		commonAssertions(5, 2, 1);
	}
	
	@Test
	public void checkParsingFileABAB() {
		setupParser("ABAB.txt", "A", "B");
		parser.parseInput();
	
		commonAssertions(5, 1, 1);
	}
	
	@Test
	public void checkParsingFileBABA() {
		setupParser("BABA.txt", "A", "B");
		parser.parseInput();
		
		commonAssertions(5, 1, 1);
	}

	@Test
	public void checkParsingFileBBBB() {
		setupParser("BBBB.txt", "A", "B");
		parser.parseInput();
		
		commonAssertions(5, 0, 3);
	}
	
	//Three pairs - AA BA ??
	@Test
	public void checkParsingFileAA_BA_AA() {
		setupParser("AABAAA.txt", "A", "B");
		parser.parseInput();
		
		commonAssertions(7, 2, 3);
	}
	
	@Test
	public void checkParsingFileAA_BA_AB() {
		setupParser("AABAAB.txt", "A", "B");
		parser.parseInput();
		
		commonAssertions(7, 1, 3);
	}
	
	@Test
	public void checkParsingFileAA_BABA() {
		setupParser("AABABA.txt", "A", "B");
		parser.parseInput();
		
		commonAssertions(7, 2, 1);
	}
	
	@Test
	public void checkParsingFileAA_BA_BB() {
		setupParser("AABABB.txt", "A", "B");
		parser.parseInput();
		
		commonAssertions(7, 1, 3);
	}
	
	//Three pairs - AA AB ??
	@Test
	public void checkParsingFileAA_AB_AA() {
		setupParser("AAABAA.txt", "A", "B");
		parser.parseInput();
		
		commonAssertions(7, 2, 3);
	}
	
	@Test
	public void checkParsingFileAA_ABAB() {
		setupParser("AAABAB.txt", "A", "B");
		parser.parseInput();
		
		commonAssertions(7, 2, 1);
	}
	
	@Test
	public void checkParsingFileAA_AB_BA() {
		setupParser("AAABBA.txt", "A", "B");
		parser.parseInput();
		
		commonAssertions(7, 1, 3);
	}
	
	@Test
	public void checkParsingFileAA_AB_BB() {
		setupParser("AAABBB.txt", "A", "B");
		parser.parseInput();
		
		commonAssertions(7, 1, 3);
	}
	
	//Three pairs - AA BB ??
	@Test
	public void checkParsingFileAA_BB_AA() {
		setupParser("AABBAA.txt", "A", "B");
		parser.parseInput();
		
		commonAssertions(7, 2, 3);
	}
	
	@Test
	public void checkParsingFileAA_BB_AB() {
		setupParser("AABBAB.txt", "A", "B");
		parser.parseInput();
		
		commonAssertions(7, 1, 3);
	}
	
	@Test
	public void checkParsingFileAA_BB_BA() {
		setupParser("AABBBA.txt", "A", "B");
		parser.parseInput();
		
		commonAssertions(7, 1, 3);
	}
	
	@Test
	public void checkParsingFileAA_BB_BB() {
		setupParser("AABBBB.txt", "A", "B");
		parser.parseInput();
		
		commonAssertions(7, 1, 3);
	}
	
	//Parsing BB ?? ??
	@Test
	public void checkParsingFileBB_ABAB() {
		setupParser("BBABAB.txt", "A", "B");
		parser.parseInput();
		
		commonAssertions(7, 1, 3);
	}	
		
	@Test
	public void checkDiscardPairOfSensorEvents() {
		assertFalse(parser.discardPairOfSensorEvents(true, true));
		assertFalse(parser.discardPairOfSensorEvents(true, false));
		assertFalse(parser.discardPairOfSensorEvents(false, true));
		assertTrue(parser.discardPairOfSensorEvents(false, false));
	}
	
	@Test
	public void checkCalculateDaysBetweenDates0() {
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(2015, Calendar.JANUARY, 5);
		
		int result = parser.calculateDaysBetweenDates(startDateOfSurvey, lastDate);
		
		assertEquals(0, result);
	}
	
	@Test
	public void checkCalculateDaysBetweenDates1() {
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(2015, Calendar.JANUARY, 6);
		
		int result = parser.calculateDaysBetweenDates(startDateOfSurvey, lastDate);
		
		assertEquals(1, result);
	}
	
	@Test
	public void checkCalculateDaysBetweenDates5() {
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(2015, Calendar.JANUARY, 10);
		
		int result = parser.calculateDaysBetweenDates(startDateOfSurvey, lastDate);
		
		assertEquals(5, result);
	}
	
	@Test
	public void checkCalculateDaysBetweenDates28acrossMonths() {
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(2015, Calendar.FEBRUARY, 2);
		
		int result = parser.calculateDaysBetweenDates(startDateOfSurvey, lastDate);
		
		assertEquals(28, result);
	}
	
	@Test
	public void checkCalculateDaysBetweenDates5acrossYears() {
		startDateOfSurvey.set(2014, Calendar.DECEMBER, 29);
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(2015, Calendar.JANUARY, 3);
		
		int result = parser.calculateDaysBetweenDates(startDateOfSurvey, lastDate);
		
		assertEquals(5, result);
	}
	
	private void setupParser(final String fileName, final String firstSensorId, final String secondSensorId) {
		final URL url = ReadFileTest.class.getClassLoader().getResource(fileName);
		final ReadFile readFile = new ReadFile(url.getFile());
		lines = readFile.readFile();
		
		vehicleSurveyEvent = new VehicleSurveyEvent(firstSensorId, secondSensorId, Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT,
				Direction.TRAFFIC_PASSES_FROM_RIGHT_TO_LEFT, Calendar.getInstance());
		
		parser = new ParseInputFileForVehicleDetectionEvents(lines, vehicleSurveyEvent);
	}
	
	private void commonAssertions(final int linesSize, final int detectionEventsSize, final int discardedEventsSize) {
		final List<VehicleDetectionEvent> detectionEvents = parser.getDetectionEvents();
		final List<SensorEvent> discardedDetectionEvents = parser.getDiscardedDetectionEvents();
		
		assertNotNull(detectionEvents);
		assertNotNull(discardedDetectionEvents);
		
		assertEquals(linesSize, lines.size());
		assertEquals(detectionEventsSize, detectionEvents.size());
		assertEquals(discardedEventsSize, discardedDetectionEvents.size());
	}

}