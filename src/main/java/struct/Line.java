package struct;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Quentin Derory
 * Object containing a line with his placeholders replaced and the set ids associated.
 */
public class Line {
	public String representation;
	public Set<String> ids;
	public String iterationName = null;

	/**
	 * @param representation
	 */
	public Line(String representation) {
		this.representation = representation;
		this.ids = new LinkedHashSet<>();
	}

	/**
	 * @param representation
	 * @param ids
	 */
	public Line(String representation, Set<String> ids) {
		this.representation = representation;
		this.ids = ids;
	}
	
	
	/**
	 * @param representation
	 * @param ids
	 * @param iterations
	 */
	public Line(String representation, Set<String> ids,String iterationName) {
		this.representation = representation;
		this.ids = ids;
		this.iterationName=iterationName;
	}
	
	public boolean hasIteration(){
		return iterationName!=null;
	}
}
