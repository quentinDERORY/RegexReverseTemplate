package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import com.google.gson.JsonObject;

import internal.ConfigurationParser;
import internal.SchemaParser;
import internal.TemplateParser;
import internal.Util;

/**
 * @author Quentin Derory
 *
 * @brief A class permitting to get back data inside a template
 */
public class ReverseTemplateEngine {
	boolean ignoreEmptyLines;
	HashMap<String, Pattern> configurationMap;

	/**
	 * @param configuration
	 *            The file containing the names and patterns associated
	 * @throws InputMismatchException
	 *             If the configuration file is not correct
	 * @throws NullPointerException
	 *             If the configuration file is null
	 * @throws IOException
	 *             If there is a parsing error of the configuration file
	 * @brief Call the constructor with two parameters with ignoreEmptyLines to
	 *        false
	 */
	public ReverseTemplateEngine(File configuration) throws InputMismatchException, NullPointerException, IOException {
		this(configuration, false);
	}

	/**
	 * @param configuration
	 *            The file containing the names and patterns associated
	 * @param ignoreEmptyLines
	 *            Choosing if the engine will ignore the empty lines or not
	 * @throws InputMismatchException
	 *             If the configuration file is not correct
	 * @throws NullPointerException
	 *             If the configuration file is null
	 * @throws IOException
	 *             If there is a IO error of the configuration file
	 * @brief Initialize the class by parsing the configuration file to get all
	 *        the patterns and names associated
	 */
	public ReverseTemplateEngine(File configuration, boolean ignoreEmptyLines)
			throws NullPointerException, InputMismatchException, IOException {
		if (configuration == null) {
			throw new NullPointerException("The configuration file is null");
		}
		if (!configuration.exists()) {
			throw new FileNotFoundException("The configuration file does not exist");
		}
		if (configuration.isDirectory()) {
			throw new InputMismatchException("The configuration file is a directory");
		}

		if (Util.isEmpty(configuration)) {
			throw new InputMismatchException("The configuration file is empty");
		}

		this.ignoreEmptyLines = ignoreEmptyLines;

		this.configurationMap = ConfigurationParser.parse(configuration);
	}

	/**
	 * @param file
	 *            The template file where the data are
	 * @param schema
	 *            The schema containing the informations about the structure of
	 *            the template
	 * @return A JSON object containing the data
	 * @throws ParserConfigurationException
	 *             If there is a parsing error of the schema or template file
	 * @throws IOException
	 *             If there is a IO error of the schema or template file
	 */
	public JsonObject extract(File file, File schema) throws IOException, ParserConfigurationException {
		if (file == null) {
			throw new NullPointerException("The template file is null");
		}
		if (!file.exists()) {
			throw new FileNotFoundException("The template file does not exist");
		}
		if (schema == null) {
			throw new NullPointerException("The schema file is null");
		}
		if (!schema.exists()) {
			throw new FileNotFoundException("The schema file does not exists");
		}

		SchemaParser schemaParser = new SchemaParser(ignoreEmptyLines, configurationMap);
		TemplateParser templateparser = new TemplateParser(ignoreEmptyLines, schemaParser.parse(schema));

		return templateparser.parse(file);
	}

}
