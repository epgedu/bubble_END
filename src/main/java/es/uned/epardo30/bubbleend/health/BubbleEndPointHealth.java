package es.uned.epardo30.bubbleend.health;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.yammer.metrics.core.HealthCheck;

import es.uned.epardo30.bubbleend.core.BubbleEngine;
import es.uned.epardo30.bubbleend.dto.LatticeDto;
import es.uned.epardo30.bubbleend.resources.BubbleEndPointResource;


/**
 * Check the correct status of Bubble end point service. 
 * We simulate the workflow:
 * 		1- calling to google service search
 *      2- processing reponse google service
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
										 bubbleEndPointResource.getTextAlyticsClient());
			
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
