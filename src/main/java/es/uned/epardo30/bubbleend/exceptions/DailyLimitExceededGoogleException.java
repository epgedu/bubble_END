package es.uned.epardo30.bubbleend.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

/**
 * Wrap an daily limit exceeded google exception during the backend processing to http response with code 403 
 *  
 * @author eduardo.guillen
 *
 */
public class DailyLimitExceededGoogleException extends WebApplicationException{

	static Logger logger = Logger.getLogger(InternalServerException.class);
	/**
	* Create a HTTP 403 (Forbidden HTTP status code) exception.
	* @param message the String that is the entity of the 403 response.
	*/
	public DailyLimitExceededGoogleException(String message) {
		super(Response.status(Response.Status.FORBIDDEN).entity(message).type(MediaType.APPLICATION_JSON).build());
		logger.debug("Building daily limit exceeded google Exception...");
	}

}