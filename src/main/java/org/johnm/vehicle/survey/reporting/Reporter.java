package org.johnm.vehicle.survey.reporting;

import java.util.List;
import java.util.Map;

import org.johnm.vehicle.survey.analysis.VehicleCountMapKey;

public interface Reporter {

	public void report(final List<Map<VehicleCountMapKey, Integer>> countAndTotals);
}
