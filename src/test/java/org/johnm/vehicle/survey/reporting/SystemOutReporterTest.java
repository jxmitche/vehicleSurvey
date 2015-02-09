package org.johnm.vehicle.survey.reporting;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

public class SystemOutReporterTest {
	private SystemOutReporter reporter;
	
	@Before
	public void setup() {
		reporter = new SystemOutReporter();
	}
	
	@Test
	public void checkAvg() {
		BigDecimal result = reporter.calculateAverage(10, 2);
		assertEquals("5", result.toPlainString());
		
		result = reporter.calculateAverage(10, 4);
		assertEquals("2.5", result.toPlainString());
	}
}
