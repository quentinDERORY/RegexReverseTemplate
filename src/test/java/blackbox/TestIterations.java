package blackbox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import core.ReverseTemplateEngine;

public class TestIterations {
	@Test
	public void testBasicIteration()
			throws InputMismatchException, NullPointerException, IOException, ParserConfigurationException {

		File configuration = new File(this.getClass().getResource("/basic/Configuration.txt").getFile());
		File file = new File(this.getClass().getResource("/iterations/Iteration.txt").getFile());
		File schema = new File(this.getClass().getResource("/iterations/Iteration-Schema.txt").getFile());

		assertTrue(configuration.exists());
		assertTrue(file.exists());
		assertTrue(schema.exists());

		JsonObject results = (JsonObject) new ReverseTemplateEngine(configuration).extract(file, schema);
		assertEquals("K76261X", results.get("id").getAsString());
		assertEquals("MX7", results.get("type").getAsString());
		assertTrue(results.get("measurements")!=null);

		// get measurements
		JsonArray measurements = results.getAsJsonArray("measurements");
		
		
		// check length
		assertEquals(3, measurements.size());

		// get elements
		JsonObject m1 = (JsonObject) measurements.get(0);
		JsonObject m2 = (JsonObject) measurements.get(1);
		JsonObject m3 = (JsonObject) measurements.get(2);

		assertEquals("pressure1", m1.get("measurement").getAsString());
		assertEquals(Integer.toString(17), m1.get("value").getAsString());

		assertEquals("pressure2", m2.get("measurement").getAsString());
		assertEquals(Integer.toString(18), m2.get("value").getAsString());

		assertEquals("pressure3", m3.get("measurement").getAsString());
		assertEquals(Integer.toString(19), m3.get("value").getAsString());
	}
	
	@Test
	public void testBasicIterationWithEmptyLines()
			throws InputMismatchException, NullPointerException, IOException, ParserConfigurationException {

		File configuration = new File(this.getClass().getResource("/basic/Configuration.txt").getFile());
		File file = new File(this.getClass().getResource("/iterations/Iteration-EmptyLines.txt").getFile());
		File schema = new File(this.getClass().getResource("/iterations/Iteration-Schema.txt").getFile());

		assertTrue(configuration.exists());
		assertTrue(file.exists());
		assertTrue(schema.exists());

		JsonObject results = (JsonObject) new ReverseTemplateEngine(configuration, true).extract(file, schema);
		assertEquals("K76261X", results.get("id").getAsString());
		assertEquals("MX7", results.get("type").getAsString());
		assertTrue(results.get("measurements")!=null);

		// get measurements
		JsonArray measurements = results.getAsJsonArray("measurements");
		
		
		// check length
		assertEquals(3, measurements.size());

		// get elements
		JsonObject m1 = (JsonObject) measurements.get(0);
		JsonObject m2 = (JsonObject) measurements.get(1);
		JsonObject m3 = (JsonObject) measurements.get(2);

		assertEquals("pressure1", m1.get("measurement").getAsString());
		assertEquals(Integer.toString(17), m1.get("value").getAsString());

		assertEquals("pressure2", m2.get("measurement").getAsString());
		assertEquals(Integer.toString(18), m2.get("value").getAsString());

		assertEquals("pressure3", m3.get("measurement").getAsString());
		assertEquals(Integer.toString(19), m3.get("value").getAsString());
	}

	@Test
	public void testJeffsFile()
			throws InputMismatchException, NullPointerException, IOException, ParserConfigurationException {

		File configuration = new File(this.getClass().getResource("/iterations/Configuration-Res.txt").getFile());
		File file = new File(this.getClass().getResource("/iterations/08502122341306242265106455_OK.res").getFile());
		File schema = new File(
				this.getClass().getResource("/iterations/08502122341306242265106455_OK-Schema.res").getFile());

		assertTrue(configuration.exists());
		assertTrue(file.exists());
		assertTrue(schema.exists());

		JsonObject results = new ReverseTemplateEngine(configuration, true).extract(file, schema);
		assertEquals("11:54:29", results.get("date").getAsString());
		assertEquals("08502122341306242265106455", results.get("id").getAsString());
		assertEquals("OK", results.get("result").getAsString());
		assertEquals("Pin Tolerance to Zero Position Inspection", results.get("description").getAsString());
		assertTrue(results.get("pins")!=null);

		// get pins
		JsonArray pins = (JsonArray) results.get("pins");

		// check length
		assertEquals(6, pins.size());

		// get pin
		JsonObject p1 = (JsonObject) pins.get(0);

		// does it have all values
		assertEquals("Taumelkreis", p1.get("type").getAsString());
		assertEquals("VorRef Nullbohrung", p1.get("description").getAsString());
		assertEquals("PatMax", p1.get("component").getAsString());
		assertEquals("", p1.get("position").getAsString());
		assertEquals("0x000000", p1.get("resolution").getAsString());
	}

}
