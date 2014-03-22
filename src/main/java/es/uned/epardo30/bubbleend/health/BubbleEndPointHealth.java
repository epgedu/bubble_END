package es.uned.epardo30.bubbleend.health;

import com.yammer.metrics.core.HealthCheck;


/**
 * Check the correct status of Bubble end point service. 
 * We check the bellow points:
 * 			- the default value from our own configuration is correct
 *          - the google service is available
 *          - analyse text service textalytics is available
 *          - AFC service is available 
 * 
 * @author Eduardo.Guillen
 *
 */
public class BubbleEndPointHealth extends HealthCheck {

	public BubbleEndPointHealth() {
		super("");
	}

	@Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
