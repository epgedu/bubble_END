package es.uned.epardo30.bubbleend.health;

import org.apache.log4j.Logger;

import com.yammer.metrics.core.HealthCheck;

import es.uned.epardo30.bubbleend.core.BubbleEngine;
import es.uned.epardo30.bubbleend.resources.BubbleEndPointResource;


/**
 * Check the correct status of Bubble end point service. 
 *  We simulate the workflow calling the workflowEngine method. Previously, we've inserted the dependencies (clients web and search text filter)
 * 	
 * This testing method is provided by dropwizard framework in order to check the deployed resource on the service. In our case, we just publish one 
 * resource, "bubble search" therefore we have one health check. This method is not a unit test, due to we are not testing the internal code, but
 * we are testing:
 * 		- The resource is listening
 *      - The communication among the different external resources is correct  	
 *      
 * @author Eduardo.Guillen
 *
 */
public class BubbleEndPointHealth extends HealthCheck {
	
	static Logger logger = Logger.getLogger(BubbleEndPointHealth.class);
	
	private BubbleEndPointResource bubbleEndPointResource;
	
	public BubbleEndPointHealth(BubbleEndPointResource bubbleEndPointResource) {
		
		super("");
		logger.debug("Health Check initializing bubbleEndPointResource...");
		
		this.bubbleEndPointResource = bubbleEndPointResource;
	}

	@Override
    protected Result check() throws Exception {
		BubbleEngine bubbleEngine = new BubbleEngine();
		try {
			logger.debug("checking external resource google service search");
			bubbleEngine.workflowEngine("universidad uned", bubbleEndPointResource.getGoogleClient(), 
										 bubbleEndPointResource.getTextAlyticsClient(), bubbleEndPointResource.getTextAlyticsRelevance(),
										 bubbleEndPointResource.getAfcClient(), bubbleEndPointResource.getQueriesToProcess());
			
			logger.debug("Health Check completed!!!");
	        return Result.healthy();	
		}
		catch(Exception exception) {
			logger.error("Health Check failled!!!");
			return Result.unhealthy(exception);
		}
		finally {
			bubbleEngine = null;
		}
		
        
		
    }
}
