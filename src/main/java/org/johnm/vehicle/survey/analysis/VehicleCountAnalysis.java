package org.johnm.vehicle.survey.analysis;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.johnm.vehicle.survey.Direction;
import org.johnm.vehicle.survey.detector.MillisecondsSinceMidnight;
import org.johnm.vehicle.survey.detector.VehicleDetectionEvent;
import org.johnm.vehicle.survey.validation.NullParamValidator;

public class VehicleCountAnalysis {
	private final NullParamValidator nullValidator = new NullParamValidator();
	private final List<VehicleDetectionEvent> detectionEvents;
	private final Calendar startDate;
	private final int numberOfDays;
	
	public VehicleCountAnalysis(final List<VehicleDetectionEvent> detectionEventsParam, final Calendar startDateParam,
			final int numberOfDaysParam) {
		
		nullValidator.checkNotNull(detectionEventsParam, "DetectionEvents");
		nullValidator.checkNotNull(startDateParam, "StartDate");
		
		this.detectionEvents = detectionEventsParam;
		this.startDate = startDateParam;
		this.numberOfDays = numberOfDaysParam;
	}
	
	public List<Map<VehicleCountMapKey, Integer>> analyseVehicleCounts(final List<Integer> periodsInOneDayList) {
		final List< Map<VehicleCountMapKey, Integer>> vehicleCountsList = new ArrayList< Map<VehicleCountMapKey, Integer>>();
		
		for (Integer periodsInOneDay : periodsInOneDayList) {
			final  Map<VehicleCountMapKey, Integer> countAnalysis = analyseVehicleCountsAcrossTime(periodsInOneDay);
			
			vehicleCountsList.add(countAnalysis);
		}
		
		return vehicleCountsList;
	}
	
	 Map<VehicleCountMapKey, Integer> analyseVehicleCountsAcrossTime(final int periodsInOneDay) {
		final int millisecondsInPeriodPerDay = MillisecondsSinceMidnight.MILLISSECONDS_IN_A_DAY / periodsInOneDay;
		
		final Map<VehicleCountMapKey, Integer> countAndTotalMap = initialiseCountMap(periodsInOneDay);
		
		for (VehicleDetectionEvent vehicleDetectionEvent : detectionEvents)	{
			final VehicleCountMapKey countMapKey = createCountMapKeyForVehicleDetectionEvent(millisecondsInPeriodPerDay,
					vehicleDetectionEvent);
			incrementMapCountForKey(countAndTotalMap, countMapKey);
			
			final VehicleCountMapKey totalMapKey = createTotalMapKeyForVehicleDetectionEvent(millisecondsInPeriodPerDay,
					vehicleDetectionEvent);
			incrementMapCountForKey(countAndTotalMap, totalMapKey);
		}
		
		addPeakVolumePeriod(countAndTotalMap, periodsInOneDay);
		
		return countAndTotalMap;
	}
	
	void addPeakVolumePeriod(final Map<VehicleCountMapKey, Integer> countAndTotalMap, final int periodsInOneDay) {
		int currentMaxVolumeInPeriod = 0;
		int currentMaxPeriod = 0;
		
		for (int i=1; i<=periodsInOneDay; i++) {
			VehicleCountMapKey key = new VehicleCountMapKey(0, 0, 0, i, Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT);
			final int totalLtoR = countAndTotalMap.get(key);
			
			key = new VehicleCountMapKey(0, 0, 0, i, Direction.TRAFFIC_PASSES_FROM_RIGHT_TO_LEFT);
			final int totalRtoL = countAndTotalMap.get(key);
			
			final int volume = totalLtoR + totalRtoL;
			
			if (volume > currentMaxVolumeInPeriod) {
				currentMaxVolumeInPeriod = volume;
				currentMaxPeriod = i;
			}
		}
		
		final VehicleCountMapKey key = new VehicleCountMapKey(0, 0, 0, currentMaxPeriod, null);
		countAndTotalMap.put(key, currentMaxVolumeInPeriod);
	}

	Map<VehicleCountMapKey, Integer> initialiseCountMap(final int periodsInOneDay) {
		final Map<VehicleCountMapKey, Integer> countMap = new LinkedHashMap<VehicleCountMapKey, Integer>();
		final Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(startDate.getTime());
		
		for (int i=0; i<numberOfDays; i++) {
			initialiseCountForPeriodsInDate(periodsInOneDay, countMap, currentDate);
			currentDate.add(Calendar.DATE, 1);
		}
		
		return countMap;
	}
	
	void initialiseCountForPeriodsInDate(final int periodsInOneDay, final Map<VehicleCountMapKey, Integer> countMap,
			final Calendar currentDate) {
		
		for (int periodInDay=1; periodInDay<=periodsInOneDay;periodInDay++) {
			initaliseCountForEachDirectionForDateAndPeriod(countMap, currentDate, periodInDay);
		}
	}

	void initaliseCountForEachDirectionForDateAndPeriod(final Map<VehicleCountMapKey, Integer> countMap, final Calendar currentDate,
			final int periodInDay) {
		
		final VehicleCountMapKey mapKeyLtoR = new VehicleCountMapKey(currentDate.get(Calendar.YEAR),
				currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE),
				periodInDay, Direction.TRAFFIC_PASSES_FROM_LEFT_TO_RIGHT);
		
		countMap.put(mapKeyLtoR, 0);
		
		final VehicleCountMapKey mapKeyRtoL = new VehicleCountMapKey(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
				currentDate.get(Calendar.DATE), periodInDay, Direction.TRAFFIC_PASSES_FROM_RIGHT_TO_LEFT);
		
		countMap.put(mapKeyRtoL, 0);
	}

	VehicleCountMapKey createCountMapKeyForVehicleDetectionEvent(final int millisecondsInPeriodPerDay, 
			final VehicleDetectionEvent vehicleDetectionEvent) {
		
		final Calendar eventDate = vehicleDetectionEvent.getVehicleDetectionEventDate();
		final Direction direction = vehicleDetectionEvent.getDirection();
		final int millis = vehicleDetectionEvent.getMillisecondsAfterMidnightOfEvent();
		final int periodInDay = calculatePeriodInDay(millisecondsInPeriodPerDay, millis);
		
		final VehicleCountMapKey mapKey = new VehicleCountMapKey(eventDate.get(Calendar.YEAR), eventDate.get(Calendar.MONTH),
				eventDate.get(Calendar.DATE), periodInDay, direction);
		
		return mapKey;
	}
	
	VehicleCountMapKey createTotalMapKeyForVehicleDetectionEvent(final int millisecondsInPeriodPerDay, 
			final VehicleDetectionEvent vehicleDetectionEvent) {
		
		final Direction direction = vehicleDetectionEvent.getDirection();
		final int millis = vehicleDetectionEvent.getMillisecondsAfterMidnightOfEvent();
		final int periodInDay = calculatePeriodInDay(millisecondsInPeriodPerDay, millis);
		
		final VehicleCountMapKey mapKey = new VehicleCountMapKey(0, 0, 0, periodInDay, direction);
		
		return mapKey;
	}

	void incrementMapCountForKey(final Map<VehicleCountMapKey, Integer> countMap, final VehicleCountMapKey mapKey) {
		int count = 1;
				
		if (countMap.containsKey(mapKey)) {
			count = countMap.get(mapKey);
			count = count + 1;
		}
		
		countMap.put(mapKey, count);
	}
	
	int calculatePeriodInDay(final int millisecondsInPeriodPerDay, final int millis) {
		return millis / millisecondsInPeriodPerDay + 1;
	}

}
