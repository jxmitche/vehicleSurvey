package org.johnm.vehicle.survey.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ReadFileTest {
	private ReadFile readFile;
	
	@Before
	public void setup() {
		final URL url = ReadFileTest.class.getClassLoader().getResource("VehicleSurveyCodingChallengeSampleData.txt");
		readFile = new ReadFile(url.getFile());
	}
	
	@Test
	public void check_file() {
		final List<String> lines = readFile.readFile();

		assertEquals(67296, lines.size());
		assertEquals("A98186", lines.get(0));
	}
	
	@Test
	public void check_NullFileName() {
		try {
			readFile = new ReadFile(null);
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("Path must not be null", ex.getMessage());
		}
	}
	
	@Test
	public void check_FileNotFound() {
		try {
			readFile = new ReadFile("abcxyz.txt");
			readFile.readFile();
			fail("should not reach here");
		} catch (IllegalArgumentException ex) {
			assertEquals("File not found:abcxyz.txt", ex.getMessage());
		}
	}
}
