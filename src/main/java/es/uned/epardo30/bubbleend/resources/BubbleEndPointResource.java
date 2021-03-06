package es.uned.epardo30.bubbleend.resources;

import es.uned.epardo30.bubbleend.BubbleEndPointConfiguation;
import es.uned.epardo30.bubbleend.core.BubbleEngine;
import es.uned.epardo30.bubbleend.externalresource.afc.dto.LatticeDto;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.yammer.dropwizard.config.Environment;

/**
 * Jersey resources are the meat-and-potatoes of a Dropwizard service. 
 * Each resource class is associated with a URI template. For our service, 
 * we need a resource which returns new RetticuleDto (Json format) instances from the URI /bubble-search
 * 
 * BubbleEndPointResource has two annotations: @Path and @Produces. @Path("/bubble-search")
 * tells Jersey that this resource is accessible at the URI /bubble-search, 
 * and @Produces(MediaType.APPLICATION_JSON) lets Jersey’s content negotiation code know that
 * this resource produces representations which are application/json.
 * 
 * #searchBubbles(Optional<String>) is the meat of this class, and it’s a fairly simple method. 
 * The @QueryParam("filter") annotation tells Jersey to map the filter parameter from the query
 * string to the filter parameter in the method. If the client sends a request to /bubble-search?text-filter="Winner league 2013",
 * searchBubbles method will be called with Optional.of("Winner league 2013"); 
 *
 * Optional the searchBubbles() method could be annotated with @Timed, Dropwizard automatically records the duration and rate
 * of its invocations as a Metrics Timer.
 * 
 * Once searchBubbles has returned, Jersey takes the RetticuleDto instance and looks for a provider class which can write RetticuleDto
 * instances as application/json.
 * Dropwizard has one such provider built in which allows for producing and consuming Java objects as JSON objects. 
 * The provider writes out the JSON and the client receives a 200 OK response with a content type of application/json.
 * 
 * We don't need to manage the concurrent access, due to we don't have shared resources 
 * 
 * @author Eduardo.Guillen
 *
 */

@Path("/bubble-search")
@Produces(MediaType.APPLICATION_JSON)
public class BubbleEndPointResource {

	static Logger logger = Logger.getLogger(BubbleEndPointResource.class);
	
	private BubbleEngine bubbleEngine;
	private BubbleEndPointConfiguation bubbleEndPointConfiguation;
	private Environment environment;

    public BubbleEndPointResource(BubbleEndPointConfiguation bubbleEndPointConfiguation, Environment environment) {
    	this.bubbleEndPointConfiguation = bubbleEndPointConfiguation;
    	this.environment = environment;
    }
    
    
	public BubbleEndPointConfiguation getBubbleEndPointConfiguation() {
		return bubbleEndPointConfiguation;
	}

	public Environment getEnvironment() {
		return environment;
	}

	/**
     * Called method when the request to /search-bubble is GET
     * 
     * @return boolean
     */
    @GET
    public boolean ping(){
    	logger.debug("calling ping() method...");
		return true;
	}
    
    /**
     * Called method when the request to /search-bubble is POST
     * 
     * @return RetticuleDto
     * @exception WebApplicationException Exception is thrown from workflowEngine() method. No exception can be thrown
     * from searchBubble() because all process is executed on BubbbleEngine class.   
     */
    @POST
    public LatticeDto searchBubbles(@FormParam("text-filter") String textFilter, @FormParam("language-search") String languageSearch) {
    	
    	logger.debug("calling searchBubbles() method...");
    	logger.debug("Text filter: "+textFilter);
    	logger.debug("Language Search: "+languageSearch);
    	
    	/* We need to call to bubble engine. We create a new instance of BubbleEngine for each request.
    	 * We don't need to manage user session, therefore we open a new BubbleEngine when we receive a
    	 * new request and when the service finishes, we will kill the instance. 
    	 * 
    	 */
    	try {
    		bubbleEngine = new BubbleEngine();
    		return bubbleEngine.workflowEngine(textFilter, languageSearch, this.bubbleEndPointConfiguation, this.environment);
        }
    	catch(WebApplicationException webApplicationException) {
    		logger.error("Exception on searchBubble. Status response: "+webApplicationException.getResponse().getStatus());
    		throw webApplicationException;
    	}
    	finally {
    		bubbleEngine = null;
    	}
    }
    
}
