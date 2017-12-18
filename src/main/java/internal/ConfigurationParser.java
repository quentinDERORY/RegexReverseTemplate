package internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.regex.Pattern;

/**
 * @author Quentin Derory
 * @brief Parser for the configuration file
 */
public  class ConfigurationParser {

	/**
	 * @param configuration The file containing the patterns and their names
	 * @return A map containing the patterns and their names
	 * @throws IOException If a IO error occurs during the parsing of the configuration file
	 * @brief Parses the configuration file to get all of the informations into
	 *        the returning map
	 */
	public static HashMap<String, Pattern> parse(File configuration) throws IOException {
		HashMap<String, Pattern> configurationMap = new HashMap<String, Pattern>();
		try (BufferedReader br = new BufferedReader(new FileReader(configuration))) {
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				String[] lineSplitted = line.split("=");
				
				// If there is less than one equal we just skip the line
				if (lineSplitted.length < 2) 
					continue;
				
				if (configurationMap.containsKey(lineSplitted[0])) {// Check for same pattern names
					throw new InputMismatchException(
							"The configuration file should have different names for each patterns.");
				}
				 
				// If there is more than two equals, we assume that the
				// first equal is the separator
				for (int i = 2; i < lineSplitted.length; i++) {
					lineSplitted[1] += "=" + lineSplitted[i];
				}
				
				configurationMap.put(lineSplitted[0], Pattern.compile(lineSplitted[1]));
			}
		}

		return configurationMap;
	}

}
