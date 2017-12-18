package internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Quentin Derory
 * @brief Class containing all stand alone functions for the ReverseTemplateEngine
 */
public class Util {
	/**
	 * @param f The file to check his emptiness 
	 * @return Whether the file is empty or not
	 * @throws IOException
	 */
	public static Boolean isEmpty(File f) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			if (br.readLine() == null) {
				return true;
			}
		}
		return false;
	}
}
