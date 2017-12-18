package internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import struct.Line;

/**
 * @author Quentin Derory
 * @brief Parser for the template file
 */
public class TemplateParser {
	private LinkedList<Line> lines;
	private boolean ignoreEmptyLines;
	private Gson gson = new Gson();

	/**
	 * @param ignoreEmptyLines
	 *            Choosing if the function will ignore the empty lines or not
	 * @param idsAndLines
	 *            The map containing the set of ids associated with the pattern
	 *            for a template line
	 */
	public TemplateParser(boolean ignoreEmptyLines, LinkedList<Line> lines) {
		this.lines = lines;
		this.ignoreEmptyLines = ignoreEmptyLines;
	}

	/**
	 * @param template
	 *            The file following the schema structure and containing the
	 *            relevant informations
	 * @return The JSON object containing the data extracted from the template
	 * @throws IOException
	 * @brief Parses the template file to get all of the informations into the
	 *        JSON object
	 */
	public JsonObject parse(File template) throws IOException {
 		Map<String, Object> obj = new HashMap<String, Object>();
		try (BufferedReader br = new BufferedReader(new FileReader(template))) {
			int pos = 0;
			Line cline = null;
			boolean stilltoparse = true;//Permit to keep parsing the file after an iteration place holder
			String input =null;
			while(stilltoparse){
				if(pos == 0){//First iteration
					stilltoparse = false;
				}
				for (; pos < lines.size(); pos++) {
					// get current line
					cline = lines.get(pos);

					// iteration detected?
					if (cline.iterationName != null)
						break;

					//If stilltoproceed is true, input has already the right value
					if(!stilltoparse){
						if ((input = nextLine(br)) == null)// get next line and return if EOF
							return gson.toJsonTree(obj).getAsJsonObject();
					}
					else
						stilltoparse=false;

					// Find variables
					Map<String, Object> map = process(input, Pattern.compile(cline.representation), cline);
					if (map != null)
						obj.putAll(map);
				}

				// no iteration detected
				if (!cline.hasIteration())
					return gson.toJsonTree(obj).getAsJsonObject();

				// one time parsing of pattern
				Pattern iterationPattern = Pattern.compile(cline.representation);

				// loop thru remaining input file
				
				ArrayList<JsonObject> iteration = new ArrayList<>();
				while (stilltoparse || (input = nextLine(br)) != null){//Checking stilltoparse permit to let the input String unchanged
					stilltoparse=false;
					Map<String, Object> mapAfter=null;
					if(pos+1<lines.size()){//Something after the iteration
						Line lineAfter= lines.get(pos+1);
						mapAfter = process(input, Pattern.compile(lineAfter.representation), lineAfter);
					}
					// Find variables
					Map<String, Object> map = process(input, iterationPattern, cline);
					if (map != null)
						iteration.add(gson.toJsonTree(map).getAsJsonObject());
					else if(mapAfter!=null){//If the iteration pattern did not match and the next line pattern matches then we stop the iteration
						pos=pos+1;
						stilltoparse = true ;
						break;	
					}else{//If None matche, stop parsing
						break;
					}


				}
				if(!iteration.isEmpty())
					obj.put(cline.iterationName, gson.toJsonTree(iteration).getAsJsonArray());
			}


			return gson.toJsonTree(obj).getAsJsonObject();
		}
	}

	/**
	 * 
	 * @param br
	 * @return
	 * @throws IOException
	 */
	private String nextLine(BufferedReader br) throws IOException {
		String next = br.readLine();
		if (ignoreEmptyLines) {
			while (next != null && next.length() == 0)
				next = br.readLine();
		}
		return next;
	}

	/**
	 * 
	 * 
	 * @param input
	 * @param compile
	 * @param line
	 * @return
	 */
	private Map<String, Object> process(String input, Pattern compile, Line line) {
		Map<String, Object> obj = new HashMap<String, Object>();
		Matcher matcher = compile.matcher(input);

		// Any matches?
		if (!matcher.matches())
			return null;

		int pos = 1;
		for (String id : line.ids) {
			obj.put(id, matcher.group(pos));
			pos++;
		}

		return obj;
	}
	
	

}
