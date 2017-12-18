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

public class TestComplexIterations {
	@Test
	public void testLineIterationLine()
			throws InputMismatchException, NullPointerException, IOException, ParserConfigurationException {

		File configuration = new File(this.getClass().getResource("/basic/Configuration.txt").getFile());
		File file = new File(this.getClass().getResource("/iterations-complex/LineIterationLine.txt").getFile());
		File schema = new File(this.getClass().getResource("/iterations-complex/LineIterationLine-Schema.txt").getFile());

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
	public void testTwoIterations()
			throws InputMismatchException, NullPointerException, IOException, ParserConfigurationException {

		File configuration = new File(this.getClass().getResource("/basic/Configuration.txt").getFile());
		File file = new File(this.getClass().getResource("/iterations-complex/TwoIterations.txt").getFile());
		File schema = new File(this.getClass().getResource("/iterations-complex/TwoIterations-Schema.txt").getFile());

		assertTrue(configuration.exists());
		assertTrue(file.exists());
		assertTrue(schema.exists());

		JsonObject results = (JsonObject) new ReverseTemplateEngine(configuration).extract(file, schema);
		assertTrue(results.get("measurements")!=null);
		assertTrue(results.get("parameters")!=null);

		// get iteration data
		JsonArray measurements = results.getAsJsonArray("measurements");
		JsonArray parameters = results.getAsJsonArray("parameters");
		
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
		
		// check length
		assertEquals(2, parameters.size());

		// get elements
		JsonObject p1 = (JsonObject) parameters.get(0);
		JsonObject p2 = (JsonObject) parameters.get(1);

		assertEquals("setMax1", p1.get("parameter").getAsString());
		assertEquals(Double.toString(8.1), p1.get("value").getAsString());
		assertEquals("text1", p1.get("description").getAsString());

		assertEquals("setMax2", p2.get("parameter").getAsString());
		assertEquals(Double.toString(8.2), p2.get("value").getAsString());
		assertEquals("text2", p2.get("description").getAsString());		
	}
	
	@Test
	public void testTwoIterationsOnlyFirstIterationData()
			throws InputMismatchException, NullPointerException, IOException, ParserConfigurationException {

		File configuration = new File(this.getClass().getResource("/basic/Configuration.txt").getFile());
		File file = new File(this.getClass().getResource("/iterations-complex/TwoIterationsOnlyFirst.txt").getFile());
		File schema = new File(this.getClass().getResource("/iterations-complex/TwoIterations-Schema.txt").getFile());

		assertTrue(configuration.exists());
		assertTrue(file.exists());
		assertTrue(schema.exists());

		JsonObject results = (JsonObject) new ReverseTemplateEngine(configuration).extract(file, schema);
		assertTrue(results.get("measurements")!=null);
		assertTrue(results.get("parameters")==null);

		// get iteration data
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
	public void testTwoIterationsOnlySecondIterationData()
			throws InputMismatchException, NullPointerException, IOException, ParserConfigurationException {

		File configuration = new File(this.getClass().getResource("/basic/Configuration.txt").getFile());
		File file = new File(this.getClass().getResource("/iterations-complex/TwoIterationsOnlySecond.txt").getFile());
		File schema = new File(this.getClass().getResource("/iterations-complex/TwoIterations-Schema.txt").getFile());

		assertTrue(configuration.exists());
		assertTrue(file.exists());
		assertTrue(schema.exists());

		JsonObject results = (JsonObject) new ReverseTemplateEngine(configuration).extract(file, schema);
		assertTrue(results.get("measurements")==null);
		assertTrue(results.get("parameters")!=null);

		// get iteration data
		
		JsonArray parameters = results.getAsJsonArray("parameters");
		
		
		// check length
		assertEquals(2, parameters.size());

		// get elements
		JsonObject p1 = (JsonObject) parameters.get(0);
		JsonObject p2 = (JsonObject) parameters.get(1);

		assertEquals("setMax1", p1.get("parameter").getAsString());
		assertEquals(Double.toString(8.1), p1.get("value").getAsString());
		assertEquals("text1", p1.get("description").getAsString());

		assertEquals("setMax2", p2.get("parameter").getAsString());
		assertEquals(Double.toString(8.2), p2.get("value").getAsString());
		assertEquals("text2", p2.get("description").getAsString());		
	}
	
	@Test
	public void testTwoIterationsFirstIterationDataAndIncorrectData()
			throws InputMismatchException, NullPointerException, IOException, ParserConfigurationException {

		File configuration = new File(this.getClass().getResource("/basic/Configuration.txt").getFile());
		File file = new File(this.getClass().getResource("/iterations-complex/TwoIterationsFirstAndIncorrectData.txt").getFile());
		File schema = new File(this.getClass().getResource("/iterations-complex/TwoIterations-Schema.txt").getFile());

		assertTrue(configuration.exists());
		assertTrue(file.exists());
		assertTrue(schema.exists());

		JsonObject results = (JsonObject) new ReverseTemplateEngine(configuration).extract(file, schema);
		assertTrue(results.get("measurements")!=null);
		assertTrue(results.get("parameters")==null);

		// get iteration data
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
}
