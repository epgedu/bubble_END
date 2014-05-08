package es.uned.epardo30.bubbleend.externalresource.afc.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Our service needs to conform to an industry standard, RFC 1149, which specifies the following JSON representation:
 * { "id": 1,
 * 	 "content": 
 * }
 * 
 * The id field is a unique identifier for the searching, and content is the found results.
 * To model this representation, we’ll create the present representation class.
 *
 * This is a pretty simple POJO, but there are a few things worth noting here. First, it’s immutable. This makes Searching
 * instances very easy to reason about in multi-threaded environments as well as single-threaded environments. 
 * Second, it uses the Java Bean standard for the id and content properties. This allows Jackson to serialize it to the JSON we need.
 * The Jackson object mapping code will populate the id field of the JSON object with the return value of #getId(), likewise with content and #getContent().
 * 
 * 
 * @author Eduardo.Guillen
 *
 */
public class LatticeDto {
	
	List<FormalConceptDto> contentObjects = new ArrayList<FormalConceptDto>();

	public List<FormalConceptDto> getContentObjects() {
		return contentObjects;
	}

	public void setContentObjects(List<FormalConceptDto> contentObjects) {
		this.contentObjects = contentObjects;
	}
	
	@Override
	public String toString() {
		return "LatticeDto [contentObjects="
							+this.contentObjects	
							+"]";
	}
	
	
}
