package org.johnm.vehicle.survey.reporting;

import java.util.List;
import java.util.Map;

import org.johnm.vehicle.survey.analysis.VehicleCountMapKey;

public class SystemOutReporter implements Reporter {

	public void report(List<Map<VehicleCountMapKey, Integer>> countAndTotals) {
		for (Map<VehicleCountMapKey, Integer> countAndTotalMap : countAndTotals) {
			
			for (Map.Entry<VehicleCountMapKey, Integer> entry : countAndTotalMap.entrySet()) {
				VehicleCountMapKey key = entry.getKey();
				
				if (key.areYearMonthAndDayZero()) {
					System.out.println(entry.getKey() + " Total = " + entry.getValue());
				} else {
					System.out.println(entry.getKey() + " Count = " + entry.getValue());
				}
			}
		}
	}

}
