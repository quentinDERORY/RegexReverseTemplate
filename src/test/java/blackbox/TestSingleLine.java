
package blackbox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;

import com.google.gson.JsonObject;

import core.ReverseTemplateEngine;

public class TestSingleLine {
	@Test
	public void testSingleLineHasIds() throws InputMismatchException, NullPointerException, IOException, ParserConfigurationException {
		File configuration = new File(this.getClass().getResource("/basic/Configuration.txt").getFile());
		File file = new File(this.getClass().getResource("/basic/SingleLineLogFile.txt").getFile());
		File schema = new File(this.getClass().getResource("/basic/SingleLineLogFile-Schema.txt").getFile());

		assertTrue(configuration.exists());
		assertTrue(file.exists());
		assertTrue(schema.exists());

		JsonObject results = new ReverseTemplateEngine(configuration).extract(file, schema);
		assertNotNull(results);
		assertFalse(results.entrySet().isEmpty());
		assertEquals("X821", results.get("id1").getAsString());
		assertEquals("MK281Jjan", results.get("id2").getAsString());
	}

	@Test
	public void testSingleLineHasIdsComplex() throws InputMismatchException, NullPointerException, IOException, ParserConfigurationException {
		File configuration = new File(this.getClass().getResource("/basic/Configuration.txt").getFile());
		File file = new File(this.getClass().getResource("/basic/SingleLineLogFile-complex.txt").getFile());
		File schema = new File(this.getClass().getResource("/basic/SingleLineLogFile-Schema-complex.txt").getFile());

		assertTrue(configuration.exists());
		assertTrue(file.exists());
		assertTrue(schema.exists());

		JsonObject results = new ReverseTemplateEngine(configuration).extract(file, schema);
		assertNotNull(results);
		assertFalse(results.entrySet().isEmpty());
		assertEquals("M2881", results.get("id1").getAsString());
		assertEquals("X1", results.get("id2").getAsString());
		assertEquals("3Z", results.get("id3").getAsString());
		assertEquals("Kkb2", results.get("id4").getAsString());
	}

	@Test
	public void testSingleLineHasIdsIgnoreEmptyLines() throws InputMismatchException, NullPointerException, IOException, ParserConfigurationException {
		File configuration = new File(this.getClass().getResource("/basic/Configuration.txt").getFile());
		File file = new File(this.getClass().getResource("/basic/SingleLineLogFile-EmptyLines.txt").getFile());
		File schema = new File(this.getClass().getResource("/basic/SingleLineLogFile-Schema.txt").getFile());

		assertTrue(configuration.exists());
		assertTrue(file.exists());
		assertTrue(schema.exists());

		JsonObject results = new ReverseTemplateEngine(configuration, true).extract(file, schema);
		assertNotNull(results);
		assertFalse(results.entrySet().isEmpty());
		assertEquals("X821", results.get("id1").getAsString());
		assertEquals("MK281Jjan", results.get("id2").getAsString());
	}
}
