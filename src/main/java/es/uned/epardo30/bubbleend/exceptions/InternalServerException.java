package es.uned.epardo30.bubbleend.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;


public class InternalServerException extends WebApplicationException {
	
	
	static Logger logger = Logger.getLogger(InternalServerException.class);
	/**
	* Create a HTTP 500 (INTERNAL_SERVER_ERROR) exception.
	* @param message the String that is the entity of the 500 response.
	*/
	public InternalServerException(String message) {
		super(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message).type(MediaType.APPLICATION_JSON).build());
		logger.debug("Bilding Internal Server Exception...");
	}
	

}
