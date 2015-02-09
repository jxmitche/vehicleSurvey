package org.johnm.vehicle.survey.client;

import org.junit.Test;

public class RunMeReadMeTest {
	private String fileName = "VehicleSurveyCodingChallengeSampleData.txt";
	private String dir = "/home/jmitch/Public/xdrive/JavaProjects/vehicleSurveyCodeChallenge/johnm-vehiclesurvey/src/test/resources/";
	
	@Test
	public void testSuppliedInputFile() {
		final String[] args = {dir + fileName, "20140101"};
		ReadMeRunMe.main(args);
	}
}
