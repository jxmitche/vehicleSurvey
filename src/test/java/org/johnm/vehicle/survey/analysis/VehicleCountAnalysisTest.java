package org.johnm.vehicle.survey.analysis;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.johnm.vehicle.survey.Direction;
import org.johnm.vehicle.survey.detector.SensorEvent;
import org.johnm.vehicle.survey.detector.SingleHoseSensorVehicleDetectionEvent;
import org.johnm.vehicle.survey.detector.VehicleDetectionEvent;
import org.junit.Before;
import org.junit.Test;

public class VehicleCountAnalysisTest {
	private VehicleCountAnalysis analyser;
	private List<VehicleDetectionEvent> detectionEvents;
	private Calendar startDate;
	private Map<VehicleCountMapKey, Integer> countMap;
	private VehicleCountMapKey mapKey;
	private SingleHoseSensorVehicleDetectionEvent event;
	private SensorEvent firstSensorEvent;
	private SensorEvent secondSensorEvent;
	private List<String>validIds;
	
	@Before
	public void setup() {
		detectionEvents = new ArrayList<VehicleDetectionEvent>();
		startDate = Calendar.getInstance();
		
		analyser = new VehicleCountAnalysis(detectionEvents, startDate, 5);
		
		countMap = new HashMap<VehicleCountMapKey, Integer>();
		mapKey = new VehicleCountMapKey(2015, 2, 3, 4, Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT);
		
		validIds = new ArrayList<String>();
		validIds.add("A");
		firstSensorEvent = new SensorEvent("A123456", 1, validIds);
		startDate = Calendar.getInstance();
		startDate.set(2015, Calendar.JANUARY, 2);
		firstSensorEvent.setMidnight(startDate);
		secondSensorEvent = new SensorEvent("A780", 1, validIds);
		event = new SingleHoseSensorVehicleDetectionEvent(firstSensorEvent, secondSensorEvent,
				Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT);
	}
	
	@Test
	public void checkCreateCountMapKeyForVehicleDetectionEvent() {
		
		final VehicleCountMapKey createdMapKey = analyser.createCountMapKeyForVehicleDetectionEvent(109, event);
				
		final Calendar eventDate = event.getVehicleDetectionEventDate();
		
		assertEquals(eventDate.get(Calendar.YEAR), createdMapKey.getYear());
		assertEquals(eventDate.get(Calendar.MONTH), createdMapKey.getMonth());
		assertEquals(eventDate.get(Calendar.DATE), createdMapKey.getDay());
		assertEquals(event.getMillisecondsAfterMidnightOfEvent()/109, createdMapKey.getPeriodInDay());
		assertEquals(event.getDirection(), createdMapKey.getVehicleDirection());
	}
	
	@Test
	public void checkCreateTotalMapKeyForVehicleDetectionEvent() {
		
		final VehicleCountMapKey createdMapKey = analyser.createTotalMapKeyForVehicleDetectionEvent(109, event);
				
		assertEquals(0, createdMapKey.getYear());
		assertEquals(0, createdMapKey.getMonth());
		assertEquals(0, createdMapKey.getDay());
		assertEquals(event.getMillisecondsAfterMidnightOfEvent()/109, createdMapKey.getPeriodInDay());
		assertEquals(event.getDirection(), createdMapKey.getVehicleDirection());
	}
	
	@Test
	public void checkCreateMapKeyForVehicleDetectionEvent() {
		final VehicleCountMapKey createdMapKey = analyser.createCountMapKeyForVehicleDetectionEvent(111, event);
		
		int periodInDay = event.getMillisecondsAfterMidnightOfEvent() / 111;
		final VehicleCountMapKey expectedMapKey = new VehicleCountMapKey(2015, Calendar.JANUARY, 2,
				periodInDay, Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT);
		
		assertEquals(expectedMapKey, createdMapKey);
	}
	
	@Test
	public void checkIncrementMapCountForKey() {
		analyser.incrementMapCountForKey(countMap, mapKey);
		int count = countMap.get(mapKey);
		
		assertEquals(1, count);
		
		analyser.incrementMapCountForKey(countMap, mapKey);
		count = countMap.get(mapKey);
		
		assertEquals(2, count);
	}
	
	@Test
	public void checkCalculatePeriodInDay() {
		int millisecondsInPeriodPerDay = 113;
		int period = analyser.calculatePeriodInDay(millisecondsInPeriodPerDay, 1000);
		
		assertEquals(8, period);
	}
}
