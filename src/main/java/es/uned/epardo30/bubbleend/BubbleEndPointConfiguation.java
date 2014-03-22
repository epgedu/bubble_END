package es.uned.epardo30.bubbleend;

import com.yammer.dropwizard.config.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Each Dropwizard service has its own subclass of the Configuration class which specify environment-specific parameters.
 * These parameters are specified in a YAML configuration file which is deserialized to an instance of your service’s
 * configuration class and validated.
 *  
 * When this class is deserialized from the YAML file, it will pull one root-level fields from the YAML object: port variable is annotated
 * with @NotEmpty, so if the YAML configuration file has blank values for it or is missing template entirely an informative exception will
 * be thrown and your service won’t start.
 * 
 * The mapping from YML to your service’s Configuration instance is done by Jackson. 
 * This means the configuration class can use all of Jackson’s object-mapping annotations.
 * The validation of @NotEmpty is handled by Hibernate Validator, which has a wide range of built-in constraints for you to use.
 * 
 * @author Eduardo.Guillen
 *
 */
public class BubbleEndPointConfiguation extends Configuration{

	
}
