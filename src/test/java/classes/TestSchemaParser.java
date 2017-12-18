
package classes;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import internal.ConfigurationParser;
import internal.SchemaParser;

public class TestSchemaParser {
	@Test
	public void testNoPlaceholders() throws IOException {
		File configuration = new File(this.getClass().getResource("/basic/Configuration.txt").getFile());
		assertTrue(configuration.exists());

		SchemaParser sp = new SchemaParser(false, ConfigurationParser.parse(configuration));

		assertEquals("\\Q**XZK (10)\\E", sp.replace("**XZK (10)").representation);
	}

	@Test
	public void testReplaceSingleVar() throws IOException {
		File configuration = new File(this.getClass().getResource("/basic/Configuration.txt").getFile());
		assertTrue(configuration.exists());

		SchemaParser sp = new SchemaParser(false, ConfigurationParser.parse(configuration));

		assertEquals("\\Q**\\E([A-Za-z0-9]+)\\Q (10)\\E", sp.replace("**{{STRING:id1}} (10)").representation);
	}

	@Test
	public void testReplaceMultiVar() throws IOException {
		File configuration = new File(this.getClass().getResource("/basic/Configuration.txt").getFile());
		assertTrue(configuration.exists());

		SchemaParser sp = new SchemaParser(false, ConfigurationParser.parse(configuration));

		assertEquals("\\Q**\\E([A-Za-z0-9]+)\\Q (\\E([0-9]+)\\Q)\\E",
				sp.replace("**{{STRING:id1}} ({{INTEGER:number}})").representation);
	}
	
	@Test(expected = InputMismatchException.class)
	public void testUnknownPlaceholders() throws IOException {
		File configuration = new File(this.getClass().getResource("/basic/Configuration.txt").getFile());
		assertTrue(configuration.exists());

		SchemaParser sp = new SchemaParser(false, ConfigurationParser.parse(configuration));
		sp.replace("**{{UNKNOWN:id1}} (10)");
	}
	
	
	@Test(expected = InputMismatchException.class)
	public void testPositionOfTheIterationPlaceholder() throws IOException {
		File configuration = new File(this.getClass().getResource("/basic/Configuration.txt").getFile());
		assertTrue(configuration.exists());

		SchemaParser sp = new SchemaParser(false, ConfigurationParser.parse(configuration));
		sp.replace("**{{UNKNOWN:id1}} (10){{*:measurements}}{{STRING:measurement}}-{{INTEGER:value}}");
	}
	

}
