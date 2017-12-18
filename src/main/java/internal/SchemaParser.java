package internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import struct.Line;

/**
 * @author Quentin Derory
 * @brief Parser for the schema file
 */
public class SchemaParser {
	private final static Pattern placeholder = Pattern.compile("\\{\\{([^\\[\\\\}]+)\\}\\}");
	private LinkedList<Line> idsAndLines = new LinkedList<>();

	private boolean ignoreEmptyLines;
	private HashMap<String, Pattern> configurationMap;

	/**
	 * @param ignoreEmptyLines Choosing if the function will ignore the empty lines or not
	 * @param configurationMap The map containing all patterns and their names
	 */
	public SchemaParser(boolean ignoreEmptyLines, HashMap<String, Pattern> configurationMap) {
		this.ignoreEmptyLines = ignoreEmptyLines;
		this.configurationMap = configurationMap;
	}

	/**
	 * @param schema The file containing the structure of the template file
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @brief Parses the schema file to get all of the informations into the map
	 */
	public LinkedList<Line> parse(File schema) throws IOException, ParserConfigurationException,InputMismatchException {
		boolean iteration=false;
		try (BufferedReader br = new BufferedReader(new FileReader(schema))) {
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				// skip empty lines
				if (ignoreEmptyLines && line.length() == 0)
					continue;
				Line l =replace(line);
				idsAndLines.add(l);
			}
		}

		return idsAndLines;
	}

	/**
	 * @param input The string containing the different placeholders
	 * @return The Line containing the string on which we have replaced the placeholders by their patterns and on which the others characters has been escaped and the set of ID of the line
	 * @throws IOException If the parsing made by the configurationParser goes wrong
	 * @brief Replace placeholders of the incoming String with their respective patterns, escape all other characters and return the sets of ID of the line
	 */
	public Line replace(String input) throws IOException {
		Set<String> idsOfTheLine = new LinkedHashSet<>();
		StringBuilder sb = new StringBuilder();
		int offset = 0;
		String iterationName = null;

		// input has no placeholder
		Matcher matcher = placeholder.matcher(input);
		if (!matcher.find())
			return new Line(Pattern.quote(input));

		// replace each placeholder
		do {
			// Get the id value
			String content = matcher.group(1);
			String[] values = content.split(":");
			if (matcher.start() > offset) {
				// Escape others characters
				sb.append(Pattern.quote(input.substring(offset, matcher.start())));
			}

			if(!values[0].equals("*")){//Check for iteration placeholder
				if(!configurationMap.containsKey(values[0]))
					throw new InputMismatchException("The name of the patttern " + values[0] + " was not found in the configuration file");

				// Add the pattern
				sb.append("(" + configurationMap.get(values[0]) + ")");
				idsOfTheLine.add(values[1]);
			}else{
				if(matcher.start()>0)
					throw new InputMismatchException("The iteration placeholder should be placed at the beginning of the line");
				iterationName=values[1];
			}		
			offset = matcher.end();
		} while (matcher.find());

		if (offset != input.length()) {
			// Escape remaining characters
			sb.append(Pattern.quote(input.substring(offset)));
		}

		return new Line(sb.toString(), idsOfTheLine,iterationName);
	}
}
