package org.johnm.vehicle.survey.client;

import org.johnm.vehicle.survey.reporting.Reporter;
import org.johnm.vehicle.survey.reporting.SystemOutReporter;
import org.johnm.vehicle.survey.validation.NullParamValidator;

public class ReadMeRunMe {
	private static NullParamValidator nullValidator = new NullParamValidator();
	
	public static void main(String[] args) {
		nullValidator.checkNotNull(args, "args");
		
		if (args.length == 0) {
			throw new IllegalArgumentException("Please pass sensor file as first parameter and start date (yyyymmdd) as second");
		}
		
		final Reporter reporter = new SystemOutReporter();
		final Setup setup = new Setup();
		
		final String fileName = args[0];
		final String startDateOfSurveyInput = args[1];
		
		setup.process(fileName, startDateOfSurveyInput);
		setup.report(reporter);
	}
}
