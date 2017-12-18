package blackbox;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.regex.PatternSyntaxException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import core.ReverseTemplateEngine;


public class TestErrorHandling {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test(expected = NullPointerException.class)
	public void testConfigurationIsNull() throws InputMismatchException, NullPointerException, IOException {
		new ReverseTemplateEngine(null);
	}

	@Test(expected = InputMismatchException.class)
	public void testConfigurationIsEmpty() throws IOException {
		new ReverseTemplateEngine(folder.newFile());
	}

	@Test(expected = FileNotFoundException.class)
	public void testConfigurationDoesNotExist() throws IOException {
		new ReverseTemplateEngine(new File(folder.getRoot(), "filedoesnotexist.no"));
	}

	@Test(expected = FileNotFoundException.class)
	public void testFileDoesNotExist() throws Exception {
		File configuration = new File(this.getClass().getResource("/basic/Configuration.txt").getFile());
		File schema = new File(this.getClass().getResource("/basic/SingleLineLogFile-Schema.txt").getFile());

		assertTrue(configuration.exists());
		assertTrue(schema.exists());

		new ReverseTemplateEngine(configuration).extract(new File(folder.getRoot(), "filedoesnotexist.no"), schema);
	}

	@Test(expected = NullPointerException.class)
	public void testFileIsNull() throws Exception {
		File configuration = new File(this.getClass().getResource("/basic/Configuration.txt").getFile());
		File schema = new File(this.getClass().getResource("/basic/SingleLineLogFile-Schema.txt").getFile());

		assertTrue(configuration.exists());
		assertTrue(schema.exists());

		new ReverseTemplateEngine(configuration).extract(null, schema);
	}

	@Test(expected = FileNotFoundException.class)
	public void testSchemaDoesNotExist() throws Exception {
		File configuration = new File(this.getClass().getResource("/basic/Configuration.txt").getFile());
		File file = new File(this.getClass().getResource("/basic/SingleLineLogFile.txt").getFile());

		assertTrue(configuration.exists());
		assertTrue(file.exists());

		new ReverseTemplateEngine(configuration).extract(file, new File(folder.getRoot(), "filedoesnotexist.no"));
	}

	@Test(expected = NullPointerException.class)
	public void testSchemaIsNull() throws Exception {
		File configuration = new File(this.getClass().getResource("/basic/Configuration.txt").getFile());
		File file = new File(this.getClass().getResource("/basic/SingleLineLogFile.txt").getFile());

		assertTrue(configuration.exists());
		assertTrue(file.exists());

		new ReverseTemplateEngine(configuration).extract(file, null);
	}

	@Test(expected = PatternSyntaxException.class)
	public void testConfigurationRegExInvalid() throws InputMismatchException, NullPointerException, IOException {
		File configuration = new File(this.getClass().getResource("/basic/Configuration-RegexInvalid.txt").getFile());
		assertTrue(configuration.exists());

		new ReverseTemplateEngine(configuration);
	}

	@Test(expected = InputMismatchException.class)
	public void testConfigurationDuplicateEntry() throws InputMismatchException, NullPointerException, IOException {
		File configuration = new File(this.getClass().getResource("/basic/Configuration-DuplicateEntry.txt").getFile());
		assertTrue(configuration.exists());

		new ReverseTemplateEngine(configuration);
	}
}
