package org.johnm.vehicle.survey.client;

import java.net.URL;

import org.johnm.vehicle.survey.input.ReadFileTest;
import org.junit.Before;
import org.junit.Test;

public class RunMeReadMeTest {
	private String fileName;
	
	@Before
	public void setup() {
		final URL url = ReadFileTest.class.getClassLoader().getResource("VehicleSurveyCodingChallengeSampleData.txt");
		fileName = url.getFile();
	}
	
	@Test
	public void testSuppliedInputFile() {
		final String[] args = {fileName, "20140101"};
		ReadMeRunMe.main(args);
	}
}
