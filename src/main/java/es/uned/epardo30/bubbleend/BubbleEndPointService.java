package es.uned.epardo30.bubbleend;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.client.JerseyClientBuilder;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.config.FilterBuilder;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import es.uned.epardo30.bubbleend.externalresource.afc.core.AfcClient;
import es.uned.epardo30.bubbleend.externalresource.goggle.core.GoogleClient;
import es.uned.epardo30.bubbleend.externalresource.textalytics.core.TextAlyticsClient;
import es.uned.epardo30.bubbleend.health.BubbleEndPointHealth;
import es.uned.epardo30.bubbleend.resources.BubbleEndPointResource;


/**
 * Combined with the project’s Configuration subclass, its Service form the core of your Dropwizard service. 
 * The Service class pulls together the various bundles and commands which provide basic functionality. 
 * 
 * BubbleEndPointService is parameterized with the service’s configuration type, BubbleEndPointConfiguation. 
 * An initialize method is used to configure aspects of the service required before the service is run,
 * in this case, our service’s name: bubble-endpoint. Also, we’ve added a static main method, which will be our service’s entry point. 
 * 
 * In its run method we can read the template from the BubbleEndPointConfiguation instance, create a new BubbleEndPointService instance,
 * and then add it to the service’s environment. 
 * 
 * When our service starts, we create a new instance of our resource class with the parameters from the configuration file
 * and hand it off to the Environment, which acts like a registry of all the things your service can do.
 * 
 * A Dropwizard service can contain many resource classes, each corresponding to its own URI pattern. 
 * 
 * @author Eduardo.Guillen
 *
 */
public class BubbleEndPointService extends Service<BubbleEndPointConfiguation> {
	
	static Logger logger = Logger.getLogger(BubbleEndPointService.class);
	
	public static void main(String[] args) throws Exception {
		logger.debug("Log4j appender configuration is successful");
		logger.info("Going on back-end bubble...");
		
		new BubbleEndPointService().run(args);
    }

    @Override
    public void initialize(Bootstrap<BubbleEndPointConfiguation> bootstrap) {
        bootstrap.setName("bubble-endpoint");
        
    }

    @Override
    public void run(BubbleEndPointConfiguation configuration, Environment environment) {
    	
    	logger.info("Setting environment...");
		
    	
    	logger.debug("Creating filter CrossOriginFilter with original parameters...");
    	final FilterBuilder fconfig = environment.addFilter(CrossOriginFilter.class, "/admin");
        fconfig.setInitParam(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");

    	
        logger.debug("Creating client jersey to connect to external resources...");
        final Client client = new JerseyClientBuilder().using(configuration.getJerseyClientConfiguration()).using(environment).build();
        
        
        logger.debug("Creating google client...");
        final GoogleClient goggleClient = new GoogleClient(client, configuration.getGoogleResourceIp(), configuration.getGoogleResourcePort(), configuration.getGoogleApiKey(), 
        											 configuration.getGoogleSearchEngineId(), configuration.getGoogleResourceProtocol(), configuration.getGoogleResourceContext());
        
        logger.debug("Creating textAlytics client...");
        final TextAlyticsClient textAlyticsClient = new TextAlyticsClient(client, configuration.getTextalyticsResourceIp(),
        																  configuration.getTextalyticsResourcePort(),
        																  configuration.getTextalyticsKey(),
        																  configuration.getTextalyticsResourceProtocol(),
        																  configuration.getTextalyticsResourceContext(),
        																  configuration.getTextalyticsLanguage());
        
        logger.debug("Creating afc client...");
        final AfcClient afcClient = new AfcClient(client, configuration.getAfcResourceIp(), configuration.getAfcResourcePort(), configuration.getAfcResourceProtocol(), configuration.getAfcResourceContext());
        
        logger.debug("Adding Bubble resource...");
        environment.addResource(new BubbleEndPointResource(goggleClient, textAlyticsClient, configuration.getTextalyticsRelevance(), afcClient));
    	
        
        logger.debug("Running the health checking...");
        environment.addHealthCheck(new BubbleEndPointHealth(new BubbleEndPointResource(goggleClient, textAlyticsClient, configuration.getTextalyticsRelevance(), afcClient)));
    	
    	logger.debug("add filter to environment modifying the original paramters...");
    	environment.addFilter(CrossOriginFilter.class, "*").setInitParam("allowedOrigins", "*").setInitParam("allowedMethods", "GET,POST,DELETE,PUT,HEAD").setInitParam("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
    	
    	logger.info("Service bubble ready!!!");
    }
}
