package es.uned.epardo30.bubbleend;

import javax.validation.constraints.NotNull;

import com.yammer.dropwizard.client.JerseyClientConfiguration;
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
	
	
	/*
	 * Settings for external resources
	 */
	@JsonProperty
	private JerseyClientConfiguration httpClient = new JerseyClientConfiguration();

	public JerseyClientConfiguration getJerseyClientConfiguration() {
		return httpClient;
	}

	/*
	 * Settings for external resource google service search
	 */
	@NotEmpty
    private String googleResourceIp;
	
	@NotNull
    private int googleResourcePort;
	
	@NotEmpty
	private String googleResourceProtocol;
	
	@NotEmpty
	private String googleResourceContext;
	
	@NotNull
	private int minThreads;
	
	@NotNull
	private int maxThreads;
	
	@NotNull
	private boolean gzipEnabled;
	
	@NotNull
	private boolean gzipEnabledForRequests;
	
	@NotEmpty
	private String googleSearchEngineId;
	
	@NotEmpty
	private String googleApiKey;
	
	@JsonProperty
	public String getGoogleResourceIp() {
		return googleResourceIp;
	}
	
	@JsonProperty
	public void setGoogleResourceIp(String googleResurceIp) {
		this.googleResourceIp = googleResurceIp;
	}
	
	@JsonProperty
	public int getGoogleResourcePort() {
		return googleResourcePort;
	}
	
	@JsonProperty
	public void setGoogleResourcePort(int googleResurcePort) {
		this.googleResourcePort = googleResurcePort;
	}
	
	@JsonProperty
	public int getMinThreads() {
		return minThreads;
	}
	
	@JsonProperty
	public void setMinThreads(int minThreads) {
		this.minThreads = minThreads;
	}
	
	@JsonProperty
	public int getMaxThreads() {
		return maxThreads;
	}
	
	@JsonProperty
	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}
	
	@JsonProperty
	public boolean getGzipEnabled() {
		return gzipEnabled;
	}
	
	@JsonProperty
	public void setGzipEnabled(Boolean gzipEnabled) {
		this.gzipEnabled = gzipEnabled;
	}
	
	@JsonProperty
	public boolean getGzipEnabledForRequests() {
		return gzipEnabledForRequests;
	}
	
	@JsonProperty
	public void setGzipEnabledForRequests(Boolean gzipEnabledForRequests) {
		this.gzipEnabledForRequests = gzipEnabledForRequests;
	}

	@JsonProperty
	public String getGoogleSearchEngineId() {
		return googleSearchEngineId;
	}

	@JsonProperty
	public void setGoogleSearchEngineId(String googleSearchEngineId) {
		this.googleSearchEngineId = googleSearchEngineId;
	}

	@JsonProperty
	public String getGoogleApiKey() {
		return googleApiKey;
	}

	@JsonProperty
	public void setGoogleApiKey(String googleApiKey) {
		this.googleApiKey = googleApiKey;
	}

	@JsonProperty
	public String getGoogleResourceProtocol() {
		return googleResourceProtocol;
	}

	@JsonProperty
	public void setGoogleResourceProtocol(String googleResourceProtocol) {
		this.googleResourceProtocol = googleResourceProtocol;
	}

	@JsonProperty
	public String getGoogleResourceContext() {
		return googleResourceContext;
	}

	@JsonProperty
	public void setGoogleResourceContext(String googleResourceContext) {
		this.googleResourceContext = googleResourceContext;
	}
	
	
	
}
