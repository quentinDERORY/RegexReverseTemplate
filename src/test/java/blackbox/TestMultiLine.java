package blackbox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;

import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;

import com.google.gson.JsonObject;

import core.ReverseTemplateEngine;

public class TestMultiLine {
	@Test
	public void testMultiLineHasAllVariables()
			throws InputMismatchException, NullPointerException, IOException, ParserConfigurationException {

		File configuration = new File(this.getClass().getResource("/basic/Configuration.txt").getFile());
		File file = new File(this.getClass().getResource("/basic/MultiLogFile.txt").getFile());
		File schema = new File(this.getClass().getResource("/basic/MultiLogFile-Schema.txt").getFile());

		assertTrue(configuration.exists());
		assertTrue(file.exists());
		assertTrue(schema.exists());

		JsonObject results = new ReverseTemplateEngine(configuration).extract(file, schema);
		assertEquals("X821", results.get("id").getAsString());
		assertEquals("B5", results.get("type").getAsString());
		assertEquals("20160202T01:11:01.2991", results.get("timestamp").getAsString());
		assertEquals("255141", results.get("deviceid").getAsString());
	}

	@Test
	public void testIgnoreEmptyLines()
			throws InputMismatchException, NullPointerException, IOException, ParserConfigurationException {

		File configuration = new File(this.getClass().getResource("/basic/Configuration.txt").getFile());
		File file = new File(this.getClass().getResource("/basic/MultiLogFileEmptyLines.txt").getFile());
		File schema = new File(this.getClass().getResource("/basic/MultiLogFile-Schema.txt").getFile());

		assertTrue(configuration.exists());
		assertTrue(file.exists());
		assertTrue(schema.exists());

		JsonObject results = new ReverseTemplateEngine(configuration, true).extract(file, schema);
		assertEquals("X821", results.get("id").getAsString());
		assertEquals("B5", results.get("type").getAsString());
		assertEquals("20160202T01:11:01.2991", results.get("timestamp").getAsString());
		assertEquals("255141", results.get("deviceid").getAsString());
	}

	@Test
	public void testUseEmptyLines()
			throws InputMismatchException, NullPointerException, IOException, ParserConfigurationException {

		File configuration = new File(this.getClass().getResource("/basic/Configuration.txt").getFile());
		File file = new File(this.getClass().getResource("/basic/MultiLogFileEmptyLines.txt").getFile());
		File schema = new File(this.getClass().getResource("/basic/MultiLogFile-Schema.txt").getFile());

		assertTrue(configuration.exists());
		assertTrue(file.exists());
		assertTrue(schema.exists());

		JsonObject results = new ReverseTemplateEngine(configuration, false).extract(file, schema);
		assertFalse(results.get("id") != null);
		assertFalse(results.get("type") != null);
		assertFalse(results.get("timestamp") != null);
		assertFalse(results.get("deviceid") != null);
		assertTrue(results.entrySet().isEmpty());
	}

	@Test
	public void testTwistedLines()
			throws InputMismatchException, NullPointerException, IOException, ParserConfigurationException {

		File configuration = new File(this.getClass().getResource("/basic/Configuration.txt").getFile());
		File file = new File(this.getClass().getResource("/basic/MultiLogFile-Twisted.txt").getFile());
		File schema = new File(this.getClass().getResource("/basic/MultiLogFile-Schema.txt").getFile());

		assertTrue(configuration.exists());
		assertTrue(file.exists());
		assertTrue(schema.exists());

		JsonObject results = new ReverseTemplateEngine(configuration, false).extract(file, schema);
		assertEquals("X821", results.get("id").getAsString());
		assertFalse(results.get("type") != null);
		assertFalse(results.get("timestamp") != null);
		assertEquals("255141", results.get("deviceid").getAsString());
	}

	@Test
	public void testInputHasLessLines()
			throws InputMismatchException, NullPointerException, IOException, ParserConfigurationException {

		File configuration = new File(this.getClass().getResource("/basic/Configuration.txt").getFile());
		File file = new File(this.getClass().getResource("/basic/MultiLogFile-Short.txt").getFile());
		File schema = new File(this.getClass().getResource("/basic/MultiLogFile-Schema.txt").getFile());

		assertTrue(configuration.exists());
		assertTrue(file.exists());
		assertTrue(schema.exists());

		JsonObject results = new ReverseTemplateEngine(configuration, false).extract(file, schema);
		assertEquals("X821", results.get("id").getAsString());
		assertEquals("B5", results.get("type").getAsString());
	}

}
