package org.johnm.vehicle.survey.reporting;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.johnm.vehicle.survey.analysis.VehicleCountMapKey;

public class SystemOutReporter implements Reporter {

	public void report(List<Map<VehicleCountMapKey, Integer>> countAndTotals, final int numberOfDaysInSurveyData) {
		for (Map<VehicleCountMapKey, Integer> countAndTotalMap : countAndTotals) {
			
			for (Map.Entry<VehicleCountMapKey, Integer> entry : countAndTotalMap.entrySet()) {
				final VehicleCountMapKey key = entry.getKey();
				
				if (key.areYearMonthAndDayZero()) {
					final BigDecimal average = calculateAverage(entry.getValue(), numberOfDaysInSurveyData);
					System.out.println("Period in Day=" + key.getPeriodInDay() + " VehicleDirection="
							+ key.getVehicleDirection().toString() + " Total = " + entry.getValue() + " Average = " + average);
				} else {
					System.out.println(key + " Count = " + entry.getValue());
				}
			}
			
			System.out.println("============================================================================================================");
		}
	}

	BigDecimal calculateAverage(final int totalAcrossDaysForPeriod, final int numberOfDaysInSurveyData) {
		final int scale = 2;
		final BigDecimal total = new BigDecimal(totalAcrossDaysForPeriod);
		total.setScale(scale);
		final BigDecimal nbrOfDays = new BigDecimal(numberOfDaysInSurveyData);
		nbrOfDays.setScale(scale);
		
		return total.divide(nbrOfDays);
	}
}
